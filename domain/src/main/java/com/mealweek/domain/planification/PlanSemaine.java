package com.mealweek.domain.planification;

import com.mealweek.domain.catalogue.Repas;
import com.mealweek.domain.courses.ListeCourses;
import com.mealweek.domain.profil.ProfilUtilisateur;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Agregat racine : le plan de repas d'un utilisateur pour sa semaine de
 * travail (lundi a vendredi).
 *
 * <p>Invariants verifies a la construction, et donc impossibles a violer
 * une fois l'objet cree :
 * <ul>
 *     <li>exactement les 5 {@link JourSemaine}, chacun une seule fois ;</li>
 *     <li>chaque repas de chaque jour est compatible avec le profil
 *     (meme regle que {@link Repas#estCompatibleAvec}, revalidee ici en
 *     defense en profondeur — voir {@link RepasIncompatibleException}).</li>
 * </ul>
 */
public record PlanSemaine(ProfilUtilisateur profil, List<Jour> jours) {

    public PlanSemaine {
        Objects.requireNonNull(profil, "profil");
        Objects.requireNonNull(jours, "jours");
        jours = List.copyOf(jours);
        exigerExactementLesCinqJours(jours);
        for (Jour jour : jours) {
            exigerCompatible(jour.petitDejeuner(), profil);
            exigerCompatible(jour.dejeuner(), profil);
        }
    }

    private static void exigerExactementLesCinqJours(List<Jour> jours) {
        int attendu = JourSemaine.values().length;
        if (jours.size() != attendu) {
            throw new PlanSemaineInvalideException(
                    "Un plan de semaine doit contenir exactement " + attendu + " jours, " + jours.size() + " fournis");
        }
        EnumSet<JourSemaine> joursCouverts = EnumSet.noneOf(JourSemaine.class);
        for (Jour jour : jours) {
            if (!joursCouverts.add(jour.jourSemaine())) {
                throw new PlanSemaineInvalideException("Le jour " + jour.jourSemaine() + " est renseigne plusieurs fois");
            }
        }
    }

    private static void exigerCompatible(Repas repas, ProfilUtilisateur profil) {
        if (!repas.estCompatibleAvec(profil)) {
            throw new RepasIncompatibleException(repas, profil);
        }
    }

    /**
     * @return les 10 repas du plan (5 petits-dejeuners + 5 dejeuners), dans
     * l'ordre des jours. Base de la future generation de la liste de
     * courses (package {@code courses}).
     */
    public List<Repas> tousLesRepas() {
        return jours.stream()
                .flatMap(jour -> Stream.of(jour.petitDejeuner(), jour.dejeuner()))
                .toList();
    }

    /**
     * @return la liste de courses consolidee a partir des ingredients des
     * 10 repas du plan (voir {@link ListeCourses#consolider}).
     */
    public ListeCourses genererListeCourses() {
        return ListeCourses.consolider(tousLesRepas().stream()
                .flatMap(repas -> repas.ingredients().stream())
                .toList());
    }
}
