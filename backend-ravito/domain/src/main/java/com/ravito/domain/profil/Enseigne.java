package com.ravito.domain.profil;

/**
 * Enseigne de distribution aupres de laquelle l'utilisateur fait ses courses.
 * Determine, en aval, quel coefficient {@code EstimationPrixPort} applique
 * pour estimer le cout du panier.
 */
public enum Enseigne {
    CARREFOUR,
    LECLERC,
    LIDL,
    AUCHAN
}
