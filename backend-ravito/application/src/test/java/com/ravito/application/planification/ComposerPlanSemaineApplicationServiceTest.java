package com.ravito.application.planification;

import com.ravito.application.catalogue.PoolIngredientsCatalogue;
import com.ravito.domain.catalogue.CatalogueRepasPort;
import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.RepasId;
import com.ravito.domain.catalogue.RepasIntrouvableException;
import com.ravito.domain.catalogue.TypeRepas;
import com.ravito.domain.courses.Ingredient;
import com.ravito.domain.courses.IngredientQuantite;
import com.ravito.domain.courses.Quantite;
import com.ravito.domain.courses.RayonMagasin;
import com.ravito.domain.courses.UniteMesure;
import com.ravito.domain.planification.ChoixJour;
import com.ravito.domain.planification.ChoixRepas;
import com.ravito.domain.planification.JourSemaine;
import com.ravito.domain.planification.PlanSemaine;
import com.ravito.domain.planification.RepasGenereInvalideException;
import com.ravito.domain.planification.RepasIncompatibleException;
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
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComposerPlanSemaineApplicationServiceTest {

    private static final ProfilUtilisateur PROFIL =
            new ProfilUtilisateur(Enseigne.CARREFOUR, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT, NombreDePersonnes.une());

    @Mock
    private CatalogueRepasPort catalogueRepasPort;

    private ComposerPlanSemaineApplicationService service;

    @BeforeEach
    void setUp() {
        // PoolIngredientsCatalogue est une classe reelle (pas mockee) : simple
        // et deterministe, mieux vaut l'exercer vraiment que la doubler.
        service = new ComposerPlanSemaineApplicationService(catalogueRepasPort, new PoolIngredientsCatalogue(catalogueRepasPort));
    }

    @Test
    void compose_un_plan_valide_en_resolvant_chaque_choix_par_id_via_le_catalogue() {
        Map<JourSemaine, ChoixJour> choix = new EnumMap<>(JourSemaine.class);
        for (JourSemaine jour : JourSemaine.values()) {
            Repas petitDejeuner = unRepas(TypeRepas.PETIT_DEJEUNER);
            Repas dejeuner = unRepas(TypeRepas.DEJEUNER);
            Repas diner = unRepas(TypeRepas.DINER);
            stubParId(petitDejeuner);
            stubParId(dejeuner);
            stubParId(diner);
            choix.put(jour, new ChoixJour(parId(petitDejeuner), parId(dejeuner), parId(diner)));
        }

        PlanSemaine plan = service.composer(PROFIL, choix);

        assertThat(plan.jours()).hasSize(5);
        assertThat(plan.profil()).isEqualTo(PROFIL);
    }

    @Test
    void leve_repas_introuvable_si_un_identifiant_ne_correspond_a_rien() {
        RepasId idInconnu = RepasId.nouveau();
        Repas dejeuner = unRepas(TypeRepas.DEJEUNER);
        Repas diner = unRepas(TypeRepas.DINER);
        stubParId(dejeuner);
        stubParId(diner);
        when(catalogueRepasPort.parId(idInconnu)).thenReturn(Optional.empty());

        Map<JourSemaine, ChoixJour> choix =
                planComplet(new ChoixRepas.ParId(idInconnu), parId(dejeuner), parId(diner));

        assertThatExceptionOfType(RepasIntrouvableException.class)
                .isThrownBy(() -> service.composer(PROFIL, choix));
    }

    @Test
    void laisse_planSemaine_refuser_un_repas_incompatible_avec_le_profil() {
        // repas resolu avec succes par le catalogue, mais d'un style different du profil :
        // le service ne revalide rien lui-meme, c'est PlanSemaine qui doit refuser.
        Repas petitDejeunerIncompatible = new Repas(RepasId.nouveau(), "petit-dejeuner gourmand",
                TypeRepas.PETIT_DEJEUNER, StyleAlimentaire.GOURMAND, NiveauCuisine.DEBUTANT, uneListeDIngredients());
        Repas dejeuner = unRepas(TypeRepas.DEJEUNER);
        Repas diner = unRepas(TypeRepas.DINER);
        stubParId(petitDejeunerIncompatible);
        stubParId(dejeuner);
        stubParId(diner);

        Map<JourSemaine, ChoixJour> choix =
                planComplet(parId(petitDejeunerIncompatible), parId(dejeuner), parId(diner));

        assertThatExceptionOfType(RepasIncompatibleException.class)
                .isThrownBy(() -> service.composer(PROFIL, choix));
    }

    @Test
    void accepte_un_repas_genere_dont_les_ingredients_sont_connus_du_catalogue() {
        Repas petitDejeuner = unRepas(TypeRepas.PETIT_DEJEUNER);
        Repas dejeuner = unRepas(TypeRepas.DEJEUNER);
        // le repas genere partage le meme couple ingredient/unite que celui deja catalogue pour DINER/NORMAL :
        when(catalogueRepasPort.rechercherParTypeEtStyle(TypeRepas.DINER, StyleAlimentaire.NORMAL))
                .thenReturn(List.of(unRepas(TypeRepas.DINER)));
        Repas dinerGenere = new Repas(RepasId.nouveau(), "Idee generee", TypeRepas.DINER, StyleAlimentaire.NORMAL,
                NiveauCuisine.DEBUTANT, uneListeDIngredients());
        stubParId(petitDejeuner);
        stubParId(dejeuner);

        Map<JourSemaine, ChoixJour> choix =
                planComplet(parId(petitDejeuner), parId(dejeuner), new ChoixRepas.Genere(dinerGenere));

        PlanSemaine plan = service.composer(PROFIL, choix);

        assertThat(plan.jours()).extracting(jour -> jour.diner().nom()).allMatch(nom -> nom.equals("Idee generee"));
    }

    @Test
    void refuse_un_repas_genere_dont_un_ingredient_n_appartient_pas_au_catalogue_connu() {
        Repas petitDejeuner = unRepas(TypeRepas.PETIT_DEJEUNER);
        Repas dejeuner = unRepas(TypeRepas.DEJEUNER);
        // catalogue DINER/NORMAL ne connait que "ingredient de test" — pas "caviar" :
        when(catalogueRepasPort.rechercherParTypeEtStyle(TypeRepas.DINER, StyleAlimentaire.NORMAL))
                .thenReturn(List.of(unRepas(TypeRepas.DINER)));
        Repas dinerGenereTruque = new Repas(RepasId.nouveau(), "Idee suspecte", TypeRepas.DINER,
                StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT, List.of(new IngredientQuantite(
                        new Ingredient("caviar", RayonMagasin.EPICERIE), new Quantite(BigDecimal.ONE, UniteMesure.UNITE))));
        stubParId(petitDejeuner);
        stubParId(dejeuner);

        Map<JourSemaine, ChoixJour> choix =
                planComplet(parId(petitDejeuner), parId(dejeuner), new ChoixRepas.Genere(dinerGenereTruque));

        assertThatExceptionOfType(RepasGenereInvalideException.class)
                .isThrownBy(() -> service.composer(PROFIL, choix));
    }

    /**
     * Construit un choix complet sur les 5 jours, en reutilisant les memes
     * choix de petit-dejeuner/dejeuner/diner sur chaque jour — suffisant
     * pour les tests qui ne portent que sur un seul jour particulier.
     */
    private Map<JourSemaine, ChoixJour> planComplet(ChoixRepas petitDejeuner, ChoixRepas dejeuner, ChoixRepas diner) {
        Map<JourSemaine, ChoixJour> choix = new EnumMap<>(JourSemaine.class);
        for (JourSemaine jour : JourSemaine.values()) {
            choix.put(jour, new ChoixJour(petitDejeuner, dejeuner, diner));
        }
        return choix;
    }

    private static ChoixRepas.ParId parId(Repas repas) {
        return new ChoixRepas.ParId(repas.id());
    }

    private void stubParId(Repas repas) {
        lenient().when(catalogueRepasPort.parId(repas.id())).thenReturn(Optional.of(repas));
    }

    private static Repas unRepas(TypeRepas type) {
        return new Repas(RepasId.nouveau(), "repas de test", type, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT,
                uneListeDIngredients());
    }

    private static List<IngredientQuantite> uneListeDIngredients() {
        return List.of(new IngredientQuantite(
                new Ingredient("ingredient de test", RayonMagasin.EPICERIE),
                new Quantite(BigDecimal.ONE, UniteMesure.UNITE)));
    }
}
