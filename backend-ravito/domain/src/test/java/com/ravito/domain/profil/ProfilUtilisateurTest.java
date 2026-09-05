package com.ravito.domain.profil;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class ProfilUtilisateurTest {

    @Test
    void construit_un_profil_valide() {
        ProfilUtilisateur profil = new ProfilUtilisateur(Enseigne.CARREFOUR, StyleAlimentaire.HEALTHY, NiveauCuisine.DEBUTANT);

        assertThat(profil.enseigne()).isEqualTo(Enseigne.CARREFOUR);
        assertThat(profil.style()).isEqualTo(StyleAlimentaire.HEALTHY);
        assertThat(profil.niveau()).isEqualTo(NiveauCuisine.DEBUTANT);
    }

    @Test
    void refuse_une_enseigne_nulle() {
        assertThatNullPointerException()
                .isThrownBy(() -> new ProfilUtilisateur(null, StyleAlimentaire.NORMAL, NiveauCuisine.CONFIRME));
    }

    @Test
    void refuse_un_style_nul() {
        assertThatNullPointerException()
                .isThrownBy(() -> new ProfilUtilisateur(Enseigne.LIDL, null, NiveauCuisine.CONFIRME));
    }

    @Test
    void refuse_un_niveau_nul() {
        assertThatNullPointerException()
                .isThrownBy(() -> new ProfilUtilisateur(Enseigne.LIDL, StyleAlimentaire.NORMAL, null));
    }
}
