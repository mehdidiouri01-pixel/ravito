package com.ravito.domain.catalogue;

/**
 * Levee quand un {@link RepasId} fourni par l'utilisateur (via
 * {@code ComposerPlanSemaineUseCase}) ne correspond a aucun repas du
 * catalogue — par exemple si le repas a ete retire du catalogue entre la
 * proposition et la composition du plan.
 */
public class RepasIntrouvableException extends RuntimeException {

    public RepasIntrouvableException(RepasId id) {
        super("Aucun repas trouve pour l'identifiant " + id.valeur());
    }
}
