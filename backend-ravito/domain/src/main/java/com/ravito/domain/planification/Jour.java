package com.ravito.domain.planification;

import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.TypeRepas;

import java.util.Objects;

/**
 * Une journee du plan de semaine : exactement un petit-dejeuner, un
 * dejeuner et un diner. Ne connait pas le profil de l'utilisateur — la
 * compatibilite des repas avec le profil est verifiee au niveau de
 * {@link PlanSemaine}, qui seul la possede.
 */
public record Jour(JourSemaine jourSemaine, Repas petitDejeuner, Repas dejeuner, Repas diner) {

    public Jour {
        Objects.requireNonNull(jourSemaine, "jourSemaine");
        Objects.requireNonNull(petitDejeuner, "petitDejeuner");
        Objects.requireNonNull(dejeuner, "dejeuner");
        Objects.requireNonNull(diner, "diner");
        exigerType(petitDejeuner, TypeRepas.PETIT_DEJEUNER);
        exigerType(dejeuner, TypeRepas.DEJEUNER);
        exigerType(diner, TypeRepas.DINER);
    }

    private static void exigerType(Repas repas, TypeRepas typeAttendu) {
        if (repas.type() != typeAttendu) {
            throw new PlanSemaineInvalideException(
                    "Le repas \"" + repas.nom() + "\" est de type " + repas.type()
                            + ", attendu " + typeAttendu);
        }
    }
}
