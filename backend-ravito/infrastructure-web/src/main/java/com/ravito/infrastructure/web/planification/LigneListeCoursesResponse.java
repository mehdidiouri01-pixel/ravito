package com.ravito.infrastructure.web.planification;

import com.ravito.domain.courses.LigneListeCourses;
import com.ravito.domain.courses.UniteMesure;
import com.ravito.domain.prix.Prix;

import java.math.BigDecimal;

/**
 * {@code prixEstime} est le cout de cette seule ligne (voir
 * {@code EstimerCoutUseCase#estimerParLigne}) — expose pour que le client
 * puisse ajuster le total affiche sans nouvel appel serveur quand
 * l'utilisateur indique posseder deja tel ou tel ingredient.
 */
public record LigneListeCoursesResponse(String ingredient, BigDecimal quantite, UniteMesure unite, BigDecimal prixEstime) {

    public static LigneListeCoursesResponse depuis(LigneListeCourses ligne, Prix prix) {
        return new LigneListeCoursesResponse(
                ligne.ingredient().nom(),
                ligne.quantiteTotale().valeur(),
                ligne.quantiteTotale().unite(),
                prix.montant());
    }
}
