package com.finance_ia.api.service.recommendation;

import com.finance_ia.api.dto.CsvResponse;
import com.finance_ia.api.service.CsvService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CsvServiceTest {

    @Autowired
    private CsvService csvService;

    @Test
    void testAnalizarCsv() {
        String csvContent = "ingreso_mensual,nivel_endeudamiento,frecuencia_ahorro,descripcion,valor,fecha\n" +
                "45000,25,Media,Dominos,498.34,12/02/2021";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                csvContent.getBytes(StandardCharsets.UTF_8)
        );

        CsvResponse response = csvService.analizarCsv(file);

        assertNotNull(response);
        assertNotNull(response.getTransacciones());
    }
}