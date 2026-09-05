package com.mealweek.domain.planification;

import com.mealweek.domain.catalogue.RepasId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class ChoixJourTest {

    @Test
    void refuse_un_identifiant_de_petit_dejeuner_nul() {
        assertThatNullPointerException().isThrownBy(() -> new ChoixJour(null, RepasId.nouveau()));
    }

    @Test
    void refuse_un_identifiant_de_dejeuner_nul() {
        assertThatNullPointerException().isThrownBy(() -> new ChoixJour(RepasId.nouveau(), null));
    }
}
