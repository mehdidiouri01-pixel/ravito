package com.ravito.application.planification;

import com.ravito.application.catalogue.PoolIngredientsCatalogue;
import com.ravito.domain.catalogue.CatalogueRepasPort;
import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.RepasId;
import com.ravito.domain.catalogue.RepasIntrouvableException;
import com.ravito.domain.planification.ChoixJour;
import com.ravito.domain.planification.ChoixRepas;
import com.ravito.domain.planification.ComposerPlanSemaineUseCase;
import com.ravito.domain.planification.Jour;
import com.ravito.domain.planification.JourSemaine;
import com.ravito.domain.planification.PlanSemaine;
import com.ravito.domain.planification.RepasGenereInvalideException;
import com.ravito.domain.profil.ProfilUtilisateur;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Implementation du port d'entree {@link ComposerPlanSemaineUseCase}.
 *
 * <p>Resout chaque {@link ChoixRepas} de chaque {@link ChoixJour} en
 * {@link Repas} reel : un {@link ChoixRepas.ParId} via
 * {@link CatalogueRepasPort#parId} ; un {@link ChoixRepas.Genere} en
 * revalidant d'abord ses ingredients contre le catalogue connu (voir
 * {@link PoolIngredientsCatalogue}) — jamais de confiance aveugle envers un
 * repas transmis par le client, puisqu'il n'existe dans aucune table et ne
 * peut donc pas etre re-verifie autrement. Delegue ensuite au constructeur
 * de {@link PlanSemaine} la verification des invariants restants (5 jours,
 * repas compatibles avec le profil).
 */
public class ComposerPlanSemaineApplicationService implements ComposerPlanSemaineUseCase {

    private final CatalogueRepasPort catalogueRepasPort;
    private final PoolIngredientsCatalogue poolIngredientsCatalogue;

    public ComposerPlanSemaineApplicationService(
            CatalogueRepasPort catalogueRepasPort, PoolIngredientsCatalogue poolIngredientsCatalogue) {
        this.catalogueRepasPort = Objects.requireNonNull(catalogueRepasPort, "catalogueRepasPort");
        this.poolIngredientsCatalogue = Objects.requireNonNull(poolIngredientsCatalogue, "poolIngredientsCatalogue");
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
        Repas petitDejeuner = resoudre(choixJour.petitDejeuner());
        Repas dejeuner = resoudre(choixJour.dejeuner());
        Repas diner = resoudre(choixJour.diner());
        return new Jour(jourSemaine, petitDejeuner, dejeuner, diner);
    }

    private Repas resoudre(ChoixRepas choix) {
        return switch (choix) {
            case ChoixRepas.ParId parId -> parId(parId.id());
            case ChoixRepas.Genere genere -> revalide(genere.repas());
        };
    }

    private Repas parId(RepasId id) {
        return catalogueRepasPort.parId(id)
                .orElseThrow(() -> new RepasIntrouvableException(id));
    }

    private Repas revalide(Repas repas) {
        boolean connu = poolIngredientsCatalogue.connusPourTypeEtStyle(repas.type(), repas.style(), repas.ingredients());
        if (!connu) {
            throw new RepasGenereInvalideException(repas);
        }
        return repas;
    }
}
