package com.ravito.domain.planification;

import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.RepasTestFactory;
import com.ravito.domain.catalogue.TypeRepas;
import com.ravito.domain.profil.NiveauCuisine;
import com.ravito.domain.profil.StyleAlimentaire;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RepasGenereInvalideExceptionTest {

    @Test
    void le_message_mentionne_le_nom_le_type_et_le_style_du_repas() {
        Repas repas = RepasTestFactory.unRepas(TypeRepas.DINER, StyleAlimentaire.GOURMAND, NiveauCuisine.DEBUTANT);

        RepasGenereInvalideException exception = new RepasGenereInvalideException(repas);

        assertThat(exception.getMessage()).contains(repas.nom()).contains("DINER").contains("GOURMAND");
    }
}
