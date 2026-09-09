package com.ravito.domain.planification;

import com.ravito.domain.catalogue.RepasId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class ChoixJourTest {

    private static final ChoixRepas UN_CHOIX = new ChoixRepas.ParId(RepasId.nouveau());

    @Test
    void refuse_un_petit_dejeuner_nul() {
        assertThatNullPointerException().isThrownBy(() -> new ChoixJour(null, UN_CHOIX, UN_CHOIX));
    }

    @Test
    void refuse_un_dejeuner_nul() {
        assertThatNullPointerException().isThrownBy(() -> new ChoixJour(UN_CHOIX, null, UN_CHOIX));
    }

    @Test
    void refuse_un_diner_nul() {
        assertThatNullPointerException().isThrownBy(() -> new ChoixJour(UN_CHOIX, UN_CHOIX, null));
    }
}
