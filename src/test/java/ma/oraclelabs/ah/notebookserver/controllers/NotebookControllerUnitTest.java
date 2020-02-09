package ma.oraclelabs.ah.notebookserver.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import ma.oraclelabs.ah.notebookserver.models.Request;
import ma.oraclelabs.ah.notebookserver.models.Response;
import ma.oraclelabs.ah.notebookserver.services.CodeExecutor;

@RunWith(SpringRunner.class)
@WebMvcTest(NotebookController.class)
public class NotebookControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CodeExecutor executor;

    @Test
    public void shouldProceedSuccessfully() throws Exception {
        when(executor.interprete(any(Request.class)))
            .thenReturn(Response.builder().result("Hello OracleLabs").build());

        Request request = Request
            .builder()
            .interpreter("python")
            .instruction("print('Hello OracleLabs')")
            .build();

        mockMvc.perform(
            post("/execute")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.result", "Hello OracleLabs").exists());
    }

    @Test
    public void shouldNotProceed_whenInterpreterEmpty() throws Exception {
        mockMvc.perform(
            post("/execute")
                .content(new ObjectMapper().writeValueAsString(new Request()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

        verifyZeroInteractions(executor);
    }
}