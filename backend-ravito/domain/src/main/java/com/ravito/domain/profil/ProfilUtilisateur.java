package com.ravito.domain.profil;

import java.util.Objects;

/**
 * Profil declaratif de l'utilisateur : sert de filtre pour proposer des
 * repas compatibles (voir {@link com.ravito.domain.catalogue.Repas#estCompatibleAvec}),
 * determine l'enseigne utilisee pour l'estimation du cout du panier, et le
 * nombre de personnes pour lequel les quantites sont calculees.
 */
public record ProfilUtilisateur(
        Enseigne enseigne,
        StyleAlimentaire style,
        NiveauCuisine niveau,
        NombreDePersonnes nombreDePersonnes) {

    public ProfilUtilisateur {
        Objects.requireNonNull(enseigne, "enseigne");
        Objects.requireNonNull(style, "style");
        Objects.requireNonNull(niveau, "niveau");
        Objects.requireNonNull(nombreDePersonnes, "nombreDePersonnes");
    }
}
