package com.ravito.infrastructure.web.planification;

import com.ravito.domain.courses.ListeCourses;
import com.ravito.domain.historique.HistoriquePlanId;
import com.ravito.domain.nutrition.EstimerNutritionUseCase;
import com.ravito.domain.planification.PlanSemaine;
import com.ravito.domain.prix.LignePrixEstime;
import com.ravito.domain.prix.Prix;

import java.util.List;
import java.util.UUID;

/**
 * Reponse complete d'une composition de plan de semaine : le plan lui-meme,
 * sa liste de courses et son cout estime.
 *
 * <p>{@code id} est celui sous lequel ce plan vient d'etre historise — la
 * composition historise systematiquement (voir {@code ComposerPlanSemaineController}),
 * cet id existe donc toujours des cette reponse, pas seulement en
 * consultant l'historique plus tard. Le client s'en sert par exemple pour
 * memoriser localement quels ingredients de la liste de courses il possede
 * deja, sans avoir besoin d'un aller-retour serveur pour ca.
 */
public record PlanSemaineResponse(UUID id, List<JourResponse> jours, ListeCoursesResponse listeCourses, PrixResponse prixEstime) {

    public static PlanSemaineResponse depuis(HistoriquePlanId id, PlanSemaine plan, ListeCourses listeCourses,
                                              List<LignePrixEstime> prixParLigne, Prix prixEstime,
                                              EstimerNutritionUseCase estimerNutritionUseCase) {
        return new PlanSemaineResponse(
                id.valeur(),
                plan.jours().stream().map(jour -> JourResponse.depuis(jour, estimerNutritionUseCase)).toList(),
                ListeCoursesResponse.depuis(listeCourses, prixParLigne),
                PrixResponse.depuis(prixEstime));
    }
}
