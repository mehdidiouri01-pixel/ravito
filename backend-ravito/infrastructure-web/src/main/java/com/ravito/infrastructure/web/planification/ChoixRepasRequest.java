package com.ravito.infrastructure.web.planification;

import com.ravito.domain.catalogue.RepasId;
import com.ravito.domain.planification.ChoixRepas;
import com.ravito.infrastructure.web.catalogue.RepasGenereRequest;
import jakarta.validation.Valid;

import java.util.UUID;

/**
 * Corps JSON du choix pour un seul repas d'une journee : soit un identifiant
 * du catalogue ({@code id}), soit un repas genere transmis en entier
 * ({@code genere}) — jamais les deux, jamais aucun des deux (voir
 * {@link #versDomaine()}).
 *
 * <p>Pas d'annotation Bean Validation pour cette regle "l'un ou l'autre" :
 * ni {@code @NotNull} (les deux champs sont individuellement optionnels) ni
 * une contrainte standard ne l'expriment simplement, donc {@link #versDomaine()}
 * la verifie elle-meme plutot que d'ajouter une contrainte customisee pour
 * ce seul cas.
 */
public record ChoixRepasRequest(UUID id, @Valid RepasGenereRequest genere) {

    public ChoixRepas versDomaine() {
        boolean aId = id != null;
        boolean aGenere = genere != null;
        if (aId == aGenere) {
            throw new ChoixRepasInvalideException();
        }
        return aId ? new ChoixRepas.ParId(new RepasId(id)) : new ChoixRepas.Genere(genere.versDomaine());
    }
}
