package com.ravito.infrastructure.web.catalogue;

import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.RepasId;
import com.ravito.domain.catalogue.TypeRepas;
import com.ravito.domain.profil.NiveauCuisine;
import com.ravito.domain.profil.StyleAlimentaire;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Corps JSON d'un repas genere (voir {@code GenererRepasUseCase}) transmis
 * en entier par le client lors de la composition du plan — puisqu'il
 * n'existe dans aucune table, il ne peut pas etre reference par id comme un
 * repas du catalogue (voir {@code ChoixRepasRequest}).
 *
 * <p>Un identifiant frais est regenere cote serveur ({@link RepasId#nouveau})
 * plutot que de reutiliser celui envoye par le client : il ne sert qu'a
 * l'affichage, jamais a retrouver quoi que ce soit en base, donc pas besoin
 * de le transmettre ni de lui faire confiance.
 *
 * <p>Ne pas confondre "valide en forme" (ce que ces annotations verifient)
 * et "digne de confiance" : c'est {@code ComposerPlanSemaineApplicationService}
 * qui revalide que chaque ingredient appartient bien au catalogue connu
 * avant d'accepter ce repas (voir {@code RepasGenereInvalideException}).
 */
public record RepasGenereRequest(
        @NotBlank String nom,
        @NotNull TypeRepas type,
        @NotNull StyleAlimentaire style,
        @NotNull NiveauCuisine niveauRequis,
        @NotEmpty @Valid List<IngredientQuantiteRequest> ingredients) {

    public Repas versDomaine() {
        return new Repas(RepasId.nouveau(), nom, type, style, niveauRequis,
                ingredients.stream().map(IngredientQuantiteRequest::versDomaine).toList());
    }
}
