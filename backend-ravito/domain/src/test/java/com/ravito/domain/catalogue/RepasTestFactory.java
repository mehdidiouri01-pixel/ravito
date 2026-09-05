package com.ravito.domain.catalogue;

import com.ravito.domain.courses.Ingredient;
import com.ravito.domain.courses.IngredientQuantite;
import com.ravito.domain.courses.Quantite;
import com.ravito.domain.courses.RayonMagasin;
import com.ravito.domain.courses.UniteMesure;
import com.ravito.domain.profil.NiveauCuisine;
import com.ravito.domain.profil.StyleAlimentaire;

import java.math.BigDecimal;
import java.util.List;

/**
 * Fabrique de {@link Repas} valides pour les tests, avec un unique
 * ingredient sans importance pour le cas teste.
 *
 * <p>Publique pour etre reutilisee par les tests d'autres packages (ex:
 * {@code planification}) qui ont besoin de construire des repas valides
 * sans repeter le detail de leurs ingredients.
 */
public final class RepasTestFactory {

    private RepasTestFactory() {
    }

    public static Repas unRepas(TypeRepas type, StyleAlimentaire style, NiveauCuisine niveauRequis) {
        return unRepas("repas de test", type, style, niveauRequis);
    }

    public static Repas unRepas(String nom, TypeRepas type, StyleAlimentaire style, NiveauCuisine niveauRequis) {
        IngredientQuantite ingredient = new IngredientQuantite(
                new Ingredient("ingredient de test", RayonMagasin.EPICERIE),
                new Quantite(BigDecimal.ONE, UniteMesure.UNITE));

        return new Repas(RepasId.nouveau(), nom, type, style, niveauRequis, List.of(ingredient));
    }
}
