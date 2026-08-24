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

            inputs.put(
                    "gasto_mensual",
                    OnnxTensor.createTensor(
                            env,
                            new float[][]{
                                {request.getGasto_total().floatValue()}
                            }
                    )
            );

            inputs.put(
                    "ingreso_mensual",
                    OnnxTensor.createTensor(
                            env,
                            new float[][]{
                                {request.getIngreso_mensual().floatValue()}
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
