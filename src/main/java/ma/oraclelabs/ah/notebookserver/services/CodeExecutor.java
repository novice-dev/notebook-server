package ma.oraclelabs.ah.notebookserver.services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import ma.oraclelabs.ah.notebookserver.exceptions.InterpreterNotFoundException;
import ma.oraclelabs.ah.notebookserver.exceptions.InterpreterRuntimeException;
import ma.oraclelabs.ah.notebookserver.exceptions.InterpreterTimeoutException;
import ma.oraclelabs.ah.notebookserver.interpreters.Interpreter;
import ma.oraclelabs.ah.notebookserver.interpreters.PythonInterpreter;
import ma.oraclelabs.ah.notebookserver.models.Request;
import ma.oraclelabs.ah.notebookserver.models.Response;

@Service
@Log4j2
public class CodeExecutor {
    private static final int TIMEOUT_IN_SECONDS = 10;

    public Interpreter getInterpreter(String interpreter) throws InterpreterNotFoundException {
        switch (interpreter) {
            case "python":
            case "python3":
                return new PythonInterpreter();
            default:
                throw new InterpreterNotFoundException(String.format("Interpreter %s not found", interpreter));
        }
    }

    public Response interprete(Request request) throws IOException, InterruptedException {
        Interpreter interpreter = getInterpreter(request.getInterpreter());
        String result = null, error = null;
        int exitCode = -1;

        Process process = interpreter.buildProcess(request);
        if (process.waitFor(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)) {
            exitCode = process.exitValue();
            result = String.join("\n", IOUtils.readLines(process.getInputStream(), StandardCharsets.UTF_8));
            error = String.join("\n", IOUtils.readLines(process.getErrorStream(), StandardCharsets.UTF_8));                
        } else {
            process.destroyForcibly();
            throw new InterpreterTimeoutException("Process timed out");
        }

        log.info("code:{}, result='{}', error='{}'",  exitCode, result, error);

        if (exitCode != 0) {
            throw new InterpreterRuntimeException(error, exitCode);
        }

        return new Response(result, request.getSessionId());
    }
}
