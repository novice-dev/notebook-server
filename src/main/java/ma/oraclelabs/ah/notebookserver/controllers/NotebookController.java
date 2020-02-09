package ma.oraclelabs.ah.notebookserver.controllers;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import ma.oraclelabs.ah.notebookserver.models.Request;
import ma.oraclelabs.ah.notebookserver.models.Response;
import ma.oraclelabs.ah.notebookserver.services.CodeExecutor;

@RequiredArgsConstructor
@RestController
@RequestMapping(consumes = "application/json")
public class NotebookController {
    private final CodeExecutor executor;

    @PostMapping("execute")
    public Response execute(@Valid @RequestBody Request request) throws IOException, InterruptedException {
        return executor.interprete(request);
    }
}
