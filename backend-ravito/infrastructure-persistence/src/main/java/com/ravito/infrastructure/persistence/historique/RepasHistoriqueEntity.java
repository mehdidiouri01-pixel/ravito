package com.ravito.infrastructure.persistence.historique;

import com.ravito.domain.catalogue.TypeRepas;
import com.ravito.domain.planification.JourSemaine;
import com.ravito.domain.profil.NiveauCuisine;
import com.ravito.domain.profil.StyleAlimentaire;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Un repas tel qu'il etait au moment ou un {@link PlanHistoriqueEntity} a
 * ete compose — nom et ingredients figes, independants du catalogue
 * courant (voir la Javadoc de {@code PlanSemaineHistorise} cote domaine).
 *
 * <p>Une ligne par (jour, creneau) : {@link #type} porte le creneau
 * (PETIT_DEJEUNER/DEJEUNER/DINER), {@link #jourSemaine} le jour — les deux
 * ensemble identifient la place de ce repas dans le plan, exactement comme
 * {@code Jour} le fait cote domaine.
 */
@Entity
@Table(name = "plan_historique_repas")
public class RepasHistoriqueEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private PlanHistoriqueEntity plan;

    @Enumerated(EnumType.STRING)
    @Column(name = "jour_semaine", nullable = false, length = 20)
    private JourSemaine jourSemaine;

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
    @CollectionTable(name = "plan_historique_repas_ingredient", joinColumns = @JoinColumn(name = "repas_id"))
    private List<IngredientQuantiteHistoriqueEmbeddable> ingredients = new ArrayList<>();

    /** Constructeur exige par JPA — ne pas utiliser directement, voir {@link PlanHistoriqueEntityMapper}. */
    protected RepasHistoriqueEntity() {
    }

    RepasHistoriqueEntity(UUID id, JourSemaine jourSemaine, String nom, TypeRepas type, StyleAlimentaire style,
                           NiveauCuisine niveauRequis, List<IngredientQuantiteHistoriqueEmbeddable> ingredients) {
        this.id = id;
        this.jourSemaine = jourSemaine;
        this.nom = nom;
        this.type = type;
        this.style = style;
        this.niveauRequis = niveauRequis;
        this.ingredients = new ArrayList<>(ingredients);
    }

    /** Cote proprietaire de la relation, mis a jour par {@link PlanHistoriqueEntity#ajouterRepas}. */
    void setPlan(PlanHistoriqueEntity plan) {
        this.plan = plan;
    }

    public UUID getId() {
        return id;
    }

    public JourSemaine getJourSemaine() {
        return jourSemaine;
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

    public List<IngredientQuantiteHistoriqueEmbeddable> getIngredients() {
        return ingredients;
    }
}
