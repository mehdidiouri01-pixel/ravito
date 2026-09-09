package com.ravito.infrastructure.persistence.historique;

import com.ravito.domain.historique.HistoriquePlanId;
import com.ravito.domain.historique.HistoriquePlansPort;
import com.ravito.domain.historique.PlanSemaineHistorise;
import com.ravito.domain.planification.PlanSemaine;
import com.ravito.domain.prix.Prix;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation JPA/PostgreSQL du port de sortie {@link HistoriquePlansPort}.
 *
 * <p>{@code @Transactional} sur les methodes de lecture pour la meme raison
 * que {@code JpaCatalogueRepasAdapter} : les collections d'ingredients de
 * {@link RepasHistoriqueEntity} sont chargees en lazy, et {@code
 * spring.jpa.open-in-view} est desactive — chaque methode publique doit
 * renvoyer un {@code PlanSemaineHistorise} entierement materialise.
 */
@Component
class JpaHistoriquePlansAdapter implements HistoriquePlansPort {

    private final PlanHistoriqueJpaRepository repository;

    JpaHistoriquePlansAdapter(PlanHistoriqueJpaRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository");
    }

    @Override
    @Transactional
    public PlanSemaineHistorise enregistrer(PlanSemaine plan, Prix prixEstime) {
        PlanSemaineHistorise historise = new PlanSemaineHistorise(HistoriquePlanId.nouveau(), Instant.now(), plan, prixEstime);
        repository.save(PlanHistoriqueEntityMapper.versEntite(historise));
        return historise;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanSemaineHistorise> lister() {
        return repository.findAllByOrderByDateCompositionDesc().stream()
                .map(PlanHistoriqueEntityMapper::versDomaine)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlanSemaineHistorise> parId(HistoriquePlanId id) {
        return repository.findById(id.valeur())
                .map(PlanHistoriqueEntityMapper::versDomaine);
    }
}
