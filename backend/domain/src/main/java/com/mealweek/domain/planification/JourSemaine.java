package com.mealweek.domain.planification;

/**
 * Les 5 jours couverts par un {@link PlanSemaine}. Volontairement limite au
 * lundi-vendredi : le week-end est hors perimetre de l'application.
 */
public enum JourSemaine {
    LUNDI,
    MARDI,
    MERCREDI,
    JEUDI,
    VENDREDI
}
