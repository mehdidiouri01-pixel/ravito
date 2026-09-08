package com.ravito.domain.courses;

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
    void multiplie_par_un_facteur_en_conservant_l_unite() {
        Quantite cent = new Quantite(BigDecimal.valueOf(100), UniteMesure.GRAMME);

        Quantite pourQuatre = cent.multiplierPar(4);

        assertThat(pourQuatre.valeur()).isEqualByComparingTo("400");
        assertThat(pourQuatre.unite()).isEqualTo(UniteMesure.GRAMME);
    }

    @Test
    void multiplier_par_un_laisse_la_quantite_inchangee() {
        Quantite cent = new Quantite(BigDecimal.valueOf(100), UniteMesure.GRAMME);

        assertThat(cent.multiplierPar(1).valeur()).isEqualByComparingTo("100");
    }

    @Test
    void refuse_un_facteur_nul_ou_negatif() {
        Quantite cent = new Quantite(BigDecimal.valueOf(100), UniteMesure.GRAMME);

        assertThatIllegalArgumentException().isThrownBy(() -> cent.multiplierPar(0));
        assertThatIllegalArgumentException().isThrownBy(() -> cent.multiplierPar(-2));
    }

    @Test
    void refuse_d_additionner_deux_unites_differentes() {
        Quantite grammes = new Quantite(BigDecimal.valueOf(200), UniteMesure.GRAMME);
        Quantite millilitres = new Quantite(BigDecimal.valueOf(200), UniteMesure.MILLILITRE);

        assertThatIllegalArgumentException().isThrownBy(() -> grammes.plus(millilitres));
    }
}
