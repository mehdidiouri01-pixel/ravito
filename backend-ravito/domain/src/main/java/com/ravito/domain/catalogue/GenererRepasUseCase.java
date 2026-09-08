package com.ravito.domain.catalogue;

import com.ravito.domain.profil.ProfilUtilisateur;

/**
 * Port d'entree : genere, a la demande, un repas inedit pour un profil et un
 * {@link TypeRepas} donnes, en composant a partir des ingredients deja
 * presents dans le catalogue (voir {@link CatalogueRepasPort}) — pas d'appel
 * a un service externe, pas de nouvel ingredient invente.
 *
 * <p>Ephemere par conception : le {@link Repas} renvoye n'est pas ecrit dans
 * le catalogue. Un identifiant genere (voir {@code RepasId#nouveau}) suffit
 * pour l'afficher et le proposer au choix de l'utilisateur ; s'il est
 * effectivement choisi, c'est {@code ComposerPlanSemaineUseCase} qui devra
 * l'accepter directement (il ne sera pas retrouvable via
 * {@link CatalogueRepasPort#parId}).
 */
public interface GenererRepasUseCase {

    Repas genererRepas(ProfilUtilisateur profil, TypeRepas type);
}
