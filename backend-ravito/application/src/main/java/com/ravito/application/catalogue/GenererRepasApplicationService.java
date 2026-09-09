package com.ravito.application.catalogue;

import com.ravito.domain.catalogue.GenererRepasUseCase;
import com.ravito.domain.catalogue.GenerationRepasImpossibleException;
import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.RepasId;
import com.ravito.domain.catalogue.TypeRepas;
import com.ravito.domain.courses.IngredientQuantite;
import com.ravito.domain.profil.ProfilUtilisateur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Implementation du port d'entree {@link GenererRepasUseCase}.
 *
 * <p>Ne fait appel a aucun service externe : elle compose un repas inedit en
 * piochant au hasard, via {@link PoolIngredientsCatalogue}, parmi les
 * ingredients deja utilises dans le catalogue pour ce {@link TypeRepas}/style,
 * de quoi construire une nouvelle combinaison. Le nom est tire parmi
 * quelques gabarits — pas de pretention "creative", juste assez pour
 * distinguer une idee generee d'une autre.
 *
 * <p>Le niveau de cuisine requis du repas genere est toujours celui du
 * profil demandeur : il est donc compatible par construction (voir
 * {@link Repas#estCompatibleAvec}), pas besoin de le revalider.
 */
public class GenererRepasApplicationService implements GenererRepasUseCase {

    private static final int NOMBRE_INGREDIENTS = 2;

    private static final List<String> GABARITS_NOM = List.of(
            "%s et %s maison",
            "Poêlée de %s et %s",
            "%s et %s à ma façon",
            "Mijoté de %s et %s",
            "%s et %s comme à la maison",
            "Assiette de %s et %s");

    private final PoolIngredientsCatalogue poolIngredientsCatalogue;
    private final Random random;

    public GenererRepasApplicationService(PoolIngredientsCatalogue poolIngredientsCatalogue, Random random) {
        this.poolIngredientsCatalogue = Objects.requireNonNull(poolIngredientsCatalogue, "poolIngredientsCatalogue");
        this.random = Objects.requireNonNull(random, "random");
    }

    @Override
    public Repas genererRepas(ProfilUtilisateur profil, TypeRepas type) {
        Objects.requireNonNull(profil, "profil");
        Objects.requireNonNull(type, "type");

        List<IngredientQuantite> pool = poolIngredientsCatalogue.ingredients(type, profil.style());
        if (pool.size() < NOMBRE_INGREDIENTS) {
            throw new GenerationRepasImpossibleException(type, profil.style());
        }

        List<IngredientQuantite> tirage = tirer(pool, NOMBRE_INGREDIENTS);
        String nom = nommer(tirage);

        return new Repas(RepasId.nouveau(), nom, type, profil.style(), profil.niveau(), tirage);
    }

    private List<IngredientQuantite> tirer(List<IngredientQuantite> pool, int nombre) {
        List<IngredientQuantite> melange = new ArrayList<>(pool);
        Collections.shuffle(melange, random);
        return melange.subList(0, nombre);
    }

    private String nommer(List<IngredientQuantite> ingredients) {
        String gabarit = GABARITS_NOM.get(random.nextInt(GABARITS_NOM.size()));
        Object[] noms = ingredients.stream().map(i -> i.ingredient().nom()).toArray();
        String nom = gabarit.formatted(noms);
        return Character.toUpperCase(nom.charAt(0)) + nom.substring(1);
    }
}
