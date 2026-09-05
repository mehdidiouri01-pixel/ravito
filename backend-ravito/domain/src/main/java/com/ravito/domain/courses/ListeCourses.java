package com.ravito.domain.courses;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Liste de courses consolidee a partir des repas d'un plan de semaine.
 *
 * <p>Regle de consolidation : deux {@link IngredientQuantite} du meme
 * {@link Ingredient} et de la meme {@link UniteMesure} sont fusionnes en
 * une seule {@link LigneListeCourses} (quantites additionnees). Si le meme
 * ingredient apparait dans des unites differentes selon les repas, les
 * lignes restent distinctes plutot que d'etre converties de maniere
 * approximative (choix v1, voir {@link UniteMesure}).
 */
public record ListeCourses(List<LigneListeCourses> lignes) {

    public ListeCourses {
        Objects.requireNonNull(lignes, "lignes");
        lignes = List.copyOf(lignes);
    }

    /**
     * Consolide une liste d'ingredients (typiquement issue de
     * {@code PlanSemaine.tousLesRepas()}) en {@link ListeCourses} : une
     * ligne par couple (ingredient, unite), quantites additionnees.
     */
    public static ListeCourses consolider(List<IngredientQuantite> ingredients) {
        Objects.requireNonNull(ingredients, "ingredients");

        Map<CleConsolidation, Quantite> quantitesParCle = new LinkedHashMap<>();
        for (IngredientQuantite ligne : ingredients) {
            CleConsolidation cle = new CleConsolidation(ligne.ingredient(), ligne.quantite().unite());
            quantitesParCle.merge(cle, ligne.quantite(), Quantite::plus);
        }

        List<LigneListeCourses> lignes = new ArrayList<>();
        quantitesParCle.forEach((cle, quantite) -> lignes.add(new LigneListeCourses(cle.ingredient(), quantite)));
        return new ListeCourses(lignes);
    }

    /**
     * @return les lignes regroupees par rayon de magasin, pour l'affichage
     * de la liste de courses par rayon dans l'application.
     */
    public Map<RayonMagasin, List<LigneListeCourses>> groupParRayon() {
        Map<RayonMagasin, List<LigneListeCourses>> parRayon = new LinkedHashMap<>();
        for (LigneListeCourses ligne : lignes) {
            parRayon.computeIfAbsent(ligne.ingredient().rayon(), r -> new ArrayList<>()).add(ligne);
        }
        return parRayon;
    }

    private record CleConsolidation(Ingredient ingredient, UniteMesure unite) {
    }
}
