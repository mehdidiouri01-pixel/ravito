package com.ravito.domain.profil;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NiveauCuisineTest {

    @Test
    void un_niveau_est_toujours_au_moins_aussi_confirme_que_lui_meme() {
        assertThat(NiveauCuisine.DEBUTANT.estAuMoinsAussiConfirmeQue(NiveauCuisine.DEBUTANT)).isTrue();
        assertThat(NiveauCuisine.CONFIRME.estAuMoinsAussiConfirmeQue(NiveauCuisine.CONFIRME)).isTrue();
    }

    @Test
    void confirme_couvre_les_repas_debutant() {
        assertThat(NiveauCuisine.CONFIRME.estAuMoinsAussiConfirmeQue(NiveauCuisine.DEBUTANT)).isTrue();
    }

    @Test
    void debutant_ne_couvre_pas_les_repas_confirme() {
        assertThat(NiveauCuisine.DEBUTANT.estAuMoinsAussiConfirmeQue(NiveauCuisine.CONFIRME)).isFalse();
    }
}
