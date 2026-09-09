package com.ravito.infrastructure.web.historique;

import com.ravito.domain.courses.ListeCourses;
import com.ravito.domain.historique.PlanSemaineHistorise;
import com.ravito.domain.nutrition.EstimerNutritionUseCase;
import com.ravito.domain.prix.EstimerCoutUseCase;
import com.ravito.domain.prix.LignePrixEstime;
import com.ravito.infrastructure.web.planification.JourResponse;
import com.ravito.infrastructure.web.planification.ListeCoursesResponse;
import com.ravito.infrastructure.web.planification.PrixResponse;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Detail complet d'un plan historise — meme forme que {@code PlanSemaineResponse}
 * (composition d'un plan tout juste calcule), plus l'id et la date de
 * composition. La liste de courses n'est pas stockee : recalculee a la
 * volee depuis le plan fige, ce qui est deterministe (voir
 * {@code PlanSemaine.genererListeCourses}) — le detail par ligne
 * ({@link EstimerCoutUseCase#estimerParLigne}) l'est de la meme facon,
 * seul le total {@link #prixEstime} reste celui fige au moment de la
 * composition (voir {@code PlanSemaineHistorise}).
 */
public record HistoriquePlanDetailResponse(
        UUID id,
        Instant dateComposition,
        List<JourResponse> jours,
        ListeCoursesResponse listeCourses,
        PrixResponse prixEstime) {

    public static HistoriquePlanDetailResponse depuis(PlanSemaineHistorise historise,
                                                        EstimerNutritionUseCase estimerNutritionUseCase,
                                                        EstimerCoutUseCase estimerCoutUseCase) {
        ListeCourses listeCourses = historise.plan().genererListeCourses();
        List<LignePrixEstime> prixParLigne = estimerCoutUseCase.estimerParLigne(listeCourses, historise.plan().profil().enseigne());

        return new HistoriquePlanDetailResponse(
                historise.id().valeur(),
                historise.dateComposition(),
                historise.plan().jours().stream().map(jour -> JourResponse.depuis(jour, estimerNutritionUseCase)).toList(),
                ListeCoursesResponse.depuis(listeCourses, prixParLigne),
                PrixResponse.depuis(historise.prixEstime()));
    }
}
