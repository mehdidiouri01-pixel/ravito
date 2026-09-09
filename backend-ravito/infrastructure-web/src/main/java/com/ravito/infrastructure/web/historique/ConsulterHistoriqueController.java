package com.ravito.infrastructure.web.historique;

import com.ravito.domain.historique.ConsulterHistoriqueUseCase;
import com.ravito.domain.historique.HistoriquePlanId;
import com.ravito.domain.historique.HistoriquePlanIntrouvableException;
import com.ravito.domain.nutrition.EstimerNutritionUseCase;
import com.ravito.domain.prix.EstimerCoutUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Adapter d'entree REST pour {@link ConsulterHistoriqueUseCase}.
 */
@RestController
@RequestMapping("/api/plans-semaine/historique")
class ConsulterHistoriqueController {

    private final ConsulterHistoriqueUseCase consulterHistoriqueUseCase;
    private final EstimerNutritionUseCase estimerNutritionUseCase;
    private final EstimerCoutUseCase estimerCoutUseCase;

    ConsulterHistoriqueController(ConsulterHistoriqueUseCase consulterHistoriqueUseCase,
                                   EstimerNutritionUseCase estimerNutritionUseCase,
                                   EstimerCoutUseCase estimerCoutUseCase) {
        this.consulterHistoriqueUseCase = Objects.requireNonNull(consulterHistoriqueUseCase, "consulterHistoriqueUseCase");
        this.estimerNutritionUseCase = Objects.requireNonNull(estimerNutritionUseCase, "estimerNutritionUseCase");
        this.estimerCoutUseCase = Objects.requireNonNull(estimerCoutUseCase, "estimerCoutUseCase");
    }

    @GetMapping
    List<HistoriquePlanResumeResponse> lister() {
        return consulterHistoriqueUseCase.lister().stream().map(HistoriquePlanResumeResponse::depuis).toList();
    }

    @GetMapping("/{id}")
    HistoriquePlanDetailResponse detail(@PathVariable("id") UUID id) {
        HistoriquePlanId historiquePlanId = new HistoriquePlanId(id);
        return consulterHistoriqueUseCase.parId(historiquePlanId)
                .map(historise -> HistoriquePlanDetailResponse.depuis(historise, estimerNutritionUseCase, estimerCoutUseCase))
                .orElseThrow(() -> new HistoriquePlanIntrouvableException(historiquePlanId));
    }
}
