package com.mealweek.domain.courses;

import java.util.Objects;

/**
 * Ligne d'un repas : la quantite d'un ingredient necessaire pour une
 * portion.
 */
public record IngredientQuantite(Ingredient ingredient, Quantite quantite) {

    public IngredientQuantite {
        Objects.requireNonNull(ingredient, "ingredient");
        Objects.requireNonNull(quantite, "quantite");
    }
}
