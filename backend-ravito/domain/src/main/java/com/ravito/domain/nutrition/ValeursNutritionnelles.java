package com.ravito.domain.nutrition;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Estimation des valeurs nutritionnelles d'un repas : calories (kcal) et les
 * trois macronutriments usuels, plus les fibres — tous en grammes sauf les
 * calories (voir {@link EstimationNutritionPort} pour l'origine du calcul).
 *
 * <p>Volontairement pas de vitamines/mineraux dans cette v1 : le catalogue
 * de reference par ingredient n'en porte pas, et rien ne les demande
 * encore. Une future extension pourrait les ajouter sans casser ce type,
 * simplement en lui ajoutant des composants.
 */
public record ValeursNutritionnelles(
        BigDecimal calories,
        BigDecimal proteines,
        BigDecimal glucides,
        BigDecimal lipides,
        BigDecimal fibres) {

    public static final ValeursNutritionnelles ZERO = new ValeursNutritionnelles(
            BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

    public ValeursNutritionnelles {
        Objects.requireNonNull(calories, "calories");
        Objects.requireNonNull(proteines, "proteines");
        Objects.requireNonNull(glucides, "glucides");
        Objects.requireNonNull(lipides, "lipides");
        Objects.requireNonNull(fibres, "fibres");
        exigerPositifOuNul(calories, "calories");
        exigerPositifOuNul(proteines, "proteines");
        exigerPositifOuNul(glucides, "glucides");
        exigerPositifOuNul(lipides, "lipides");
        exigerPositifOuNul(fibres, "fibres");
    }

    private static void exigerPositifOuNul(BigDecimal valeur, String nom) {
        if (valeur.signum() < 0) {
            throw new IllegalArgumentException("La valeur \"" + nom + "\" ne peut pas etre negative : " + valeur);
        }
    }

    /**
     * Additionne deux estimations — utilise pour cumuler la contribution de
     * chaque ligne d'ingredient d'un repas (voir les adapters de
     * {@link EstimationNutritionPort}).
     */
    public ValeursNutritionnelles plus(ValeursNutritionnelles autre) {
        Objects.requireNonNull(autre, "autre");
        return new ValeursNutritionnelles(
                this.calories.add(autre.calories),
                this.proteines.add(autre.proteines),
                this.glucides.add(autre.glucides),
                this.lipides.add(autre.lipides),
                this.fibres.add(autre.fibres));
    }
}
