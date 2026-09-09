package com.ravito.infrastructure.web.planification;

import com.ravito.domain.planification.ChoixJour;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record ChoixJourRequest(
        @NotNull @Valid ChoixRepasRequest petitDejeuner,
        @NotNull @Valid ChoixRepasRequest dejeuner,
        @NotNull @Valid ChoixRepasRequest diner) {

    public ChoixJour versDomaine() {
        return new ChoixJour(petitDejeuner.versDomaine(), dejeuner.versDomaine(), diner.versDomaine());
    }
}
