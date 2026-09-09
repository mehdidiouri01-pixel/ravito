package com.ravito.domain.catalogue;

import com.ravito.domain.courses.IngredientQuantite;
import com.ravito.domain.profil.NiveauCuisine;
import com.ravito.domain.profil.ProfilUtilisateur;
import com.ravito.domain.profil.StyleAlimentaire;

import java.util.List;
import java.util.Objects;

/**
 * Un repas du catalogue (petit-dejeuner, dejeuner ou diner), propose a
 * l'utilisateur s'il est compatible avec son profil (voir
 * {@link #estCompatibleAvec}).
 *
 * <p>La compatibilite de niveau est hierarchique : un repas {@code DEBUTANT}
 * convient a un profil {@code CONFIRME}, mais un repas {@code CONFIRME} ne
 * convient qu'a un profil {@code CONFIRME} (voir
 * {@link NiveauCuisine#estAuMoinsAussiConfirmeQue}).
 *
 * <p>{@code etapesPreparation} est une liste ordonnee de lignes de texte
 * (une par etape) plutot qu'un seul bloc — c'est deja la forme attendue par
 * l'affichage (une liste numerotee), pas la peine de reparser un texte plus
 * tard pour la retrouver.
 */
public record Repas(
        RepasId id,
        String nom,
        TypeRepas type,
        StyleAlimentaire style,
        NiveauCuisine niveauRequis,
        List<IngredientQuantite> ingredients,
        List<String> etapesPreparation) {

    public Repas {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(style, "style");
        Objects.requireNonNull(niveauRequis, "niveauRequis");
        Objects.requireNonNull(ingredients, "ingredients");
        Objects.requireNonNull(etapesPreparation, "etapesPreparation");
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom du repas ne peut pas etre vide");
        }
        if (ingredients.isEmpty()) {
            throw new IllegalArgumentException("Un repas doit comporter au moins un ingredient");
        }
        if (etapesPreparation.isEmpty()) {
            throw new IllegalArgumentException("Un repas doit comporter au moins une etape de preparation");
        }
        ingredients = List.copyOf(ingredients);
        etapesPreparation = List.copyOf(etapesPreparation);
    }

    /**
     * @return {@code true} si ce repas peut etre propose au profil donne :
     * meme style alimentaire, et niveau de cuisine du profil au moins aussi
     * eleve que le niveau requis par le repas.
     */
    public boolean estCompatibleAvec(ProfilUtilisateur profil) {
        Objects.requireNonNull(profil, "profil");
        return this.style == profil.style()
                && profil.niveau().estAuMoinsAussiConfirmeQue(this.niveauRequis);
    }
}
