package com.ravito.application.nutrition;

import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.nutrition.EstimationNutritionPort;
import com.ravito.domain.nutrition.EstimerNutritionUseCase;
import com.ravito.domain.nutrition.ValeursNutritionnelles;

import java.util.Objects;

/**
 * Implementation du port d'entree {@link EstimerNutritionUseCase}.
 *
 * <p>Pur passe-plat vers {@link EstimationNutritionPort} pour l'instant —
 * meme raisonnement que {@code EstimerCoutApplicationService} pour le prix :
 * garder ce use case distinct du port de sortie laisse la porte ouverte a
 * une regle metier future sans casser la frontiere entree/sortie du
 * domaine.
 */
public class EstimerNutritionApplicationService implements EstimerNutritionUseCase {

    private final EstimationNutritionPort estimationNutritionPort;

    public EstimerNutritionApplicationService(EstimationNutritionPort estimationNutritionPort) {
        this.estimationNutritionPort = Objects.requireNonNull(estimationNutritionPort, "estimationNutritionPort");
    }

    @Override
    public ValeursNutritionnelles estimer(Repas repas) {
        Objects.requireNonNull(repas, "repas");

        return estimationNutritionPort.estimer(repas.ingredients());
    }
}
