package com.ravito.infrastructure.persistence.catalogue;

import com.ravito.domain.catalogue.TypeRepas;
import com.ravito.domain.profil.StyleAlimentaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface RepasJpaRepository extends JpaRepository<RepasEntity, UUID> {

    List<RepasEntity> findByTypeAndStyle(TypeRepas type, StyleAlimentaire style);
}
