package com.finance_ia.api.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.finance_ia.api.dto.AnalisisFinancieroRequest;
import com.finance_ia.api.dto.AnalisisFinancieroResponse;

import ai.onnxruntime.OnnxMap;
import ai.onnxruntime.OnnxSequence;
import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import jakarta.annotation.PostConstruct;

@Service
public class ClasificadorCategoriasOnnxService {

    private OrtEnvironment env;
    private OrtSession session;

    // Este metodo se ejecuta automaticamente al iniciar Spring Boot
    @PostConstruct
    public void init() {
        try {
            // 1. Inicializar el entorno de ONNX Runtime
            env = OrtEnvironment.getEnvironment();

            // 2. Cargar el archivo .onnx desde la carpeta resources/models/
            File modelFile = new ClassPathResource(
                    "models/clasificador_perfil_financiero.onnx"
            ).getFile();

            // 3. Crear la sesion de inferencia
            session = env.createSession(
                    modelFile.getAbsolutePath()
            );

            System.out.println(
                    "Modelo clasificador de perfil financiero (.ONNX) cargado exitosamente en memoria."
            );

            // --- INSPECCION AUTOMATICA DE ENTRADAS Y SALIDAS ---
            System.out.println("--- INFORMACIÓN DEL MODELO ONNX ---");

            session.getInputInfo().forEach(
                    (name, nodeInfo)
                    -> System.out.println(
                            "📥 Entrada esperada -> Nombre: "
                            + name
                            + " | Tipo: "
                            + nodeInfo.getInfo().toString()
                    )
            );

            session.getOutputInfo().forEach(
                    (name, nodeInfo)
                    -> System.out.println(
                            "📤 Salida generada -> Nombre: "
                            + name
                            + " | Tipo: "
                            + nodeInfo.getInfo().toString()
                    )
            );

            System.out.println("-----------------------------------");

        } catch (IOException | OrtException e) {
            System.err.println(
                    "Error al cargar el modelo clasificador de perfil financiero (.ONNX): "
                    + e.getMessage()
            );
        }
    }

    public AnalisisFinancieroResponse predecir(AnalisisFinancieroRequest request) {

        try {

            Map<String, OnnxTensor> inputs = new HashMap<>();

            // gasto_total
            inputs.put(
                    "gasto_total",
                    OnnxTensor.createTensor(
                            env,
                            new float[][]{
                                {
                                    request.getGasto_total().floatValue()
                                }
                            }
                    )
            );

            // ingreso_mensual
            inputs.put(
                    "ingreso_mensual",
                    OnnxTensor.createTensor(
                            env,
                            new float[][]{
                                {
                                    request.getIngreso_mensual().floatValue()
                                }
                            }
                    )
            );

            // nivel_endeudamiento
            inputs.put(
                    "nivel_endeudamiento",
                    OnnxTensor.createTensor(
                            env,
                            new float[][]{
                                {
                                    request.getNivel_endeudamiento().floatValue()
                                }
                            }
                    )
            );

            // frecuencia_ahorro
            inputs.put(
                    "frecuencia_ahorro",
                    OnnxTensor.createTensor(
                            env,
                            new String[][]{
                                {
                                    request.getFrecuencia_ahorro()
                                }
                            }
                    )
            );

            // Ejecutar modelo
            OrtSession.Result resultado = session.run(inputs);

            // Obtener etiqueta predicha
            OnnxTensor labelTensor = (OnnxTensor) resultado
                    .get("output_label")
                    .get();

            String[] labels = (String[]) labelTensor.getValue();

            String perfil = labels[0];

            // Obtener probabilidades
            OnnxSequence probabilitySequence = (OnnxSequence) resultado
                    .get("output_probability")
                    .get();

            // La secuencia contiene un solo elemento que es un mapa
            @SuppressWarnings("unchecked")
            List<OnnxMap> listaMapas = (List<OnnxMap>) probabilitySequence.getValue();

            // Obtener el mapa de probabilidades
            @SuppressWarnings("unchecked")
            Map<String, Float> probabilidades
                    = (Map<String, Float>) listaMapas.get(0).getValue();

            // Obtener probabilidad de la clase predicha
            Float probabilidad = probabilidades.get(perfil);

            // Crear respuesta
            AnalisisFinancieroResponse response
                    = new AnalisisFinancieroResponse();

            response.setPerfil_financiero(perfil);

            response.setProbabilidad(
                    probabilidad != null
                            ? probabilidad.doubleValue()
                            : null
            );

            return response;

        } catch (OrtException e) {

            throw new RuntimeException(
                    "Error ejecutando inferencia ONNX: "
                    + e.getMessage()
            );
        }
    }

    // Metodo de ejemplo para realizar predicciones/inferencias
    public String ejecutarInferencia() {
        if (session != null) {
            return "El modelo clasificador de perfil financiero (.ONNX) está activo y listo para procesar datos.";
        }
        return "El modelo clasificador de perfil financiero (.ONNX) no está disponible.";
    }
}
