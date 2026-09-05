package com.ravito.domain.catalogue;

import com.ravito.domain.profil.StyleAlimentaire;

import java.util.List;
import java.util.Optional;

/**
 * Port de sortie : acces en lecture au catalogue de repas.
 *
 * <p>Le filtrage par {@link TypeRepas} et {@link StyleAlimentaire} est une
 * simple condition de requete, laissee a l'adapter (ex: clause SQL). Le
 * filtrage par niveau de cuisine, lui, est une regle metier : il reste de la
 * responsabilite du domaine — {@link ProposerRepasUseCase} l'applique via
 * {@link Repas#estCompatibleAvec} sur le resultat de cette methode, plutot
 * que de faire confiance a l'adapter.
 */
public interface CatalogueRepasPort {

    List<Repas> rechercherParTypeEtStyle(TypeRepas type, StyleAlimentaire style);

    Optional<Repas> parId(RepasId id);
}
