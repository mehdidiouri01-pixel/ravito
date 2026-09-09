package com.ravito.infrastructure.persistence.nutrition;

import com.ravito.domain.courses.Ingredient;
import com.ravito.domain.courses.IngredientQuantite;
import com.ravito.domain.courses.Quantite;
import com.ravito.domain.courses.RayonMagasin;
import com.ravito.domain.courses.UniteMesure;
import com.ravito.domain.nutrition.EstimationNutritionImpossibleException;
import com.ravito.domain.nutrition.ValeursNutritionnelles;
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
 * {@link JpaEstimationNutritionAdapter} (somme des valeurs nutritionnelles
 * moyennes ponderee par la quantite de chaque ingredient).
 *
 * <p>Noms d'ingredients volontairement des noms de test explicites, jamais
 * plausibles du catalogue reel — meme raisonnement que
 * {@code JpaEstimationPrixAdapterIT} : cette table a une contrainte
 * d'unicite sur (nom, unite), un nom realiste finirait par entrer en
 * collision avec un ingredient du catalogue seme par Flyway.
 */
@SpringBootTest(classes = TestApplication.class)
@Testcontainers
@Transactional
class JpaEstimationNutritionAdapterIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private ValeurNutritionnelleIngredientJpaRepository valeurNutritionnelleRepository;

    @Autowired
    private JpaEstimationNutritionAdapter adapter;

    @Test
    void additionne_les_valeurs_nutritionnelles_ponderees_par_la_quantite() {
        valeurNutritionnelleRepository.save(new ValeurNutritionnelleIngredientEntity("ingredient-test-un", UniteMesure.GRAMME,
                BigDecimal.valueOf(2.0000), BigDecimal.valueOf(0.1000), BigDecimal.valueOf(0.3000),
                BigDecimal.valueOf(0.0500), BigDecimal.valueOf(0.0200)));
        valeurNutritionnelleRepository.save(new ValeurNutritionnelleIngredientEntity("ingredient-test-deux", UniteMesure.MILLILITRE,
                BigDecimal.valueOf(0.6000), BigDecimal.valueOf(0.0300), BigDecimal.valueOf(0.0500),
                BigDecimal.valueOf(0.0400), BigDecimal.valueOf(0.0000)));

        List<IngredientQuantite> ingredients = List.of(
                new IngredientQuantite(new Ingredient("ingredient-test-un", RayonMagasin.EPICERIE),
                        new Quantite(BigDecimal.valueOf(100), UniteMesure.GRAMME)),
                new IngredientQuantite(new Ingredient("ingredient-test-deux", RayonMagasin.CREMERIE),
                        new Quantite(BigDecimal.valueOf(200), UniteMesure.MILLILITRE)));

        // ingredient-test-un (100g) : 200 kcal, 10g proteines, 30g glucides, 5g lipides, 2g fibres
        // ingredient-test-deux (200ml) : 120 kcal, 6g proteines, 10g glucides, 8g lipides, 0g fibres
        // total : 320 kcal, 16g proteines, 40g glucides, 13g lipides, 2g fibres
        ValeursNutritionnelles valeurs = adapter.estimer(ingredients);

        assertThat(valeurs.calories()).isEqualByComparingTo("320.0000");
        assertThat(valeurs.proteines()).isEqualByComparingTo("16.0000");
        assertThat(valeurs.glucides()).isEqualByComparingTo("40.0000");
        assertThat(valeurs.lipides()).isEqualByComparingTo("13.0000");
        assertThat(valeurs.fibres()).isEqualByComparingTo("2.0000");
    }

    @Test
    void leve_une_exception_si_un_ingredient_n_a_pas_de_valeur_nutritionnelle_connue() {
        List<IngredientQuantite> ingredients = List.of(
                new IngredientQuantite(new Ingredient("ingredient-test-inconnu", RayonMagasin.EPICERIE),
                        new Quantite(BigDecimal.TEN, UniteMesure.GRAMME)));

        assertThatExceptionOfType(EstimationNutritionImpossibleException.class)
                .isThrownBy(() -> adapter.estimer(ingredients));
    }
}
