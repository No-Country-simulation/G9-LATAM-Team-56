package com.finance_ia.api.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.finance_ia.api.dto.ClasificacionTransaccionesRequest;
import com.finance_ia.api.dto.ClasificacionTransaccionesResponse;
import com.finance_ia.api.dto.TransaccionRequest;
import com.finance_ia.api.dto.TransaccionResponse;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;

@Service
public class ClasificadorTransaccionesOnnxService {

    private final OrtEnvironment environment;
    private final OrtSession session;

    public ClasificadorTransaccionesOnnxService() throws Exception {

        environment = OrtEnvironment.getEnvironment();

        ClassPathResource resource =
                new ClassPathResource(
                        "models/clasificador_transacciones.onnx"
                );

        session = environment.createSession(
                resource.getInputStream().readAllBytes(),
                new OrtSession.SessionOptions()
        );
    }

    public ClasificacionTransaccionesResponse clasificar(
            ClasificacionTransaccionesRequest request) {

        try {

            List<TransaccionResponse> transaccionesCategorizadas =
                    new ArrayList<>();

            for (TransaccionRequest transaccion
                    : request.getTransacciones()) {

                String categoria = clasificarDescripcion(
                        transaccion.getDescripcion()
                );

                TransaccionResponse response =
                        new TransaccionResponse(
                                transaccion.getDescripcion(),
                                transaccion.getValor(),
                                categoria
                        );

                transaccionesCategorizadas.add(response);
            }

            return new ClasificacionTransaccionesResponse(
                    transaccionesCategorizadas
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error al clasificar las transacciones",
                    e
            );
        }
    }

    private String clasificarDescripcion(
            String descripcion) throws Exception {

        try (
                OnnxTensor input = OnnxTensor.createTensor(
                        environment,
                        new String[][]{{descripcion}}
                )
        ) {

            Map<String, OnnxTensor> inputs =
                    Collections.singletonMap(
                            "descripcion",
                            input
                    );

            try (OrtSession.Result result = session.run(inputs)) {

                Object output = result.get(0).getValue();

                if (output instanceof String[]) {
                    return ((String[]) output)[0];
                }

                if (output instanceof String[][]) {
                    return ((String[][]) output)[0][0];
                }

                throw new RuntimeException(
                        "Formato de salida ONNX inesperado: "
                                + output.getClass()
                );
            }
        }
    }
}

