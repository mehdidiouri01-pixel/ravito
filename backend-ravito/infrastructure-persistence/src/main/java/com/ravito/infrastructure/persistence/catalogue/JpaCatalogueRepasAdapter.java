package com.ravito.infrastructure.persistence.catalogue;

import com.ravito.domain.catalogue.CatalogueRepasPort;
import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.RepasId;
import com.ravito.domain.catalogue.TypeRepas;
import com.ravito.domain.profil.StyleAlimentaire;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
 *
 * <p>{@code @Transactional} est necessaire ici, pas optionnel : {@code
 * RepasEntity.ingredients} est une collection chargee en lazy (comportement
 * par defaut d'{@code @ElementCollection}), et {@code spring.jpa.open-in-view}
 * est desactive (a raison — le garder ouvert jusqu'a la vue est un anti-
 * pattern qui masque ce genre de probleme). Sans transaction ici, la session
 * Hibernate se ferme des la sortie de {@code repository.findBy...}, avant que
 * {@link RepasEntityMapper} n'ait pu lire la collection — chaque methode
 * publique de cet adapter est donc responsable de renvoyer un {@link Repas}
 * entierement materialise, jamais un objet qui exigerait encore une session
 * active pour etre lu par son appelant (qui n'en a et ne doit en avoir aucune
 * idee).
 */
@Component
class JpaCatalogueRepasAdapter implements CatalogueRepasPort {

    private final RepasJpaRepository repository;

    JpaCatalogueRepasAdapter(RepasJpaRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository");
    }

    @Override
    @Transactional(readOnly = true)
    public List<Repas> rechercherParTypeEtStyle(TypeRepas type, StyleAlimentaire style) {
        return repository.findByTypeAndStyle(type, style).stream()
                .map(RepasEntityMapper::versDomaine)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Repas> parId(RepasId id) {
        return repository.findById(id.valeur())
                .map(RepasEntityMapper::versDomaine);
    }
}
