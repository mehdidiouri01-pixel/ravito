package com.ravito.application.prix;

import com.ravito.domain.courses.ListeCourses;
import com.ravito.domain.prix.EstimationPrixPort;
import com.ravito.domain.prix.EstimerCoutUseCase;
import com.ravito.domain.prix.LignePrixEstime;
import com.ravito.domain.prix.Prix;
import com.ravito.domain.profil.Enseigne;

import java.util.List;
import java.util.Objects;

/**
 * Implementation du port d'entree {@link EstimerCoutUseCase}.
 *
 * <p>Pur passe-plat vers {@link EstimationPrixPort} pour l'instant : garder
 * ce use case distinct du port de sortie, plutot que d'exposer directement
 * ce dernier au futur controleur, laisse la porte ouverte a une regle
 * metier future (arrondi, frais fixes, remise) sans casser la frontiere
 * entree/sortie du domaine.
 */
public class EstimerCoutApplicationService implements EstimerCoutUseCase {

    private final EstimationPrixPort estimationPrixPort;

    public EstimerCoutApplicationService(EstimationPrixPort estimationPrixPort) {
        this.estimationPrixPort = Objects.requireNonNull(estimationPrixPort, "estimationPrixPort");
    }

    @Override
    public Prix estimer(ListeCourses listeCourses, Enseigne enseigne) {
        Objects.requireNonNull(listeCourses, "listeCourses");
        Objects.requireNonNull(enseigne, "enseigne");

        return estimationPrixPort.estimerCout(listeCourses, enseigne);
    }

    @Override
    public List<LignePrixEstime> estimerParLigne(ListeCourses listeCourses, Enseigne enseigne) {
        Objects.requireNonNull(listeCourses, "listeCourses");
        Objects.requireNonNull(enseigne, "enseigne");

        return estimationPrixPort.estimerCoutParLigne(listeCourses, enseigne);
    }
}
