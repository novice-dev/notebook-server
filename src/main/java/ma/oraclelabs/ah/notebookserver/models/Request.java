package ma.oraclelabs.ah.notebookserver.models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Request {
    private static final String PATTERN = "^\\%(\\w+)(\\s+)(.*)";

    public void setCode(String code) {
        Matcher matcher = Pattern.compile(PATTERN, Pattern.DOTALL).matcher(code);
        if (matcher.find()) {
            setInterpreter(matcher.group(1));
            setInstruction(matcher.group(3));
        }
    }

    private String sessionId;

    @NotBlank(message = "code is not valid, must be of format '%<interpreter><whitespace><instruction>'")
    private String interpreter;

    @NotBlank(message = "code is not valid, must be of format '%<interpreter><whitespace><instruction>'")
    private String instruction;
}