package com.ravito.infrastructure.web.catalogue;

import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.TypeRepas;
import com.ravito.domain.nutrition.EstimerNutritionUseCase;
import com.ravito.domain.profil.NiveauCuisine;
import com.ravito.domain.profil.StyleAlimentaire;

import java.util.List;
import java.util.UUID;

/**
 * Traduction JSON d'un {@link Repas}. Distincte du record domaine pour la
 * meme raison que {@code RepasEntity} cote persistence : ne pas coupler le
 * contrat public de l'API a la forme interne du modele metier — un
 * renommage de champ dans {@code Repas} ne doit pas casser silencieusement
 * le JSON expose aux clients.
 *
 * <p>{@link #depuis} prend {@link EstimerNutritionUseCase} en parametre
 * plutot que de l'appeler lui-meme : cette factory reste une simple
 * traduction de valeurs, c'est a l'appelant (le controleur) d'orchestrer
 * les use cases — meme separation que domaine/application partout ailleurs
 * dans ce projet, appliquee ici a la frontiere web.
 */
public record RepasResponse(
        UUID id,
        String nom,
        TypeRepas type,
        StyleAlimentaire style,
        NiveauCuisine niveauRequis,
        List<IngredientQuantiteResponse> ingredients,
        List<String> etapesPreparation,
        ValeursNutritionnellesResponse valeursNutritionnelles) {

    public static RepasResponse depuis(Repas repas, EstimerNutritionUseCase estimerNutritionUseCase) {
        return new RepasResponse(
                repas.id().valeur(),
                repas.nom(),
                repas.type(),
                repas.style(),
                repas.niveauRequis(),
                repas.ingredients().stream().map(IngredientQuantiteResponse::depuis).toList(),
                repas.etapesPreparation(),
                ValeursNutritionnellesResponse.depuis(estimerNutritionUseCase.estimer(repas)));
    }
}
