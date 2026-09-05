package com.ravito.infrastructure.persistence.catalogue;

import com.ravito.domain.catalogue.CatalogueRepasPort;
import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.RepasId;
import com.ravito.domain.catalogue.TypeRepas;
import com.ravito.domain.profil.StyleAlimentaire;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation JPA/PostgreSQL du port de sortie {@link CatalogueRepasPort}.
 *
 * <p>Volontairement fine : ne fait que traduire une requete Spring Data en
 * objets domaine via {@link RepasEntityMapper}. Aucune regle metier ici — le
 * filtrage par niveau de cuisine, par exemple, reste dans le domaine
 * (voir {@code Repas.estCompatibleAvec}, applique par la couche application).
 */
@Component
class JpaCatalogueRepasAdapter implements CatalogueRepasPort {

    private final RepasJpaRepository repository;

    JpaCatalogueRepasAdapter(RepasJpaRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository");
    }

    @Override
    public List<Repas> rechercherParTypeEtStyle(TypeRepas type, StyleAlimentaire style) {
        return repository.findByTypeAndStyle(type, style).stream()
                .map(RepasEntityMapper::versDomaine)
                .toList();
    }

    @Override
    public Optional<Repas> parId(RepasId id) {
        return repository.findById(id.valeur())
                .map(RepasEntityMapper::versDomaine);
    }
}
