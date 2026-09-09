package com.ravito.infrastructure.web.catalogue;

import com.ravito.domain.catalogue.GenererRepasUseCase;
import com.ravito.domain.nutrition.EstimerNutritionUseCase;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * Adapter d'entree REST pour {@link GenererRepasUseCase}.
 *
 * <p>Distinct de {@link ProposerRepasController} malgre le meme package :
 * chaque controleur reste dedie a un seul use case, comme le reste de
 * cette couche (voir {@code ComposerPlanSemaineController}).
 */
@RestController
@RequestMapping("/api/repas")
class GenererRepasController {

    private final GenererRepasUseCase genererRepasUseCase;
    private final EstimerNutritionUseCase estimerNutritionUseCase;

    GenererRepasController(GenererRepasUseCase genererRepasUseCase, EstimerNutritionUseCase estimerNutritionUseCase) {
        this.genererRepasUseCase = Objects.requireNonNull(genererRepasUseCase, "genererRepasUseCase");
        this.estimerNutritionUseCase = Objects.requireNonNull(estimerNutritionUseCase, "estimerNutritionUseCase");
    }

    @PostMapping("/generation")
    RepasResponse generer(@Valid @RequestBody GenererRepasRequest requete) {
        return RepasResponse.depuis(genererRepasUseCase.genererRepas(requete.profil().versDomaine(), requete.type()), estimerNutritionUseCase);
    }
}
