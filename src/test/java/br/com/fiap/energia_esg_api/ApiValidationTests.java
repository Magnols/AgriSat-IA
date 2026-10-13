package br.com.fiap.energia_esg_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser
class ApiValidationTests {
    @Autowired MockMvc mockMvc;

    @Test
    void nomeEmBrancoDeveRetornarErroDeValidacao() throws Exception {
        mockMvc.perform(post("/areas").contentType("application/json").content("{\"nome\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.nome").exists());
    }

    @Test
    void jsonMalformadoDeveRetornar400SemExporDetalhesInternos() throws Exception {
        mockMvc.perform(post("/areas").contentType("application/json").content("{"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Requisicao invalida"))
                .andExpect(jsonPath("$.trace").doesNotExist());
    }

    @Test
    void identificadorNaoNumericoDeveRetornar400() throws Exception {
        mockMvc.perform(put("/equipamentos/invalido").contentType("application/json")
                        .content("{\"nome\":\"Sensor\",\"status\":\"ATIVO\",\"limiteConsumo\":1,\"idArea\":1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}
