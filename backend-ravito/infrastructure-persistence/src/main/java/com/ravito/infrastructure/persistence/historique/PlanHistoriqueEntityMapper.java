package com.ravito.infrastructure.persistence.historique;

import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.RepasId;
import com.ravito.domain.catalogue.TypeRepas;
import com.ravito.domain.courses.Ingredient;
import com.ravito.domain.courses.IngredientQuantite;
import com.ravito.domain.courses.Quantite;
import com.ravito.domain.historique.HistoriquePlanId;
import com.ravito.domain.historique.PlanSemaineHistorise;
import com.ravito.domain.planification.Jour;
import com.ravito.domain.planification.JourSemaine;
import com.ravito.domain.planification.PlanSemaine;
import com.ravito.domain.profil.NombreDePersonnes;
import com.ravito.domain.profil.ProfilUtilisateur;
import com.ravito.domain.prix.Prix;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Seule classe qui connaisse a la fois {@code PlanSemaineHistorise}
 * (domaine) et les entites JPA de ce package.
 */
final class PlanHistoriqueEntityMapper {

    private PlanHistoriqueEntityMapper() {
    }

    static PlanHistoriqueEntity versEntite(PlanSemaineHistorise historise) {
        PlanSemaine plan = historise.plan();
        ProfilUtilisateur profil = plan.profil();

        PlanHistoriqueEntity entity = new PlanHistoriqueEntity(
                historise.id().valeur(), historise.dateComposition(), profil.enseigne(), profil.style(),
                profil.niveau(), profil.nombreDePersonnes().valeur(), historise.prixEstime().montant());

        for (Jour jour : plan.jours()) {
            jour.petitDejeuner().ifPresent(repas -> entity.ajouterRepas(versRepasEntite(jour.jourSemaine(), repas)));
            jour.dejeuner().ifPresent(repas -> entity.ajouterRepas(versRepasEntite(jour.jourSemaine(), repas)));
            jour.diner().ifPresent(repas -> entity.ajouterRepas(versRepasEntite(jour.jourSemaine(), repas)));
        }
        return entity;
    }

    static PlanSemaineHistorise versDomaine(PlanHistoriqueEntity entity) {
        ProfilUtilisateur profil = new ProfilUtilisateur(entity.getEnseigne(), entity.getStyle(), entity.getNiveau(),
                new NombreDePersonnes(entity.getNombreDePersonnes()));

        // regroupe les lignes plates (jour, creneau) — jusqu'a 15, moins pour
        // un plan partiel — en 5 Jour, chacun jusqu'a 3 repas. Itere sur les 5
        // JourSemaine plutot que sur les clefs presentes dans les lignes : un
        // jour sans aucune ligne (aucun repas choisi ce jour-la) doit quand
        // meme redevenir un Jour a part entiere, juste avec ses 3 creneaux
        // vides (voir Jour) — PlanSemaine exige exactement les 5 jours.
        Map<JourSemaine, Map<TypeRepas, Repas>> parJourEtType = entity.getRepas().stream()
                .collect(Collectors.groupingBy(
                        RepasHistoriqueEntity::getJourSemaine,
                        Collectors.toMap(RepasHistoriqueEntity::getType, PlanHistoriqueEntityMapper::versRepasDomaine)));

        List<Jour> jours = List.of(JourSemaine.values()).stream()
                .map(jourSemaine -> {
                    Map<TypeRepas, Repas> parType = parJourEtType.getOrDefault(jourSemaine, Map.of());
                    return new Jour(jourSemaine,
                            Optional.ofNullable(parType.get(TypeRepas.PETIT_DEJEUNER)),
                            Optional.ofNullable(parType.get(TypeRepas.DEJEUNER)),
                            Optional.ofNullable(parType.get(TypeRepas.DINER)));
                })
                .toList();

        PlanSemaine plan = new PlanSemaine(profil, jours);
        return new PlanSemaineHistorise(new HistoriquePlanId(entity.getId()), entity.getDateComposition(), plan,
                new Prix(entity.getPrixEstime()));
    }

    private static RepasHistoriqueEntity versRepasEntite(JourSemaine jourSemaine, Repas repas) {
        List<IngredientQuantiteHistoriqueEmbeddable> ingredients = repas.ingredients().stream()
                .map(ingredientQuantite -> new IngredientQuantiteHistoriqueEmbeddable(
                        ingredientQuantite.ingredient().nom(),
                        ingredientQuantite.ingredient().rayon(),
                        ingredientQuantite.quantite().valeur(),
                        ingredientQuantite.quantite().unite()))
                .toList();
        return new RepasHistoriqueEntity(UUID.randomUUID(), jourSemaine, repas.nom(), repas.type(), repas.style(),
                repas.niveauRequis(), ingredients, repas.etapesPreparation());
    }

    /**
     * Reconstruit un {@code RepasId} a partir de l'id propre de la ligne
     * historisee, pas de celui du repas d'origine (jamais conserve — un
     * repas genere n'en avait de toute facon aucun qui survive ailleurs, et
     * un repas du catalogue a pu changer depuis). Purement une identite
     * technique pour ce {@code Repas} reconstruit, jamais reutilisee pour
     * retrouver quoi que ce soit via {@code CatalogueRepasPort}.
     */
    private static Repas versRepasDomaine(RepasHistoriqueEntity entity) {
        List<IngredientQuantite> ingredients = entity.getIngredients().stream()
                .map(embeddable -> new IngredientQuantite(
                        new Ingredient(embeddable.getIngredientNom(), embeddable.getRayon()),
                        new Quantite(embeddable.getQuantiteValeur(), embeddable.getUnite())))
                .toList();
        return new Repas(new RepasId(entity.getId()), entity.getNom(), entity.getType(), entity.getStyle(),
                entity.getNiveauRequis(), ingredients, entity.getEtapesPreparation());
    }
}
