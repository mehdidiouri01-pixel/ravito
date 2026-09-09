package com.ravito.domain.planification;

import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.RepasTestFactory;
import com.ravito.domain.catalogue.TypeRepas;
import com.ravito.domain.profil.NiveauCuisine;
import com.ravito.domain.profil.StyleAlimentaire;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class JourTest {

    @Test
    void construit_un_jour_valide() {
        Repas petitDejeuner = RepasTestFactory.unRepas(TypeRepas.PETIT_DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT);
        Repas dejeuner = RepasTestFactory.unRepas(TypeRepas.DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT);
        Repas diner = RepasTestFactory.unRepas(TypeRepas.DINER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT);

        Jour jour = new Jour(JourSemaine.LUNDI, Optional.of(petitDejeuner), Optional.of(dejeuner), Optional.of(diner));

        assertThat(jour.petitDejeuner()).contains(petitDejeuner);
        assertThat(jour.dejeuner()).contains(dejeuner);
        assertThat(jour.diner()).contains(diner);
        assertThat(jour.repasChoisis()).containsExactly(petitDejeuner, dejeuner, diner);
    }

    @Test
    void accepte_un_jour_partiellement_ou_totalement_vide() {
        Repas dejeuner = RepasTestFactory.unRepas(TypeRepas.DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT);

        Jour partiel = new Jour(JourSemaine.LUNDI, Optional.empty(), Optional.of(dejeuner), Optional.empty());
        Jour vide = new Jour(JourSemaine.MARDI, Optional.empty(), Optional.empty(), Optional.empty());

        assertThat(partiel.repasChoisis()).containsExactly(dejeuner);
        assertThat(vide.repasChoisis()).isEmpty();
    }

    @Test
    void refuse_un_dejeuner_a_la_place_du_petit_dejeuner() {
        Repas dejeuner = RepasTestFactory.unRepas(TypeRepas.DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT);
        Repas autreDejeuner = RepasTestFactory.unRepas(TypeRepas.DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT);
        Repas diner = RepasTestFactory.unRepas(TypeRepas.DINER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT);

        assertThatExceptionOfType(PlanSemaineInvalideException.class)
                .isThrownBy(() -> new Jour(JourSemaine.LUNDI, Optional.of(dejeuner), Optional.of(autreDejeuner), Optional.of(diner)));
    }

    @Test
    void refuse_un_petit_dejeuner_a_la_place_du_dejeuner() {
        Repas petitDejeuner = RepasTestFactory.unRepas(TypeRepas.PETIT_DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT);
        Repas autrePetitDejeuner = RepasTestFactory.unRepas(TypeRepas.PETIT_DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT);
        Repas diner = RepasTestFactory.unRepas(TypeRepas.DINER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT);

        assertThatExceptionOfType(PlanSemaineInvalideException.class)
                .isThrownBy(() -> new Jour(JourSemaine.LUNDI, Optional.of(petitDejeuner), Optional.of(autrePetitDejeuner), Optional.of(diner)));
    }

    @Test
    void refuse_un_dejeuner_a_la_place_du_diner() {
        Repas petitDejeuner = RepasTestFactory.unRepas(TypeRepas.PETIT_DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT);
        Repas dejeuner = RepasTestFactory.unRepas(TypeRepas.DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT);
        Repas autreDejeuner = RepasTestFactory.unRepas(TypeRepas.DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT);

        assertThatExceptionOfType(PlanSemaineInvalideException.class)
                .isThrownBy(() -> new Jour(JourSemaine.LUNDI, Optional.of(petitDejeuner), Optional.of(dejeuner), Optional.of(autreDejeuner)));
    }

    @Test
    void refuse_des_optional_nuls_au_sens_java() {
        Repas petitDejeuner = RepasTestFactory.unRepas(TypeRepas.PETIT_DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT);
        Repas dejeuner = RepasTestFactory.unRepas(TypeRepas.DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT);

        assertThatNullPointerException()
                .isThrownBy(() -> new Jour(JourSemaine.LUNDI, Optional.of(petitDejeuner), Optional.of(dejeuner), null));
        assertThatNullPointerException()
                .isThrownBy(() -> new Jour(JourSemaine.LUNDI, Optional.of(petitDejeuner), null, Optional.of(petitDejeuner)));
        assertThatNullPointerException()
                .isThrownBy(() -> new Jour(JourSemaine.LUNDI, null, Optional.of(dejeuner), Optional.of(dejeuner)));
        assertThatNullPointerException()
                .isThrownBy(() -> new Jour(null, Optional.of(petitDejeuner), Optional.of(dejeuner), Optional.of(petitDejeuner)));
    }
}
