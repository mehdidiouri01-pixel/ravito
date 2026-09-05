package com.mealweek.domain.catalogue;

import com.mealweek.domain.profil.ProfilUtilisateur;

/**
 * Port d'entree : propose, pour un profil donne, les repas du catalogue
 * compatibles avec son style et son niveau de cuisine.
 *
 * <p>L'implementation (future, module {@code application}) recupere via
 * {@link CatalogueRepasPort#rechercherParTypeEtStyle} les repas du style du
 * profil, puis applique le filtre de niveau — regle metier portee par
 * {@link Repas#estCompatibleAvec} — avant de construire un
 * {@link RepasProposes}, qui refuse lui-meme une selection incomplete.
 */
public interface ProposerRepasUseCase {

    RepasProposes proposer(ProfilUtilisateur profil);
}
