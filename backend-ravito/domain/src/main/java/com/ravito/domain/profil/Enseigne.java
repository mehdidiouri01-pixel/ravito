package com.ravito.domain.profil;

/**
 * Enseigne de distribution aupres de laquelle l'utilisateur fait ses courses.
 * Determine, en aval, quel coefficient {@code EstimationPrixPort} applique
 * pour estimer le cout du panier.
 *
 * <p>Les principales enseignes d'hyper/supermarches presentes en France —
 * pas une liste exhaustive de toutes les enseignes regionales, mais de quoi
 * couvrir la tres large majorite des foyers.
 */
public enum Enseigne {
    CARREFOUR,
    LECLERC,
    LIDL,
    AUCHAN,
    INTERMARCHE,
    SYSTEME_U,
    CASINO,
    CORA,
    ALDI,
    MONOPRIX
}
