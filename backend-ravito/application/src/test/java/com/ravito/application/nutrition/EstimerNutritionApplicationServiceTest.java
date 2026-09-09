package com.ravito.application.nutrition;

import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.RepasId;
import com.ravito.domain.catalogue.TypeRepas;
import com.ravito.domain.courses.Ingredient;
import com.ravito.domain.courses.IngredientQuantite;
import com.ravito.domain.courses.Quantite;
import com.ravito.domain.courses.RayonMagasin;
import com.ravito.domain.courses.UniteMesure;
import com.ravito.domain.nutrition.EstimationNutritionPort;
import com.ravito.domain.nutrition.ValeursNutritionnelles;
import com.ravito.domain.profil.NiveauCuisine;
import com.ravito.domain.profil.StyleAlimentaire;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstimerNutritionApplicationServiceTest {

    @Mock
    private EstimationNutritionPort estimationNutritionPort;

    private EstimerNutritionApplicationService service;

    @BeforeEach
    void setUp() {
        service = new EstimerNutritionApplicationService(estimationNutritionPort);
    }

    @Test
    void delegue_l_estimation_au_port_de_sortie_avec_les_ingredients_du_repas() {
        Repas repas = unRepas();
        ValeursNutritionnelles attendues = new ValeursNutritionnelles(
                BigDecimal.valueOf(250), BigDecimal.valueOf(12), BigDecimal.valueOf(30), BigDecimal.valueOf(8), BigDecimal.valueOf(4));
        when(estimationNutritionPort.estimer(repas.ingredients())).thenReturn(attendues);

        ValeursNutritionnelles valeurs = service.estimer(repas);

        assertThat(valeurs).isEqualTo(attendues);
        verify(estimationNutritionPort).estimer(repas.ingredients());
    }

    @Test
    void refuse_un_repas_nul() {
        assertThatNullPointerException().isThrownBy(() -> service.estimer(null));
    }

    private static Repas unRepas() {
        IngredientQuantite ingredient = new IngredientQuantite(
                new Ingredient("ingredient de test", RayonMagasin.EPICERIE),
                new Quantite(BigDecimal.ONE, UniteMesure.UNITE));
        return new Repas(RepasId.nouveau(), "repas de test", TypeRepas.DEJEUNER, StyleAlimentaire.NORMAL,
                NiveauCuisine.DEBUTANT, List.of(ingredient), List.of("Etape de test"));
    }
}
