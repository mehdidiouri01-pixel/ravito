package com.ravito.infrastructure.persistence.prix;

import com.ravito.domain.courses.Ingredient;
import com.ravito.domain.courses.UniteMesure;

/**
 * Levee quand {@link JpaEstimationPrixAdapter} ne trouve aucun prix moyen
 * pour un couple (ingredient, unite) de la liste de courses.
 *
 * <p>Volontairement definie ici, pas dans le domaine : c'est une limite de
 * cette strategie d'estimation precise (catalogue de prix moyens), pas une
 * regle metier universelle. Un futur adapter Open Prices pourrait ne
 * jamais lever ce genre d'erreur (renvoyer une estimation par defaut par
 * exemple) sans que la signature d'{@code EstimationPrixPort} ait a changer.
 */
public class PrixIngredientInconnuException extends RuntimeException {

    public PrixIngredientInconnuException(Ingredient ingredient, UniteMesure unite) {
        super("Aucun prix moyen connu pour l'ingredient \"" + ingredient.nom() + "\" en " + unite);
    }
}
