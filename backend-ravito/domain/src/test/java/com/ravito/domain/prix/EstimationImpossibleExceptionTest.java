package com.ravito.domain.prix;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EstimationImpossibleExceptionTest {

    @Test
    void porte_le_message_fourni_par_l_adapter() {
        EstimationImpossibleException exception = new EstimationImpossibleException("prix moyen manquant pour le riz");

        assertThat(exception.getMessage()).isEqualTo("prix moyen manquant pour le riz");
    }
}
