package com.mealweek.domain.planification;

import com.mealweek.domain.catalogue.Repas;
import com.mealweek.domain.profil.ProfilUtilisateur;

/**
 * Levee quand un repas selectionne pour un {@link PlanSemaine} n'est pas
 * compatible avec le profil (style different, ou niveau de cuisine requis
 * trop eleve pour le profil).
 *
 * <p>{@code ComposerPlanSemaineUseCase} est cense n'utiliser que des repas
 * deja filtres par {@code ProposerRepasUseCase} — mais {@link PlanSemaine}
 * revalide quand meme cette regle a la construction plutot que de faire
 * confiance a son appelant.
 */
public class RepasIncompatibleException extends RuntimeException {

    public RepasIncompatibleException(Repas repas, ProfilUtilisateur profil) {
        super("Le repas \"" + repas.nom() + "\" (style " + repas.style() + ", niveau " + repas.niveauRequis()
                + ") n'est pas compatible avec le profil " + profil);
    }
}
