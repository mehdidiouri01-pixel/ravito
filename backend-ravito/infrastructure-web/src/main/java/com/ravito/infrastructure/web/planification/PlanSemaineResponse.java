package com.ravito.infrastructure.web.planification;

import com.ravito.domain.courses.ListeCourses;
import com.ravito.domain.nutrition.EstimerNutritionUseCase;
import com.ravito.domain.planification.PlanSemaine;
import com.ravito.domain.prix.Prix;

import java.util.List;

/**
 * Reponse complete d'une composition de plan de semaine : le plan lui-meme,
 * sa liste de courses et son cout estime. Les trois sont calcules dans le
 * meme appel HTTP — {@code PlanSemaine} n'etant pas persiste (pas de port
 * d'ecriture dans le domaine pour l'instant), il n'y a pas d'etat entre
 * "composer le plan" et "voir sa liste de courses et son prix".
 */
public record PlanSemaineResponse(List<JourResponse> jours, ListeCoursesResponse listeCourses, PrixResponse prixEstime) {

    public static PlanSemaineResponse depuis(
            PlanSemaine plan, ListeCourses listeCourses, Prix prixEstime, EstimerNutritionUseCase estimerNutritionUseCase) {
        return new PlanSemaineResponse(
                plan.jours().stream().map(jour -> JourResponse.depuis(jour, estimerNutritionUseCase)).toList(),
                ListeCoursesResponse.depuis(listeCourses),
                PrixResponse.depuis(prixEstime));
    }
}
