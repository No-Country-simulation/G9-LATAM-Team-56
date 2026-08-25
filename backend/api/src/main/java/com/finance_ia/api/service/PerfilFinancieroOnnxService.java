package com.finance_ia.api.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.finance_ia.api.dto.PerfilFinancieroRequest;
import com.finance_ia.api.dto.PerfilFinancieroResponse;

import ai.onnxruntime.OnnxMap;
import ai.onnxruntime.OnnxSequence;
import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import jakarta.annotation.PostConstruct;

@Service
public class PerfilFinancieroOnnxService {

    private OrtEnvironment env;
    private OrtSession session;

    @PostConstruct
    public void init() {

        try {

            env = OrtEnvironment.getEnvironment();

            ClassPathResource resource
                    = new ClassPathResource(
                            "models/clasificador_perfil_financiero.onnx"
                    );

            session = env.createSession(
                    resource.getInputStream().readAllBytes(),
                    new OrtSession.SessionOptions()
            );

            System.out.println(
                    "Modelo clasificador de perfil financiero (.ONNX) cargado exitosamente."
            );

            System.out.println("--- INFORMACIÓN DEL MODELO ONNX ---");

            session.getInputInfo().forEach(
                    (name, nodeInfo)
                    -> System.out.println(
                            "📥 Entrada -> "
                            + name
                            + " | "
                            + nodeInfo.getInfo()
                    )
            );

            session.getOutputInfo().forEach(
                    (name, nodeInfo)
                    -> System.out.println(
                            "📤 Salida -> "
                            + name
                            + " | "
                            + nodeInfo.getInfo()
                    )
            );

            System.out.println("-----------------------------------");

        } catch (IOException | OrtException e) {

            throw new RuntimeException(
                    "No fue posible cargar el modelo de perfil financiero.",
                    e
            );
        }
    }

    public PerfilFinancieroResponse predecirPerfil(
            PerfilFinancieroRequest request
    ) {

        try {

            Map<String, OnnxTensor> inputs = new HashMap<>();

            // Cálculo del ratio financiero (gasto_mensual / ingreso_mensual).
            // Se usa doubleValue() y se valida que el ingreso no sea nulo o cero para evitar excepciones aritméticas (Division by zero).
            double gasto = request.getGasto_total() != null ? request.getGasto_total().doubleValue() : 0.0;
            double ingreso = request.getIngreso_mensual() != null ? request.getIngreso_mensual().doubleValue() : 0.0;
            double ratioCalculado = (ingreso > 0.0) ? (gasto / ingreso) : 0.0;

            // Se elimina la entrada separada de "gasto_mensual" e "ingreso_mensual"
            // y se reemplazan por la única entrada "ratio" requerida por la nueva versión del modelo ONNX.
            inputs.put(
                    "ratio",
                    OnnxTensor.createTensor(
                            env,
                            new float[][]{
                                    {(float) ratioCalculado}
                            }
                    )
            );

            inputs.put(
                    "nivel_endeudamiento",
                    OnnxTensor.createTensor(
                            env,
                            new float[][]{
                                {request.getNivel_endeudamiento().floatValue()}
                            }
                    )
            );

            inputs.put(
                    "frecuencia_ahorro",
                    OnnxTensor.createTensor(
                            env,
                            new String[][]{
                                {request.getFrecuencia_ahorro()}
                            }
                    )
            );

            OrtSession.Result resultado = session.run(inputs);

            OnnxTensor labelTensor
                    = (OnnxTensor) resultado.get("output_label").get();

            String perfil
                    = ((String[]) labelTensor.getValue())[0];

            OnnxSequence probabilitySequence
                    = (OnnxSequence) resultado.get("output_probability").get();

            @SuppressWarnings("unchecked")
            List<OnnxMap> listaMapas
                    = (List<OnnxMap>) probabilitySequence.getValue();

            @SuppressWarnings("unchecked")
            Map<String, Float> probabilidades
                    = (Map<String, Float>) listaMapas.get(0).getValue();

            Float probabilidad = probabilidades.get(perfil);

            PerfilFinancieroResponse response
                    = new PerfilFinancieroResponse();

            response.setPerfil_financiero(perfil);

            response.setProbabilidad(
                    probabilidad != null
                            ? probabilidad.doubleValue()
                            : null
            );

            return response;

        } catch (OrtException e) {

            throw new RuntimeException(
                    "Error ejecutando el modelo de perfil financiero.",
                    e
            );
        }
    }

    public boolean modeloDisponible() {
        return session != null;
    }

}
