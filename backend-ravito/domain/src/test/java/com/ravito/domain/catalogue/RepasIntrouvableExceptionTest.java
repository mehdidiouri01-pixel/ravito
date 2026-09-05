package com.ravito.domain.catalogue;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RepasIntrouvableExceptionTest {

    @Test
    void le_message_mentionne_l_identifiant_recherche() {
        RepasId id = RepasId.nouveau();

        RepasIntrouvableException exception = new RepasIntrouvableException(id);

        assertThat(exception.getMessage()).contains(id.valeur().toString());
    }
}
