package com.ravito.infrastructure.persistence.catalogue;

import com.ravito.domain.courses.RayonMagasin;
import com.ravito.domain.courses.UniteMesure;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.math.BigDecimal;

/**
 * Contrepartie JPA de {@code IngredientQuantite} (domaine) : une ligne de la
 * table de collection {@code repas_ingredient}. Denormalisee (le nom et le
 * rayon de l'ingredient sont repetes sur chaque ligne) — voir la Javadoc de
 * {@link RepasEntity} pour la justification.
 */
@Embeddable
public class IngredientQuantiteEmbeddable {

    @Column(name = "ingredient_nom", nullable = false)
    private String ingredientNom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RayonMagasin rayon;

    @Column(name = "quantite_valeur", nullable = false, precision = 12, scale = 3)
    private BigDecimal quantiteValeur;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UniteMesure unite;

    protected IngredientQuantiteEmbeddable() {
    }

    IngredientQuantiteEmbeddable(String ingredientNom, RayonMagasin rayon, BigDecimal quantiteValeur, UniteMesure unite) {
        this.ingredientNom = ingredientNom;
        this.rayon = rayon;
        this.quantiteValeur = quantiteValeur;
        this.unite = unite;
    }

    public String getIngredientNom() {
        return ingredientNom;
    }

    public RayonMagasin getRayon() {
        return rayon;
    }

    public BigDecimal getQuantiteValeur() {
        return quantiteValeur;
    }

    public UniteMesure getUnite() {
        return unite;
    }
}
