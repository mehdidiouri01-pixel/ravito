package com.ravito.infrastructure.web.catalogue;

import com.ravito.domain.courses.IngredientQuantite;
import com.ravito.domain.courses.RayonMagasin;
import com.ravito.domain.courses.UniteMesure;

import java.math.BigDecimal;

public record IngredientQuantiteResponse(String ingredient, RayonMagasin rayon, BigDecimal quantite, UniteMesure unite) {

    public static IngredientQuantiteResponse depuis(IngredientQuantite ingredientQuantite) {
        return new IngredientQuantiteResponse(
                ingredientQuantite.ingredient().nom(),
                ingredientQuantite.ingredient().rayon(),
                ingredientQuantite.quantite().valeur(),
                ingredientQuantite.quantite().unite());
    }
}
