package com.ravito.infrastructure.persistence.prix;

import com.ravito.domain.courses.LigneListeCourses;
import com.ravito.domain.courses.ListeCourses;
import com.ravito.domain.prix.EstimationImpossibleException;
import com.ravito.domain.prix.EstimationPrixPort;
import com.ravito.domain.prix.LignePrixEstime;
import com.ravito.domain.prix.Prix;
import com.ravito.domain.profil.Enseigne;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * Implementation v1 du port de sortie {@link EstimationPrixPort} : somme des
 * prix moyens de chaque ligne de la liste de courses (voir
 * {@link PrixMoyenIngredientEntity}), ponderee par le coefficient de
 * l'enseigne (voir {@link CoefficientEnseigneProperties}).
 *
 * <p>{@link #estimerCout} est deliberement derive de
 * {@link #estimerCoutParLigne} (somme des lignes) plutot que recalcule
 * independamment : le coefficient etant applique ligne par ligne, la
 * distributivite de la multiplication garantit que le total colle toujours
 * exactement a la somme des lignes affichees cote client.
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
        return estimerCoutParLigne(listeCourses, enseigne).stream()
                .map(LignePrixEstime::prix)
                .reduce(Prix.ZERO, Prix::plus);
    }

    @Override
    public List<LignePrixEstime> estimerCoutParLigne(ListeCourses listeCourses, Enseigne enseigne) {
        BigDecimal coefficient = coefficients.pour(enseigne);
        return listeCourses.lignes().stream()
                .map(ligne -> new LignePrixEstime(ligne,
                        new Prix(prixDeLaLigne(ligne).montant().multiply(coefficient))))
                .toList();
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
