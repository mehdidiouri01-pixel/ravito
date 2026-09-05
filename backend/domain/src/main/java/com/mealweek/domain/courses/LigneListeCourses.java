package com.mealweek.domain.courses;

import java.util.Objects;

/**
 * Une ligne de la liste de courses : la quantite totale d'un ingredient
 * necessaire sur l'ensemble du plan de semaine, apres consolidation des
 * repas qui le partagent (voir {@link ListeCourses#consolider}).
 */
public record LigneListeCourses(Ingredient ingredient, Quantite quantiteTotale) {

    public LigneListeCourses {
        Objects.requireNonNull(ingredient, "ingredient");
        Objects.requireNonNull(quantiteTotale, "quantiteTotale");
    }
}
