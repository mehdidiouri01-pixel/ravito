package com.ravito.infrastructure.web.catalogue;

import com.ravito.domain.catalogue.RepasProposes;
import com.ravito.domain.nutrition.EstimerNutritionUseCase;

import java.util.List;

public record RepasProposesResponse(List<RepasResponse> petitsDejeuners, List<RepasResponse> dejeuners, List<RepasResponse> diners) {

    public static RepasProposesResponse depuis(RepasProposes repasProposes, EstimerNutritionUseCase estimerNutritionUseCase) {
        return new RepasProposesResponse(
                repasProposes.petitsDejeuners().stream().map(repas -> RepasResponse.depuis(repas, estimerNutritionUseCase)).toList(),
                repasProposes.dejeuners().stream().map(repas -> RepasResponse.depuis(repas, estimerNutritionUseCase)).toList(),
                repasProposes.diners().stream().map(repas -> RepasResponse.depuis(repas, estimerNutritionUseCase)).toList());
    }
}
