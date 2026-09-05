package com.ravito.infrastructure.persistence.catalogue;

import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.RepasId;
import com.ravito.domain.catalogue.TypeRepas;
import com.ravito.domain.courses.Ingredient;
import com.ravito.domain.courses.IngredientQuantite;
import com.ravito.domain.courses.Quantite;
import com.ravito.domain.courses.RayonMagasin;
import com.ravito.domain.courses.UniteMesure;
import com.ravito.domain.profil.NiveauCuisine;
import com.ravito.domain.profil.StyleAlimentaire;
import com.ravito.infrastructure.persistence.TestApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test d'integration contre un vrai PostgreSQL (Testcontainers) : verifie
 * que {@link JpaCatalogueRepasAdapter} traduit correctement les requetes
 * Spring Data et le mapping entite/domaine (voir {@link RepasEntityMapper}).
 *
 * <p>Les fixtures sont creees directement via {@link RepasJpaRepository}
 * (pas via l'adapter, qui est un port de LECTURE seule — il n'existe pas
 * de port d'ecriture dans le domaine, le catalogue est peuple par Flyway
 * ou une future fonctionnalite d'administration).
 */
@SpringBootTest(classes = TestApplication.class)
@Testcontainers
@Transactional
class JpaCatalogueRepasAdapterIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private RepasJpaRepository repasJpaRepository;

    @Autowired
    private JpaCatalogueRepasAdapter adapter;

    @Test
    void retrouve_un_repas_par_son_identifiant() {
        RepasEntity entite = repasJpaRepository.save(uneEntite(TypeRepas.DEJEUNER, StyleAlimentaire.HEALTHY, NiveauCuisine.CONFIRME));

        Optional<Repas> repas = adapter.parId(new RepasId(entite.getId()));

        assertThat(repas).isPresent();
        assertThat(repas.get().nom()).isEqualTo(entite.getNom());
        assertThat(repas.get().ingredients()).hasSize(1);
    }

    @Test
    void renvoie_vide_si_l_identifiant_est_inconnu() {
        Optional<Repas> repas = adapter.parId(RepasId.nouveau());

        assertThat(repas).isEmpty();
    }

    @Test
    void ne_retrouve_que_les_repas_du_type_et_du_style_demandes() {
        // Le catalogue seme par Flyway contient deja des repas HEALTHY/GOURMAND
        // (voir V2/V3) : on ne peut pas supposer un total exact, seulement que
        // notre fixture apparait bien, et qu'un GOURMAND ou un DEJEUNER n'apparait
        // jamais dans une recherche PETIT_DEJEUNER/HEALTHY.
        RepasEntity gourmand = repasJpaRepository.save(
                uneEntite(TypeRepas.PETIT_DEJEUNER, StyleAlimentaire.GOURMAND, NiveauCuisine.DEBUTANT));
        RepasEntity attendue = repasJpaRepository.save(
                uneEntite(TypeRepas.PETIT_DEJEUNER, StyleAlimentaire.HEALTHY, NiveauCuisine.DEBUTANT));
        RepasEntity dejeuner = repasJpaRepository.save(
                uneEntite(TypeRepas.DEJEUNER, StyleAlimentaire.HEALTHY, NiveauCuisine.DEBUTANT));

        List<Repas> resultat = adapter.rechercherParTypeEtStyle(TypeRepas.PETIT_DEJEUNER, StyleAlimentaire.HEALTHY);

        assertThat(resultat).extracting(Repas::id).contains(new RepasId(attendue.getId()));
        assertThat(resultat).extracting(Repas::id)
                .doesNotContain(new RepasId(gourmand.getId()), new RepasId(dejeuner.getId()));
    }

    private static RepasEntity uneEntite(TypeRepas type, StyleAlimentaire style, NiveauCuisine niveauRequis) {
        Repas repas = new Repas(RepasId.nouveau(), "repas de test", type, style, niveauRequis,
                List.of(new IngredientQuantite(
                        new Ingredient("ingredient de test", RayonMagasin.EPICERIE),
                        new Quantite(BigDecimal.ONE, UniteMesure.UNITE))));
        return RepasEntityMapper.versEntite(repas);
    }
}
