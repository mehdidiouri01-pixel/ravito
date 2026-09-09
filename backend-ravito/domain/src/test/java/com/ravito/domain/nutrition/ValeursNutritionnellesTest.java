package com.ravito.domain.nutrition;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class ValeursNutritionnellesTest {

    @Test
    void accepte_des_valeurs_nulles_ou_positives() {
        ValeursNutritionnelles valeurs = new ValeursNutritionnelles(
                BigDecimal.valueOf(250), BigDecimal.valueOf(12), BigDecimal.valueOf(30), BigDecimal.valueOf(8), BigDecimal.valueOf(4));

        assertThat(valeurs.calories()).isEqualByComparingTo("250");
        assertThat(valeurs.proteines()).isEqualByComparingTo("12");
    }

    @Test
    void refuse_une_valeur_negative() {
        assertThatIllegalArgumentException().isThrownBy(() -> new ValeursNutritionnelles(
                BigDecimal.valueOf(-1), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
    }

    @Test
    void refuse_une_valeur_nulle_au_sens_java() {
        assertThatNullPointerException().isThrownBy(() -> new ValeursNutritionnelles(
                null, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
    }

    @Test
    void additionne_deux_estimations_champ_par_champ() {
        ValeursNutritionnelles a = new ValeursNutritionnelles(
                BigDecimal.valueOf(100), BigDecimal.valueOf(5), BigDecimal.valueOf(10), BigDecimal.valueOf(2), BigDecimal.valueOf(1));
        ValeursNutritionnelles b = new ValeursNutritionnelles(
                BigDecimal.valueOf(50), BigDecimal.valueOf(3), BigDecimal.valueOf(4), BigDecimal.valueOf(1), BigDecimal.valueOf(2));

        ValeursNutritionnelles total = a.plus(b);

        assertThat(total.calories()).isEqualByComparingTo("150");
        assertThat(total.proteines()).isEqualByComparingTo("8");
        assertThat(total.glucides()).isEqualByComparingTo("14");
        assertThat(total.lipides()).isEqualByComparingTo("3");
        assertThat(total.fibres()).isEqualByComparingTo("3");
    }

    @Test
    void zero_est_neutre_pour_l_addition() {
        ValeursNutritionnelles valeurs = new ValeursNutritionnelles(
                BigDecimal.valueOf(250), BigDecimal.valueOf(12), BigDecimal.valueOf(30), BigDecimal.valueOf(8), BigDecimal.valueOf(4));

        assertThat(valeurs.plus(ValeursNutritionnelles.ZERO)).isEqualTo(valeurs);
    }
}
