package ma.oraclelabs.ah.notebookserver.exceptions;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.log4j.Log4j2;
import ma.oraclelabs.ah.notebookserver.models.InterpretationError;

@ControllerAdvice
@Log4j2
public class ExceptionHandlerAdvice {
    @ExceptionHandler(IOException.class)
    public ResponseEntity<InterpretationError> handleException(IOException e) {
        return handleExceptionError("Error reading/writing data", e);
    }

    @ExceptionHandler(InterruptedException.class)
    public ResponseEntity<InterpretationError> handleException(InterruptedException e) {
        return handleExceptionError("Process has been interrupted", e);
    }

    @ExceptionHandler({InterpreterNotFoundException.class, InterpreterTimeoutException.class})
    public ResponseEntity<InterpretationError> handleException(RuntimeException e) {
        return handleExceptionError(e.getMessage(), e);
    }

    private ResponseEntity<InterpretationError> handleExceptionError(String error, Exception e) {
        log.error(error, e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new InterpretationError(error));
    }

    @ExceptionHandler(InterpreterRuntimeException.class)
    public ResponseEntity<InterpretationError> handleException(InterpreterRuntimeException e) {
        String error = e.getMessage();
        log.error("Runtime interpretation error: exitCode={}, error='{}'", e.getExitCode(), error, e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new InterpretationError(error, e.getExitCode()));
    }
}