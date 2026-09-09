package com.ravito.domain.planification;

import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.TypeRepas;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Une journee du plan de semaine : au plus un petit-dejeuner, un dejeuner
 * et un diner — chacun optionnel plutot qu'obligatoire, pour laisser
 * l'utilisateur composer un plan sans avoir choisi les 15 repas (voir
 * {@code SelectionRepasComponent}, cote frontend, qui laisse valider avec
 * des cases encore vides). Ne connait pas le profil de l'utilisateur — la
 * compatibilite des repas avec le profil est verifiee au niveau de
 * {@link PlanSemaine}, qui seul la possede.
 */
public record Jour(JourSemaine jourSemaine, Optional<Repas> petitDejeuner, Optional<Repas> dejeuner, Optional<Repas> diner) {

    public Jour {
        Objects.requireNonNull(jourSemaine, "jourSemaine");
        Objects.requireNonNull(petitDejeuner, "petitDejeuner");
        Objects.requireNonNull(dejeuner, "dejeuner");
        Objects.requireNonNull(diner, "diner");
        petitDejeuner.ifPresent(repas -> exigerType(repas, TypeRepas.PETIT_DEJEUNER));
        dejeuner.ifPresent(repas -> exigerType(repas, TypeRepas.DEJEUNER));
        diner.ifPresent(repas -> exigerType(repas, TypeRepas.DINER));
    }

    /** @return les repas effectivement choisis ce jour-la, dans l'ordre petit-dejeuner/dejeuner/diner — 0 a 3 elements. */
    public List<Repas> repasChoisis() {
        return Stream.of(petitDejeuner, dejeuner, diner).flatMap(Optional::stream).toList();
    }

    private static void exigerType(Repas repas, TypeRepas typeAttendu) {
        if (repas.type() != typeAttendu) {
            throw new PlanSemaineInvalideException(
                    "Le repas \"" + repas.nom() + "\" est de type " + repas.type()
                            + ", attendu " + typeAttendu);
        }
    }
}
