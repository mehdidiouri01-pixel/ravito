package com.ravito.domain.prix;

import com.ravito.domain.courses.Ingredient;
import com.ravito.domain.courses.LigneListeCourses;
import com.ravito.domain.courses.Quantite;
import com.ravito.domain.courses.RayonMagasin;
import com.ravito.domain.courses.UniteMesure;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class LignePrixEstimeTest {

    private static final LigneListeCourses UNE_LIGNE = new LigneListeCourses(
            new Ingredient("riz", RayonMagasin.EPICERIE), new Quantite(BigDecimal.valueOf(150), UniteMesure.GRAMME));

    @Test
    void refuse_une_ligne_nulle() {
        assertThatNullPointerException().isThrownBy(() -> new LignePrixEstime(null, Prix.ZERO));
    }

    @Test
    void refuse_un_prix_nul() {
        assertThatNullPointerException().isThrownBy(() -> new LignePrixEstime(UNE_LIGNE, null));
    }
}
