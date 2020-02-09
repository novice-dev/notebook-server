package ma.oraclelabs.ah.notebookserver.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InterpretationError {
    private String error;
    private Integer exitCode;

    public InterpretationError(String error) {
        this(error, null);
    }
}

