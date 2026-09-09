package com.ravito.infrastructure.web.catalogue;

import com.ravito.domain.catalogue.GenerationRepasImpossibleException;
import com.ravito.domain.catalogue.GenererRepasUseCase;
import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.RepasId;
import com.ravito.domain.catalogue.TypeRepas;
import com.ravito.domain.courses.Ingredient;
import com.ravito.domain.courses.IngredientQuantite;
import com.ravito.domain.courses.Quantite;
import com.ravito.domain.courses.RayonMagasin;
import com.ravito.domain.courses.UniteMesure;
import com.ravito.domain.nutrition.EstimerNutritionUseCase;
import com.ravito.domain.nutrition.ValeursNutritionnelles;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenererRepasController.class)
class GenererRepasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenererRepasUseCase genererRepasUseCase;

    @MockBean
    private EstimerNutritionUseCase estimerNutritionUseCase;

    @Test
    void renvoie_200_avec_le_repas_genere() throws Exception {
        IngredientQuantite ingredient = new IngredientQuantite(
                new Ingredient("riz", RayonMagasin.EPICERIE), new Quantite(BigDecimal.TEN, UniteMesure.GRAMME));
        Repas genere = new Repas(RepasId.nouveau(), "Riz et poulet maison", TypeRepas.DINER,
                StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT, List.of(ingredient), List.of("Etape de test"));
        when(genererRepasUseCase.genererRepas(any(), any())).thenReturn(genere);
        when(estimerNutritionUseCase.estimer(any())).thenReturn(new ValeursNutritionnelles(
                BigDecimal.valueOf(250), BigDecimal.valueOf(12), BigDecimal.valueOf(30), BigDecimal.valueOf(8), BigDecimal.valueOf(4)));

        mockMvc.perform(post("/api/repas/generation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"profil":{"enseigne":"CARREFOUR","style":"NORMAL","niveau":"DEBUTANT","nombreDePersonnes":2},"type":"DINER"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Riz et poulet maison"))
                .andExpect(jsonPath("$.ingredients", org.hamcrest.Matchers.hasSize(1)))
                .andExpect(jsonPath("$.etapesPreparation", org.hamcrest.Matchers.hasSize(1)))
                .andExpect(jsonPath("$.valeursNutritionnelles.calories").value(250));
    }

    @Test
    void renvoie_400_si_le_type_est_manquant() throws Exception {
        mockMvc.perform(post("/api/repas/generation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"profil":{"enseigne":"CARREFOUR","style":"NORMAL","niveau":"DEBUTANT","nombreDePersonnes":2}}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void renvoie_409_si_la_generation_est_impossible() throws Exception {
        when(genererRepasUseCase.genererRepas(any(), any()))
                .thenThrow(new GenerationRepasImpossibleException(TypeRepas.DINER, StyleAlimentaire.NORMAL));

        mockMvc.perform(post("/api/repas/generation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"profil":{"enseigne":"CARREFOUR","style":"NORMAL","niveau":"DEBUTANT","nombreDePersonnes":2},"type":"DINER"}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists());
    }
}
