package com.ravito.infrastructure.persistence.historique;

import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.RepasId;
import com.ravito.domain.catalogue.TypeRepas;
import com.ravito.domain.courses.Ingredient;
import com.ravito.domain.courses.IngredientQuantite;
import com.ravito.domain.courses.Quantite;
import com.ravito.domain.courses.RayonMagasin;
import com.ravito.domain.courses.UniteMesure;
import com.ravito.domain.historique.HistoriquePlanId;
import com.ravito.domain.historique.PlanSemaineHistorise;
import com.ravito.domain.planification.Jour;
import com.ravito.domain.planification.JourSemaine;
import com.ravito.domain.planification.PlanSemaine;
import com.ravito.domain.profil.Enseigne;
import com.ravito.domain.profil.NiveauCuisine;
import com.ravito.domain.profil.NombreDePersonnes;
import com.ravito.domain.profil.ProfilUtilisateur;
import com.ravito.domain.profil.StyleAlimentaire;
import com.ravito.domain.prix.Prix;
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
 * que {@link JpaHistoriquePlansAdapter} enregistre et relit correctement un
 * plan compose, ingredients de chaque repas compris (voir
 * {@link PlanHistoriqueEntityMapper}).
 */
@SpringBootTest(classes = TestApplication.class)
@Testcontainers
@Transactional
class JpaHistoriquePlansAdapterIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private JpaHistoriquePlansAdapter adapter;

    private static final ProfilUtilisateur PROFIL =
            new ProfilUtilisateur(Enseigne.CARREFOUR, StyleAlimentaire.NORMAL, NiveauCuisine.CONFIRME, new NombreDePersonnes(3));

    @Test
    void enregistre_puis_retrouve_un_plan_par_son_identifiant_ingredients_compris() {
        PlanSemaine plan = unPlanValide();
        Prix prix = new Prix(BigDecimal.valueOf(51.30));

        PlanSemaineHistorise enregistre = adapter.enregistrer(plan, prix);
        Optional<PlanSemaineHistorise> relu = adapter.parId(enregistre.id());

        assertThat(relu).isPresent();
        assertThat(relu.get().prixEstime()).isEqualTo(prix);
        assertThat(relu.get().plan().profil()).isEqualTo(PROFIL);
        assertThat(relu.get().plan().jours()).hasSize(5);
        assertThat(relu.get().plan().tousLesRepas()).hasSize(15);
        assertThat(relu.get().plan().tousLesRepas())
                .flatExtracting(Repas::ingredients)
                .extracting(ingredientQuantite -> ingredientQuantite.ingredient().nom())
                .allMatch(nom -> nom.equals("ingredient de test"));
    }

    @Test
    void renvoie_vide_si_l_identifiant_est_inconnu() {
        assertThat(adapter.parId(HistoriquePlanId.nouveau())).isEmpty();
    }

    @Test
    void liste_du_plus_recent_au_plus_ancien() {
        PlanSemaineHistorise premier = adapter.enregistrer(unPlanValide(), new Prix(BigDecimal.ONE));
        PlanSemaineHistorise second = adapter.enregistrer(unPlanValide(), new Prix(BigDecimal.TEN));

        List<PlanSemaineHistorise> liste = adapter.lister();

        assertThat(liste).extracting(PlanSemaineHistorise::id).containsSubsequence(second.id(), premier.id());
    }

    private static PlanSemaine unPlanValide() {
        List<Jour> jours = List.of(JourSemaine.values()).stream()
                .map(jourSemaine -> new Jour(jourSemaine, unRepas(TypeRepas.PETIT_DEJEUNER), unRepas(TypeRepas.DEJEUNER), unRepas(TypeRepas.DINER)))
                .toList();
        return new PlanSemaine(PROFIL, jours);
    }

    private static Repas unRepas(TypeRepas type) {
        IngredientQuantite ingredient = new IngredientQuantite(
                new Ingredient("ingredient de test", RayonMagasin.EPICERIE),
                new Quantite(BigDecimal.ONE, UniteMesure.UNITE));
        return new Repas(RepasId.nouveau(), "repas de test", type, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT,
                List.of(ingredient), List.of("Etape de test"));
    }
}
