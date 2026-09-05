package com.ravito.infrastructure.persistence.prix;

import com.ravito.domain.courses.UniteMesure;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;

/**
 * Catalogue de prix moyens par (ingredient, unite) : la strategie v1 pour
 * {@code EstimationPrixPort}, en attendant un futur adapter branche sur
 * l'API Open Prices d'Open Food Facts (le port ne change pas, seule cette
 * classe et {@link JpaEstimationPrixAdapter} seraient remplaces).
 */
@Entity
@Table(name = "prix_moyen_ingredient",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ingredient_nom", "unite"}))
public class PrixMoyenIngredientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ingredient_nom", nullable = false)
    private String ingredientNom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UniteMesure unite;

    @Column(name = "prix_unitaire", nullable = false, precision = 10, scale = 4)
    private BigDecimal prixUnitaire;

    protected PrixMoyenIngredientEntity() {
    }

    PrixMoyenIngredientEntity(String ingredientNom, UniteMesure unite, BigDecimal prixUnitaire) {
        this.ingredientNom = ingredientNom;
        this.unite = unite;
        this.prixUnitaire = prixUnitaire;
    }

    public Long getId() {
        return id;
    }

    public String getIngredientNom() {
        return ingredientNom;
    }

    public UniteMesure getUnite() {
        return unite;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }
}
