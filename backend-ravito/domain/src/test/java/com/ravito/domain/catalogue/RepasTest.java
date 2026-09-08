package com.ravito.domain.catalogue;

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
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.ravito.domain.catalogue.RepasTestFactory.unRepas;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class RepasTest {

    @Test
    void refuse_un_nom_vide() {
        assertThatIllegalArgumentException().isThrownBy(() ->
                unRepas("  ", TypeRepas.DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT));
    }

    @Test
    void refuse_un_repas_sans_ingredient() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Repas(
                RepasId.nouveau(), "sans ingredient", TypeRepas.DEJEUNER,
                StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT, List.of()));
    }

    @Test
    void la_liste_d_ingredients_est_immuable() {
        Ingredient riz = new Ingredient("riz", RayonMagasin.EPICERIE);
        Quantite quantite = new Quantite(BigDecimal.TEN, UniteMesure.GRAMME);
        Repas repas = new Repas(RepasId.nouveau(), "riz nature", TypeRepas.DEJEUNER,
                StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT,
                List.of(new IngredientQuantite(riz, quantite)));

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> repas.ingredients().add(new IngredientQuantite(riz, quantite)));
    }

    @Test
    void est_compatible_avec_un_profil_de_meme_style_et_meme_niveau() {
        Repas repas = unRepas(TypeRepas.DEJEUNER, StyleAlimentaire.HEALTHY, NiveauCuisine.CONFIRME);
        ProfilUtilisateur profil = new ProfilUtilisateur(Enseigne.LIDL, StyleAlimentaire.HEALTHY, NiveauCuisine.CONFIRME, NombreDePersonnes.une());

        assertThat(repas.estCompatibleAvec(profil)).isTrue();
    }

    @Test
    void un_repas_debutant_est_compatible_avec_un_profil_confirme() {
        Repas repas = unRepas(TypeRepas.DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT);
        ProfilUtilisateur profil = new ProfilUtilisateur(Enseigne.AUCHAN, StyleAlimentaire.NORMAL, NiveauCuisine.CONFIRME, NombreDePersonnes.une());

        assertThat(repas.estCompatibleAvec(profil)).isTrue();
    }

    @Test
    void un_repas_confirme_n_est_pas_compatible_avec_un_profil_debutant() {
        Repas repas = unRepas(TypeRepas.DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.CONFIRME);
        ProfilUtilisateur profil = new ProfilUtilisateur(Enseigne.AUCHAN, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT, NombreDePersonnes.une());

        assertThat(repas.estCompatibleAvec(profil)).isFalse();
    }

    @Test
    void un_style_different_n_est_jamais_compatible_meme_avec_le_bon_niveau() {
        Repas repas = unRepas(TypeRepas.DEJEUNER, StyleAlimentaire.GOURMAND, NiveauCuisine.DEBUTANT);
        ProfilUtilisateur profil = new ProfilUtilisateur(Enseigne.CARREFOUR, StyleAlimentaire.HEALTHY, NiveauCuisine.CONFIRME, NombreDePersonnes.une());

        assertThat(repas.estCompatibleAvec(profil)).isFalse();
    }
}
