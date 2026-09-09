package com.ravito.infrastructure.web.planification;

import com.ravito.domain.planification.ChoixJour;
import jakarta.validation.Valid;

import java.util.Optional;

/**
 * Chaque champ est individuellement optionnel : absent (ou {@code null})
 * signifie "pas de repas choisi pour ce creneau ce jour-la", pas une
 * erreur de requete — voir {@code SelectionRepasComponent} cote frontend,
 * qui laisse composer un plan meme partiellement rempli.
 */
public record ChoixJourRequest(
        @Valid ChoixRepasRequest petitDejeuner,
        @Valid ChoixRepasRequest dejeuner,
        @Valid ChoixRepasRequest diner) {

    public ChoixJour versDomaine() {
        return new ChoixJour(
                Optional.ofNullable(petitDejeuner).map(ChoixRepasRequest::versDomaine),
                Optional.ofNullable(dejeuner).map(ChoixRepasRequest::versDomaine),
                Optional.ofNullable(diner).map(ChoixRepasRequest::versDomaine));
    }
}
