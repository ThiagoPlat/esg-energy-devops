package br.com.fiap.esg.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SistemaController.class)
class SistemaControllerTest {

    @Autowired MockMvc mvc;

    @Test
    void infoMostraAmbiente() throws Exception {
        mvc.perform(get("/api/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.environment").value("local"));
    }

    @Test
    void avaliarClassificaSeveridade() throws Exception {
        mvc.perform(get("/api/alertas/avaliar").param("consumoKwh", "150").param("limiteKwh", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.severidade").value("CRITICA"));
    }
}
