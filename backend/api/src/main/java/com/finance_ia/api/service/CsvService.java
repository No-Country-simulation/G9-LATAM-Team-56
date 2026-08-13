package com.finance_ia.api.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
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

@Service
public class CsvService {

    private final AnalisisFinancieroService analisisFinancieroService;

    public CsvService(
            AnalisisFinancieroService analisisFinancieroService
    ) {
        this.analisisFinancieroService = analisisFinancieroService;
    }

    public CsvResponse analizarCsv(MultipartFile file) {

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

            List<TransaccionRequest> transacciones =
                    new ArrayList<>();

            double ingresoMensual = 0;
            double nivelEndeudamiento = 0;
            String frecuenciaAhorro = null;

            boolean primeraFila = true;

            for (CSVRecord record : parser) {

                if (primeraFila) {

                    ingresoMensual = Double.parseDouble(
                            record.get("ingreso_mensual")
                    );

                    nivelEndeudamiento = Double.parseDouble(
                            record.get("nivel_endeudamiento")
                    );

                    frecuenciaAhorro =
                            record.get("frecuencia_ahorro");

                    primeraFila = false;
                }

                TransaccionRequest transaccion =
                        new TransaccionRequest();

                transaccion.setDescripcion(
                        record.get("descripcion")
                );

                transaccion.setValor(
                        Double.parseDouble(
                                record.get("valor")
                        )
                );

                transaccion.setFecha(
                        record.get("fecha")
                );

                transacciones.add(transaccion);
            }

            /*
             * 1. Clasificar las transacciones
             */
            ClasificacionTransaccionesRequest clasificacionRequest =
                    new ClasificacionTransaccionesRequest();

            clasificacionRequest.setTransacciones(transacciones);

            ClasificacionTransaccionesResponse clasificacionResponse =
                    analisisFinancieroService.clasificarTransacciones(
                            clasificacionRequest
                    );

            List<TransaccionResponse> transaccionesCategorizadas =
                    clasificacionResponse.getTransacciones();

            /*
             * 2. Calcular gasto total
             */
            double gastoTotal = 0.0;

            for (TransaccionResponse transaccion :
                    transaccionesCategorizadas) {

                gastoTotal += transaccion.getValor();
            }

            /*
             * 3. Obtener perfil financiero
             */
            PerfilFinancieroRequest perfilRequest =
                    new PerfilFinancieroRequest();

            perfilRequest.setIngreso_mensual(ingresoMensual);
            perfilRequest.setNivel_endeudamiento(nivelEndeudamiento);
            perfilRequest.setFrecuencia_ahorro(frecuenciaAhorro);
            perfilRequest.setGasto_total(gastoTotal);

            PerfilFinancieroResponse perfilResponse =
                    analisisFinancieroService.obtenerPerfil(
                            perfilRequest
                    );

            /*
             * 4. Construir respuesta
             */
            CsvResponse response = new CsvResponse();

            response.setIngreso_mensual(ingresoMensual);
            response.setNivel_endeudamiento(nivelEndeudamiento);
            response.setFrecuencia_ahorro(frecuenciaAhorro);

            response.setPerfil_financiero(
                    perfilResponse.getPerfil_financiero()
            );

            response.setProbabilidad(
                    perfilResponse.getProbabilidad()
            );

            response.setTransacciones(
                    transaccionesCategorizadas
            );

            return response;

        } catch (IOException e) {

            throw new IllegalArgumentException(
                    "No se pudo leer el archivo CSV",
                    e
            );
        }
    }
}