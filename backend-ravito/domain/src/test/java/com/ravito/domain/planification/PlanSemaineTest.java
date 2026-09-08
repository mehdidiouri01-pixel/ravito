package com.ravito.domain.planification;

import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.RepasTestFactory;
import com.ravito.domain.catalogue.TypeRepas;
import com.ravito.domain.courses.ListeCourses;
import com.ravito.domain.profil.Enseigne;
import com.ravito.domain.profil.NiveauCuisine;
import com.ravito.domain.profil.NombreDePersonnes;
import com.ravito.domain.profil.ProfilUtilisateur;
import com.ravito.domain.profil.StyleAlimentaire;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PlanSemaineTest {

    private static final ProfilUtilisateur PROFIL = profilPour(NombreDePersonnes.une());

    private static ProfilUtilisateur profilPour(NombreDePersonnes nombreDePersonnes) {
        return new ProfilUtilisateur(
                Enseigne.CARREFOUR, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT, nombreDePersonnes);
    }

    @Test
    void construit_un_plan_valide_avec_les_cinq_jours() {
        PlanSemaine plan = new PlanSemaine(PROFIL, cinqJoursValides());

        assertThat(plan.jours()).hasSize(5);
        assertThat(plan.tousLesRepas()).hasSize(10);
    }

    @Test
    void refuse_moins_de_cinq_jours() {
        List<Jour> quatreJours = cinqJoursValides().subList(0, 4);

        assertThatExceptionOfType(PlanSemaineInvalideException.class)
                .isThrownBy(() -> new PlanSemaine(PROFIL, quatreJours));
    }

    @Test
    void refuse_un_jour_renseigne_plusieurs_fois_meme_si_le_compte_est_bon() {
        List<Jour> jours = new ArrayList<>(cinqJoursValides().subList(0, 4)); // LUNDI..JEUDI
        jours.add(jourValide(JourSemaine.LUNDI)); // doublon a la place de VENDREDI

        assertThatExceptionOfType(PlanSemaineInvalideException.class)
                .isThrownBy(() -> new PlanSemaine(PROFIL, jours));
    }

    @Test
    void refuse_un_repas_incompatible_avec_le_profil() {
        List<Jour> jours = new ArrayList<>(cinqJoursValides());
        Repas dejeunerGourmand = RepasTestFactory.unRepas(TypeRepas.DEJEUNER, StyleAlimentaire.GOURMAND, NiveauCuisine.DEBUTANT);
        jours.set(0, new Jour(JourSemaine.LUNDI, jours.get(0).petitDejeuner(), dejeunerGourmand));

        assertThatExceptionOfType(RepasIncompatibleException.class)
                .isThrownBy(() -> new PlanSemaine(PROFIL, jours));
    }

    @Test
    void genere_la_liste_de_courses_en_consolidant_les_dix_repas() {
        // RepasTestFactory donne a chaque repas le meme ingredient (1 unite) :
        // les 10 repas du plan doivent se consolider en une seule ligne de 10.
        PlanSemaine plan = new PlanSemaine(PROFIL, cinqJoursValides());

        ListeCourses listeCourses = plan.genererListeCourses();

        assertThat(listeCourses.lignes()).hasSize(1);
        assertThat(listeCourses.lignes().get(0).quantiteTotale().valeur()).isEqualByComparingTo("10");
    }

    @Test
    void trie_toujours_les_jours_en_ordre_calendaire_quel_que_soit_l_ordre_fourni() {
        // Ordre delibrement quelconque en entree (ex : celui d'une HashMap
        // desserialisee depuis du JSON, qui ne suit pas le calendrier) :
        // le domaine doit le corriger lui-meme, pas en faire porter la
        // responsabilite a l'appelant.
        List<Jour> joursEnDesordre = List.of(
                jourValide(JourSemaine.JEUDI),
                jourValide(JourSemaine.LUNDI),
                jourValide(JourSemaine.VENDREDI),
                jourValide(JourSemaine.MERCREDI),
                jourValide(JourSemaine.MARDI));

        PlanSemaine plan = new PlanSemaine(PROFIL, joursEnDesordre);

        assertThat(plan.jours()).extracting(Jour::jourSemaine)
                .containsExactly(JourSemaine.LUNDI, JourSemaine.MARDI, JourSemaine.MERCREDI,
                        JourSemaine.JEUDI, JourSemaine.VENDREDI);
    }

    @Test
    void multiplie_les_quantites_par_le_nombre_de_personnes_du_foyer() {
        // RepasTestFactory donne a chaque repas 1 unite du meme ingredient :
        // 10 repas x 1 unite x 4 personnes = 40.
        PlanSemaine plan = new PlanSemaine(profilPour(new NombreDePersonnes(4)), cinqJoursValides());

        ListeCourses listeCourses = plan.genererListeCourses();

        assertThat(listeCourses.lignes()).hasSize(1);
        assertThat(listeCourses.lignes().get(0).quantiteTotale().valeur()).isEqualByComparingTo("40");
    }

    @Test
    void la_liste_de_jours_est_immuable() {
        PlanSemaine plan = new PlanSemaine(PROFIL, cinqJoursValides());

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> plan.jours().add(jourValide(JourSemaine.LUNDI)));
    }

    private static List<Jour> cinqJoursValides() {
        return List.of(JourSemaine.values()).stream()
                .map(PlanSemaineTest::jourValide)
                .toList();
    }

    private static Jour jourValide(JourSemaine jourSemaine) {
        Repas petitDejeuner = RepasTestFactory.unRepas(TypeRepas.PETIT_DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT);
        Repas dejeuner = RepasTestFactory.unRepas(TypeRepas.DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT);
        return new Jour(jourSemaine, petitDejeuner, dejeuner);
    }
}
