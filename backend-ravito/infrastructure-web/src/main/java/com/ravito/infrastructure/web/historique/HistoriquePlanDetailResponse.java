package com.ravito.infrastructure.web.historique;

import com.ravito.domain.historique.PlanSemaineHistorise;
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
 * {@code PlanSemaine.genererListeCourses}).
 */
public record HistoriquePlanDetailResponse(
        UUID id,
        Instant dateComposition,
        List<JourResponse> jours,
        ListeCoursesResponse listeCourses,
        PrixResponse prixEstime) {

    public static HistoriquePlanDetailResponse depuis(PlanSemaineHistorise historise) {
        return new HistoriquePlanDetailResponse(
                historise.id().valeur(),
                historise.dateComposition(),
                historise.plan().jours().stream().map(JourResponse::depuis).toList(),
                ListeCoursesResponse.depuis(historise.plan().genererListeCourses()),
                PrixResponse.depuis(historise.prixEstime()));
    }
}
