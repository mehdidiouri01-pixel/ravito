package com.ravito.domain.planification;

/**
 * Levee quand la structure d'un {@link PlanSemaine} ou d'un {@link Jour} ne
 * respecte pas les invariants attendus : jour manquant ou renseigne
 * plusieurs fois, ou repas place au mauvais type de repas de la journee
 * (ex: un dejeuner a la place du petit-dejeuner).
 */
public class PlanSemaineInvalideException extends RuntimeException {

    public PlanSemaineInvalideException(String message) {
        super(message);
    }
}
