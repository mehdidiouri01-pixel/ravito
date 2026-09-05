package com.ravito.domain.prix;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Estimation d'un cout, en euros (devise implicite : l'application ne vise
 * que le marche francais pour cette v1, pas la peine de porter une
 * {@link java.util.Currency} qui ne servira jamais).
 */
public record Prix(BigDecimal montant) {

    public static final Prix ZERO = new Prix(BigDecimal.ZERO);

    public Prix {
        Objects.requireNonNull(montant, "montant");
        if (montant.signum() < 0) {
            throw new IllegalArgumentException("Le montant ne peut pas etre negatif : " + montant);
        }
    }

    public Prix plus(Prix autre) {
        Objects.requireNonNull(autre, "autre");
        return new Prix(this.montant.add(autre.montant));
    }
}
