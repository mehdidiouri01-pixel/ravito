package com.ravito.domain.historique;

import com.ravito.domain.planification.PlanSemaine;
import com.ravito.domain.prix.Prix;

import java.util.List;
import java.util.Optional;

/**
 * Port de sortie : conserve chaque {@link PlanSemaine} compose, pour
 * pouvoir les relister plus tard (voir {@link ConsulterHistoriqueUseCase}).
 */
public interface HistoriquePlansPort {

    /**
     * Enregistre un plan tout juste compose et renvoie sa version historisee
     * (avec son identifiant et sa date de composition attribues).
     */
    PlanSemaineHistorise enregistrer(PlanSemaine plan, Prix prixEstime);

    /**
     * @return tous les plans historises, du plus recent au plus ancien.
     */
    List<PlanSemaineHistorise> lister();

    Optional<PlanSemaineHistorise> parId(HistoriquePlanId id);
}
