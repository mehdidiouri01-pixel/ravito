package com.ravito.domain.historique;

import com.ravito.domain.planification.PlanSemaine;
import com.ravito.domain.prix.Prix;

/**
 * Port d'entree : enregistre un plan de semaine tout juste compose dans
 * l'historique.
 *
 * <p>Distinct de {@code ComposerPlanSemaineUseCase} : composer un plan est
 * un calcul pur (aucune ecriture), l'historiser en est un effet de bord
 * explicite, declenche par l'appelant (voir {@code ComposerPlanSemaineController})
 * une fois le plan compose et son cout estime — le meme principe que la
 * separation deja en place entre composer et estimer le cout.
 */
public interface HistoriserPlanUseCase {

    PlanSemaineHistorise historiser(PlanSemaine plan, Prix prixEstime);
}
