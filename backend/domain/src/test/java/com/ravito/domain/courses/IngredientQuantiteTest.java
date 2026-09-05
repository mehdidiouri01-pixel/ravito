package com.ravito.domain.courses;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class IngredientQuantiteTest {

    @Test
    void refuse_un_ingredient_nul() {
        Quantite quantite = new Quantite(BigDecimal.TEN, UniteMesure.GRAMME);

        assertThatNullPointerException().isThrownBy(() -> new IngredientQuantite(null, quantite));
    }

    @Test
    void refuse_une_quantite_nulle() {
        Ingredient riz = new Ingredient("riz", RayonMagasin.EPICERIE);

        assertThatNullPointerException().isThrownBy(() -> new IngredientQuantite(riz, null));
    }
}
