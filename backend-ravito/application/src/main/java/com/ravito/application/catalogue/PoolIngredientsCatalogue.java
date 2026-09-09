package com.ravito.application.catalogue;

import com.ravito.domain.catalogue.CatalogueRepasPort;
import com.ravito.domain.catalogue.TypeRepas;
import com.ravito.domain.courses.IngredientQuantite;
import com.ravito.domain.profil.StyleAlimentaire;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Les ingredients (couples ingredient+unite) deja utilises dans le
 * catalogue stocke pour un {@link TypeRepas} et un {@link StyleAlimentaire}
 * donnes.
 *
 * <p>Collaborateur partage par deux usages distincts qui s'appuient tous
 * les deux sur la meme notion de "catalogue connu" :
 * {@code GenererRepasApplicationService} y pioche des ingredients pour
 * composer un repas inedit, et {@code ComposerPlanSemaineApplicationService}
 * s'en sert pour revalider qu'un repas genere recu du client n'a pas ete
 * altere (voir {@code RepasGenereInvalideException}).
 */
public class PoolIngredientsCatalogue {

    private final CatalogueRepasPort catalogueRepasPort;

    public PoolIngredientsCatalogue(CatalogueRepasPort catalogueRepasPort) {
        this.catalogueRepasPort = Objects.requireNonNull(catalogueRepasPort, "catalogueRepasPort");
    }

    /**
     * @return les ingredients deja utilises dans le catalogue pour ce type et
     * ce style, dedupliques par (ingredient, unite) — un meme couple ne doit
     * apparaitre qu'une fois, sinon un tirage aleatoire pourrait le choisir
     * deux fois pour le meme repas genere.
     */
    public List<IngredientQuantite> ingredients(TypeRepas type, StyleAlimentaire style) {
        Map<String, IngredientQuantite> dedupliques = new LinkedHashMap<>();
        catalogueRepasPort.rechercherParTypeEtStyle(type, style).stream()
                .flatMap(repas -> repas.ingredients().stream())
                .forEach(ingredientQuantite -> dedupliques.putIfAbsent(cle(ingredientQuantite), ingredientQuantite));
        return List.copyOf(dedupliques.values());
    }

    /**
     * @return {@code true} si chaque ingredient de {@code ingredients}
     * appartient au catalogue connu pour ce type et ce style (meme couple
     * ingredient+unite) — utilise pour revalider un repas genere recu du
     * client avant de l'accepter.
     */
    public boolean connusPourTypeEtStyle(TypeRepas type, StyleAlimentaire style, List<IngredientQuantite> ingredients) {
        Set<String> connus = ingredients(type, style).stream().map(PoolIngredientsCatalogue::cle)
                .collect(Collectors.toSet());
        return ingredients.stream().map(PoolIngredientsCatalogue::cle).allMatch(connus::contains);
    }

    private static String cle(IngredientQuantite ingredientQuantite) {
        return ingredientQuantite.ingredient().nom() + '|' + ingredientQuantite.quantite().unite();
    }
}
