package com.mealweek.domain.catalogue;

/**
 * Levee lorsque le catalogue ne propose pas assez de repas d'un
 * {@link TypeRepas} donne, compatibles avec le profil, pour permettre a
 * l'utilisateur de choisir un repas par jour de la semaine.
 */
public class CatalogueInsuffisantException extends RuntimeException {

    public CatalogueInsuffisantException(TypeRepas type, int nombreDisponible, int minimumRequis) {
        super("Seulement " + nombreDisponible + " repas de type " + type
                + " compatibles avec le profil (" + minimumRequis + " minimum requis)");
    }
}
