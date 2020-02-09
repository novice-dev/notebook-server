package ma.oraclelabs.ah.notebookserver.exceptions;

public class InterpreterNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 7479003469398971576L;

    public InterpreterNotFoundException(String message) {
        super(message);
    }
}