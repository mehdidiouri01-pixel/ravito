package com.ravito.domain.planification;

import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.RepasId;
import com.ravito.domain.catalogue.RepasTestFactory;
import com.ravito.domain.catalogue.TypeRepas;
import com.ravito.domain.profil.NiveauCuisine;
import com.ravito.domain.profil.StyleAlimentaire;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class ChoixRepasTest {

    @Test
    void parId_refuse_un_identifiant_nul() {
        assertThatNullPointerException().isThrownBy(() -> new ChoixRepas.ParId(null));
    }

    @Test
    void genere_refuse_un_repas_nul() {
        assertThatNullPointerException().isThrownBy(() -> new ChoixRepas.Genere(null));
    }

    @Test
    void distingue_bien_les_deux_variantes_au_pattern_matching() {
        RepasId id = RepasId.nouveau();
        Repas repas = RepasTestFactory.unRepas(TypeRepas.DINER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT);

        ChoixRepas parId = new ChoixRepas.ParId(id);
        ChoixRepas genere = new ChoixRepas.Genere(repas);

        assertThat(resume(parId)).isEqualTo("id:" + id.valeur());
        assertThat(resume(genere)).isEqualTo("genere:" + repas.nom());
    }

    private static String resume(ChoixRepas choix) {
        return switch (choix) {
            case ChoixRepas.ParId parId -> "id:" + parId.id().valeur();
            case ChoixRepas.Genere genere -> "genere:" + genere.repas().nom();
        };
    }
}
