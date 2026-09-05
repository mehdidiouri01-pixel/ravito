package com.ravito.infrastructure.persistence.prix;

import com.ravito.domain.courses.LigneListeCourses;
import com.ravito.domain.courses.ListeCourses;
import com.ravito.domain.prix.EstimationImpossibleException;
import com.ravito.domain.prix.EstimationPrixPort;
import com.ravito.domain.prix.Prix;
import com.ravito.domain.profil.Enseigne;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Implementation v1 du port de sortie {@link EstimationPrixPort} : somme des
 * prix moyens de chaque ligne de la liste de courses (voir
 * {@link PrixMoyenIngredientEntity}), ponderee par le coefficient de
 * l'enseigne (voir {@link CoefficientEnseigneProperties}).
 */
@Component
class JpaEstimationPrixAdapter implements EstimationPrixPort {

    private final PrixMoyenIngredientJpaRepository repository;
    private final CoefficientEnseigneProperties coefficients;

    JpaEstimationPrixAdapter(PrixMoyenIngredientJpaRepository repository, CoefficientEnseigneProperties coefficients) {
        this.repository = Objects.requireNonNull(repository, "repository");
        this.coefficients = Objects.requireNonNull(coefficients, "coefficients");
    }

    @Override
    public Prix estimerCout(ListeCourses listeCourses, Enseigne enseigne) {
        Prix sousTotal = listeCourses.lignes().stream()
                .map(this::prixDeLaLigne)
                .reduce(Prix.ZERO, Prix::plus);

        return new Prix(sousTotal.montant().multiply(coefficients.pour(enseigne)));
    }

    private Prix prixDeLaLigne(LigneListeCourses ligne) {
        PrixMoyenIngredientEntity prixMoyen = repository
                .findByIngredientNomAndUnite(ligne.ingredient().nom(), ligne.quantiteTotale().unite())
                .orElseThrow(() -> new EstimationImpossibleException(
                        "Aucun prix moyen connu pour l'ingredient \"" + ligne.ingredient().nom()
                                + "\" en " + ligne.quantiteTotale().unite()));

        return new Prix(prixMoyen.getPrixUnitaire().multiply(ligne.quantiteTotale().valeur()));
    }
}
