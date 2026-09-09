package com.ravito.infrastructure.web.catalogue;

import com.ravito.domain.courses.Ingredient;
import com.ravito.domain.courses.IngredientQuantite;
import com.ravito.domain.courses.Quantite;
import com.ravito.domain.courses.RayonMagasin;
import com.ravito.domain.courses.UniteMesure;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Corps JSON d'une ligne d'ingredient au sein d'un repas transmis en entier
 * par le client (voir {@link RepasGenereRequest}) — pendant en entree de
 * {@link IngredientQuantiteResponse}.
 */
public record IngredientQuantiteRequest(
        @NotBlank String ingredient,
        @NotNull RayonMagasin rayon,
        @NotNull @DecimalMin(value = "0", inclusive = false) BigDecimal quantite,
        @NotNull UniteMesure unite) {

    public IngredientQuantite versDomaine() {
        return new IngredientQuantite(new Ingredient(ingredient, rayon), new Quantite(quantite, unite));
    }
}
