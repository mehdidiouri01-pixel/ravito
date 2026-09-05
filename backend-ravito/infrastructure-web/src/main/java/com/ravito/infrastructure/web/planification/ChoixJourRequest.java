package com.ravito.infrastructure.web.planification;

import com.ravito.domain.catalogue.RepasId;
import com.ravito.domain.planification.ChoixJour;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ChoixJourRequest(@NotNull UUID petitDejeunerId, @NotNull UUID dejeunerId) {

    public ChoixJour versDomaine() {
        return new ChoixJour(new RepasId(petitDejeunerId), new RepasId(dejeunerId));
    }
}
