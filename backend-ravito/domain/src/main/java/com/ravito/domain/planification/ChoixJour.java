package com.ravito.domain.planification;

import com.ravito.domain.catalogue.RepasId;

import java.util.Objects;

/**
 * Choix de l'utilisateur pour une journee : les identifiants du
 * petit-dejeuner et du dejeuner selectionnes dans le catalogue propose.
 *
 * <p>Simple porteur de donnees a la frontiere de {@link ComposerPlanSemaineUseCase}
 * — la resolution de ces identifiants en {@code Repas} (via
 * {@code CatalogueRepasPort}) est de la responsabilite de l'implementation
 * du use case, pas du domaine lui-meme.
 */
public record ChoixJour(RepasId petitDejeunerId, RepasId dejeunerId) {

    public ChoixJour {
        Objects.requireNonNull(petitDejeunerId, "petitDejeunerId");
        Objects.requireNonNull(dejeunerId, "dejeunerId");
    }
}
