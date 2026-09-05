package com.mealweek.domain.prix;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class PrixTest {

    @Test
    void accepte_un_montant_nul_ou_positif() {
        assertThat(new Prix(BigDecimal.ZERO).montant()).isEqualByComparingTo("0");
        assertThat(new Prix(BigDecimal.TEN).montant()).isEqualByComparingTo("10");
    }

    @Test
    void refuse_un_montant_negatif() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Prix(BigDecimal.valueOf(-0.01)));
    }

    @Test
    void refuse_un_montant_nul_au_sens_java() {
        assertThatNullPointerException().isThrownBy(() -> new Prix(null));
    }

    @Test
    void additionne_deux_prix() {
        Prix total = new Prix(BigDecimal.valueOf(12.50)).plus(new Prix(BigDecimal.valueOf(7.30)));

        assertThat(total.montant()).isEqualByComparingTo("19.80");
    }

    @Test
    void zero_est_neutre_pour_l_addition() {
        Prix prix = new Prix(BigDecimal.valueOf(42));

        assertThat(prix.plus(Prix.ZERO).montant()).isEqualByComparingTo("42");
    }
}
