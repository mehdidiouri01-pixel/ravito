package com.ravito.domain.planification;

import java.util.Objects;

/**
 * Choix de l'utilisateur pour une journee : petit-dejeuner, dejeuner et
 * diner, chacun soit reference par identifiant, soit genere (voir
 * {@link ChoixRepas}).
 *
 * <p>Simple porteur de donnees a la frontiere de {@link ComposerPlanSemaineUseCase}
 * — la resolution de chaque {@link ChoixRepas} en {@code Repas} reel (via
 * {@code CatalogueRepasPort} ou revalidation directe) est de la
 * responsabilite de l'implementation du use case, pas du domaine lui-meme.
 */
public record ChoixJour(ChoixRepas petitDejeuner, ChoixRepas dejeuner, ChoixRepas diner) {

    public ChoixJour {
        Objects.requireNonNull(petitDejeuner, "petitDejeuner");
        Objects.requireNonNull(dejeuner, "dejeuner");
        Objects.requireNonNull(diner, "diner");
    }
}
