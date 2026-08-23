package com.finance_ia.api.dto;

import com.finance_ia.api.dto.recommendation.RecommendationResultDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(
        name = "DashboardResponse",
        description = "Información financiera consolidada de un usuario para la visualización del dashboard."
)
public record DashboardResponse(

        @Schema(
                description = "Perfil financiero identificado para el usuario.",
                example = "EN_OBSERVACION"
        )
        String perfil_financiero,

        @Schema(
                description = "Probabilidad asociada al perfil financiero identificado.",
                example = "0.82"
        )
        Double probabilidad,

        @Schema(
                description = "Ingreso mensual del usuario.",
                example = "4500.0"
        )
        Double ingreso_mensual,

        @Schema(
                description = "Nivel de endeudamiento del usuario expresado como porcentaje.",
                example = "25.0"
        )
        Double nivel_endeudamiento,

        @Schema(
                description = "Frecuencia con la que el usuario realiza ahorros.",
                example = "Media"
        )
        String frecuencia_ahorro,

        @Schema(
                description = "Divisa utilizada para los valores financieros.",
                example = "BOB"
        )
        String divisa,

        @Schema(
                description = "Saldo total calculado a partir de la información financiera del usuario.",
                example = "1250.50"
        )
        Double saldo_total,

        @Schema(
                description = "Resumen de los gastos agrupados por categoría.",
                example = """
                        {
                          "Alimentacion": 420.0,
                          "Transporte": 300.0,
                          "Entretenimiento": 40.0
                        }
                        """
        )
        Map<String, Double> resumen_gastos,

        @Schema(
                description = "Lista de transacciones financieras procesadas."
        )
        List<DashboardTransaccionDto> transacciones,

        @Schema(
                description = "Recomendaciones financieras generadas a partir del análisis."
        )
        List<RecommendationResultDto> recomendaciones

) {

    public DashboardResponse {
        if (perfil_financiero == null) {
            throw new IllegalArgumentException(
                    "El perfil financiero es obligatorio"
            );
        }

        if (probabilidad == null) {
            throw new IllegalArgumentException(
                    "La probabilidad es obligatoria"
            );
        }

        if (ingreso_mensual == null) {
            throw new IllegalArgumentException(
                    "El ingreso mensual es obligatorio"
            );
        }

        if (nivel_endeudamiento == null) {
            throw new IllegalArgumentException(
                    "El nivel de endeudamiento es obligatorio"
            );
        }

        if (frecuencia_ahorro == null) {
            throw new IllegalArgumentException(
                    "La frecuencia de ahorro es obligatoria"
            );

        }

        if (saldo_total == null) {
            throw new IllegalArgumentException(
                    "El saldo total es obligatorio"
            );
        }

        if (resumen_gastos == null) {
            throw new IllegalArgumentException(
                    "El resumen de gastos es obligatorio"
            );
        }

        if (transacciones == null) {
            throw new IllegalArgumentException(
                    "La lista de transacciones es obligatoria"
            );
        }

        if (recomendaciones == null) {
            throw new IllegalArgumentException(
                    "La lista de recomendaciones es obligatoria"
            );
        }

        recomendaciones = List.copyOf(recomendaciones);
    }
}
