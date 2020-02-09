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
        String error = "Error reading/writing data";
        log.error(error, e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new InterpretationError(error));
    }

    @ExceptionHandler(InterruptedException.class)
    public ResponseEntity<InterpretationError> handleException(InterruptedException e) {
        String error = "Process has been interrupted";
        log.error(error, e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new InterpretationError(error));
    }

    @ExceptionHandler({InterpreterNotFoundException.class, InterpreterTimeoutException.class})
    public ResponseEntity<InterpretationError> handleException(RuntimeException e) {
        String error = e.getMessage();
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