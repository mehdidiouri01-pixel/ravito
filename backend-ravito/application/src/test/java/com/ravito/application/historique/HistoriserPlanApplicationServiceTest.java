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
import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoriserPlanApplicationServiceTest {

    private static final ProfilUtilisateur PROFIL =
            new ProfilUtilisateur(Enseigne.CARREFOUR, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT, NombreDePersonnes.une());

    @Mock
    private HistoriquePlansPort historiquePlansPort;

    private HistoriserPlanApplicationService service;

    @BeforeEach
    void setUp() {
        service = new HistoriserPlanApplicationService(historiquePlansPort);
    }

    @Test
    void delegue_l_enregistrement_au_port_de_sortie() {
        PlanSemaine plan = unPlanValide();
        Prix prix = new Prix(BigDecimal.valueOf(42));
        PlanSemaineHistorise attendu = new PlanSemaineHistorise(HistoriquePlanId.nouveau(), Instant.now(), plan, prix);
        when(historiquePlansPort.enregistrer(plan, prix)).thenReturn(attendu);

        PlanSemaineHistorise resultat = service.historiser(plan, prix);

        assertThat(resultat).isEqualTo(attendu);
        verify(historiquePlansPort).enregistrer(plan, prix);
    }

    @Test
    void refuse_un_plan_nul() {
        assertThatNullPointerException().isThrownBy(() -> service.historiser(null, new Prix(BigDecimal.TEN)));
    }

    @Test
    void refuse_un_prix_nul() {
        assertThatNullPointerException().isThrownBy(() -> service.historiser(unPlanValide(), null));
    }

    private static PlanSemaine unPlanValide() {
        List<Jour> jours = List.of(JourSemaine.values()).stream()
                .map(jourSemaine -> new Jour(jourSemaine, Optional.of(unRepas(TypeRepas.PETIT_DEJEUNER)), Optional.of(unRepas(TypeRepas.DEJEUNER)), Optional.of(unRepas(TypeRepas.DINER))))
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
