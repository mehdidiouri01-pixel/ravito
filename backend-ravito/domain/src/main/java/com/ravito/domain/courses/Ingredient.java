package com.ravito.domain.courses;

import java.util.Objects;

/**
 * Ingredient generique, identifie par son nom normalise (minuscules, sans
 * espaces superflus) afin que "Riz" et " riz" designent le meme ingredient
 * lors de la consolidation de la liste de courses.
 */
public record Ingredient(String nom, RayonMagasin rayon) {

    public Ingredient {
        Objects.requireNonNull(nom, "nom");
        Objects.requireNonNull(rayon, "rayon");
        nom = nom.trim().toLowerCase();
        if (nom.isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'ingredient ne peut pas etre vide");
        }
    }
}
