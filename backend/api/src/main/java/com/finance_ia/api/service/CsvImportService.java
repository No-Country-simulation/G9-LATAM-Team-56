package com.finance_ia.api.service;

import com.finance_ia.api.model.FinancialRecord;
import com.finance_ia.api.repository.FinancialRecordRepository;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvImportService {

    @Autowired
    private FinancialRecordRepository repository;

    /**
     Procesa un archivo CSV recibido por Multipart, lo mapea a entidades y los persiste en MySQL.
     */

    public void importCsv(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo CSV está vacío o no ha sido adjuntado.");
        }

        List<FinancialRecord> records = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVReader csvReader = new CSVReader(reader)) {

            List<String[]> rows = csvReader.readAll();

            if (rows.isEmpty()) {
                throw new IllegalArgumentException("El archivo CSV no contiene filas.");
            }

            // Omitir la primera línea (cabecera del CSV) y recorrer el resto de filas
            boolean isHeader = true;
            for (String[] row : rows) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Salta la cabecera
                }

                // Validar que la fila tenga al menos las 4 columnas requeridas
                if (row.length < 4) {
                    continue; // Ignora filas incompletas o corruptas
                }

                FinancialRecord record = new FinancialRecord();
                // Suponiendo formato de fecha yyyy-MM-dd en la columna 0
                record.setDate(LocalDate.parse(row[0].trim(), DateTimeFormatter.ISO_LOCAL_DATE));
                record.setDescription(row[1].trim());
                record.setAmount(new BigDecimal(row[2].trim()));
                record.setCategory(row[3].trim());

                records.add(record);
            }

            // Guardar todos los registros procesados en la base de datos de una sola vez
            repository.saveAll(records);

        } catch (Exception e) {
            throw new RuntimeException("Error al procesar y guardar el archivo CSV: " + e.getMessage(), e);
        }
    }
}