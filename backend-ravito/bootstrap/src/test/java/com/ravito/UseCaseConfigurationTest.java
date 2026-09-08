package com.ravito;

import com.ravito.application.catalogue.GenererRepasApplicationService;
import com.ravito.application.catalogue.ProposerRepasApplicationService;
import com.ravito.application.planification.ComposerPlanSemaineApplicationService;
import com.ravito.application.prix.EstimerCoutApplicationService;
import com.ravito.domain.catalogue.CatalogueRepasPort;
import com.ravito.domain.prix.EstimationPrixPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifie le cablage lui-meme, sans demarrer de contexte Spring : chaque
 * @Bean doit renvoyer l'implementation attendue du use case, construite
 * avec le port qu'on lui passe.
 */
class UseCaseConfigurationTest {

    private final UseCaseConfiguration configuration = new UseCaseConfiguration();

    @Test
    void cable_proposerRepasUseCase_sur_le_catalogue() {
        CatalogueRepasPort port = Mockito.mock(CatalogueRepasPort.class);

        assertThat(configuration.proposerRepasUseCase(port))
                .isInstanceOf(ProposerRepasApplicationService.class);
    }

    @Test
    void cable_composerPlanSemaineUseCase_sur_le_catalogue() {
        CatalogueRepasPort port = Mockito.mock(CatalogueRepasPort.class);

        assertThat(configuration.composerPlanSemaineUseCase(port))
                .isInstanceOf(ComposerPlanSemaineApplicationService.class);
    }

    @Test
    void cable_estimerCoutUseCase_sur_l_estimation_de_prix() {
        EstimationPrixPort port = Mockito.mock(EstimationPrixPort.class);

        assertThat(configuration.estimerCoutUseCase(port))
                .isInstanceOf(EstimerCoutApplicationService.class);
    }

    @Test
    void cable_genererRepasUseCase_sur_le_catalogue() {
        CatalogueRepasPort port = Mockito.mock(CatalogueRepasPort.class);

        assertThat(configuration.genererRepasUseCase(port))
                .isInstanceOf(GenererRepasApplicationService.class);
    }
}
