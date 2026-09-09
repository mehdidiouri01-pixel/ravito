package com.ravito.domain.nutrition;

import com.ravito.domain.catalogue.Repas;

/**
 * Port d'entree : estime les valeurs nutritionnelles d'un repas.
 *
 * <p>Comme {@code EstimerCoutUseCase} pour le prix, reste un use case a
 * part entiere plutot qu'un simple passe-plat expose au controleur : ca
 * garde la porte ouverte a une regle metier future (ex : ponderer selon le
 * niveau d'activite du profil) sans avoir a changer la frontiere
 * entree/sortie du domaine.
 */
public interface EstimerNutritionUseCase {

    ValeursNutritionnelles estimer(Repas repas);
}
