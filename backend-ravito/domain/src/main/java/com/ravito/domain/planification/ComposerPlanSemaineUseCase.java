package com.ravito.domain.planification;

import com.ravito.domain.profil.ProfilUtilisateur;

import java.util.Map;

/**
 * Port d'entree : compose le plan de semaine de l'utilisateur a partir de
 * son profil et de son choix pour chacun des 5 jours.
 *
 * <p>L'implementation (module {@code application}) resout chaque
 * {@link ChoixRepas} de chaque {@link ChoixJour} en repas reel — via
 * {@code CatalogueRepasPort.parId} pour un {@link ChoixRepas.ParId}, ou en
 * revalidant ses ingredients pour un {@link ChoixRepas.Genere} — construit
 * les {@link Jour} puis le {@link PlanSemaine}, dont le constructeur fait
 * respecter les invariants restants (5 jours, repas compatibles avec le
 * profil).
 */
public interface ComposerPlanSemaineUseCase {

    PlanSemaine composer(ProfilUtilisateur profil, Map<JourSemaine, ChoixJour> choix);
}
