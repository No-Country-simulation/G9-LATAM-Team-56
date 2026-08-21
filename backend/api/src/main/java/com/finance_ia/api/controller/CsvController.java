package com.finance_ia.api.controller;

import com.finance_ia.api.dto.CsvResponse;
import com.finance_ia.api.service.CsvService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/csv")
@CrossOrigin(origins = "*")
public class CsvController {

    private final CsvService csvService;

    public CsvController(CsvService csvService) {
        this.csvService = csvService;
    }

    @PostMapping("/upload")
    public ResponseEntity<CsvResponse> subirCsv(
            @RequestParam("file") MultipartFile file,
            @RequestParam("usuario") String usuario // Parámetro extra para asociar el nombre en MySQL
    ) {
        CsvResponse response = csvService.analizarYGuardarCsv(file, usuario);
        return ResponseEntity.ok(response);
    }
}
