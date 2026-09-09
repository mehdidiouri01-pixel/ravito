package com.ravito.domain.nutrition;

import com.ravito.domain.courses.IngredientQuantite;

import java.util.List;

/**
 * Port de sortie : estime les valeurs nutritionnelles totales d'une liste
 * d'ingredients — typiquement ceux d'un seul repas (voir
 * {@link EstimerNutritionUseCase}).
 *
 * <p>La v1 s'appuiera sur un catalogue de valeurs moyennes par (ingredient,
 * unite) en base, sommees et ponderees par les quantites — meme strategie
 * que {@code EstimationPrixPort} pour le prix. Une source plus riche (table
 * CIQUAL, Open Food Facts) pourra remplacer cet adapter plus tard sans
 * changer cette interface ni ses appelants.
 *
 * @throws EstimationNutritionImpossibleException si l'implementation ne
 * peut pas chiffrer un des ingredients (ex : valeur nutritionnelle
 * manquante en v1)
 */
public interface EstimationNutritionPort {

    ValeursNutritionnelles estimer(List<IngredientQuantite> ingredients);
}
