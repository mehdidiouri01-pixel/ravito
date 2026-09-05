package com.mealweek.domain.profil;

/**
 * Style alimentaire choisi par l'utilisateur. Chaque style dispose de son
 * propre catalogue de repas (au moins 15 petits-dejeuners et 15 dejeuners) :
 * un repas HEALTHY n'est jamais propose a un profil NORMAL ou GOURMAND, et
 * inversement. Voir {@link com.mealweek.domain.catalogue.Repas#estCompatibleAvec}.
 */
public enum StyleAlimentaire {
    HEALTHY,
    NORMAL,
    GOURMAND
}
