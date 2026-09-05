package com.ravito.infrastructure.web.catalogue;

import com.ravito.domain.catalogue.ProposerRepasUseCase;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * Adapter d'entree REST pour {@link ProposerRepasUseCase}.
 *
 * <p>Ne depend que de l'interface du use case, jamais de son
 * implementation ({@code ProposerRepasApplicationService}, module
 * {@code application}) — Spring l'injecte au demarrage, cable dans le
 * futur module {@code bootstrap}.
 */
@RestController
@RequestMapping("/api/repas")
class ProposerRepasController {

    private final ProposerRepasUseCase proposerRepasUseCase;

    ProposerRepasController(ProposerRepasUseCase proposerRepasUseCase) {
        this.proposerRepasUseCase = Objects.requireNonNull(proposerRepasUseCase, "proposerRepasUseCase");
    }

    @PostMapping("/propositions")
    RepasProposesResponse proposer(@Valid @RequestBody ProfilRequest requete) {
        return RepasProposesResponse.depuis(proposerRepasUseCase.proposer(requete.versDomaine()));
    }
}
