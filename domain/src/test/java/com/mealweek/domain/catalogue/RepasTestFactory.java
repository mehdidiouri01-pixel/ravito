package com.mealweek.domain.catalogue;

import com.mealweek.domain.courses.Ingredient;
import com.mealweek.domain.courses.IngredientQuantite;
import com.mealweek.domain.courses.Quantite;
import com.mealweek.domain.courses.RayonMagasin;
import com.mealweek.domain.courses.UniteMesure;
import com.mealweek.domain.profil.NiveauCuisine;
import com.mealweek.domain.profil.StyleAlimentaire;

import java.math.BigDecimal;
import java.util.List;

/**
 * Fabrique de {@link Repas} valides pour les tests, avec un unique
 * ingredient sans importance pour le cas teste.
 */
final class RepasTestFactory {

    private RepasTestFactory() {
    }

    static Repas unRepas(TypeRepas type, StyleAlimentaire style, NiveauCuisine niveauRequis) {
        return unRepas("repas de test", type, style, niveauRequis);
    }

    static Repas unRepas(String nom, TypeRepas type, StyleAlimentaire style, NiveauCuisine niveauRequis) {
        IngredientQuantite ingredient = new IngredientQuantite(
                new Ingredient("ingredient de test", RayonMagasin.EPICERIE),
                new Quantite(BigDecimal.ONE, UniteMesure.UNITE));

        return new Repas(RepasId.nouveau(), nom, type, style, niveauRequis, List.of(ingredient));
    }
}
