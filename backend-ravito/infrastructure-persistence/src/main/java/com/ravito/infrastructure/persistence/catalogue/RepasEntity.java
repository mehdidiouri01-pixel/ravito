package com.ravito.infrastructure.persistence.catalogue;

import com.ravito.domain.catalogue.TypeRepas;
import com.ravito.domain.profil.NiveauCuisine;
import com.ravito.domain.profil.StyleAlimentaire;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entite JPA du repas, distincte du {@code Repas} du domaine.
 *
 * <p>Necessaire, pas seulement "propre" : {@code Repas} est un {@code record}
 * immuable, or JPA a besoin d'un constructeur sans argument et de champs
 * mutables pour instancier ses entites par reflexion (et les proxifier pour
 * le lazy-loading). Les deux mondes ne peuvent pas etre la meme classe — la
 * conversion se fait dans {@link RepasEntityMapper}, seule classe qui
 * connaisse les deux.
 *
 * <p>Pas de table {@code ingredient} separee : les lignes d'ingredients sont
 * stockees denormalisees dans la table de collection {@code repas_ingredient}
 * (voir {@link IngredientQuantiteEmbeddable}). Normaliser aurait du sens si
 * on avait besoin d'interroger "tous les repas utilisant du riz" ou de
 * corriger le nom d'un ingredient partout d'un coup — pas le cas pour l'instant.
 */
@Entity
@Table(name = "repas")
public class RepasEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String nom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TypeRepas type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StyleAlimentaire style;

    @Enumerated(EnumType.STRING)
    @Column(name = "niveau_requis", nullable = false, length = 20)
    private NiveauCuisine niveauRequis;

    @ElementCollection
    @CollectionTable(name = "repas_ingredient", joinColumns = @JoinColumn(name = "repas_id"))
    private List<IngredientQuantiteEmbeddable> ingredients = new ArrayList<>();

    /** Constructeur exige par JPA — ne pas utiliser directement, voir {@link RepasEntityMapper}. */
    protected RepasEntity() {
    }

    RepasEntity(UUID id, String nom, TypeRepas type, StyleAlimentaire style, NiveauCuisine niveauRequis,
                List<IngredientQuantiteEmbeddable> ingredients) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.style = style;
        this.niveauRequis = niveauRequis;
        this.ingredients = new ArrayList<>(ingredients);
    }

    public UUID getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public TypeRepas getType() {
        return type;
    }

    public StyleAlimentaire getStyle() {
        return style;
    }

    public NiveauCuisine getNiveauRequis() {
        return niveauRequis;
    }

    public List<IngredientQuantiteEmbeddable> getIngredients() {
        return ingredients;
    }
}
