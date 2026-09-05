package com.mealweek.domain.courses;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ListeCoursesTest {

    private static final Ingredient RIZ = new Ingredient("riz", RayonMagasin.EPICERIE);
    private static final Ingredient LAIT = new Ingredient("lait", RayonMagasin.CREMERIE);

    @Test
    void fusionne_deux_lignes_du_meme_ingredient_et_de_la_meme_unite() {
        List<IngredientQuantite> ingredients = List.of(
                new IngredientQuantite(RIZ, new Quantite(BigDecimal.valueOf(200), UniteMesure.GRAMME)),
                new IngredientQuantite(RIZ, new Quantite(BigDecimal.valueOf(100), UniteMesure.GRAMME)));

        ListeCourses liste = ListeCourses.consolider(ingredients);

        assertThat(liste.lignes()).hasSize(1);
        LigneListeCourses ligne = liste.lignes().get(0);
        assertThat(ligne.ingredient()).isEqualTo(RIZ);
        assertThat(ligne.quantiteTotale().valeur()).isEqualByComparingTo("300");
        assertThat(ligne.quantiteTotale().unite()).isEqualTo(UniteMesure.GRAMME);
    }

    @Test
    void garde_deux_lignes_distinctes_pour_le_meme_ingredient_dans_des_unites_differentes() {
        List<IngredientQuantite> ingredients = List.of(
                new IngredientQuantite(RIZ, new Quantite(BigDecimal.valueOf(200), UniteMesure.GRAMME)),
                new IngredientQuantite(RIZ, new Quantite(BigDecimal.ONE, UniteMesure.UNITE)));

        ListeCourses liste = ListeCourses.consolider(ingredients);

        assertThat(liste.lignes()).hasSize(2);
    }

    @Test
    void garde_deux_lignes_distinctes_pour_deux_ingredients_differents() {
        List<IngredientQuantite> ingredients = List.of(
                new IngredientQuantite(RIZ, new Quantite(BigDecimal.valueOf(200), UniteMesure.GRAMME)),
                new IngredientQuantite(LAIT, new Quantite(BigDecimal.valueOf(500), UniteMesure.MILLILITRE)));

        ListeCourses liste = ListeCourses.consolider(ingredients);

        assertThat(liste.lignes()).hasSize(2);
    }

    @Test
    void regroupe_les_lignes_par_rayon() {
        List<IngredientQuantite> ingredients = List.of(
                new IngredientQuantite(RIZ, new Quantite(BigDecimal.valueOf(200), UniteMesure.GRAMME)),
                new IngredientQuantite(LAIT, new Quantite(BigDecimal.valueOf(500), UniteMesure.MILLILITRE)));
        ListeCourses liste = ListeCourses.consolider(ingredients);

        Map<RayonMagasin, List<LigneListeCourses>> parRayon = liste.groupParRayon();

        assertThat(parRayon.keySet()).containsExactlyInAnyOrder(RayonMagasin.EPICERIE, RayonMagasin.CREMERIE);
        assertThat(parRayon.get(RayonMagasin.EPICERIE)).extracting(LigneListeCourses::ingredient).containsExactly(RIZ);
        assertThat(parRayon.get(RayonMagasin.CREMERIE)).extracting(LigneListeCourses::ingredient).containsExactly(LAIT);
    }

    @Test
    void la_liste_de_lignes_est_immuable() {
        ListeCourses liste = ListeCourses.consolider(List.of(
                new IngredientQuantite(RIZ, new Quantite(BigDecimal.TEN, UniteMesure.GRAMME))));

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> liste.lignes().add(
                        new LigneListeCourses(LAIT, new Quantite(BigDecimal.ONE, UniteMesure.LITRE))));
    }
}
