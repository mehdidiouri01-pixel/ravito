package com.ravito.domain.historique;

import com.ravito.domain.planification.PlanSemaine;
import com.ravito.domain.prix.Prix;

import java.time.Instant;
import java.util.Objects;

/**
 * Un {@link PlanSemaine} tel qu'il a ete compose a un instant donne,
 * conserve pour l'historique.
 *
 * <p>Capture le {@link PlanSemaine} en entier (chaque {@code Jour} porte
 * deja ses {@code Repas} resolus, ingredients compris — voir
 * {@code ComposerPlanSemaineApplicationService}), pas seulement des
 * identifiants : un plan compose a partir d'un repas genere n'existe nulle
 * part ailleurs (voir {@code ChoixRepas.Genere}), et un plan compose a
 * partir du catalogue doit rester lisible meme si ce repas est retire ou
 * modifie plus tard. Le prix est fige de la meme facon : {@code EstimationPrixPort}
 * pourrait renvoyer un montant different demain si les prix moyens
 * changent.
 */
public record PlanSemaineHistorise(HistoriquePlanId id, Instant dateComposition, PlanSemaine plan, Prix prixEstime) {

    public PlanSemaineHistorise {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(dateComposition, "dateComposition");
        Objects.requireNonNull(plan, "plan");
        Objects.requireNonNull(prixEstime, "prixEstime");
    }
}
