package com.ravito.infrastructure.web.catalogue;

import com.ravito.domain.profil.Enseigne;
import com.ravito.domain.profil.NiveauCuisine;
import com.ravito.domain.profil.ProfilUtilisateur;
import com.ravito.domain.profil.StyleAlimentaire;
import jakarta.validation.constraints.NotNull;

/**
 * Corps JSON attendu pour decrire un profil utilisateur.
 *
 * <p>Reutilise directement les enums du domaine (inertes, sans dependance
 * framework) plutot que d'en dupliquer une copie web — seule
 * {@link ProfilUtilisateur} elle-meme, un agregat avec ses propres
 * invariants, reste construite via {@link #versDomaine()} plutot
 * qu'exposee telle quelle.
 */
public record ProfilRequest(
        @NotNull Enseigne enseigne,
        @NotNull StyleAlimentaire style,
        @NotNull NiveauCuisine niveau) {

    public ProfilUtilisateur versDomaine() {
        return new ProfilUtilisateur(enseigne, style, niveau);
    }
}
