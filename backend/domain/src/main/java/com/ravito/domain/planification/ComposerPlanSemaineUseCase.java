package com.ravito.domain.planification;

import com.ravito.domain.profil.ProfilUtilisateur;

import java.util.Map;

/**
 * Port d'entree : compose le plan de semaine de l'utilisateur a partir de
 * son profil et de son choix pour chacun des 5 jours.
 *
 * <p>L'implementation (future, module {@code application}) resout chaque
 * {@link ChoixJour} en repas reels via {@code CatalogueRepasPort.parId},
 * construit les {@link Jour} puis le {@link PlanSemaine} — dont le
 * constructeur fait respecter les invariants (5 jours, repas compatibles).
 */
public interface ComposerPlanSemaineUseCase {

    PlanSemaine composer(ProfilUtilisateur profil, Map<JourSemaine, ChoixJour> choix);
}
