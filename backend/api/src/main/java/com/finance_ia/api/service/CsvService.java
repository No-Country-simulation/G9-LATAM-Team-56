package com.finance_ia.api.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.finance_ia.api.dto.ClasificacionTransaccionesRequest;
import com.finance_ia.api.dto.ClasificacionTransaccionesResponse;
import com.finance_ia.api.dto.CsvResponse;
import com.finance_ia.api.dto.PerfilFinancieroRequest;
import com.finance_ia.api.dto.PerfilFinancieroResponse;
import com.finance_ia.api.dto.TransaccionRequest;
import com.finance_ia.api.dto.TransaccionResponse;
import com.finance_ia.api.model.AnalisisFinancieroEntity;
import com.finance_ia.api.model.TransaccionEntity;
import com.finance_ia.api.repository.AnalisisFinancieroRepository;

@Service
public class CsvService {

    private final AnalisisFinancieroService analisisFinancieroService;
    private final AnalisisFinancieroRepository analisisRepository; // Inyectamos el repositorio de MySQL

    // Inyección de dependencias por constructor (Práctica recomendada en Spring Boot)
    public CsvService(
            AnalisisFinancieroService analisisFinancieroService,
            AnalisisFinancieroRepository analisisRepository
    ) {
        this.analisisFinancieroService = analisisFinancieroService;
        this.analisisRepository = analisisRepository;
    }

    // Modificamos el método para que ahora reciba también el nombre de la persona
    public CsvResponse analizarYGuardarCsv(MultipartFile file, String nombreUsuario) {

        try (
                Reader reader = new InputStreamReader(
                        file.getInputStream(),
                        StandardCharsets.UTF_8
                );

                CSVParser parser = CSVFormat.DEFAULT
                        .builder()
                        .setHeader()
                        .setSkipHeaderRecord(true)
                        .build()
                        .parse(reader)
        ) {

            List<TransaccionRequest> transacciones = new ArrayList<>();
            double ingresoMensual = 0;
            double nivelEndeudamiento = 0;
            String frecuenciaAhorro = null;
            boolean primeraFila = true;

            // Recorremos cada línea del archivo CSV
            for (CSVRecord record : parser) {
                if (primeraFila) {
                    ingresoMensual = Double.parseDouble(record.get("ingreso_mensual"));
                    nivelEndeudamiento = Double.parseDouble(record.get("nivel_endeudamiento"));
                    frecuenciaAhorro = record.get("frecuencia_ahorro");
                    primeraFila = false;
                }

                TransaccionRequest transaccion = new TransaccionRequest();
                transaccion.setDescripcion(record.get("descripcion"));
                transaccion.setValor(Double.parseDouble(record.get("valor")));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
                LocalDate fechaParseada = LocalDate.parse(record.get("fecha"), formatter);
                transaccion.setFecha(fechaParseada);

                transacciones.add(transaccion);
            }

            // 1. Clasificar las transacciones llamando al servicio financiero
            ClasificacionTransaccionesRequest clasificacionRequest = new ClasificacionTransaccionesRequest();
            clasificacionRequest.setTransacciones(transacciones);

            ClasificacionTransaccionesResponse clasificacionResponse =
                    analisisFinancieroService.clasificarTransacciones(clasificacionRequest);

            List<TransaccionResponse> transaccionesCategorizadas =
                    clasificacionResponse.getTransacciones();

            // 2. Calcular el gasto total sumando todas las transacciones
            double gastoTotal = 0.0;
            for (TransaccionResponse transaccion : transaccionesCategorizadas) {
                gastoTotal += transaccion.getValor();
            }

            // 3. Obtener el perfil financiero del usuario
            PerfilFinancieroRequest perfilRequest = new PerfilFinancieroRequest();
            perfilRequest.setIngreso_mensual(ingresoMensual);
            perfilRequest.setNivel_endeudamiento(nivelEndeudamiento);
            perfilRequest.setFrecuencia_ahorro(frecuenciaAhorro);
            perfilRequest.setGasto_total(gastoTotal);

            PerfilFinancieroResponse perfilResponse =
                    analisisFinancieroService.obtenerPerfil(perfilRequest);

            // ==========================================
            // 4. NUEVO: GUARDAR EN LA BASE DE DATOS MYSQL
            // ==========================================
            AnalisisFinancieroEntity entidadAnalisis = new AnalisisFinancieroEntity();
            entidadAnalisis.setUsuarioNombre(nombreUsuario);
            entidadAnalisis.setIngresoMensual(ingresoMensual);
            entidadAnalisis.setNivelEndeudamiento(nivelEndeudamiento);
            entidadAnalisis.setFrecuenciaAhorro(frecuenciaAhorro);
            entidadAnalisis.setPerfilFinanciero(perfilResponse.getPerfil_financiero());
            entidadAnalisis.setProbabilidad(perfilResponse.getProbabilidad());

            // Convertimos las transacciones del DTO a Entidades de Base de Datos
            List<TransaccionEntity> listaTransaccionesEntities = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

            for (TransaccionResponse txDto : transaccionesCategorizadas) {
                TransaccionEntity txEntity = new TransaccionEntity();
                txEntity.setDescripcion(txDto.getDescripcion());
                txEntity.setValor(txDto.getValor());
                txEntity.setCategoria(txDto.getCategoria());

                listaTransaccionesEntities.add(txEntity);
            }

            entidadAnalisis.setTransacciones(listaTransaccionesEntities);

            // Guardamos todo en MySQL en una sola instrucción
            analisisRepository.save(entidadAnalisis);

            // 5. Construir la respuesta final (DTO) para Postman
            CsvResponse response = new CsvResponse();
            response.setIngreso_mensual(ingresoMensual);
            response.setNivel_endeudamiento(nivelEndeudamiento);
            response.setFrecuencia_ahorro(frecuenciaAhorro);
            response.setPerfil_financiero(perfilResponse.getPerfil_financiero());
            response.setProbabilidad(perfilResponse.getProbabilidad());
            response.setTransacciones(transaccionesCategorizadas);

            return response;

        } catch (IOException e) {
            throw new IllegalArgumentException("No se pudo leer el archivo CSV", e);
        }
    }
}