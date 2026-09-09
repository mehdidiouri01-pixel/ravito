package com.ravito.domain.planification;

import com.ravito.domain.catalogue.RepasId;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class ChoixJourTest {

    private static final Optional<ChoixRepas> UN_CHOIX = Optional.of(new ChoixRepas.ParId(RepasId.nouveau()));

    @Test
    void accepte_un_ou_plusieurs_repas_non_choisis() {
        ChoixJour choixJour = new ChoixJour(Optional.empty(), UN_CHOIX, Optional.empty());

        assertThat(choixJour.petitDejeuner()).isEmpty();
        assertThat(choixJour.dejeuner()).isEqualTo(UN_CHOIX);
        assertThat(choixJour.diner()).isEmpty();
    }

    @Test
    void refuse_un_petit_dejeuner_nul_au_sens_java() {
        assertThatNullPointerException().isThrownBy(() -> new ChoixJour(null, UN_CHOIX, UN_CHOIX));
    }

    @Test
    void refuse_un_dejeuner_nul_au_sens_java() {
        assertThatNullPointerException().isThrownBy(() -> new ChoixJour(UN_CHOIX, null, UN_CHOIX));
    }

    @Test
    void refuse_un_diner_nul_au_sens_java() {
        assertThatNullPointerException().isThrownBy(() -> new ChoixJour(UN_CHOIX, UN_CHOIX, null));
    }
}
