package com.ravito.application.historique;

import com.ravito.domain.historique.ConsulterHistoriqueUseCase;
import com.ravito.domain.historique.HistoriquePlanId;
import com.ravito.domain.historique.HistoriquePlansPort;
import com.ravito.domain.historique.PlanSemaineHistorise;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation du port d'entree {@link ConsulterHistoriqueUseCase}. Pur
 * passe-plat vers {@link HistoriquePlansPort} — voir la Javadoc de
 * {@code HistoriserPlanApplicationService} pour le raisonnement.
 */
public class ConsulterHistoriqueApplicationService implements ConsulterHistoriqueUseCase {

    private final HistoriquePlansPort historiquePlansPort;

    public ConsulterHistoriqueApplicationService(HistoriquePlansPort historiquePlansPort) {
        this.historiquePlansPort = Objects.requireNonNull(historiquePlansPort, "historiquePlansPort");
    }

    @Override
    public List<PlanSemaineHistorise> lister() {
        return historiquePlansPort.lister();
    }

    @Override
    public Optional<PlanSemaineHistorise> parId(HistoriquePlanId id) {
        Objects.requireNonNull(id, "id");
        return historiquePlansPort.parId(id);
    }
}
