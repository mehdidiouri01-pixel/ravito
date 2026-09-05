package com.ravito.domain.profil;

/**
 * Niveau de competence culinaire de l'utilisateur.
 *
 * <p>L'ordre de declaration est significatif : un profil {@code CONFIRME}
 * peut se voir proposer les repas destines a un {@code DEBUTANT}, mais
 * l'inverse est faux. La comparaison s'appuie sur {@link #ordinal()} via
 * {@link #estAuMoinsAussiConfirmeQue(NiveauCuisine)} — ne pas reordonner les
 * constantes sans mettre a jour cette regle.
 */
public enum NiveauCuisine {
    DEBUTANT,
    CONFIRME;

    /**
     * @return {@code true} si ce niveau permet de cuisiner un repas qui
     * demande au moins le niveau {@code niveauRequis}.
     */
    public boolean estAuMoinsAussiConfirmeQue(NiveauCuisine niveauRequis) {
        return this.ordinal() >= niveauRequis.ordinal();
    }
}
