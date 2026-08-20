package com.finance_ia.api.controller;

import com.finance_ia.api.dto.AnalisisFinancieroRequest;
import com.finance_ia.api.dto.AnalisisFinancieroResponse;
import com.finance_ia.api.service.AnalisisFinancieroService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.argThat;


@WebMvcTest(AnalisisFinancieroController.class)
class AnalisisFinancieroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AnalisisFinancieroService service;

    @Test
    void debeIgnorarFechaInvalidaEnRequestManual() throws Exception {

        when(service.analizar(any(AnalisisFinancieroRequest.class)))
                .thenReturn(new AnalisisFinancieroResponse());

        String requestJson = """
        {
            "ingreso_mensual": 5000,
            "nivel_endeudamiento": 50,
            "frecuencia_ahorro": "alta",
            "transacciones": [
                {
                    "descripcion": "Compra",
                    "valor": 100,
                    "fecha": "fecha-invalida"
                }
            ]
        }
        """;

        mockMvc.perform(
                        post("/analisis-financiero")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk());
    }
}