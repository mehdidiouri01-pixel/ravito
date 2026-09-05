package com.mealweek.domain.prix;

import com.mealweek.domain.courses.ListeCourses;
import com.mealweek.domain.profil.Enseigne;

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
}
