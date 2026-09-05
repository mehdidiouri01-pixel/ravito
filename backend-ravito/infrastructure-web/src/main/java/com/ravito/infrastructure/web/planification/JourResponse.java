package com.ravito.infrastructure.web.planification;

import com.ravito.domain.planification.Jour;
import com.ravito.domain.planification.JourSemaine;
import com.ravito.infrastructure.web.catalogue.RepasResponse;

public record JourResponse(JourSemaine jour, RepasResponse petitDejeuner, RepasResponse dejeuner) {

    public static JourResponse depuis(Jour jour) {
        return new JourResponse(
                jour.jourSemaine(),
                RepasResponse.depuis(jour.petitDejeuner()),
                RepasResponse.depuis(jour.dejeuner()));
    }
}
