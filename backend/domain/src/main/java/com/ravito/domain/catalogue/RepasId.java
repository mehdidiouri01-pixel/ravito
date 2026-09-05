package com.ravito.domain.catalogue;

import java.util.Objects;
import java.util.UUID;

/**
 * Identifiant type d'un {@link Repas}, pour eviter de faire circuler des
 * UUID ou des chaines nues dans les signatures des ports.
 */
public record RepasId(UUID valeur) {

    public RepasId {
        Objects.requireNonNull(valeur, "valeur");
    }

    public static RepasId nouveau() {
        return new RepasId(UUID.randomUUID());
    }
}
