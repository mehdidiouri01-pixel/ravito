package com.ravito.domain.catalogue;

import com.ravito.domain.profil.StyleAlimentaire;

/**
 * Levee par {@link GenererRepasUseCase} quand le catalogue existant ne
 * fournit pas assez d'ingredients distincts, pour un {@link TypeRepas} et un
 * {@link StyleAlimentaire} donnes, pour en composer un nouveau repas.
 *
 * <p>Purement defensive : avec le catalogue actuel (au moins 15 repas par
 * type/style, 2 ingredients chacun), le pool disponible est toujours tres
 * largement suffisant. Cette exception protège contre un catalogue qui
 * deviendrait un jour anormalement pauvre, plutot que de laisser
 * {@code GenererRepasApplicationService} echouer silencieusement ou tirer le
 * meme ingredient deux fois.
 */
public class GenerationRepasImpossibleException extends RuntimeException {

    public GenerationRepasImpossibleException(TypeRepas type, StyleAlimentaire style) {
        super("Pas assez d'ingredients connus pour generer un repas de type " + type + " et de style " + style);
    }
}
