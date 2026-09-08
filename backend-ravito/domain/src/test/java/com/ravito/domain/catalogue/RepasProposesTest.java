package com.ravito.domain.catalogue;

import com.ravito.domain.profil.NiveauCuisine;
import com.ravito.domain.profil.StyleAlimentaire;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static com.ravito.domain.catalogue.RepasTestFactory.unRepas;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RepasProposesTest {

    @Test
    void accepte_exactement_cinq_repas_de_chaque_type() {
        List<Repas> petitsDejeuners = cinqRepas(TypeRepas.PETIT_DEJEUNER);
        List<Repas> dejeuners = cinqRepas(TypeRepas.DEJEUNER);
        List<Repas> diners = cinqRepas(TypeRepas.DINER);

        RepasProposes proposition = new RepasProposes(petitsDejeuners, dejeuners, diners);

        assertThat(proposition.petitsDejeuners()).hasSize(5);
        assertThat(proposition.dejeuners()).hasSize(5);
        assertThat(proposition.diners()).hasSize(5);
    }

    @Test
    void refuse_moins_de_cinq_petits_dejeuners() {
        List<Repas> petitsDejeunersInsuffisants = cinqRepas(TypeRepas.PETIT_DEJEUNER).subList(0, 4);
        List<Repas> dejeuners = cinqRepas(TypeRepas.DEJEUNER);
        List<Repas> diners = cinqRepas(TypeRepas.DINER);

        assertThatExceptionOfType(CatalogueInsuffisantException.class)
                .isThrownBy(() -> new RepasProposes(petitsDejeunersInsuffisants, dejeuners, diners));
    }

    @Test
    void refuse_moins_de_cinq_dejeuners() {
        List<Repas> petitsDejeuners = cinqRepas(TypeRepas.PETIT_DEJEUNER);
        List<Repas> dejeunersInsuffisants = cinqRepas(TypeRepas.DEJEUNER).subList(0, 3);
        List<Repas> diners = cinqRepas(TypeRepas.DINER);

        assertThatExceptionOfType(CatalogueInsuffisantException.class)
                .isThrownBy(() -> new RepasProposes(petitsDejeuners, dejeunersInsuffisants, diners));
    }

    @Test
    void refuse_moins_de_cinq_diners() {
        List<Repas> petitsDejeuners = cinqRepas(TypeRepas.PETIT_DEJEUNER);
        List<Repas> dejeuners = cinqRepas(TypeRepas.DEJEUNER);
        List<Repas> dinersInsuffisants = cinqRepas(TypeRepas.DINER).subList(0, 2);

        assertThatExceptionOfType(CatalogueInsuffisantException.class)
                .isThrownBy(() -> new RepasProposes(petitsDejeuners, dejeuners, dinersInsuffisants));
    }

    private static List<Repas> cinqRepas(TypeRepas type) {
        return IntStream.range(0, 5)
                .mapToObj(i -> unRepas("repas " + i, type, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT))
                .toList();
    }
}
