package com.ravito.domain.planification;

import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.RepasId;

import java.util.Objects;

/**
 * Le choix de l'utilisateur pour un seul repas d'une journee : soit un
 * identifiant a resoudre dans le catalogue stocke ({@link ParId}), soit un
 * repas genere a la volee ({@link Genere}, voir {@code GenererRepasUseCase})
 * transmis en entier puisqu'il n'existe dans aucune table.
 *
 * <p>Sealed plutot qu'un simple champ nullable : le compilateur force
 * chaque appelant (voir {@code ComposerPlanSemaineApplicationService}) a
 * traiter explicitement les deux cas, sans risque d'en oublier un.
 */
public sealed interface ChoixRepas {

    record ParId(RepasId id) implements ChoixRepas {
        public ParId {
            Objects.requireNonNull(id, "id");
        }
    }

    /**
     * @param repas le repas genere tel que transmis par le client — jamais
     * digne de confiance tel quel : l'implementation de
     * {@code ComposerPlanSemaineUseCase} doit revalider que ses ingredients
     * appartiennent bien au catalogue connu avant de l'utiliser (voir
     * {@link RepasGenereInvalideException}).
     */
    record Genere(Repas repas) implements ChoixRepas {
        public Genere {
            Objects.requireNonNull(repas, "repas");
        }
    }
}
