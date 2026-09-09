package com.ravito.application.catalogue;

import com.ravito.domain.catalogue.CatalogueRepasPort;
import com.ravito.domain.catalogue.GenerationRepasImpossibleException;
import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.RepasId;
import com.ravito.domain.catalogue.TypeRepas;
import com.ravito.domain.courses.Ingredient;
import com.ravito.domain.courses.IngredientQuantite;
import com.ravito.domain.courses.Quantite;
import com.ravito.domain.courses.RayonMagasin;
import com.ravito.domain.courses.UniteMesure;
import com.ravito.domain.profil.Enseigne;
import com.ravito.domain.profil.NiveauCuisine;
import com.ravito.domain.profil.NombreDePersonnes;
import com.ravito.domain.profil.ProfilUtilisateur;
import com.ravito.domain.profil.StyleAlimentaire;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenererRepasApplicationServiceTest {

    private static final ProfilUtilisateur PROFIL =
            new ProfilUtilisateur(Enseigne.CARREFOUR, StyleAlimentaire.NORMAL, NiveauCuisine.CONFIRME, NombreDePersonnes.une());

    @Mock
    private CatalogueRepasPort catalogueRepasPort;

    private GenererRepasApplicationService service;

    @BeforeEach
    void setUp() {
        // graine fixe : tirage et gabarit de nom deterministes pour les assertions.
        // PoolIngredientsCatalogue est une classe reelle (pas mockee) : simple
        // et deterministe, mieux vaut l'exercer vraiment que la doubler.
        service = new GenererRepasApplicationService(new PoolIngredientsCatalogue(catalogueRepasPort), new Random(42));
    }

    @Test
    void compose_un_repas_a_partir_des_ingredients_du_catalogue_existant() {
        when(catalogueRepasPort.rechercherParTypeEtStyle(TypeRepas.DINER, StyleAlimentaire.NORMAL))
                .thenReturn(List.of(
                        unRepas("riz", UniteMesure.GRAMME),
                        unRepas("poulet", UniteMesure.GRAMME),
                        unRepas("tomate", UniteMesure.GRAMME)));

        Repas genere = service.genererRepas(PROFIL, TypeRepas.DINER);

        assertThat(genere.type()).isEqualTo(TypeRepas.DINER);
        assertThat(genere.style()).isEqualTo(StyleAlimentaire.NORMAL);
        assertThat(genere.ingredients()).hasSize(2);
        // les ingredients choisis viennent forcement du pool fourni par le catalogue :
        assertThat(genere.ingredients())
                .extracting(i -> i.ingredient().nom())
                .allMatch(nom -> List.of("riz", "poulet", "tomate").contains(nom));
        // les etapes generees citent les deux ingredients tires, quel qu'en soit l'ordre :
        assertThat(genere.etapesPreparation()).isNotEmpty();
        assertThat(genere.etapesPreparation().get(0))
                .contains(genere.ingredients().get(0).ingredient().nom())
                .contains(genere.ingredients().get(1).ingredient().nom());
    }

    @Test
    void le_repas_genere_est_toujours_compatible_avec_le_profil_demandeur() {
        when(catalogueRepasPort.rechercherParTypeEtStyle(TypeRepas.DEJEUNER, StyleAlimentaire.NORMAL))
                .thenReturn(List.of(unRepas("riz", UniteMesure.GRAMME), unRepas("poulet", UniteMesure.GRAMME)));

        Repas genere = service.genererRepas(PROFIL, TypeRepas.DEJEUNER);

        assertThat(genere.niveauRequis()).isEqualTo(PROFIL.niveau());
        assertThat(genere.estCompatibleAvec(PROFIL)).isTrue();
    }

    @Test
    void ne_choisit_jamais_deux_fois_le_meme_couple_ingredient_unite() {
        // "riz" apparait deux fois dans deux repas differents : le pool ne
        // doit le compter qu'une fois, donc avec seulement 2 ingredients
        // distincts au total le tirage de 2 doit reussir sans doublon.
        when(catalogueRepasPort.rechercherParTypeEtStyle(TypeRepas.DEJEUNER, StyleAlimentaire.NORMAL))
                .thenReturn(List.of(unRepas("riz", UniteMesure.GRAMME), unRepas("riz", UniteMesure.GRAMME),
                        unRepas("poulet", UniteMesure.GRAMME)));

        Repas genere = service.genererRepas(PROFIL, TypeRepas.DEJEUNER);

        assertThat(genere.ingredients()).extracting(i -> i.ingredient().nom())
                .containsExactlyInAnyOrder("riz", "poulet");
    }

    @Test
    void leve_generation_impossible_si_le_pool_a_moins_de_deux_ingredients() {
        when(catalogueRepasPort.rechercherParTypeEtStyle(TypeRepas.DEJEUNER, StyleAlimentaire.NORMAL))
                .thenReturn(List.of(unRepas("riz", UniteMesure.GRAMME)));

        assertThatExceptionOfType(GenerationRepasImpossibleException.class)
                .isThrownBy(() -> service.genererRepas(PROFIL, TypeRepas.DEJEUNER));
    }

    private static Repas unRepas(String nomIngredient, UniteMesure unite) {
        IngredientQuantite ingredient = new IngredientQuantite(
                new Ingredient(nomIngredient, RayonMagasin.EPICERIE),
                new Quantite(BigDecimal.TEN, unite));
        return new Repas(RepasId.nouveau(), "repas de test", TypeRepas.DEJEUNER, StyleAlimentaire.NORMAL,
                NiveauCuisine.DEBUTANT, List.of(ingredient), List.of("Etape de test"));
    }
}
