package com.ravito.infrastructure.web.planification;

import com.ravito.domain.prix.Prix;

import java.math.BigDecimal;

public record PrixResponse(BigDecimal montant) {

    public static PrixResponse depuis(Prix prix) {
        return new PrixResponse(prix.montant());
    }
}
