package com.ravito.infrastructure.web.historique;

import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.RepasId;
import com.ravito.domain.catalogue.TypeRepas;
import com.ravito.domain.courses.Ingredient;
import com.ravito.domain.courses.IngredientQuantite;
import com.ravito.domain.courses.Quantite;
import com.ravito.domain.courses.RayonMagasin;
import com.ravito.domain.courses.UniteMesure;
import com.ravito.domain.historique.ConsulterHistoriqueUseCase;
import com.ravito.domain.historique.HistoriquePlanId;
import com.ravito.domain.historique.HistoriquePlanIntrouvableException;
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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConsulterHistoriqueController.class)
class ConsulterHistoriqueControllerTest {

    private static final ProfilUtilisateur PROFIL =
            new ProfilUtilisateur(Enseigne.CARREFOUR, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT, NombreDePersonnes.une());

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConsulterHistoriqueUseCase consulterHistoriqueUseCase;

    @Test
    void renvoie_200_avec_la_liste_des_plans_historises() throws Exception {
        when(consulterHistoriqueUseCase.lister()).thenReturn(List.of(unPlanHistorise()));

        mockMvc.perform(get("/api/plans-semaine/historique"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].style").value("NORMAL"))
                .andExpect(jsonPath("$[0].nombreDePersonnes").value(1));
    }

    @Test
    void renvoie_200_avec_le_detail_d_un_plan_historise() throws Exception {
        PlanSemaineHistorise historise = unPlanHistorise();
        when(consulterHistoriqueUseCase.parId(historise.id())).thenReturn(Optional.of(historise));

        mockMvc.perform(get("/api/plans-semaine/historique/" + historise.id().valeur()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jours", org.hamcrest.Matchers.hasSize(5)))
                .andExpect(jsonPath("$.prixEstime.montant").value(51.30));
    }

    @Test
    void renvoie_404_si_l_identifiant_est_inconnu() throws Exception {
        when(consulterHistoriqueUseCase.parId(any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/plans-semaine/historique/" + HistoriquePlanId.nouveau().valeur()))
                .andExpect(status().isNotFound());
    }

    private static PlanSemaineHistorise unPlanHistorise() {
        List<Jour> jours = List.of(JourSemaine.values()).stream()
                .map(jourSemaine -> new Jour(jourSemaine, unRepas(TypeRepas.PETIT_DEJEUNER), unRepas(TypeRepas.DEJEUNER), unRepas(TypeRepas.DINER)))
                .toList();
        PlanSemaine plan = new PlanSemaine(PROFIL, jours);
        return new PlanSemaineHistorise(HistoriquePlanId.nouveau(), Instant.now(), plan, new Prix(BigDecimal.valueOf(51.30)));
    }

    private static Repas unRepas(TypeRepas type) {
        IngredientQuantite ingredient = new IngredientQuantite(
                new Ingredient("ingredient de test", RayonMagasin.EPICERIE),
                new Quantite(BigDecimal.ONE, UniteMesure.UNITE));
        return new Repas(RepasId.nouveau(), "repas de test", type, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT, List.of(ingredient));
    }
}
