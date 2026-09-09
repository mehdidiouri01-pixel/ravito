package com.ravito.infrastructure.persistence.nutrition;

import com.ravito.domain.courses.IngredientQuantite;
import com.ravito.domain.nutrition.EstimationNutritionImpossibleException;
import com.ravito.domain.nutrition.EstimationNutritionPort;
import com.ravito.domain.nutrition.ValeursNutritionnelles;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Implementation v1 du port de sortie {@link EstimationNutritionPort} :
 * somme des valeurs nutritionnelles moyennes de chaque ingredient (voir
 * {@link ValeurNutritionnelleIngredientEntity}), ponderees par leur
 * quantite — meme strategie que {@code JpaEstimationPrixAdapter} pour le
 * prix, mais operant directement sur les ingredients d'un repas plutot que
 * sur une liste de courses consolidee : la nutrition se calcule repas par
 * repas, elle n'a pas besoin d'etre agregee sur toute la semaine.
 */
@Component
class JpaEstimationNutritionAdapter implements EstimationNutritionPort {

    private final ValeurNutritionnelleIngredientJpaRepository repository;

    JpaEstimationNutritionAdapter(ValeurNutritionnelleIngredientJpaRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository");
    }

    @Override
    public ValeursNutritionnelles estimer(List<IngredientQuantite> ingredients) {
        Objects.requireNonNull(ingredients, "ingredients");

        return ingredients.stream()
                .map(this::valeursDeLaLigne)
                .reduce(ValeursNutritionnelles.ZERO, ValeursNutritionnelles::plus);
    }

    private ValeursNutritionnelles valeursDeLaLigne(IngredientQuantite ligne) {
        ValeurNutritionnelleIngredientEntity valeursPourUnite = repository
                .findByIngredientNomAndUnite(ligne.ingredient().nom(), ligne.quantite().unite())
                .orElseThrow(() -> new EstimationNutritionImpossibleException(
                        "Aucune valeur nutritionnelle connue pour l'ingredient \"" + ligne.ingredient().nom()
                                + "\" en " + ligne.quantite().unite()));

        return new ValeursNutritionnelles(
                valeursPourUnite.getCalories().multiply(ligne.quantite().valeur()),
                valeursPourUnite.getProteines().multiply(ligne.quantite().valeur()),
                valeursPourUnite.getGlucides().multiply(ligne.quantite().valeur()),
                valeursPourUnite.getLipides().multiply(ligne.quantite().valeur()),
                valeursPourUnite.getFibres().multiply(ligne.quantite().valeur()));
    }
}
