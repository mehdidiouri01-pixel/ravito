package com.ravito.application.catalogue;

import com.ravito.domain.catalogue.CatalogueRepasPort;
import com.ravito.domain.catalogue.ProposerRepasUseCase;
import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.RepasProposes;
import com.ravito.domain.catalogue.TypeRepas;
import com.ravito.domain.profil.ProfilUtilisateur;

import java.util.List;
import java.util.Objects;

/**
 * Implementation du port d'entree {@link ProposerRepasUseCase}.
 *
 * <p>Le filtrage par style est delegue au port de sortie (requete), le
 * filtrage par niveau de cuisine reste applique ici via
 * {@link Repas#estCompatibleAvec} — c'est une regle metier, portee par le
 * domaine, que cette classe se contente d'invoquer sans la reimplementer.
 * La construction de {@link RepasProposes} fait ensuite respecter elle-meme
 * le minimum de 5 repas par type.
 */
public class ProposerRepasApplicationService implements ProposerRepasUseCase {

    private final CatalogueRepasPort catalogueRepasPort;

    public ProposerRepasApplicationService(CatalogueRepasPort catalogueRepasPort) {
        this.catalogueRepasPort = Objects.requireNonNull(catalogueRepasPort, "catalogueRepasPort");
    }

    @Override
    public RepasProposes proposer(ProfilUtilisateur profil) {
        Objects.requireNonNull(profil, "profil");

        List<Repas> petitsDejeuners = repasCompatibles(TypeRepas.PETIT_DEJEUNER, profil);
        List<Repas> dejeuners = repasCompatibles(TypeRepas.DEJEUNER, profil);
        List<Repas> diners = repasCompatibles(TypeRepas.DINER, profil);

        return new RepasProposes(petitsDejeuners, dejeuners, diners);
    }

    private List<Repas> repasCompatibles(TypeRepas type, ProfilUtilisateur profil) {
        return catalogueRepasPort.rechercherParTypeEtStyle(type, profil.style()).stream()
                .filter(repas -> repas.estCompatibleAvec(profil))
                .toList();
    }
}
