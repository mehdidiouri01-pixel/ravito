package com.ravito.application.planification;

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
import com.ravito.domain.planification.JourSemaine;
import com.ravito.domain.planification.PlanSemaine;
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
        service = new ComposerPlanSemaineApplicationService(catalogueRepasPort);
    }

    @Test
    void compose_un_plan_valide_en_resolvant_chaque_choix_via_le_catalogue() {
        Map<JourSemaine, ChoixJour> choix = new EnumMap<>(JourSemaine.class);
        for (JourSemaine jour : JourSemaine.values()) {
            Repas petitDejeuner = unRepas(TypeRepas.PETIT_DEJEUNER);
            Repas dejeuner = unRepas(TypeRepas.DEJEUNER);
            Repas diner = unRepas(TypeRepas.DINER);
            stubParId(petitDejeuner);
            stubParId(dejeuner);
            stubParId(diner);
            choix.put(jour, new ChoixJour(petitDejeuner.id(), dejeuner.id(), diner.id()));
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

        Map<JourSemaine, ChoixJour> choix = planComplet(idInconnu, dejeuner.id(), diner.id());

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

        Map<JourSemaine, ChoixJour> choix = planComplet(petitDejeunerIncompatible.id(), dejeuner.id(), diner.id());

        assertThatExceptionOfType(RepasIncompatibleException.class)
                .isThrownBy(() -> service.composer(PROFIL, choix));
    }

    /**
     * Construit un choix complet sur les 5 jours, en reutilisant les memes
     * identifiants de petit-dejeuner/dejeuner/diner sur chaque jour —
     * suffisant pour les tests qui ne portent que sur un seul jour
     * particulier.
     */
    private Map<JourSemaine, ChoixJour> planComplet(RepasId petitDejeunerId, RepasId dejeunerId, RepasId dinerId) {
        Map<JourSemaine, ChoixJour> choix = new EnumMap<>(JourSemaine.class);
        for (JourSemaine jour : JourSemaine.values()) {
            choix.put(jour, new ChoixJour(petitDejeunerId, dejeunerId, dinerId));
        }
        return choix;
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
