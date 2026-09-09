package com.ravito.infrastructure.persistence.nutrition;

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
 * Catalogue de valeurs nutritionnelles moyennes par (ingredient, unite) : la
 * strategie v1 pour {@code EstimationNutritionPort}, exactement comme
 * {@code PrixMoyenIngredientEntity} pour le prix — memes cles, meme forme,
 * une table separee plutot qu'un elargissement de celle du prix puisque ce
 * sont deux catalogues de reference distincts (rien n'oblige les deux a
 * couvrir exactement les memes lignes a l'avenir).
 */
@Entity
@Table(name = "valeur_nutritionnelle_ingredient",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ingredient_nom", "unite"}))
public class ValeurNutritionnelleIngredientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ingredient_nom", nullable = false)
    private String ingredientNom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UniteMesure unite;

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal calories;

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal proteines;

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal glucides;

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal lipides;

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal fibres;

    protected ValeurNutritionnelleIngredientEntity() {
    }

    ValeurNutritionnelleIngredientEntity(String ingredientNom, UniteMesure unite, BigDecimal calories,
                                          BigDecimal proteines, BigDecimal glucides, BigDecimal lipides, BigDecimal fibres) {
        this.ingredientNom = ingredientNom;
        this.unite = unite;
        this.calories = calories;
        this.proteines = proteines;
        this.glucides = glucides;
        this.lipides = lipides;
        this.fibres = fibres;
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

    public BigDecimal getCalories() {
        return calories;
    }

    public BigDecimal getProteines() {
        return proteines;
    }

    public BigDecimal getGlucides() {
        return glucides;
    }

    public BigDecimal getLipides() {
        return lipides;
    }

    public BigDecimal getFibres() {
        return fibres;
    }
}
