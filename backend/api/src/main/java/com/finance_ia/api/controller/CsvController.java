package com.finance_ia.api.controller;

import com.finance_ia.api.service.CsvImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/csv")
@CrossOrigin(origins = "*") // Permite la conexión cruzada con el frontend de React/Vite
public class CsvController {

    @Autowired
    private CsvImportService csvImportService;

    /**
     Endpoint para recibir la carga del CSV desde el frontend.
     URL: POST http://localhost:8080/api/csv/upload
     */

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file) {
        try {
            csvImportService.importCsv(file);
            return ResponseEntity.ok("¡Archivo CSV importado y guardado en MySQL con éxito!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error en la importación: " + e.getMessage());
        }
    }
}