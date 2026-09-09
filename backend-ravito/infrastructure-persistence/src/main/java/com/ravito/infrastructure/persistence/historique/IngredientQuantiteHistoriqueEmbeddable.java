package com.ravito.infrastructure.persistence.historique;

import com.ravito.domain.courses.RayonMagasin;
import com.ravito.domain.courses.UniteMesure;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.math.BigDecimal;

/**
 * Contrepartie JPA d'un {@code IngredientQuantite} figé dans un instantané
 * historisé — meme forme que {@code IngredientQuantiteEmbeddable} du
 * module {@code catalogue}, volontairement dupliquee plutot que partagee :
 * historique et catalogue sont deux contextes distincts (l'un lit le
 * catalogue courant, l'autre fige un etat passe qui ne doit plus jamais
 * changer), les coupler par une classe JPA commune creerait une dependance
 * qui n'a pas lieu d'etre.
 */
@Embeddable
public class IngredientQuantiteHistoriqueEmbeddable {

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

    protected IngredientQuantiteHistoriqueEmbeddable() {
    }

    IngredientQuantiteHistoriqueEmbeddable(String ingredientNom, RayonMagasin rayon, BigDecimal quantiteValeur, UniteMesure unite) {
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
