package com.finance_ia.api.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.*;
import java.util.stream.Collectors;

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
import com.finance_ia.api.dto.recommendation.RecommendationResult;
import com.finance_ia.api.dto.recommendation.RecommendationResultDto;
import com.finance_ia.api.model.AnalisisFinancieroEntity;
import com.finance_ia.api.model.RecomendacionEntity;
import com.finance_ia.api.model.TransaccionEntity;
import com.finance_ia.api.model.recommendation.RecommendationResponse;
import com.finance_ia.api.repository.AnalisisFinancieroRepository;
import com.finance_ia.api.infra.exception.CsvValidationException;
import com.finance_ia.api.infra.exception.ErrorDetail;

@Service
public class CsvService {

    private final AnalisisFinancieroService analisisFinancieroService;
    private final AnalisisFinancieroRepository analisisRepository;
    private final FinancialValidationService financialValidationService;
    private static final List<String> COLUMNAS_OBLIGATORIAS = List.of(
        "ingreso_mensual",
        "nivel_endeudamiento",
        "frecuencia_ahorro",
        "divisa",
        "descripcion",
        "valor",
        "fecha"
    );
    private final PerfilFinancieroMapper perfilMapper;

    // Inyección de dependencias por constructor
    public CsvService(
        AnalisisFinancieroService analisisFinancieroService,
        AnalisisFinancieroRepository analisisRepository, FinancialValidationService financialValidationService, PerfilFinancieroMapper perfilMapper
    ) {
        this.analisisFinancieroService = analisisFinancieroService;
        this.analisisRepository = analisisRepository;
        this.financialValidationService = financialValidationService;
        this.perfilMapper = perfilMapper;
    }

    // Modificamos el mtodo para que ahora reciba también el nombre de la persona
    private void validarColumnas(CSVParser parser) {

        List<ErrorDetail> errors = new ArrayList<>();

        Map<String, Integer> headers = parser.getHeaderMap();

        for (String columna : COLUMNAS_OBLIGATORIAS) {

            if (!headers.containsKey(columna)) {

                errors.add(new ErrorDetail(
                    columna,
                    "Falta la columna obligatoria '" + columna + "'.",
                    null
                ));
            }
        }

        if (!errors.isEmpty()) {
            throw new CsvValidationException(errors);
        }
    }

    private void validarRegistros(
        List<CSVRecord> records
    ) {

        List<ErrorDetail> errors =
            new ArrayList<>();

        if (records.isEmpty()) {

            errors.add(
                new ErrorDetail(
                    "archivo",
                    "El archivo CSV debe contener al menos una transacción.",
                    null
                )
            );

            throw new CsvValidationException(errors);
        }

        for (CSVRecord record : records) {

            // Se suma 1 porque el número de registro de CSV no representa directamente
            // el número de línea física del archivo: la primera
            // fila de datos corresponde a la línea 2, después del encabezado.
            int row = (int) record.getRecordNumber() + 1;
            int cantidadEsperada = COLUMNAS_OBLIGATORIAS.size();

            if (record.size() != cantidadEsperada) {

                errors.add(new ErrorDetail(
                        "columnas",
                        "La fila debe contener exactamente "
                                + cantidadEsperada
                                + " columnas, pero contiene "
                                + record.size()
                                + ".",
                        row
                ));
                continue;
            }

            errors.addAll(
                financialValidationService.validarRegistroCsv(
                    record.get("ingreso_mensual"),
                    record.get("nivel_endeudamiento"),
                    record.get("frecuencia_ahorro"),
                    record.get("divisa"),
                    record.get("descripcion"),
                    record.get("valor"),
                    record.get("fecha"),
                    row
                )
            );
        }

        if (!errors.isEmpty()) {
            throw new CsvValidationException(errors);
        }
    }

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

            // Validamos la estructura y todos los registros antes de iniciar el análisis,
            // para evitar procesar parcialmente un CSV inválido.
            validarColumnas(parser);
            List<CSVRecord> records = parser.getRecords();
            validarRegistros(records);

            List<TransaccionRequest> transacciones = new ArrayList<>();
            double ingresoMensual = 0;
            double nivelEndeudamiento = 0;
            String frecuenciaAhorro = null;
            String divisa = null;
            boolean primeraFila = true;

            // Recorremos cada línea del archivo CSV
            for (CSVRecord record : records) {
                if (primeraFila) {
                    ingresoMensual = Double.parseDouble(record.get("ingreso_mensual"));
                    nivelEndeudamiento = Double.parseDouble(record.get("nivel_endeudamiento"));
                    frecuenciaAhorro = record.get("frecuencia_ahorro");
                    divisa = record.get("divisa").trim();
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

            // Definir el rango del mes financiero (Desde el mismo día del mes anterior hasta hoy)
            LocalDate finMes = LocalDate.now(); // Fecha actual del sistema (Hoy)
            LocalDate inicioMes = finMes.minusMonths(1); // Exactamente un mes antes (ej: 12/07/2026 si es 12/08/2026)

            // Filtrar transacciones comprendidas exactamente en el rango móvil del mes financiero
            List<TransaccionRequest> transaccionesUltimoMes = transacciones.stream()
                    .filter(t -> {
                        LocalDate f = t.getFecha();
                        return !f.isBefore(inicioMes) && !f.isAfter(finMes);
                    })
                    .toList();

            // Clasificar las transacciones llamando al servicio financiero
            ClasificacionTransaccionesRequest clasificacionRequest = new ClasificacionTransaccionesRequest();
            clasificacionRequest.setTransacciones(transacciones);
            ClasificacionTransaccionesResponse clasificacionResponse =

            analisisFinancieroService.clasificarTransacciones(clasificacionRequest);

            List<TransaccionResponse> transaccionesCategorizadas =
                clasificacionResponse.getTransacciones();

            // Calcular Gasto y Saldo con las transacciones filtradas del período correcto (día 1 al día actual)
            double gastoTotalUltimoMes = transaccionesUltimoMes.stream()
                    .mapToDouble(TransaccionRequest::getValor)
                    .sum();

            double saldoTotalUltimoMes = ingresoMensual - gastoTotalUltimoMes;

            // Agrupar usando las categorías reales ya procesadas, respetando el mismo filtro de fechas
            Map<String, Double> resumenGastos = transaccionesCategorizadas.stream()
                    .filter(t -> {
                        LocalDate f = t.getFecha();
                        return !f.isBefore(inicioMes) && !f.isAfter(finMes);
                    })
                    .collect(Collectors.groupingBy(
                            TransaccionResponse::getCategoria, // Usamos la categoría real del Enum
                            Collectors.summingDouble(TransaccionResponse::getValor)
                    ));

            // Obtener el perfil financiero del usuario
            PerfilFinancieroRequest perfilRequest = new PerfilFinancieroRequest();
            perfilRequest.setIngreso_mensual(ingresoMensual);
            perfilRequest.setNivel_endeudamiento(nivelEndeudamiento);
            perfilRequest.setFrecuencia_ahorro(frecuenciaAhorro);
            perfilRequest.setGasto_total(gastoTotalUltimoMes);

            PerfilFinancieroResponse perfilResponse =
                analisisFinancieroService.obtenerPerfil(perfilRequest);

            RecommendationResponse recommendationResponse =
                analisisFinancieroService.generarRecomendaciones(
                    perfilResponse.getPerfil_financiero(),
                    resumenGastos
                );

            // =========================================================================
            // GUARDAR O ACTUALIZAR EN LA BASE DE DATOS MYSQL (Evita duplicados)
            // =========================================================================

            Optional<AnalisisFinancieroEntity> analisisExistente = analisisRepository.findByUsuarioNombre(nombreUsuario);

            AnalisisFinancieroEntity entidadAnalisis;

            if (analisisExistente.isPresent()) {
                // Recuperamos el registro anterior
                entidadAnalisis = analisisExistente.get();
                entidadAnalisis.setIngresoMensual(ingresoMensual);
                entidadAnalisis.setNivelEndeudamiento(nivelEndeudamiento);
                entidadAnalisis.setFrecuenciaAhorro(frecuenciaAhorro);
                entidadAnalisis.setDivisa(divisa);
                entidadAnalisis.setPerfilFinanciero(perfilResponse.getPerfil_financiero());
                entidadAnalisis.setProbabilidad(perfilResponse.getProbabilidad());
                entidadAnalisis.setSaldoTotal(saldoTotalUltimoMes);
                entidadAnalisis.setGastoTotal(gastoTotalUltimoMes);

                // Vaciamos la lista existente y añadimos los nuevos elementos
                entidadAnalisis.getTransacciones().clear();

                entidadAnalisis.getRecomendaciones().clear();
                agregarRecomendaciones(
                    entidadAnalisis,
                    recommendationResponse
                );

                for (int i = 0; i < transaccionesCategorizadas.size(); i++) {
                    TransaccionResponse txDto = transaccionesCategorizadas.get(i);
                    TransaccionRequest txReq = transacciones.get(i); // Obtenemos la fecha original del CSV

                    TransaccionEntity txEntity = new TransaccionEntity();
                    txEntity.setDescripcion(txDto.getDescripcion());
                    txEntity.setValor(txDto.getValor());
                    txEntity.setCategoria(txDto.getCategoria());
                    txEntity.setFecha(txReq.getFecha());
                    txEntity.setAnalisisFinanciero(entidadAnalisis); // ASIGNAR LA RELACIÓN BIDIRECCIONAL

                    entidadAnalisis.getTransacciones().add(txEntity);
                }

            } else {
                // SI NO EXISTE -> Creamos un objeto completamente nuevo
                entidadAnalisis = new AnalisisFinancieroEntity();
                agregarRecomendaciones(
                    entidadAnalisis,
                    recommendationResponse
                );
                entidadAnalisis.setUsuarioNombre(nombreUsuario);
                entidadAnalisis.setIngresoMensual(ingresoMensual);
                entidadAnalisis.setNivelEndeudamiento(nivelEndeudamiento);
                entidadAnalisis.setFrecuenciaAhorro(frecuenciaAhorro);
                entidadAnalisis.setDivisa(divisa);
                entidadAnalisis.setPerfilFinanciero(perfilResponse.getPerfil_financiero());
                entidadAnalisis.setProbabilidad(perfilResponse.getProbabilidad());
                entidadAnalisis.setSaldoTotal(saldoTotalUltimoMes);
                entidadAnalisis.setGastoTotal(gastoTotalUltimoMes);

                List<TransaccionEntity> listaTransaccionesEntities = new ArrayList<>();
                for (int i = 0; i < transaccionesCategorizadas.size(); i++) {
                    TransaccionResponse txDto = transaccionesCategorizadas.get(i);
                    TransaccionRequest txReq = transacciones.get(i);

                    TransaccionEntity txEntity = new TransaccionEntity();
                    txEntity.setDescripcion(txDto.getDescripcion());
                    txEntity.setValor(txDto.getValor());
                    txEntity.setCategoria(txDto.getCategoria());
                    txEntity.setFecha(txReq.getFecha());
                    txEntity.setAnalisisFinanciero(entidadAnalisis); // ASIGNAR LA RELACIÓN BIDIRECCIONAL

                    listaTransaccionesEntities.add(txEntity);
                }

                entidadAnalisis.setTransacciones(listaTransaccionesEntities);
            }

            // Guardamos o actualizamos en MySQL
            analisisRepository.save(entidadAnalisis);

            // 5. Construir la respuesta final (DTO) para Postman / Frontend
            CsvResponse response = new CsvResponse();
            response.setIngreso_mensual(ingresoMensual);
            response.setNivel_endeudamiento(nivelEndeudamiento);
            response.setFrecuencia_ahorro(frecuenciaAhorro);
            response.setDivisa(divisa);
            response.setPerfil_financiero(perfilResponse.getPerfil_financiero());
            response.setProbabilidad(perfilResponse.getProbabilidad());
            response.setTransacciones(transaccionesCategorizadas);
            response.setRecomendaciones(
                recommendationResponse.recommendations()
                    .stream()
                    .map(result ->
                        new RecommendationResultDto(
                            result.category().name(),
                            result.recommendation()
                        )
                    )
                    .toList()
            );


            return response;

        } catch (IOException e) {
            throw new IllegalArgumentException("No se pudo leer el archivo CSV", e);
        }
    }

    private void agregarRecomendaciones(
        AnalisisFinancieroEntity entidadAnalisis,
        RecommendationResponse recommendationResponse
    ) {

        for (RecommendationResult result :
            recommendationResponse.recommendations()) {

            RecomendacionEntity recomendacion =
                new RecomendacionEntity();

            recomendacion.setCategoria(
                result.category().name()
            );

            recomendacion.setMensaje(
                result.recommendation()
            );

            recomendacion.setAnalisisFinanciero(
                entidadAnalisis
            );

            entidadAnalisis
                .getRecomendaciones()
                .add(recomendacion);
        }
    }
}