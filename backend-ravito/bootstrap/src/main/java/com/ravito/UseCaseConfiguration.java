package com.ravito;

import com.ravito.application.catalogue.ProposerRepasApplicationService;
import com.ravito.application.planification.ComposerPlanSemaineApplicationService;
import com.ravito.application.prix.EstimerCoutApplicationService;
import com.ravito.domain.catalogue.CatalogueRepasPort;
import com.ravito.domain.catalogue.ProposerRepasUseCase;
import com.ravito.domain.planification.ComposerPlanSemaineUseCase;
import com.ravito.domain.prix.EstimationPrixPort;
import com.ravito.domain.prix.EstimerCoutUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cablage explicite des ports d'entree sur leurs implementations.
 *
 * <p>Le module {@code application} n'a volontairement aucune annotation
 * Spring (@Service, @Component...) — c'est ici, et seulement ici, qu'on
 * decide quelle implementation sert chaque use case. Les ports de sortie
 * ({@link CatalogueRepasPort}, {@link EstimationPrixPort}) sont eux
 * injectes automatiquement : leurs implementations JPA, dans
 * {@code infrastructure-persistence}, portent @Component et sont trouvees
 * par le component scan de {@link RavitoApplication}.
 */
@Configuration
class UseCaseConfiguration {

    @Bean
    ProposerRepasUseCase proposerRepasUseCase(CatalogueRepasPort catalogueRepasPort) {
        return new ProposerRepasApplicationService(catalogueRepasPort);
    }

    @Bean
    ComposerPlanSemaineUseCase composerPlanSemaineUseCase(CatalogueRepasPort catalogueRepasPort) {
        return new ComposerPlanSemaineApplicationService(catalogueRepasPort);
    }

    @Bean
    EstimerCoutUseCase estimerCoutUseCase(EstimationPrixPort estimationPrixPort) {
        return new EstimerCoutApplicationService(estimationPrixPort);
    }
}
