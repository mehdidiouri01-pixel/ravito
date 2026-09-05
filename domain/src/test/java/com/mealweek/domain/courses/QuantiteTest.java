package com.mealweek.domain.courses;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class QuantiteTest {

    @Test
    void refuse_une_valeur_nulle_ou_negative() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Quantite(BigDecimal.ZERO, UniteMesure.GRAMME));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Quantite(BigDecimal.valueOf(-1), UniteMesure.GRAMME));
    }

    @Test
    void additionne_deux_quantites_de_meme_unite() {
        Quantite deuxCents = new Quantite(BigDecimal.valueOf(200), UniteMesure.GRAMME);
        Quantite cent = new Quantite(BigDecimal.valueOf(100), UniteMesure.GRAMME);

        Quantite total = deuxCents.plus(cent);

        assertThat(total.valeur()).isEqualByComparingTo("300");
        assertThat(total.unite()).isEqualTo(UniteMesure.GRAMME);
    }

    @Test
    void refuse_d_additionner_deux_unites_differentes() {
        Quantite grammes = new Quantite(BigDecimal.valueOf(200), UniteMesure.GRAMME);
        Quantite millilitres = new Quantite(BigDecimal.valueOf(200), UniteMesure.MILLILITRE);

        assertThatIllegalArgumentException().isThrownBy(() -> grammes.plus(millilitres));
    }
}
