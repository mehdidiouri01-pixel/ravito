package com.ravito.domain.prix;

import com.ravito.domain.courses.ListeCourses;
import com.ravito.domain.profil.Enseigne;

import java.util.List;

/**
 * Port d'entree : estime le cout d'une liste de courses pour une enseigne.
 *
 * <p>Meme si l'implementation v1 se contentera probablement de deleguer a
 * {@link EstimationPrixPort#estimerCout}, ce use case reste un port a part
 * entiere plutot qu'un simple passe-plat expose au futur controleur : il
 * garde la porte ouverte a une regle metier future (arrondi, frais fixes,
 * remise) sans avoir a changer la frontiere entree/sortie du domaine.
 */
public interface EstimerCoutUseCase {

    Prix estimer(ListeCourses listeCourses, Enseigne enseigne);

    /**
     * Meme estimation que {@link #estimer}, detaillee ligne par ligne — sert
     * a afficher un prix par ingredient cote client, pour que l'utilisateur
     * puisse voir le cout s'ajuster quand il indique posseder deja tel ou
     * tel ingredient (voir {@code LigneListeCoursesResponse}), sans nouvel
     * appel serveur a chaque coche.
     */
    List<LignePrixEstime> estimerParLigne(ListeCourses listeCourses, Enseigne enseigne);
}
