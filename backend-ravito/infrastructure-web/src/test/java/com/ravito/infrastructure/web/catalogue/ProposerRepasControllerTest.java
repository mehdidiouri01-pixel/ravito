package com.ravito.infrastructure.web.catalogue;

import com.ravito.domain.catalogue.CatalogueInsuffisantException;
import com.ravito.domain.catalogue.ProposerRepasUseCase;
import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.RepasId;
import com.ravito.domain.catalogue.RepasProposes;
import com.ravito.domain.catalogue.TypeRepas;
import com.ravito.domain.courses.Ingredient;
import com.ravito.domain.courses.IngredientQuantite;
import com.ravito.domain.courses.Quantite;
import com.ravito.domain.courses.RayonMagasin;
import com.ravito.domain.courses.UniteMesure;
import com.ravito.domain.profil.NiveauCuisine;
import com.ravito.domain.profil.StyleAlimentaire;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProposerRepasController.class)
class ProposerRepasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProposerRepasUseCase proposerRepasUseCase;

    @Test
    void renvoie_200_avec_la_proposition_de_repas() throws Exception {
        RepasProposes proposition = new RepasProposes(
                cinqRepas(TypeRepas.PETIT_DEJEUNER), cinqRepas(TypeRepas.DEJEUNER));
        when(proposerRepasUseCase.proposer(any())).thenReturn(proposition);

        mockMvc.perform(post("/api/repas/propositions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"enseigne\":\"CARREFOUR\",\"style\":\"NORMAL\",\"niveau\":\"DEBUTANT\",\"nombreDePersonnes\":2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.petitsDejeuners", hasSize(5)))
                .andExpect(jsonPath("$.dejeuners", hasSize(5)));
    }

    @Test
    void renvoie_400_si_un_champ_du_profil_est_manquant() throws Exception {
        mockMvc.perform(post("/api/repas/propositions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"enseigne\":\"CARREFOUR\",\"niveau\":\"DEBUTANT\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void renvoie_400_si_le_nombre_de_personnes_est_hors_bornes() throws Exception {
        mockMvc.perform(post("/api/repas/propositions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"enseigne\":\"CARREFOUR\",\"style\":\"NORMAL\",\"niveau\":\"DEBUTANT\",\"nombreDePersonnes\":0}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/repas/propositions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"enseigne\":\"CARREFOUR\",\"style\":\"NORMAL\",\"niveau\":\"DEBUTANT\",\"nombreDePersonnes\":99}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void renvoie_409_si_le_catalogue_est_insuffisant() throws Exception {
        when(proposerRepasUseCase.proposer(any()))
                .thenThrow(new CatalogueInsuffisantException(TypeRepas.DEJEUNER, 2, 5));

        mockMvc.perform(post("/api/repas/propositions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"enseigne\":\"CARREFOUR\",\"style\":\"NORMAL\",\"niveau\":\"DEBUTANT\",\"nombreDePersonnes\":2}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists());
    }

    private static List<Repas> cinqRepas(TypeRepas type) {
        return IntStream.range(0, 5).mapToObj(i -> unRepas(type)).toList();
    }

    private static Repas unRepas(TypeRepas type) {
        IngredientQuantite ingredient = new IngredientQuantite(
                new Ingredient("ingredient de test", RayonMagasin.EPICERIE),
                new Quantite(BigDecimal.ONE, UniteMesure.UNITE));
        return new Repas(RepasId.nouveau(), "repas de test", type, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT,
                List.of(ingredient));
    }
}
