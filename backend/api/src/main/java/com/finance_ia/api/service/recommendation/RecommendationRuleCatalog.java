package com.finance_ia.api.service.recommendation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.finance_ia.api.model.recommendation.ExpenseCategory;
import com.finance_ia.api.model.recommendation.FinancialProfile;
import com.finance_ia.api.model.recommendation.RecommendationKey;
import com.finance_ia.api.model.recommendation.RecommendationPriority;
import com.finance_ia.api.model.recommendation.RecommendationRule;

@Component
public class RecommendationRuleCatalog {

    private final Map<RecommendationKey, RecommendationRule> rules;

    public RecommendationRuleCatalog() {
        this.rules = buildRules();
    }

    public Optional<RecommendationRule> find(RecommendationKey key) {
        return Optional.ofNullable(rules.get(key));
    }

    public int size() {
        return rules.size();
    }

    private Map<RecommendationKey, RecommendationRule> buildRules() {
        Map<RecommendationKey, RecommendationRule> rules = new HashMap<>();

        // Vivienda
        add(rules, ExpenseCategory.VIVIENDA, FinancialProfile.SALUDABLE,
                "Mantener una planificación estable del gasto de vivienda y evitar que aumente sin justificación.",
                RecommendationPriority.BAJA);

        add(rules, ExpenseCategory.VIVIENDA, FinancialProfile.EN_OBSERVACION,
                "Revisar el peso del gasto de vivienda dentro del presupuesto y buscar oportunidades de optimización.",
                RecommendationPriority.MEDIA);

        add(rules, ExpenseCategory.VIVIENDA, FinancialProfile.RIESGO,
                "Priorizar la revisión del gasto de vivienda y evaluar alternativas para reducir la presión financiera cuando sea viable.",
                RecommendationPriority.ALTA);

        // Servicios
        add(rules, ExpenseCategory.SERVICIOS, FinancialProfile.SALUDABLE,
                "Mantener controlados los servicios mediante hábitos de consumo eficientes.",
                RecommendationPriority.BAJA);

        add(rules, ExpenseCategory.SERVICIOS, FinancialProfile.EN_OBSERVACION,
                "Revisar servicios recurrentes y reducir consumos o costos que puedan optimizarse.",
                RecommendationPriority.MEDIA);

        add(rules, ExpenseCategory.SERVICIOS, FinancialProfile.RIESGO,
                "Priorizar la reducción de servicios no esenciales y revisar contratos o consumos para disminuir gastos.",
                RecommendationPriority.ALTA);

        // Alimentación
        add(rules, ExpenseCategory.ALIMENTACION, FinancialProfile.SALUDABLE,
                "Mantener una planificación de compras que permita conservar el gasto bajo control.",
                RecommendationPriority.BAJA);

        add(rules, ExpenseCategory.ALIMENTACION, FinancialProfile.EN_OBSERVACION,
                "Revisar hábitos de compra y planificar mejor las compras para evitar gastos innecesarios.",
                RecommendationPriority.MEDIA);

        add(rules, ExpenseCategory.ALIMENTACION, FinancialProfile.RIESGO,
                "Priorizar la planificación de compras y reducir gastos evitables en alimentación sin comprometer necesidades básicas.",
                RecommendationPriority.ALTA);

        // Transporte
        add(rules, ExpenseCategory.TRANSPORTE, FinancialProfile.SALUDABLE,
                "Mantener una planificación eficiente de los gastos de transporte.",
                RecommendationPriority.BAJA);

        add(rules, ExpenseCategory.TRANSPORTE, FinancialProfile.EN_OBSERVACION,
                "Revisar alternativas de transporte y gastos recurrentes que puedan optimizarse.",
                RecommendationPriority.MEDIA);

        add(rules, ExpenseCategory.TRANSPORTE, FinancialProfile.RIESGO,
                "Priorizar alternativas de menor costo y reducir gastos de transporte evitables cuando sea posible.",
                RecommendationPriority.ALTA);

        // Restaurante
        add(rules, ExpenseCategory.RESTAURANT, FinancialProfile.SALUDABLE,
                "Mantener el gasto dentro de un presupuesto destinado a consumo fuera del hogar.",
                RecommendationPriority.BAJA);

        add(rules, ExpenseCategory.RESTAURANT, FinancialProfile.EN_OBSERVACION,
                "Reducir la frecuencia de comidas fuera del hogar y establecer un límite de gasto.",
                RecommendationPriority.MEDIA);

        add(rules, ExpenseCategory.RESTAURANT, FinancialProfile.RIESGO,
                "Priorizar la reducción de gastos en restaurantes y limitar el consumo fuera del hogar mientras se estabiliza la situación financiera.",
                RecommendationPriority.ALTA);

        // Entretenimiento
        add(rules, ExpenseCategory.ENTRETENIMIENTO, FinancialProfile.SALUDABLE,
                "Mantener un presupuesto definido para entretenimiento.",
                RecommendationPriority.BAJA);

        add(rules, ExpenseCategory.ENTRETENIMIENTO, FinancialProfile.EN_OBSERVACION,
                "Revisar gastos de entretenimiento y establecer límites para evitar afectar la capacidad de ahorro.",
                RecommendationPriority.MEDIA);

        add(rules, ExpenseCategory.ENTRETENIMIENTO, FinancialProfile.RIESGO,
                "Priorizar la reducción de gastos de entretenimiento y concentrarse temporalmente en gastos esenciales.",
                RecommendationPriority.ALTA);

        // Vestuario
        add(rules, ExpenseCategory.VESTUARIO, FinancialProfile.SALUDABLE,
                "Planificar las compras de vestuario y evitar compras innecesarias.",
                RecommendationPriority.BAJA);

        add(rules, ExpenseCategory.VESTUARIO, FinancialProfile.EN_OBSERVACION,
                "Revisar la frecuencia de compras y priorizar necesidades sobre compras no planificadas.",
                RecommendationPriority.MEDIA);

        add(rules, ExpenseCategory.VESTUARIO, FinancialProfile.RIESGO,
                "Reducir temporalmente las compras de vestuario no esenciales y priorizar únicamente necesidades.",
                RecommendationPriority.ALTA);

        // Electrónicos
        add(rules, ExpenseCategory.ELECTRONICOS, FinancialProfile.SALUDABLE,
                "Planificar las compras de electrónicos y priorizar necesidades reales.",
                RecommendationPriority.BAJA);

        add(rules, ExpenseCategory.ELECTRONICOS, FinancialProfile.EN_OBSERVACION,
                "Evitar compras impulsivas y evaluar si las adquisiciones pueden postergarse.",
                RecommendationPriority.MEDIA);

        add(rules, ExpenseCategory.ELECTRONICOS, FinancialProfile.RIESGO,
                "Postergar compras de electrónicos no esenciales y priorizar la recuperación de la estabilidad financiera.",
                RecommendationPriority.ALTA);

        // Salud
        add(rules, ExpenseCategory.SALUD, FinancialProfile.SALUDABLE,
                "Mantener una planificación preventiva para afrontar gastos de salud.",
                RecommendationPriority.BAJA);

        add(rules, ExpenseCategory.SALUD, FinancialProfile.EN_OBSERVACION,
                "Revisar la planificación de gastos médicos y procurar contar con una reserva para necesidades futuras.",
                RecommendationPriority.MEDIA);

        // Excepción: Salud + Riesgo = MEDIA
        add(rules, ExpenseCategory.SALUD, FinancialProfile.RIESGO,
                "Priorizar la planificación de gastos de salud y evitar recomendaciones que impliquen postergar necesidades médicas.",
                RecommendationPriority.MEDIA);

        // Educación
        add(rules, ExpenseCategory.EDUCACION, FinancialProfile.SALUDABLE,
                "Mantener una planificación adecuada de los gastos educativos.",
                RecommendationPriority.BAJA);

        add(rules, ExpenseCategory.EDUCACION, FinancialProfile.EN_OBSERVACION,
                "Revisar el impacto de los gastos educativos en el presupuesto y planificar pagos futuros.",
                RecommendationPriority.MEDIA);

        // Excepción: Educación + Riesgo = MEDIA
        add(rules, ExpenseCategory.EDUCACION, FinancialProfile.RIESGO,
                "Priorizar la sostenibilidad del gasto educativo y revisar alternativas de financiamiento o planificación cuando corresponda.",
                RecommendationPriority.MEDIA);

        // Otros
        add(rules, ExpenseCategory.OTROS, FinancialProfile.SALUDABLE,
                "Revisar periódicamente los gastos clasificados como \"Otros\" para mantenerlos identificados.",
                RecommendationPriority.BAJA);

        add(rules, ExpenseCategory.OTROS, FinancialProfile.EN_OBSERVACION,
                "Analizar y desglosar los gastos \"Otros\" para identificar oportunidades de control.",
                RecommendationPriority.MEDIA);

        add(rules, ExpenseCategory.OTROS, FinancialProfile.RIESGO,
                "Priorizar la identificación y revisión de los gastos \"Otros\", especialmente cuando estén contribuyendo a la presión financiera.",
                RecommendationPriority.ALTA);

        return Map.copyOf(rules);
    }

    private void add(
            Map<RecommendationKey, RecommendationRule> rules,
            ExpenseCategory category,
            FinancialProfile profile,
            String recommendation,
            RecommendationPriority priority
    ) {
        rules.put(
                new RecommendationKey(category, profile),
                new RecommendationRule(recommendation, priority)
        );
    }
}
