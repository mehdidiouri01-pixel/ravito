package com.ravito.domain.planification;

import com.ravito.domain.catalogue.Repas;

/**
 * Levee quand un {@link ChoixRepas.Genere} transmis par le client contient
 * un ingredient (couple nom+unite) qui n'appartient pas au catalogue connu
 * pour le type et le style du repas.
 *
 * <p>Defense en profondeur : un repas genere n'est jamais stocke, donc rien
 * ne garantit que les donnees recues a la composition du plan sont bien
 * celles renvoyees par {@code GenererRepasUseCase} — le client pourrait les
 * avoir modifiees avant de les renvoyer. Sans cette verification,
 * l'estimation de prix echouerait plus loin avec un message moins clair
 * (voir {@code EstimationImpossibleException}), ou pire, un ingredient
 * partageant un nom mais pas le prix attendu serait accepte a tort.
 */
public class RepasGenereInvalideException extends RuntimeException {

    public RepasGenereInvalideException(Repas repas) {
        super("Le repas genere \"" + repas.nom() + "\" contient un ingredient qui n'appartient pas au catalogue"
                + " connu pour " + repas.type() + "/" + repas.style());
    }
}
