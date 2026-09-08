package com.ravito.infrastructure.web.catalogue;

import com.ravito.domain.catalogue.TypeRepas;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * Corps JSON de demande de generation : le profil (meme forme que pour
 * {@code ProposerRepasController}) et le type de repas cible pour lequel
 * generer une idee.
 */
public record GenererRepasRequest(@NotNull @Valid ProfilRequest profil, @NotNull TypeRepas type) {
}
