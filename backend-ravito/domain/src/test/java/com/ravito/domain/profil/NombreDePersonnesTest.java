package com.ravito.domain.profil;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class NombreDePersonnesTest {

    @Test
    void accepte_les_valeurs_du_minimum_au_maximum() {
        assertThat(new NombreDePersonnes(NombreDePersonnes.MINIMUM).valeur()).isEqualTo(1);
        assertThat(new NombreDePersonnes(4).valeur()).isEqualTo(4);
        assertThat(new NombreDePersonnes(NombreDePersonnes.MAXIMUM).valeur()).isEqualTo(12);
    }

    @Test
    void refuse_moins_d_une_personne() {
        assertThatIllegalArgumentException().isThrownBy(() -> new NombreDePersonnes(0));
        assertThatIllegalArgumentException().isThrownBy(() -> new NombreDePersonnes(-3));
    }

    @Test
    void refuse_au_dela_du_maximum() {
        assertThatIllegalArgumentException().isThrownBy(() -> new NombreDePersonnes(NombreDePersonnes.MAXIMUM + 1));
    }

    @Test
    void une_correspond_a_un_foyer_d_une_personne() {
        assertThat(NombreDePersonnes.une()).isEqualTo(new NombreDePersonnes(1));
    }
}
