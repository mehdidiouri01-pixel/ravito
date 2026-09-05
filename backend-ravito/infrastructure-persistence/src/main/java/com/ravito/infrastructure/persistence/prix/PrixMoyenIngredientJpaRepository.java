package com.ravito.infrastructure.persistence.prix;

import com.ravito.domain.courses.UniteMesure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface PrixMoyenIngredientJpaRepository extends JpaRepository<PrixMoyenIngredientEntity, Long> {

    Optional<PrixMoyenIngredientEntity> findByIngredientNomAndUnite(String ingredientNom, UniteMesure unite);
}
