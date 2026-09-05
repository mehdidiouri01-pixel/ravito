package com.ravito.infrastructure.persistence.prix;

import com.ravito.domain.courses.Ingredient;
import com.ravito.domain.courses.LigneListeCourses;
import com.ravito.domain.courses.ListeCourses;
import com.ravito.domain.courses.Quantite;
import com.ravito.domain.courses.RayonMagasin;
import com.ravito.domain.courses.UniteMesure;
import com.ravito.domain.prix.EstimationImpossibleException;
import com.ravito.domain.prix.Prix;
import com.ravito.domain.profil.Enseigne;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Test d'integration contre un vrai PostgreSQL : verifie le calcul de
 * {@link JpaEstimationPrixAdapter} (somme des prix moyens ponderee par le
 * coefficient de l'enseigne, lu depuis {@code application.yml} de test via
 * {@link CoefficientEnseigneProperties}).
 *
 * <p>Les noms d'ingredients utilises ici sont volontairement des noms de
 * test explicites ("ingredient-test-..."), jamais des noms d'ingredients
 * plausibles du catalogue reel (seme par Flyway, voir V2/V3) : ce test
 * insere ses propres lignes dans {@code prix_moyen_ingredient}, qui a une
 * contrainte d'unicite sur (nom, unite) — un nom realiste choisi ici
 * finirait tot ou tard par entrer en collision avec un ingredient ajoute
 * au catalogue.
 */
@SpringBootTest(classes = TestApplication.class)
@Testcontainers
@Transactional
class JpaEstimationPrixAdapterIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private PrixMoyenIngredientJpaRepository prixMoyenRepository;

    @Autowired
    private JpaEstimationPrixAdapter adapter;

    @Test
    void additionne_les_prix_moyens_et_applique_le_coefficient_de_l_enseigne() {
        prixMoyenRepository.save(new PrixMoyenIngredientEntity("ingredient-test-un", UniteMesure.GRAMME, BigDecimal.valueOf(0.0030)));
        prixMoyenRepository.save(new PrixMoyenIngredientEntity("ingredient-test-deux", UniteMesure.MILLILITRE, BigDecimal.valueOf(0.0011)));

        ListeCourses listeCourses = new ListeCourses(List.of(
                new LigneListeCourses(new Ingredient("ingredient-test-un", RayonMagasin.EPICERIE),
                        new Quantite(BigDecimal.valueOf(300), UniteMesure.GRAMME)),
                new LigneListeCourses(new Ingredient("ingredient-test-deux", RayonMagasin.CREMERIE),
                        new Quantite(BigDecimal.valueOf(500), UniteMesure.MILLILITRE))));

        // sous-total attendu : 300*0.0030 + 500*0.0011 = 0.90 + 0.55 = 1.45
        // LIDL, coefficient 0.85 (voir application.yml de test) : 1.45 * 0.85 = 1.2325
        Prix prix = adapter.estimerCout(listeCourses, Enseigne.LIDL);

        assertThat(prix.montant()).isEqualByComparingTo("1.2325");
    }

    @Test
    void leve_une_exception_si_un_ingredient_n_a_pas_de_prix_moyen_connu() {
        ListeCourses listeCourses = new ListeCourses(List.of(
                new LigneListeCourses(new Ingredient("ingredient-test-inconnu", RayonMagasin.EPICERIE),
                        new Quantite(BigDecimal.TEN, UniteMesure.GRAMME))));

        assertThatExceptionOfType(EstimationImpossibleException.class)
                .isThrownBy(() -> adapter.estimerCout(listeCourses, Enseigne.CARREFOUR));
    }
}
