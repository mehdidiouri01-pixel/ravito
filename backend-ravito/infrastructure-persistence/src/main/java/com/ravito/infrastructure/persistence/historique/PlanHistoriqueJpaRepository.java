package com.ravito.infrastructure.persistence.historique;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface PlanHistoriqueJpaRepository extends JpaRepository<PlanHistoriqueEntity, UUID> {

    List<PlanHistoriqueEntity> findAllByOrderByDateCompositionDesc();
}
