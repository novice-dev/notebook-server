package ma.oraclelabs.ah.notebookserver.interpreters;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import lombok.extern.log4j.Log4j2;
import ma.oraclelabs.ah.notebookserver.models.Request;

@Log4j2
public class PythonInterpreter implements Interpreter {

    public Process buildProcess(Request request) throws IOException {
        File tempFile = File.createTempFile(String.valueOf(request.hashCode()), null);
        boolean hasSession = !StringUtils.isEmpty(request.getSessionId());

        if (!hasSession) {
            request.setSessionId(RandomStringUtils.random(10, true, true));
        }
        
        FileUtils.copyFile(new ClassPathResource("interpreters/python.py").getFile(), tempFile);

        String sessionPath = sessionPath(request);
        String code = String.format("%s%npickle_session('%s')%n", request.getInstruction(), sessionPath);
        FileUtils.writeStringToFile(tempFile, code, StandardCharsets.UTF_8, true);

        String command = String.format("%s %s %s",
            request.getInterpreter(),
            tempFile.getAbsolutePath(),
            hasSession ? String.format("'%s'", sessionPath): ""
        );

        log.debug(command);
        return new ProcessBuilder("bash", "-c", command).start();
    }

    public String sessionPath(Request request) {
        return Paths.get("session_files", request.getSessionId()).toAbsolutePath().toString();
    }
}