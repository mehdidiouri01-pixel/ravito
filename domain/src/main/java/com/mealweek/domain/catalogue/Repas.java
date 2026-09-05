package com.mealweek.domain.catalogue;

import com.mealweek.domain.courses.IngredientQuantite;
import com.mealweek.domain.profil.NiveauCuisine;
import com.mealweek.domain.profil.ProfilUtilisateur;
import com.mealweek.domain.profil.StyleAlimentaire;

import java.util.List;
import java.util.Objects;

/**
 * Un repas du catalogue (petit-dejeuner ou dejeuner), propose a l'utilisateur
 * s'il est compatible avec son profil (voir {@link #estCompatibleAvec}).
 *
 * <p>La compatibilite de niveau est hierarchique : un repas {@code DEBUTANT}
 * convient a un profil {@code CONFIRME}, mais un repas {@code CONFIRME} ne
 * convient qu'a un profil {@code CONFIRME} (voir
 * {@link NiveauCuisine#estAuMoinsAussiConfirmeQue}).
 */
public record Repas(
        RepasId id,
        String nom,
        TypeRepas type,
        StyleAlimentaire style,
        NiveauCuisine niveauRequis,
        List<IngredientQuantite> ingredients) {

    public Repas {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(style, "style");
        Objects.requireNonNull(niveauRequis, "niveauRequis");
        Objects.requireNonNull(ingredients, "ingredients");
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom du repas ne peut pas etre vide");
        }
        if (ingredients.isEmpty()) {
            throw new IllegalArgumentException("Un repas doit comporter au moins un ingredient");
        }
        ingredients = List.copyOf(ingredients);
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
