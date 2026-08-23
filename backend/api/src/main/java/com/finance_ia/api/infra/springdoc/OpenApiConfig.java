package com.finance_ia.api.infra.springdoc;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI financeAiOpenAPI() {
        Server ociServer = new Server()
                .url("http://148.116.110.236")
                .description("Servidor de Producción (OCI Compute)");

        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Servidor de Desarrollo Local");

        return new OpenAPI()
                .info(new Info()
                        .title("FinanceAI API")
                        .version("1.0.0")
                        .description("""
                                ### API REST para análisis inteligente de salud financiera
                                
                                FinanceAI transforma datos financieros sin procesar en conocimiento útil mediante Machine Learning.
                                
                                **Funcionalidades principales:**
                                * **Clasificación Automática:** Categorización de transacciones en rubros financieros.
                                * **Perfil Financiero:** Evaluación del nivel de riesgo (Saludable, En observación, En riesgo).
                                * **Recomendaciones Personalizadas:** Sugerencias automáticas basadas en hábitos de gasto.
                                * **Procesamiento Masivo:** Carga e ingesta de transacciones en lote vía archivos CSV.
                                
                                _Proyecto desarrollado por el Equipo 56 (G9 LATAM - No Country) para el Hackathon ONE (Alura + Oracle) utilizando modelos ONNX alojados en infraestructura Oracle Cloud (OCI)._
                                """)
                        .contact(new Contact()
                                .name("Equipo 56 (G9 LATAM - No Country)")
                                .url("https://github.com/No-Country-simulation/G9-LATAM-Team-56"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(ociServer, localServer));
    }
}