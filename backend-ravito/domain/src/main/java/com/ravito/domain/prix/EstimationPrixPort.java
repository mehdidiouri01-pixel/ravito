package com.ravito.domain.prix;

import com.ravito.domain.courses.ListeCourses;
import com.ravito.domain.profil.Enseigne;

import java.util.List;

/**
 * Port de sortie : estime le cout d'une liste de courses pour une enseigne
 * donnee.
 *
 * <p>La v1 s'appuiera sur un catalogue de prix moyens en base, pondere par
 * un coefficient propre a chaque enseigne — un detail d'implementation de
 * l'adapter, pas du domaine. Une version ulterieure pourra brancher l'API
 * Open Prices d'Open Food Facts derriere ce meme port, sans changer cette
 * interface ni son appelant.
 *
 * @throws EstimationImpossibleException si l'implementation ne peut pas
 * chiffrer une ligne de la liste de courses (ex : prix moyen manquant en v1)
 */
public interface EstimationPrixPort {

    Prix estimerCout(ListeCourses listeCourses, Enseigne enseigne);

    /**
     * Meme estimation que {@link #estimerCout}, mais detaillee ligne par
     * ligne plutot que sommee — voir {@link LignePrixEstime}.
     */
    List<LignePrixEstime> estimerCoutParLigne(ListeCourses listeCourses, Enseigne enseigne);
}
