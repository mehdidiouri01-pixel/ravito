package com.ravito.application.historique;

import com.ravito.domain.historique.HistoriquePlansPort;
import com.ravito.domain.historique.HistoriserPlanUseCase;
import com.ravito.domain.historique.PlanSemaineHistorise;
import com.ravito.domain.planification.PlanSemaine;
import com.ravito.domain.prix.Prix;

import java.util.Objects;

/**
 * Implementation du port d'entree {@link HistoriserPlanUseCase}.
 *
 * <p>Pur passe-plat vers {@link HistoriquePlansPort} pour l'instant — meme
 * raisonnement que {@code EstimerCoutApplicationService} : garder ce use
 * case distinct du port de sortie laisse la porte ouverte a une regle
 * metier future (ex: limiter le nombre de plans conserves) sans casser la
 * frontiere entree/sortie du domaine.
 */
public class HistoriserPlanApplicationService implements HistoriserPlanUseCase {

    private final HistoriquePlansPort historiquePlansPort;

    public HistoriserPlanApplicationService(HistoriquePlansPort historiquePlansPort) {
        this.historiquePlansPort = Objects.requireNonNull(historiquePlansPort, "historiquePlansPort");
    }

    @Override
    public PlanSemaineHistorise historiser(PlanSemaine plan, Prix prixEstime) {
        Objects.requireNonNull(plan, "plan");
        Objects.requireNonNull(prixEstime, "prixEstime");

        return historiquePlansPort.enregistrer(plan, prixEstime);
    }
}
