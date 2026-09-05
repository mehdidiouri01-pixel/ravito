package com.ravito.infrastructure.web.planification;

import com.ravito.domain.courses.LigneListeCourses;
import com.ravito.domain.courses.UniteMesure;

import java.math.BigDecimal;

public record LigneListeCoursesResponse(String ingredient, BigDecimal quantite, UniteMesure unite) {

    public static LigneListeCoursesResponse depuis(LigneListeCourses ligne) {
        return new LigneListeCoursesResponse(
                ligne.ingredient().nom(),
                ligne.quantiteTotale().valeur(),
                ligne.quantiteTotale().unite());
    }
}
