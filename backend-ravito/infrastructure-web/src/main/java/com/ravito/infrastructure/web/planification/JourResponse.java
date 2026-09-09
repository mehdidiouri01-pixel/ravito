package com.ravito.infrastructure.web.planification;

import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.nutrition.EstimerNutritionUseCase;
import com.ravito.domain.planification.Jour;
import com.ravito.domain.planification.JourSemaine;
import com.ravito.infrastructure.web.catalogue.RepasResponse;

import java.util.Optional;

/**
 * {@code petitDejeuner}/{@code dejeuner}/{@code diner} peuvent chacun etre
 * {@code null} en JSON : un plan peut etre compose partiellement (voir
 * {@link Jour}), un creneau sans repas choisi ce jour-la n'a simplement
 * rien a montrer.
 */
public record JourResponse(JourSemaine jour, RepasResponse petitDejeuner, RepasResponse dejeuner, RepasResponse diner) {

    public static JourResponse depuis(Jour jour, EstimerNutritionUseCase estimerNutritionUseCase) {
        return new JourResponse(
                jour.jourSemaine(),
                depuis(jour.petitDejeuner(), estimerNutritionUseCase),
                depuis(jour.dejeuner(), estimerNutritionUseCase),
                depuis(jour.diner(), estimerNutritionUseCase));
    }

    private static RepasResponse depuis(Optional<Repas> repas, EstimerNutritionUseCase estimerNutritionUseCase) {
        return repas.map(r -> RepasResponse.depuis(r, estimerNutritionUseCase)).orElse(null);
    }
}
