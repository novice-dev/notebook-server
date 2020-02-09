package ma.oraclelabs.ah.notebookserver.interpreters;

import java.io.IOException;

import ma.oraclelabs.ah.notebookserver.models.Request;

public interface Interpreter {
    Process buildProcess(Request request) throws IOException;
}
