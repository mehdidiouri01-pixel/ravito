package com.ravito.domain.prix;

import com.ravito.domain.courses.LigneListeCourses;

import java.util.Objects;

/**
 * Le prix estime d'une seule ligne de la liste de courses — meme
 * granularite que {@link LigneListeCourses}, pour permettre a un appelant
 * (le frontend, via {@code LigneListeCoursesResponse}) d'ajuster le total
 * sans refaire d'appel serveur, par exemple quand l'utilisateur indique
 * qu'il possede deja tel ou tel ingredient.
 *
 * <p>La somme de tous les {@link #prix} d'une {@code ListeCourses} est
 * toujours egale au {@link Prix} global renvoye par
 * {@link EstimerCoutUseCase#estimer} — voir {@code JpaEstimationPrixAdapter},
 * qui derive l'un de l'autre pour le garantir plutot que de recalculer
 * separement.
 */
public record LignePrixEstime(LigneListeCourses ligne, Prix prix) {

    public LignePrixEstime {
        Objects.requireNonNull(ligne, "ligne");
        Objects.requireNonNull(prix, "prix");
    }
}
