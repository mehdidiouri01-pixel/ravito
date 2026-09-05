package com.ravito.application.planification;

import com.ravito.domain.catalogue.CatalogueRepasPort;
import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.RepasId;
import com.ravito.domain.catalogue.RepasIntrouvableException;
import com.ravito.domain.planification.ChoixJour;
import com.ravito.domain.planification.ComposerPlanSemaineUseCase;
import com.ravito.domain.planification.Jour;
import com.ravito.domain.planification.JourSemaine;
import com.ravito.domain.planification.PlanSemaine;
import com.ravito.domain.profil.ProfilUtilisateur;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Implementation du port d'entree {@link ComposerPlanSemaineUseCase}.
 *
 * <p>Resout chaque {@link ChoixJour} en {@link Repas} reels via
 * {@link CatalogueRepasPort#parId}, puis delegue au constructeur de
 * {@link PlanSemaine} la verification des invariants (5 jours, repas
 * compatibles avec le profil) — cette classe ne fait qu'assembler, elle
 * ne revalide rien elle-meme.
 */
public class ComposerPlanSemaineApplicationService implements ComposerPlanSemaineUseCase {

    private final CatalogueRepasPort catalogueRepasPort;

    public ComposerPlanSemaineApplicationService(CatalogueRepasPort catalogueRepasPort) {
        this.catalogueRepasPort = Objects.requireNonNull(catalogueRepasPort, "catalogueRepasPort");
    }

    @Override
    public PlanSemaine composer(ProfilUtilisateur profil, Map<JourSemaine, ChoixJour> choix) {
        Objects.requireNonNull(profil, "profil");
        Objects.requireNonNull(choix, "choix");

        List<Jour> jours = choix.entrySet().stream()
                .map(entree -> versJour(entree.getKey(), entree.getValue()))
                .toList();

        return new PlanSemaine(profil, jours);
    }

    private Jour versJour(JourSemaine jourSemaine, ChoixJour choixJour) {
        Repas petitDejeuner = parId(choixJour.petitDejeunerId());
        Repas dejeuner = parId(choixJour.dejeunerId());
        return new Jour(jourSemaine, petitDejeuner, dejeuner);
    }

    private Repas parId(RepasId id) {
        return catalogueRepasPort.parId(id)
                .orElseThrow(() -> new RepasIntrouvableException(id));
    }
}
