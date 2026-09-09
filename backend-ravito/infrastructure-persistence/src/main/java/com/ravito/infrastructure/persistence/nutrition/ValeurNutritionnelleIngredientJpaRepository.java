package com.ravito.infrastructure.persistence.nutrition;

import com.ravito.domain.courses.UniteMesure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface ValeurNutritionnelleIngredientJpaRepository extends JpaRepository<ValeurNutritionnelleIngredientEntity, Long> {

    Optional<ValeurNutritionnelleIngredientEntity> findByIngredientNomAndUnite(String ingredientNom, UniteMesure unite);
}
