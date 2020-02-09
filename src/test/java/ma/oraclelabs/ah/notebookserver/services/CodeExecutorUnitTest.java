package ma.oraclelabs.ah.notebookserver.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import ma.oraclelabs.ah.notebookserver.exceptions.InterpreterNotFoundException;
import ma.oraclelabs.ah.notebookserver.exceptions.InterpreterTimeoutException;
import ma.oraclelabs.ah.notebookserver.interpreters.Interpreter;
import ma.oraclelabs.ah.notebookserver.interpreters.PythonInterpreter;
import ma.oraclelabs.ah.notebookserver.models.Request;
import ma.oraclelabs.ah.notebookserver.models.Response;

public class CodeExecutorUnitTest {
    private CodeExecutor executor;

    @Before
    public void setup() {
        executor = new CodeExecutor();
    }

    @Test
    public void shouldReturnPythonInterpreter() {
        Interpreter interpreter = executor.getInterpreter("python");
        assert interpreter instanceof PythonInterpreter;
    }

    @Test(expected = InterpreterNotFoundException.class)
    public void shouldThrowUnknowInterpreterException() {
        executor.getInterpreter("golang");
    }

    @Test(expected = InterpreterTimeoutException.class)
    public void shouldThrowTimeoutInterpreterException() throws Exception {
        Request request = Request
            .builder()
            .interpreter("python")
            .instruction("import time\ntime.sleep(15)")
            .build();
        executor.interprete(request);
    }

    @Test
    public void shouldProceedSuccessfully() throws Exception {
        Request request = Request
            .builder()
            .interpreter("python")
            .instruction("print('Hello OracleLabs')")
            .build();

        Response response = executor.interprete(request);
        assertNotNull(response);
        assertEquals(response.getResult(), "Hello OracleLabs");
    }
}