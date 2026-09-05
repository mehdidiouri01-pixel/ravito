package com.mealweek.domain.catalogue;

import com.mealweek.domain.profil.NiveauCuisine;
import com.mealweek.domain.profil.StyleAlimentaire;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static com.mealweek.domain.catalogue.RepasTestFactory.unRepas;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RepasProposesTest {

    @Test
    void accepte_exactement_cinq_repas_de_chaque_type() {
        List<Repas> petitsDejeuners = cinqRepas(TypeRepas.PETIT_DEJEUNER);
        List<Repas> dejeuners = cinqRepas(TypeRepas.DEJEUNER);

        RepasProposes proposition = new RepasProposes(petitsDejeuners, dejeuners);

        assertThat(proposition.petitsDejeuners()).hasSize(5);
        assertThat(proposition.dejeuners()).hasSize(5);
    }

    @Test
    void refuse_moins_de_cinq_petits_dejeuners() {
        List<Repas> petitsDejeunersInsuffisants = cinqRepas(TypeRepas.PETIT_DEJEUNER).subList(0, 4);
        List<Repas> dejeuners = cinqRepas(TypeRepas.DEJEUNER);

        assertThatExceptionOfType(CatalogueInsuffisantException.class)
                .isThrownBy(() -> new RepasProposes(petitsDejeunersInsuffisants, dejeuners));
    }

    @Test
    void refuse_moins_de_cinq_dejeuners() {
        List<Repas> petitsDejeuners = cinqRepas(TypeRepas.PETIT_DEJEUNER);
        List<Repas> dejeunersInsuffisants = cinqRepas(TypeRepas.DEJEUNER).subList(0, 3);

        assertThatExceptionOfType(CatalogueInsuffisantException.class)
                .isThrownBy(() -> new RepasProposes(petitsDejeuners, dejeunersInsuffisants));
    }

    private static List<Repas> cinqRepas(TypeRepas type) {
        return IntStream.range(0, 5)
                .mapToObj(i -> unRepas("repas " + i, type, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT))
                .toList();
    }
}
