package com.ravito.infrastructure.web.planification;

import com.ravito.domain.nutrition.EstimerNutritionUseCase;
import com.ravito.domain.planification.Jour;
import com.ravito.domain.planification.JourSemaine;
import com.ravito.infrastructure.web.catalogue.RepasResponse;

public record JourResponse(JourSemaine jour, RepasResponse petitDejeuner, RepasResponse dejeuner, RepasResponse diner) {

    public static JourResponse depuis(Jour jour, EstimerNutritionUseCase estimerNutritionUseCase) {
        return new JourResponse(
                jour.jourSemaine(),
                RepasResponse.depuis(jour.petitDejeuner(), estimerNutritionUseCase),
                RepasResponse.depuis(jour.dejeuner(), estimerNutritionUseCase),
                RepasResponse.depuis(jour.diner(), estimerNutritionUseCase));
    }
}
