package com.mealweek.domain.courses;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class IngredientTest {

    @Test
    void normalise_le_nom_en_minuscules_sans_espaces_superflus() {
        Ingredient ingredient = new Ingredient("  Riz  ", RayonMagasin.EPICERIE);

        assertThat(ingredient.nom()).isEqualTo("riz");
    }

    @Test
    void deux_ingredients_de_meme_nom_normalise_sont_egaux() {
        Ingredient riz1 = new Ingredient("Riz", RayonMagasin.EPICERIE);
        Ingredient riz2 = new Ingredient(" riz", RayonMagasin.EPICERIE);

        assertThat(riz1).isEqualTo(riz2);
    }

    @Test
    void refuse_un_nom_vide_ou_blanc() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Ingredient("   ", RayonMagasin.EPICERIE));
    }
}
