package com.ravito.infrastructure.persistence.historique;

import com.ravito.domain.profil.Enseigne;
import com.ravito.domain.profil.NiveauCuisine;
import com.ravito.domain.profil.StyleAlimentaire;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entite JPA d'un {@code PlanSemaineHistorise} (domaine) — l'"en-tete" du
 * plan (profil, date, prix), les 15 repas de la semaine vivant dans
 * {@link RepasHistoriqueEntity} (voir {@link #ajouterRepas}).
 *
 * <p>Pas de notion d'utilisateur : cette table est globale, comme le reste
 * de l'application (voir la Javadoc de {@code ConsulterHistoriqueUseCase}).
 */
@Entity
@Table(name = "plan_historique")
public class PlanHistoriqueEntity {

    @Id
    private UUID id;

    @Column(name = "date_composition", nullable = false)
    private Instant dateComposition;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Enseigne enseigne;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StyleAlimentaire style;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NiveauCuisine niveau;

    @Column(name = "nombre_de_personnes", nullable = false)
    private int nombreDePersonnes;

    @Column(name = "prix_estime", nullable = false, precision = 10, scale = 2)
    private BigDecimal prixEstime;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RepasHistoriqueEntity> repas = new ArrayList<>();

    /** Constructeur exige par JPA — ne pas utiliser directement, voir {@link PlanHistoriqueEntityMapper}. */
    protected PlanHistoriqueEntity() {
    }

    PlanHistoriqueEntity(UUID id, Instant dateComposition, Enseigne enseigne, StyleAlimentaire style,
                          NiveauCuisine niveau, int nombreDePersonnes, BigDecimal prixEstime) {
        this.id = id;
        this.dateComposition = dateComposition;
        this.enseigne = enseigne;
        this.style = style;
        this.niveau = niveau;
        this.nombreDePersonnes = nombreDePersonnes;
        this.prixEstime = prixEstime;
    }

    void ajouterRepas(RepasHistoriqueEntity repasEntity) {
        repas.add(repasEntity);
        repasEntity.setPlan(this);
    }

    public UUID getId() {
        return id;
    }

    public Instant getDateComposition() {
        return dateComposition;
    }

    public Enseigne getEnseigne() {
        return enseigne;
    }

    public StyleAlimentaire getStyle() {
        return style;
    }

    public NiveauCuisine getNiveau() {
        return niveau;
    }

    public int getNombreDePersonnes() {
        return nombreDePersonnes;
    }

    public BigDecimal getPrixEstime() {
        return prixEstime;
    }

    public List<RepasHistoriqueEntity> getRepas() {
        return repas;
    }
}
