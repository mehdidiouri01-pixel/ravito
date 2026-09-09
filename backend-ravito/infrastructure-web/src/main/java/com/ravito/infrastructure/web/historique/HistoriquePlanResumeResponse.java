package com.ravito.infrastructure.web.historique;

import com.ravito.domain.historique.PlanSemaineHistorise;
import com.ravito.domain.profil.NiveauCuisine;
import com.ravito.domain.profil.StyleAlimentaire;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Forme allegee d'un plan historise, pour la liste — pas de detail des
 * repas ici (voir {@link HistoriquePlanDetailResponse} pour ca), juste de
 * quoi afficher une ligne par plan.
 */
public record HistoriquePlanResumeResponse(
        UUID id,
        Instant dateComposition,
        StyleAlimentaire style,
        NiveauCuisine niveau,
        int nombreDePersonnes,
        BigDecimal prixEstime) {

    public static HistoriquePlanResumeResponse depuis(PlanSemaineHistorise historise) {
        return new HistoriquePlanResumeResponse(
                historise.id().valeur(),
                historise.dateComposition(),
                historise.plan().profil().style(),
                historise.plan().profil().niveau(),
                historise.plan().profil().nombreDePersonnes().valeur(),
                historise.prixEstime().montant());
    }
}
