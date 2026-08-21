package com.finance_ia.api.service.recommendation;

import com.finance_ia.api.config.RecommendationProperties;
import com.finance_ia.api.dto.recommendation.RecommendationResult;
import com.finance_ia.api.model.recommendation.ExpenseCategory;
import com.finance_ia.api.model.recommendation.RecommendationCandidate;
import com.finance_ia.api.model.recommendation.RecommendationKey;
import com.finance_ia.api.model.recommendation.RecommendationRequest;
import com.finance_ia.api.model.recommendation.RecommendationResponse;
import com.finance_ia.api.model.recommendation.RecommendationRule;
import com.finance_ia.api.model.recommendation.TopCategory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class RecommendationService {

    private final RecommendationRuleCatalog catalog;
    private final int maxRecommendations;

    public RecommendationService(
            RecommendationRuleCatalog catalog,
            RecommendationProperties properties
    ) {
        if (catalog == null) {
            throw new IllegalArgumentException(
                    "El catálogo de reglas es obligatorio"
            );
        }

        if (properties == null) {
            throw new IllegalArgumentException(
                    "La configuración de recomendaciones es obligatoria"
            );
        }

        if (properties.getMaxRecommendations() < 1) {
            throw new IllegalArgumentException(
                    "MAX_RECOMENDACIONES debe ser mayor que cero"
            );
        }

        this.catalog = catalog;
        this.maxRecommendations = properties.getMaxRecommendations();
    }

    /**
     * Genera recomendaciones a partir del perfil financiero y las
     * categorías que forman parte del TOP N de gastos.
     *
     * <p>Las recomendaciones se ordenan primero por prioridad y,
     * en caso de empate, por la posición original dentro del TOP N.
     * Finalmente se aplica el máximo configurado de recomendaciones.</p>
     *
     * @param request entrada del motor de recomendaciones
     * @return recomendaciones seleccionadas y ordenadas
     */
    public RecommendationResponse generateRecommendations(
        RecommendationRequest request
    ) {
        if (request == null) {
            throw new IllegalArgumentException(
                "La solicitud de recomendaciones es obligatoria"
            );
        }

        List<RecommendationCandidate> candidates =
            createCandidates(request);

        sortCandidates(candidates);

        List<RecommendationResult> recommendations =
            limitAndExtractRecommendations(candidates);

        return new RecommendationResponse(recommendations);
    }

    /**
     * Convierte cada categoría del TOP N en un candidato utilizando
     * la regla correspondiente al perfil financiero.
     */
    private List<RecommendationCandidate> createCandidates(RecommendationRequest request) {
        List<RecommendationCandidate> candidates = new ArrayList<>();

        for (TopCategory topCategory : request.topCategories()) {

            ExpenseCategory category = topCategory.category();

            RecommendationKey key = new RecommendationKey(
                    category,
                    request.profile()
            );

            RecommendationRule rule = catalog.find(key)
                    .orElseThrow(() -> new IllegalStateException(
                            "No existe una regla para la combinación: "
                                    + category + " + "
                                    + request.profile()
                    ));

            candidates.add(
                    new RecommendationCandidate(
                            category,
                            topCategory.position(),
                            rule.recommendation(),
                            rule.priority()
                    )
            );
        }

        return candidates;
    }

    /**
     * La prioridad es el primer criterio de ordenamiento.
     * En caso de empate, la posición original dentro del TOP N
     * determina cuál recomendación tiene precedencia.
     */
    private void sortCandidates(
            List<RecommendationCandidate> candidates
    ) {
        candidates.sort(
                Comparator
                        .comparing(
                                RecommendationCandidate::priority
                        )
                        .thenComparing(
                                RecommendationCandidate::position
                        )
        );
    }

    /**
     * El motor devuelve como máximo MAX_RECOMENDACIONES.
     * Si existen menos candidatos que el máximo configurado,
     * se devuelven todos los candidatos disponibles.
     */
    private List<RecommendationResult> limitAndExtractRecommendations(
        List<RecommendationCandidate> candidates
    ) {
        return candidates.stream()
            .limit(maxRecommendations)
            .map(candidate ->
                new RecommendationResult(
                    candidate.category(),
                    candidate.recommendation()
                )
            )
            .toList();
    }
}