package com.ravito.domain.historique;

/**
 * Levee quand un {@link HistoriquePlanId} demande ne correspond a aucun
 * plan historise — sur le meme modele que {@code RepasIntrouvableException}.
 */
public class HistoriquePlanIntrouvableException extends RuntimeException {

    public HistoriquePlanIntrouvableException(HistoriquePlanId id) {
        super("Aucun plan historise trouve pour l'identifiant " + id.valeur());
    }
}
