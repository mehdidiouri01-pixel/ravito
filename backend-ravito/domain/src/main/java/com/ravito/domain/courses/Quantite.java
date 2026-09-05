package com.ravito.domain.courses;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Quantite d'un ingredient, toujours strictement positive.
 *
 * <p>Ne sait additionner qu'une quantite de meme {@link UniteMesure} : le
 * domaine ne fait aucune conversion entre unites (voir {@link UniteMesure}).
 */
public record Quantite(BigDecimal valeur, UniteMesure unite) {

    public Quantite {
        Objects.requireNonNull(valeur, "valeur");
        Objects.requireNonNull(unite, "unite");
        if (valeur.signum() <= 0) {
            throw new IllegalArgumentException("La quantite doit etre strictement positive : " + valeur);
        }
    }

    /**
     * @throws IllegalArgumentException si {@code autre} n'a pas la meme unite que celle-ci
     */
    public Quantite plus(Quantite autre) {
        Objects.requireNonNull(autre, "autre");
        if (this.unite != autre.unite) {
            throw new IllegalArgumentException(
                    "Impossible d'additionner des quantites d'unites differentes : " + this.unite + " et " + autre.unite);
        }
        return new Quantite(this.valeur.add(autre.valeur), this.unite);
    }
}
