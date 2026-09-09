package com.ravito.infrastructure.web.catalogue;

import com.ravito.domain.nutrition.ValeursNutritionnelles;

import java.math.BigDecimal;

public record ValeursNutritionnellesResponse(
        BigDecimal calories,
        BigDecimal proteines,
        BigDecimal glucides,
        BigDecimal lipides,
        BigDecimal fibres) {

    public static ValeursNutritionnellesResponse depuis(ValeursNutritionnelles valeurs) {
        return new ValeursNutritionnellesResponse(
                valeurs.calories(), valeurs.proteines(), valeurs.glucides(), valeurs.lipides(), valeurs.fibres());
    }
}
