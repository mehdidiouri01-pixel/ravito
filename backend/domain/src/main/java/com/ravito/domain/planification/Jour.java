package com.ravito.domain.planification;

import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.TypeRepas;

import java.util.Objects;

/**
 * Une journee du plan de semaine : exactement un petit-dejeuner et un
 * dejeuner. Ne connait pas le profil de l'utilisateur — la compatibilite
 * des repas avec le profil est verifiee au niveau de {@link PlanSemaine},
 * qui seul la possede.
 */
public record Jour(JourSemaine jourSemaine, Repas petitDejeuner, Repas dejeuner) {

    public Jour {
        Objects.requireNonNull(jourSemaine, "jourSemaine");
        Objects.requireNonNull(petitDejeuner, "petitDejeuner");
        Objects.requireNonNull(dejeuner, "dejeuner");
        exigerType(petitDejeuner, TypeRepas.PETIT_DEJEUNER);
        exigerType(dejeuner, TypeRepas.DEJEUNER);
    }

    private static void exigerType(Repas repas, TypeRepas typeAttendu) {
        if (repas.type() != typeAttendu) {
            throw new PlanSemaineInvalideException(
                    "Le repas \"" + repas.nom() + "\" est de type " + repas.type()
                            + ", attendu " + typeAttendu);
        }
    }
}
