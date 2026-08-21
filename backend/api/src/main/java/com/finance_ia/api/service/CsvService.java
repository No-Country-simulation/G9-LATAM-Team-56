package com.finance_ia.api.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.finance_ia.api.dto.recommendation.RecommendationResult;
import com.finance_ia.api.dto.recommendation.RecommendationResultDto;
import com.finance_ia.api.model.RecomendacionEntity;
import com.finance_ia.api.model.recommendation.RecommendationResponse;
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

    // Modificamos el mtodo para que ahora reciba también el nombre de la persona
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

            // Identificar el último mes presente en el CSV
            LocalDate fechaMasReciente = transacciones.stream()
                    .map(TransaccionRequest::getFecha)
                    .max(LocalDate::compareTo)
                    .orElse(LocalDate.now());
            int mesObjetivo = fechaMasReciente.getMonthValue();
            int anioObjetivo = fechaMasReciente.getYear();

            // Definir el rango del mes actual (desde el día 1 hasta el día actual de ese mes)
            // Si el mes objetivo coincide con el mes actual del sistema, limitamos hasta LocalDate.now()
            // De lo contrario, si es un mes histórico cerrado, toma el mes completo.
            LocalDate inicioMes = LocalDate.of(anioObjetivo, mesObjetivo, 1);
            LocalDate finMes;

            LocalDate hoySistema = LocalDate.now();
            if (mesObjetivo == hoySistema.getMonthValue() && anioObjetivo == hoySistema.getYear()) {
                finMes = hoySistema; // Limita hasta el día actual del mes en curso
            } else {
                finMes = inicioMes.withDayOfMonth(inicioMes.lengthOfMonth()); // Mes completo histórico
            }

            // Filtrar transacciones del último mes pero acotadas desde el día 1 hasta el fin calculado (día actual o fin de mes)
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