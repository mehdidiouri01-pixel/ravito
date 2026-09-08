package com.ravito.domain.catalogue;

import com.ravito.domain.profil.StyleAlimentaire;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenerationRepasImpossibleExceptionTest {

    @Test
    void le_message_mentionne_le_type_et_le_style_recherches() {
        GenerationRepasImpossibleException exception =
                new GenerationRepasImpossibleException(TypeRepas.DINER, StyleAlimentaire.GOURMAND);

        assertThat(exception.getMessage()).contains("DINER").contains("GOURMAND");
    }
}
