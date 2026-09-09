package com.ravito.domain.nutrition;

/**
 * Levee par une implementation d'{@link EstimationNutritionPort} lorsqu'elle
 * ne peut pas chiffrer un ingredient (ex : aucune valeur nutritionnelle
 * connue pour lui, dans la strategie v1).
 *
 * <p>Meme raisonnement que {@code EstimationImpossibleException} cote prix :
 * definie ici, au niveau du port, pas dans l'adapter qui la leve
 * effectivement — ses appelants ont besoin d'un type stable pour reagir a
 * un echec d'estimation, sans dependre d'un adapter concret.
 */
public class EstimationNutritionImpossibleException extends RuntimeException {

    public EstimationNutritionImpossibleException(String message) {
        super(message);
    }
}
