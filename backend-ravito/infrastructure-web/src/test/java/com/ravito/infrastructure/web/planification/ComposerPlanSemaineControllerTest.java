package com.ravito.infrastructure.web.planification;

import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.RepasId;
import com.ravito.domain.catalogue.RepasIntrouvableException;
import com.ravito.domain.catalogue.TypeRepas;
import com.ravito.domain.courses.Ingredient;
import com.ravito.domain.courses.IngredientQuantite;
import com.ravito.domain.courses.Quantite;
import com.ravito.domain.courses.RayonMagasin;
import com.ravito.domain.courses.UniteMesure;
import com.ravito.domain.historique.HistoriserPlanUseCase;
import com.ravito.domain.planification.ComposerPlanSemaineUseCase;
import com.ravito.domain.planification.Jour;
import com.ravito.domain.planification.JourSemaine;
import com.ravito.domain.planification.PlanSemaine;
import com.ravito.domain.planification.RepasIncompatibleException;
import com.ravito.domain.prix.EstimerCoutUseCase;
import com.ravito.domain.prix.Prix;
import com.ravito.domain.profil.Enseigne;
import com.ravito.domain.profil.NiveauCuisine;
import com.ravito.domain.profil.NombreDePersonnes;
import com.ravito.domain.profil.ProfilUtilisateur;
import com.ravito.domain.profil.StyleAlimentaire;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ComposerPlanSemaineController.class)
class ComposerPlanSemaineControllerTest {

    private static final ProfilUtilisateur PROFIL =
            new ProfilUtilisateur(Enseigne.CARREFOUR, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT, NombreDePersonnes.une());

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComposerPlanSemaineUseCase composerPlanSemaineUseCase;

    @MockBean
    private EstimerCoutUseCase estimerCoutUseCase;

    @MockBean
    private HistoriserPlanUseCase historiserPlanUseCase;

    @Test
    void renvoie_200_avec_le_plan_la_liste_de_courses_et_le_prix() throws Exception {
        when(composerPlanSemaineUseCase.composer(any(), any())).thenReturn(unPlanValide());
        when(estimerCoutUseCase.estimer(any(), any())).thenReturn(new Prix(BigDecimal.valueOf(42.50)));

        mockMvc.perform(post("/api/plans-semaine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(uneRequeteValide()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jours", org.hamcrest.Matchers.hasSize(5)))
                .andExpect(jsonPath("$.prixEstime.montant").value(42.50));
    }

    @Test
    void historise_le_plan_tout_juste_compose() throws Exception {
        PlanSemaine plan = unPlanValide();
        Prix prix = new Prix(BigDecimal.valueOf(42.50));
        when(composerPlanSemaineUseCase.composer(any(), any())).thenReturn(plan);
        when(estimerCoutUseCase.estimer(any(), any())).thenReturn(prix);

        mockMvc.perform(post("/api/plans-semaine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(uneRequeteValide()))
                .andExpect(status().isOk());

        verify(historiserPlanUseCase).historiser(plan, prix);
    }

    @Test
    void renvoie_404_si_un_identifiant_de_repas_est_introuvable() throws Exception {
        when(composerPlanSemaineUseCase.composer(any(), any()))
                .thenThrow(new RepasIntrouvableException(RepasId.nouveau()));

        mockMvc.perform(post("/api/plans-semaine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(uneRequeteValide()))
                .andExpect(status().isNotFound());
    }

    @Test
    void renvoie_200_avec_un_diner_genere_transmis_en_entier() throws Exception {
        when(composerPlanSemaineUseCase.composer(any(), any())).thenReturn(unPlanValide());
        when(estimerCoutUseCase.estimer(any(), any())).thenReturn(new Prix(BigDecimal.valueOf(42.50)));

        mockMvc.perform(post("/api/plans-semaine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(uneRequeteAvecDinerGenere()))
                .andExpect(status().isOk());
    }

    @Test
    void renvoie_400_si_un_choix_de_repas_ne_fournit_ni_id_ni_genere() throws Exception {
        mockMvc.perform(post("/api/plans-semaine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(uneRequeteAvecChoixAmbigu()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void renvoie_422_si_un_repas_est_incompatible_avec_le_profil() throws Exception {
        Repas repasIncompatible = unRepas(TypeRepas.DEJEUNER, StyleAlimentaire.GOURMAND);
        when(composerPlanSemaineUseCase.composer(any(), any()))
                .thenThrow(new RepasIncompatibleException(repasIncompatible, PROFIL));

        mockMvc.perform(post("/api/plans-semaine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(uneRequeteValide()))
                .andExpect(status().isUnprocessableEntity());
    }

    /**
     * Le detail du contenu de la requete n'importe pas pour ces tests
     * (le use case est mocke) : seule sa validite structurelle compte, pour
     * passer la validation Bean Validation du controleur.
     */
    private static String uneRequeteValide() {
        UUID unId = UUID.randomUUID();
        String choixRepas = "{\"id\":\"" + unId + "\"}";
        String choixJour = "{\"petitDejeuner\":" + choixRepas + ",\"dejeuner\":" + choixRepas + ",\"diner\":" + choixRepas + "}";
        return "{\"profil\":{\"enseigne\":\"CARREFOUR\",\"style\":\"NORMAL\",\"niveau\":\"DEBUTANT\",\"nombreDePersonnes\":2},"
                + "\"choix\":{"
                + "\"LUNDI\":" + choixJour + ","
                + "\"MARDI\":" + choixJour + ","
                + "\"MERCREDI\":" + choixJour + ","
                + "\"JEUDI\":" + choixJour + ","
                + "\"VENDREDI\":" + choixJour
                + "}}";
    }

    private static String uneRequeteAvecDinerGenere() {
        UUID unId = UUID.randomUUID();
        String choixParId = "{\"id\":\"" + unId + "\"}";
        String dinerGenere = "{\"genere\":{\"nom\":\"Idee generee\",\"type\":\"DINER\",\"style\":\"NORMAL\","
                + "\"niveauRequis\":\"DEBUTANT\",\"ingredients\":[{\"ingredient\":\"riz\",\"rayon\":\"EPICERIE\","
                + "\"quantite\":150,\"unite\":\"GRAMME\"}]}}";
        String choixJour = "{\"petitDejeuner\":" + choixParId + ",\"dejeuner\":" + choixParId + ",\"diner\":" + dinerGenere + "}";
        return "{\"profil\":{\"enseigne\":\"CARREFOUR\",\"style\":\"NORMAL\",\"niveau\":\"DEBUTANT\",\"nombreDePersonnes\":2},"
                + "\"choix\":{"
                + "\"LUNDI\":" + choixJour + ","
                + "\"MARDI\":" + choixJour + ","
                + "\"MERCREDI\":" + choixJour + ","
                + "\"JEUDI\":" + choixJour + ","
                + "\"VENDREDI\":" + choixJour
                + "}}";
    }

    private static String uneRequeteAvecChoixAmbigu() {
        UUID unId = UUID.randomUUID();
        // ni id ni genere fournis pour le petit-dejeuner : requete malformee.
        String choixVide = "{}";
        String choixParId = "{\"id\":\"" + unId + "\"}";
        String choixJour = "{\"petitDejeuner\":" + choixVide + ",\"dejeuner\":" + choixParId + ",\"diner\":" + choixParId + "}";
        return "{\"profil\":{\"enseigne\":\"CARREFOUR\",\"style\":\"NORMAL\",\"niveau\":\"DEBUTANT\",\"nombreDePersonnes\":2},"
                + "\"choix\":{"
                + "\"LUNDI\":" + choixJour + ","
                + "\"MARDI\":" + choixJour + ","
                + "\"MERCREDI\":" + choixJour + ","
                + "\"JEUDI\":" + choixJour + ","
                + "\"VENDREDI\":" + choixJour
                + "}}";
    }

    private static PlanSemaine unPlanValide() {
        List<Jour> jours = List.of(JourSemaine.values()).stream()
                .map(jourSemaine -> new Jour(jourSemaine,
                        unRepas(TypeRepas.PETIT_DEJEUNER, StyleAlimentaire.NORMAL),
                        unRepas(TypeRepas.DEJEUNER, StyleAlimentaire.NORMAL),
                        unRepas(TypeRepas.DINER, StyleAlimentaire.NORMAL)))
                .toList();
        return new PlanSemaine(PROFIL, jours);
    }

    private static Repas unRepas(TypeRepas type, StyleAlimentaire style) {
        IngredientQuantite ingredient = new IngredientQuantite(
                new Ingredient("ingredient de test", RayonMagasin.EPICERIE),
                new Quantite(BigDecimal.ONE, UniteMesure.UNITE));
        return new Repas(RepasId.nouveau(), "repas de test", type, style, NiveauCuisine.DEBUTANT, List.of(ingredient));
    }
}
