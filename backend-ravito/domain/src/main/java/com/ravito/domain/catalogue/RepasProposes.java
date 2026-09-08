package com.ravito.domain.catalogue;

import java.util.List;
import java.util.Objects;

/**
 * Selection de repas proposee a l'utilisateur pour un profil donne : au
 * moins 5 petits-dejeuners, 5 dejeuners et 5 diners, de quoi choisir un
 * repas par jour de la semaine.
 *
 * <p>Le minimum de 5 repas par type est un invariant du domaine : c'est
 * cette classe, pas le futur controleur, qui refuse la construction d'une
 * proposition incomplete (voir {@link CatalogueInsuffisantException}).
 */
public record RepasProposes(List<Repas> petitsDejeuners, List<Repas> dejeuners, List<Repas> diners) {

    public static final int MINIMUM_PAR_TYPE = 5;

    public RepasProposes {
        Objects.requireNonNull(petitsDejeuners, "petitsDejeuners");
        Objects.requireNonNull(dejeuners, "dejeuners");
        Objects.requireNonNull(diners, "diners");
        petitsDejeuners = List.copyOf(petitsDejeuners);
        dejeuners = List.copyOf(dejeuners);
        diners = List.copyOf(diners);
        exigerMinimum(TypeRepas.PETIT_DEJEUNER, petitsDejeuners);
        exigerMinimum(TypeRepas.DEJEUNER, dejeuners);
        exigerMinimum(TypeRepas.DINER, diners);
    }

    private static void exigerMinimum(TypeRepas type, List<Repas> repas) {
        if (repas.size() < MINIMUM_PAR_TYPE) {
            throw new CatalogueInsuffisantException(type, repas.size(), MINIMUM_PAR_TYPE);
        }
    }
}
