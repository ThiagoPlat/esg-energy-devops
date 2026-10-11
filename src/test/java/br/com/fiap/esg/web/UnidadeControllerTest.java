package br.com.fiap.esg.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.fiap.esg.domain.UnidadeOperacional;
import br.com.fiap.esg.repository.UnidadeOperacionalRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UnidadeController.class)
class UnidadeControllerTest {

    @Autowired MockMvc mvc;
    @MockBean UnidadeOperacionalRepository repo;

    private static UnidadeOperacional unidade() {
        return new UnidadeOperacional("1", "UN-01", "Fabrica Central - Mooca", "Industrial", 12.5, Map.of());
    }

    @Test
    void listarRetorna200ComUnidades() throws Exception {
        when(repo.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(unidade())));

        mvc.perform(get("/api/unidades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigo").value("UN-01"))
                .andExpect(jsonPath("$[0].metaReducaoAnualPct").value(12.5));
    }

    @Test
    void buscarInexistenteRetorna404() throws Exception {
        when(repo.findById("x")).thenReturn(Optional.empty());

        mvc.perform(get("/api/unidades/x")).andExpect(status().isNotFound());
    }

    @Test
    void criarSemCodigoRetorna400() throws Exception {
        mvc.perform(post("/api/unidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Sem codigo\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void criarValidoRetorna201() throws Exception {
        when(repo.save(any(UnidadeOperacional.class))).thenReturn(unidade());

        mvc.perform(post("/api/unidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigo\":\"UN-01\",\"nome\":\"Fabrica Central - Mooca\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"));
    }
}
