package com.ravito.infrastructure.web.planification;

import com.ravito.domain.planification.ChoixJour;
import com.ravito.domain.planification.JourSemaine;
import com.ravito.infrastructure.web.catalogue.ProfilRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Corps JSON de composition d'un plan de semaine. Le nombre de jours et
 * leur unicite ne sont pas revalides ici : c'est un invariant du domaine
 * ({@code PlanSemaine}), pas une regle de format HTTP — le controleur
 * laisse le use case (et l'agregat en dessous) le faire respecter.
 */
public record ComposerPlanSemaineRequest(
        @NotNull @Valid ProfilRequest profil,
        @NotNull @Valid Map<JourSemaine, ChoixJourRequest> choix) {

    public Map<JourSemaine, ChoixJour> choixVersDomaine() {
        return choix.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entree -> entree.getValue().versDomaine()));
    }
}
