package com.ravito.domain.prix;

/**
 * Levee par une implementation d'{@link EstimationPrixPort} lorsqu'elle ne
 * peut pas chiffrer une ligne de la liste de courses (ex : aucun prix moyen
 * connu pour un ingredient, dans la strategie v1).
 *
 * <p>Definie ici, au niveau du port, et pas dans l'adapter qui la leve
 * effectivement : ses appelants (couche application, controleurs web) ont
 * besoin d'un type stable pour reagir a un echec d'estimation, sans
 * dependre d'un adapter concret ni de la raison technique precise de
 * l'echec (qui differera d'une implementation a l'autre — un futur adapter
 * Open Prices pourrait echouer pour une toute autre raison).
 */
public class EstimationImpossibleException extends RuntimeException {

    public EstimationImpossibleException(String message) {
        super(message);
    }
}
