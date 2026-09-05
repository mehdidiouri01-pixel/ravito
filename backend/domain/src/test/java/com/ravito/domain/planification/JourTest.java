package com.ravito.domain.planification;

import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.RepasTestFactory;
import com.ravito.domain.catalogue.TypeRepas;
import com.ravito.domain.profil.NiveauCuisine;
import com.ravito.domain.profil.StyleAlimentaire;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class JourTest {

    @Test
    void construit_un_jour_valide() {
        Repas petitDejeuner = RepasTestFactory.unRepas(TypeRepas.PETIT_DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT);
        Repas dejeuner = RepasTestFactory.unRepas(TypeRepas.DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT);

        Jour jour = new Jour(JourSemaine.LUNDI, petitDejeuner, dejeuner);

        assertThat(jour.petitDejeuner()).isEqualTo(petitDejeuner);
        assertThat(jour.dejeuner()).isEqualTo(dejeuner);
    }

    @Test
    void refuse_un_dejeuner_a_la_place_du_petit_dejeuner() {
        Repas dejeuner = RepasTestFactory.unRepas(TypeRepas.DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT);
        Repas autreDejeuner = RepasTestFactory.unRepas(TypeRepas.DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT);

        assertThatExceptionOfType(PlanSemaineInvalideException.class)
                .isThrownBy(() -> new Jour(JourSemaine.LUNDI, dejeuner, autreDejeuner));
    }

    @Test
    void refuse_un_petit_dejeuner_a_la_place_du_dejeuner() {
        Repas petitDejeuner = RepasTestFactory.unRepas(TypeRepas.PETIT_DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT);
        Repas autrePetitDejeuner = RepasTestFactory.unRepas(TypeRepas.PETIT_DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT);

        assertThatExceptionOfType(PlanSemaineInvalideException.class)
                .isThrownBy(() -> new Jour(JourSemaine.LUNDI, petitDejeuner, autrePetitDejeuner));
    }

    @Test
    void refuse_des_repas_nuls() {
        Repas petitDejeuner = RepasTestFactory.unRepas(TypeRepas.PETIT_DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT);

        assertThatNullPointerException().isThrownBy(() -> new Jour(JourSemaine.LUNDI, petitDejeuner, null));
        assertThatNullPointerException().isThrownBy(() -> new Jour(JourSemaine.LUNDI, null, petitDejeuner));
        assertThatNullPointerException().isThrownBy(() -> new Jour(null, petitDejeuner, petitDejeuner));
    }
}
