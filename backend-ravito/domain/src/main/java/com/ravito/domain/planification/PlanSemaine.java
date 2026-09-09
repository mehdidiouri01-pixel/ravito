package com.ravito.domain.planification;

import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.courses.IngredientQuantite;
import com.ravito.domain.courses.ListeCourses;
import com.ravito.domain.profil.ProfilUtilisateur;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Agregat racine : le plan de repas d'un utilisateur pour sa semaine de
 * travail (lundi a vendredi).
 *
 * <p>Invariants verifies a la construction, et donc impossibles a violer
 * une fois l'objet cree :
 * <ul>
 *     <li>exactement les 5 {@link JourSemaine}, chacun une seule fois —
 *     chaque {@link Jour} peut cela dit n'avoir aucun repas choisi, voir
 *     {@link Jour} ;</li>
 *     <li>au moins un repas choisi au total : un plan entierement vide
 *     n'a pas de sens (pas de liste de courses a en tirer) ;</li>
 *     <li>chaque repas choisi, dans chaque jour, est compatible avec le
 *     profil (meme regle que {@link Repas#estCompatibleAvec}, revalidee
 *     ici en defense en profondeur — voir {@link RepasIncompatibleException}).</li>
 * </ul>
 *
 * <p>{@link #jours()} est aussi toujours trie en ordre calendaire
 * (lundi -> vendredi), quel que soit l'ordre fourni au constructeur : ce
 * n'est pas cosmetique, c'est un invariant a part entiere. La source
 * habituelle de cette liste est une {@code Map<JourSemaine, ChoixJour>}
 * desserialisee depuis une requete JSON — selon son type concret cote
 * appelant (une simple {@code HashMap} par exemple), son ordre d'iteration
 * n'a aucune raison de suivre le calendrier. Le domaine ne peut pas compter
 * sur ses appelants pour ca, donc il l'impose lui-meme.
 */
public record PlanSemaine(ProfilUtilisateur profil, List<Jour> jours) {

    public PlanSemaine {
        Objects.requireNonNull(profil, "profil");
        Objects.requireNonNull(jours, "jours");
        jours = jours.stream()
                .sorted(Comparator.comparing(jour -> jour.jourSemaine().ordinal()))
                .toList();
        exigerExactementLesCinqJours(jours);
        exigerAuMoinsUnRepasChoisi(jours);
        for (Jour jour : jours) {
            exigerCompatible(jour.petitDejeuner(), profil);
            exigerCompatible(jour.dejeuner(), profil);
            exigerCompatible(jour.diner(), profil);
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

    private static void exigerAuMoinsUnRepasChoisi(List<Jour> jours) {
        boolean auMoinsUnChoisi = jours.stream().anyMatch(jour -> !jour.repasChoisis().isEmpty());
        if (!auMoinsUnChoisi) {
            throw new PlanSemaineInvalideException("Le plan ne contient aucun repas choisi");
        }
    }

    private static void exigerCompatible(Optional<Repas> repas, ProfilUtilisateur profil) {
        repas.ifPresent(r -> {
            if (!r.estCompatibleAvec(profil)) {
                throw new RepasIncompatibleException(r, profil);
            }
        });
    }

    /**
     * @return les repas effectivement choisis du plan (jusqu'a 15 : 5
     * petits-dejeuners + 5 dejeuners + 5 diners, moins ceux qu'un jour n'a
     * pas — voir {@link Jour}), dans l'ordre des jours. Base de la
     * generation de la liste de courses (package {@code courses}).
     */
    public List<Repas> tousLesRepas() {
        return jours.stream()
                .flatMap(jour -> jour.repasChoisis().stream())
                .toList();
    }

    /**
     * @return la liste de courses consolidee a partir des ingredients des
     * 10 repas du plan (voir {@link ListeCourses#consolider}).
     *
     * <p>Les quantites du catalogue sont exprimees <b>pour une portion</b> :
     * elles sont donc multipliees par le nombre de personnes du foyer avant
     * consolidation. C'est une regle metier, elle vit ici et pas dans un
     * controleur ou dans le frontend — le cout estime suit automatiquement,
     * puisque {@code EstimationPrixPort} recoit une liste deja mise a
     * l'echelle.
     *
     * <p>Limite assumee : la multiplication est lineaire pour tous les
     * ingredients, y compris ceux qui ne passent pas vraiment a l'echelle
     * (une pincee de sel pour 6 personnes reste une pincee). Distinguer ces
     * ingredients demanderait de le modeliser dans le catalogue.
     */
    public ListeCourses genererListeCourses() {
        int facteur = profil.nombreDePersonnes().valeur();
        return ListeCourses.consolider(tousLesRepas().stream()
                .flatMap(repas -> repas.ingredients().stream())
                .map(ingredient -> new IngredientQuantite(
                        ingredient.ingredient(),
                        ingredient.quantite().multiplierPar(facteur)))
                .toList());
    }
}
