package ma.oraclelabs.ah.notebookserver.exceptions;

public class InterpreterTimeoutException extends RuntimeException {
    private static final long serialVersionUID = 6074791934098776359L;

    public InterpreterTimeoutException(String message) {
        super(message);
    }
}