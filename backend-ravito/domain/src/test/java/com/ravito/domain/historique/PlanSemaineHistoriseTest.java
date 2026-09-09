package com.ravito.domain.historique;

import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.RepasTestFactory;
import com.ravito.domain.catalogue.TypeRepas;
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

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class PlanSemaineHistoriseTest {

    private static final ProfilUtilisateur PROFIL =
            new ProfilUtilisateur(Enseigne.CARREFOUR, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT, NombreDePersonnes.une());

    private static final PlanSemaine PLAN = new PlanSemaine(PROFIL, List.of(JourSemaine.values()).stream()
            .map(PlanSemaineHistoriseTest::jourValide)
            .toList());

    @Test
    void refuse_un_id_nul() {
        assertThatNullPointerException()
                .isThrownBy(() -> new PlanSemaineHistorise(null, Instant.now(), PLAN, new Prix(BigDecimal.TEN)));
    }

    @Test
    void refuse_une_date_de_composition_nulle() {
        assertThatNullPointerException()
                .isThrownBy(() -> new PlanSemaineHistorise(HistoriquePlanId.nouveau(), null, PLAN, new Prix(BigDecimal.TEN)));
    }

    @Test
    void refuse_un_plan_nul() {
        assertThatNullPointerException()
                .isThrownBy(() -> new PlanSemaineHistorise(HistoriquePlanId.nouveau(), Instant.now(), null, new Prix(BigDecimal.TEN)));
    }

    @Test
    void refuse_un_prix_nul() {
        assertThatNullPointerException()
                .isThrownBy(() -> new PlanSemaineHistorise(HistoriquePlanId.nouveau(), Instant.now(), PLAN, null));
    }

    private static Jour jourValide(JourSemaine jourSemaine) {
        Repas petitDejeuner = RepasTestFactory.unRepas(TypeRepas.PETIT_DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT);
        Repas dejeuner = RepasTestFactory.unRepas(TypeRepas.DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT);
        Repas diner = RepasTestFactory.unRepas(TypeRepas.DINER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT);
        return new Jour(jourSemaine, petitDejeuner, dejeuner, diner);
    }
}
