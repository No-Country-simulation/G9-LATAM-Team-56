package com.finance_ia.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc // Permite simular peticiones HTTP sin levantar todo el servidor web
public class CsvControllerTest {

    @Autowired
    private MockMvc mockMvc; // Herramienta para simular peticiones HTTP (como Postman o React)

    @Test
    public void testSubirCsvExitoso() throws Exception {
        // 1. Creamos el contenido simulado de un archivo CSV con el formato de fecha correcto (d/M/yyyy)
        String contenidoCsv = "ingreso_mensual,nivel_endeudamiento,frecuencia_ahorro,descripcion,valor,fecha\n" +
                "45000,25,Media,Dominos,498.34,12/02/2021\n" +
                "45000,25,Media,Samsung Store,3134.80,12/03/2021\n" +
                "45000,25,Media,Apple Music,744.99,6/02/2021";

        // 2. Creamos un archivo Multipart simulado (nombre del campo en el backend: "file")
        MockMultipartFile archivoCsv = new MockMultipartFile(
                "file",               // Nombre del @RequestParam en el Controller
                "transacciones.csv",  // Nombre del archivo simulado
                MediaType.TEXT_PLAIN_VALUE,
                contenidoCsv.getBytes()
        );

        // 3. Simulamos la petición POST enviando el archivo y el parámetro de usuario
        mockMvc.perform(multipart("/api/csv/upload")
                        .file(archivoCsv)
                        .param("usuario", "Marcela G.")) // El @RequestParam("usuario") que espera tu backend
                .andExpect(status().isOk()) // Esperamos que la respuesta HTTP sea 200 OK
                .andExpect(jsonPath("$.ingreso_mensual").exists()); // Verificamos el campo ingreso_mensual que devuelve el JSON
    }
}