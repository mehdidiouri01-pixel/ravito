package com.mealweek.domain.profil;

import java.util.Objects;

/**
 * Profil declaratif de l'utilisateur : sert de filtre pour proposer des
 * repas compatibles (voir {@link com.mealweek.domain.catalogue.Repas#estCompatibleAvec})
 * et determine l'enseigne utilisee pour l'estimation du cout du panier.
 */
public record ProfilUtilisateur(Enseigne enseigne, StyleAlimentaire style, NiveauCuisine niveau) {

    public ProfilUtilisateur {
        Objects.requireNonNull(enseigne, "enseigne");
        Objects.requireNonNull(style, "style");
        Objects.requireNonNull(niveau, "niveau");
    }
}
