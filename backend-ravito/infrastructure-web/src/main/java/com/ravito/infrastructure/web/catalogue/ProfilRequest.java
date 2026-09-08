package com.ravito.infrastructure.web.catalogue;

import com.ravito.domain.profil.Enseigne;
import com.ravito.domain.profil.NiveauCuisine;
import com.ravito.domain.profil.NombreDePersonnes;
import com.ravito.domain.profil.ProfilUtilisateur;
import com.ravito.domain.profil.StyleAlimentaire;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Corps JSON attendu pour decrire un profil utilisateur.
 *
 * <p>Reutilise directement les enums du domaine (inertes, sans dependance
 * framework) plutot que d'en dupliquer une copie web — seuls
 * {@link ProfilUtilisateur} et {@link NombreDePersonnes}, qui portent leurs
 * propres invariants, restent construits via {@link #versDomaine()} plutot
 * qu'exposes tels quels.
 *
 * <p>Les bornes de {@code nombreDePersonnes} sont declarees ici en Bean
 * Validation pour renvoyer un 400 lisible plutot qu'une 500 : le domaine
 * les fait de toute facon respecter de son cote, cette validation-ci n'est
 * qu'une traduction du meme invariant a la frontiere HTTP.
 */
public record ProfilRequest(
        @NotNull Enseigne enseigne,
        @NotNull StyleAlimentaire style,
        @NotNull NiveauCuisine niveau,
        @NotNull
        @Min(NombreDePersonnes.MINIMUM)
        @Max(NombreDePersonnes.MAXIMUM)
        Integer nombreDePersonnes) {

    public ProfilUtilisateur versDomaine() {
        return new ProfilUtilisateur(enseigne, style, niveau, new NombreDePersonnes(nombreDePersonnes));
    }
}
