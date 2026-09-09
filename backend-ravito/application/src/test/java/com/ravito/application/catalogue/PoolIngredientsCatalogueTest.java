package com.ravito.application.catalogue;

import com.ravito.domain.catalogue.CatalogueRepasPort;
import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.RepasId;
import com.ravito.domain.catalogue.TypeRepas;
import com.ravito.domain.courses.Ingredient;
import com.ravito.domain.courses.IngredientQuantite;
import com.ravito.domain.courses.Quantite;
import com.ravito.domain.courses.RayonMagasin;
import com.ravito.domain.courses.UniteMesure;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PoolIngredientsCatalogueTest {

    @Mock
    private CatalogueRepasPort catalogueRepasPort;

    private PoolIngredientsCatalogue pool;

    @BeforeEach
    void setUp() {
        pool = new PoolIngredientsCatalogue(catalogueRepasPort);
    }

    @Test
    void deduplique_un_meme_couple_ingredient_unite_apparu_dans_plusieurs_repas() {
        when(catalogueRepasPort.rechercherParTypeEtStyle(TypeRepas.DINER, StyleAlimentaire.NORMAL))
                .thenReturn(List.of(
                        unRepas("riz", UniteMesure.GRAMME),
                        unRepas("riz", UniteMesure.GRAMME),
                        unRepas("poulet", UniteMesure.GRAMME)));

        List<IngredientQuantite> ingredients = pool.ingredients(TypeRepas.DINER, StyleAlimentaire.NORMAL);

        assertThat(ingredients).extracting(i -> i.ingredient().nom()).containsExactlyInAnyOrder("riz", "poulet");
    }

    @Test
    void connusPourTypeEtStyle_est_vrai_si_tous_les_ingredients_appartiennent_au_catalogue() {
        when(catalogueRepasPort.rechercherParTypeEtStyle(TypeRepas.DINER, StyleAlimentaire.NORMAL))
                .thenReturn(List.of(unRepas("riz", UniteMesure.GRAMME), unRepas("poulet", UniteMesure.GRAMME)));

        boolean connus = pool.connusPourTypeEtStyle(TypeRepas.DINER, StyleAlimentaire.NORMAL,
                List.of(ingredient("riz", UniteMesure.GRAMME)));

        assertThat(connus).isTrue();
    }

    @Test
    void connusPourTypeEtStyle_est_faux_si_un_ingredient_n_appartient_pas_au_catalogue() {
        when(catalogueRepasPort.rechercherParTypeEtStyle(TypeRepas.DINER, StyleAlimentaire.NORMAL))
                .thenReturn(List.of(unRepas("riz", UniteMesure.GRAMME)));

        boolean connus = pool.connusPourTypeEtStyle(TypeRepas.DINER, StyleAlimentaire.NORMAL,
                List.of(ingredient("caviar", UniteMesure.GRAMME)));

        assertThat(connus).isFalse();
    }

    @Test
    void connusPourTypeEtStyle_est_faux_si_l_unite_ne_correspond_pas_meme_avec_le_meme_nom() {
        when(catalogueRepasPort.rechercherParTypeEtStyle(TypeRepas.DINER, StyleAlimentaire.NORMAL))
                .thenReturn(List.of(unRepas("riz", UniteMesure.GRAMME)));

        boolean connus = pool.connusPourTypeEtStyle(TypeRepas.DINER, StyleAlimentaire.NORMAL,
                List.of(ingredient("riz", UniteMesure.KILOGRAMME)));

        assertThat(connus).isFalse();
    }

    private static Repas unRepas(String nomIngredient, UniteMesure unite) {
        return new Repas(RepasId.nouveau(), "repas de test", TypeRepas.DINER, StyleAlimentaire.NORMAL,
                NiveauCuisine.DEBUTANT, List.of(ingredient(nomIngredient, unite)), List.of("Etape de test"));
    }

    private static IngredientQuantite ingredient(String nom, UniteMesure unite) {
        return new IngredientQuantite(new Ingredient(nom, RayonMagasin.EPICERIE), new Quantite(BigDecimal.TEN, unite));
    }
}
