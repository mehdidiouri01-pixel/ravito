package com.ravito.domain.courses;

/**
 * Unite de mesure d'une {@link Quantite}.
 *
 * <p>Volontairement sans logique de conversion entre unites : deux quantites
 * ne s'additionnent que si elles partagent la meme unite (voir
 * {@link Quantite#plus(Quantite)}). Si un meme ingredient apparait dans deux
 * repas avec des unites differentes, la liste de courses conservera deux
 * lignes distinctes plutot que de tenter une conversion approximative
 * (choix v1 — voir la discussion d'architecture).
 */
public enum UniteMesure {
    GRAMME,
    KILOGRAMME,
    MILLILITRE,
    LITRE,
    UNITE,
    CUILLERE_A_SOUPE,
    CUILLERE_A_CAFE,
    PINCEE
}
