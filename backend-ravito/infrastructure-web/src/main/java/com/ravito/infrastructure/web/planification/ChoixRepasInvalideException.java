package com.ravito.infrastructure.web.planification;

/**
 * Levee quand un {@link ChoixRepasRequest} ne fournit ni {@code id} ni
 * {@code genere}, ou fournit les deux — une erreur de forme de la requete,
 * pas une regle metier (voir {@code RepasGenereInvalideException} pour le
 * cas ou un repas genere est bien fourni seul mais avec un contenu
 * invalide).
 *
 * <p>Publique (pas package-private comme la plupart des classes de cette
 * couche) : {@link com.ravito.infrastructure.web.GlobalExceptionHandler},
 * qui la mappe en reponse HTTP, vit dans un autre package.
 */
public class ChoixRepasInvalideException extends RuntimeException {

    public ChoixRepasInvalideException() {
        super("Chaque repas doit etre choisi soit par identifiant (id), soit genere (genere) — pas les deux, pas aucun");
    }
}
