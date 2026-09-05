package com.ravito.infrastructure.web.catalogue;

import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.TypeRepas;
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
 */
public record RepasResponse(
        UUID id,
        String nom,
        TypeRepas type,
        StyleAlimentaire style,
        NiveauCuisine niveauRequis,
        List<IngredientQuantiteResponse> ingredients) {

    public static RepasResponse depuis(Repas repas) {
        return new RepasResponse(
                repas.id().valeur(),
                repas.nom(),
                repas.type(),
                repas.style(),
                repas.niveauRequis(),
                repas.ingredients().stream().map(IngredientQuantiteResponse::depuis).toList());
    }
}
