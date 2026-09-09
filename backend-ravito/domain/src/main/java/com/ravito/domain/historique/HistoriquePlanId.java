package com.ravito.domain.historique;

import java.util.Objects;
import java.util.UUID;

/**
 * Identifiant type d'un {@link PlanSemaineHistorise}, sur le meme modele que
 * {@code RepasId} — evite de faire circuler des UUID nus dans les
 * signatures des ports.
 */
public record HistoriquePlanId(UUID valeur) {

    public HistoriquePlanId {
        Objects.requireNonNull(valeur, "valeur");
    }

    public static HistoriquePlanId nouveau() {
        return new HistoriquePlanId(UUID.randomUUID());
    }
}
