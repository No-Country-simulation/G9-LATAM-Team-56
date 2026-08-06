package com.finance_ia.api.service;

import ai.onnxruntime.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Service
public class OnnxService {

    private OrtEnvironment env;
    private OrtSession session;

    // Este metodo se ejecuta automaticamente al iniciar Spring Boot
    @PostConstruct
    public void init() {
        try {
            // 1. Inicializar el entorno de ONNX Runtime
            env = OrtEnvironment.getEnvironment();

            // 2. Cargar el archivo .onnx desde la carpeta resources/models/
            File modelFile = new ClassPathResource("models/clasificador_perfil_financiero.onnx").getFile();

            // 3. Crear la sesion de inferencia
            session = env.createSession(modelFile.getAbsolutePath());

            System.out.println("Modelo clasificador de perfil financiero (.ONNX) cargado exitosamente en memoria.");

            // --- INSPECCION AUTOMATICA DE ENTRADAS Y SALIDAS (verificacion de carga en memoria)---
            System.out.println("--- INFORMACIÓN DEL MODELO ONNX ---");

            // Ver nombres y tipos de las ENTRADAS que exige el modelo
            session.getInputInfo().forEach((name, nodeInfo) -> {
                System.out.println("📥 Entrada esperada -> Nombre: " + name + " | Tipo: " + nodeInfo.getInfo().toString());
            });

            // Ver nombres y tipos de las SALIDAS que devuelve el modelo
            session.getOutputInfo().forEach((name, nodeInfo) -> {
                System.out.println("📤 Salida generada -> Nombre: " + name + " | Tipo: " + nodeInfo.getInfo().toString());
            });
            System.out.println("-----------------------------------");

        } catch (IOException | OrtException e) {
            System.err.println("Error al cargar el modelo clasificador de perfil financiero (.ONNX): " + e.getMessage());
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
