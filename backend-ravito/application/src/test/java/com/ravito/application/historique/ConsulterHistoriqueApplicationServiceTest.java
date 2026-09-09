package com.ravito.application.historique;

import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.RepasId;
import com.ravito.domain.catalogue.TypeRepas;
import com.ravito.domain.courses.Ingredient;
import com.ravito.domain.courses.IngredientQuantite;
import com.ravito.domain.courses.Quantite;
import com.ravito.domain.courses.RayonMagasin;
import com.ravito.domain.courses.UniteMesure;
import com.ravito.domain.historique.HistoriquePlanId;
import com.ravito.domain.historique.HistoriquePlansPort;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsulterHistoriqueApplicationServiceTest {

    private static final ProfilUtilisateur PROFIL =
            new ProfilUtilisateur(Enseigne.CARREFOUR, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT, NombreDePersonnes.une());

    @Mock
    private HistoriquePlansPort historiquePlansPort;

    private ConsulterHistoriqueApplicationService service;

    @BeforeEach
    void setUp() {
        service = new ConsulterHistoriqueApplicationService(historiquePlansPort);
    }

    @Test
    void delegue_le_listing_au_port_de_sortie() {
        PlanSemaineHistorise unPlan = unPlanHistorise();
        when(historiquePlansPort.lister()).thenReturn(List.of(unPlan));

        assertThat(service.lister()).containsExactly(unPlan);
    }

    @Test
    void delegue_la_recherche_par_id_au_port_de_sortie() {
        PlanSemaineHistorise unPlan = unPlanHistorise();
        when(historiquePlansPort.parId(unPlan.id())).thenReturn(Optional.of(unPlan));

        assertThat(service.parId(unPlan.id())).contains(unPlan);
    }

    @Test
    void refuse_un_id_nul() {
        assertThatNullPointerException().isThrownBy(() -> service.parId(null));
    }

    private static PlanSemaineHistorise unPlanHistorise() {
        List<Jour> jours = List.of(JourSemaine.values()).stream()
                .map(jourSemaine -> new Jour(jourSemaine, unRepas(TypeRepas.PETIT_DEJEUNER), unRepas(TypeRepas.DEJEUNER), unRepas(TypeRepas.DINER)))
                .toList();
        PlanSemaine plan = new PlanSemaine(PROFIL, jours);
        return new PlanSemaineHistorise(HistoriquePlanId.nouveau(), Instant.now(), plan, new Prix(BigDecimal.TEN));
    }

    private static Repas unRepas(TypeRepas type) {
        IngredientQuantite ingredient = new IngredientQuantite(
                new Ingredient("ingredient de test", RayonMagasin.EPICERIE),
                new Quantite(BigDecimal.ONE, UniteMesure.UNITE));
        return new Repas(RepasId.nouveau(), "repas de test", type, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT, List.of(ingredient));
    }
}
