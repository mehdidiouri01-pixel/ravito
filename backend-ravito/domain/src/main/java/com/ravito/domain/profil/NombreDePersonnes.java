package com.ravito.domain.profil;

/**
 * Nombre de personnes du foyer, pour lequel les quantites de la liste de
 * courses sont calculees.
 *
 * <p>Type dedie plutot qu'un {@code int} nu, pour la meme raison que
 * {@code RepasId} : un entier nu se glisse trop facilement au mauvais
 * endroit d'une signature, et l'invariant (au moins une personne) n'aurait
 * pas de domicile clair.
 *
 * <p>Plafond volontaire a {@value #MAXIMUM} : au-dela, il ne s'agit plus
 * d'un foyer mais d'une cantine, cas que l'application ne pretend pas
 * couvrir (les quantites par portion du catalogue ne tiendraient plus).
 */
public record NombreDePersonnes(int valeur) {

    public static final int MINIMUM = 1;
    public static final int MAXIMUM = 12;

    public NombreDePersonnes {
        if (valeur < MINIMUM || valeur > MAXIMUM) {
            throw new IllegalArgumentException(
                    "Le nombre de personnes doit etre compris entre " + MINIMUM + " et " + MAXIMUM + " : " + valeur);
        }
    }

    /** Un foyer d'une seule personne — les quantites du catalogue telles quelles. */
    public static NombreDePersonnes une() {
        return new NombreDePersonnes(1);
    }
}
