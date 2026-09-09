package com.ravito.infrastructure.web.planification;

import com.ravito.domain.courses.ListeCourses;
import com.ravito.domain.historique.HistoriserPlanUseCase;
import com.ravito.domain.planification.ComposerPlanSemaineUseCase;
import com.ravito.domain.planification.PlanSemaine;
import com.ravito.domain.prix.EstimerCoutUseCase;
import com.ravito.domain.prix.Prix;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * Adapter d'entree REST qui enchaine trois use cases : composer le plan,
 * estimer son cout, puis l'historiser. Ce n'est pas de la logique metier —
 * juste l'ordre dans lequel appeler les ports pour repondre a une seule
 * requete HTTP ; la meme distinction domain/application que partout
 * ailleurs dans ce projet, appliquee ici a la frontiere web plutot qu'a un
 * service applicatif.
 *
 * <p>L'historisation est systematique, pas optionnelle : c'est le point
 * souleve par la daily du 09/09 ("aujourd'hui... calcule le plan et le
 * renvoie sans le stocker") — chaque plan compose est desormais retrouvable
 * plus tard via {@code ConsulterHistoriqueController}, sans action
 * supplementaire de l'appelant.
 */
@RestController
@RequestMapping("/api/plans-semaine")
class ComposerPlanSemaineController {

    private final ComposerPlanSemaineUseCase composerPlanSemaineUseCase;
    private final EstimerCoutUseCase estimerCoutUseCase;
    private final HistoriserPlanUseCase historiserPlanUseCase;

    ComposerPlanSemaineController(ComposerPlanSemaineUseCase composerPlanSemaineUseCase,
                                   EstimerCoutUseCase estimerCoutUseCase,
                                   HistoriserPlanUseCase historiserPlanUseCase) {
        this.composerPlanSemaineUseCase = Objects.requireNonNull(composerPlanSemaineUseCase, "composerPlanSemaineUseCase");
        this.estimerCoutUseCase = Objects.requireNonNull(estimerCoutUseCase, "estimerCoutUseCase");
        this.historiserPlanUseCase = Objects.requireNonNull(historiserPlanUseCase, "historiserPlanUseCase");
    }

    @PostMapping
    PlanSemaineResponse composer(@Valid @RequestBody ComposerPlanSemaineRequest requete) {
        PlanSemaine plan = composerPlanSemaineUseCase.composer(requete.profil().versDomaine(), requete.choixVersDomaine());
        ListeCourses listeCourses = plan.genererListeCourses();
        Prix prixEstime = estimerCoutUseCase.estimer(listeCourses, plan.profil().enseigne());
        historiserPlanUseCase.historiser(plan, prixEstime);

        return PlanSemaineResponse.depuis(plan, listeCourses, prixEstime);
    }
}
