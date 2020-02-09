package ma.oraclelabs.ah.notebookserver.exceptions;

import lombok.Getter;

public class InterpreterRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 7934600479176398975L;

    @Getter
    private int exitCode;

    public InterpreterRuntimeException(String message, int exitCode) {
        super(message);
        this.exitCode = exitCode;
    }
}