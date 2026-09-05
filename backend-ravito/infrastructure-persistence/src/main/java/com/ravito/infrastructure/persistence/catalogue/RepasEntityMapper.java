package com.ravito.infrastructure.persistence.catalogue;

import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.RepasId;
import com.ravito.domain.courses.Ingredient;
import com.ravito.domain.courses.IngredientQuantite;
import com.ravito.domain.courses.Quantite;

import java.util.List;

/**
 * Seule classe qui connaisse a la fois {@code Repas} (domaine) et
 * {@link RepasEntity} (JPA). Aucune des deux ne doit connaitre l'autre.
 */
final class RepasEntityMapper {

    private RepasEntityMapper() {
    }

    static RepasEntity versEntite(Repas repas) {
        List<IngredientQuantiteEmbeddable> ingredients = repas.ingredients().stream()
                .map(RepasEntityMapper::versEmbeddable)
                .toList();
        return new RepasEntity(repas.id().valeur(), repas.nom(), repas.type(), repas.style(),
                repas.niveauRequis(), ingredients);
    }

    static Repas versDomaine(RepasEntity entity) {
        List<IngredientQuantite> ingredients = entity.getIngredients().stream()
                .map(RepasEntityMapper::versIngredientQuantite)
                .toList();
        return new Repas(new RepasId(entity.getId()), entity.getNom(), entity.getType(), entity.getStyle(),
                entity.getNiveauRequis(), ingredients);
    }

    private static IngredientQuantiteEmbeddable versEmbeddable(IngredientQuantite ingredientQuantite) {
        return new IngredientQuantiteEmbeddable(
                ingredientQuantite.ingredient().nom(),
                ingredientQuantite.ingredient().rayon(),
                ingredientQuantite.quantite().valeur(),
                ingredientQuantite.quantite().unite());
    }

    private static IngredientQuantite versIngredientQuantite(IngredientQuantiteEmbeddable embeddable) {
        Ingredient ingredient = new Ingredient(embeddable.getIngredientNom(), embeddable.getRayon());
        Quantite quantite = new Quantite(embeddable.getQuantiteValeur(), embeddable.getUnite());
        return new IngredientQuantite(ingredient, quantite);
    }
}
