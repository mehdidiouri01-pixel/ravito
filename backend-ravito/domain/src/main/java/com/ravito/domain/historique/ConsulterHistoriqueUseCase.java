package com.ravito.domain.historique;

import java.util.List;
import java.util.Optional;

/**
 * Port d'entree : consulte l'historique des plans de semaine deja composes.
 *
 * <p>Pas de notion d'utilisateur dans ce projet (pas d'authentification) :
 * l'historique est global, comme le reste de l'application — un choix
 * assume pour un projet personnel a un seul utilisateur, a revisiter si des
 * comptes utilisateurs apparaissent un jour.
 */
public interface ConsulterHistoriqueUseCase {

    List<PlanSemaineHistorise> lister();

    Optional<PlanSemaineHistorise> parId(HistoriquePlanId id);
}
