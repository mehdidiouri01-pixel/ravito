import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import { PdfExportService } from '../core/pdf-export.service';
import { RavitoApiService } from '../core/ravito-api.service';
import { JOURS } from '../core/reference-data';
import type {
  ChoixJourRequest,
  ComposerPlanSemaineRequest,
  JourSemaine,
  PlanSemaineResponse,
  ProfilRequest,
  RepasProposesResponse,
} from '../core/models';
import { ProfilFormComponent } from '../features/profil-form/profil-form';
import { SelectionRepasComponent, type SelectionRepas } from '../features/selection-repas/selection-repas';
import { PlanSemaineComponent } from '../features/plan-semaine/plan-semaine';

type Etape = 'PROFIL' | 'SELECTION' | 'RESULTAT';

/**
 * Orchestre les 3 etapes du parcours (profil -> selection -> resultat) et
 * les appels a l'API. Les composants enfants sont volontairement passifs
 * (input/output) : toute la coordination et l'etat vivent ici, en signals.
 */
@Component({
  selector: 'app-planificateur',
  imports: [ProfilFormComponent, SelectionRepasComponent, PlanSemaineComponent],
  template: `
    <main class="planificateur">
      <header>
        <h1>Ravito</h1>
        <p class="sous-titre">Votre plan de repas de la semaine, en 3 étapes.</p>
      </header>

      @if (erreur()) {
        <p class="erreur" role="alert">{{ erreur() }}</p>
      }

      @if (chargement()) {
        <p class="chargement">Chargement…</p>
      }

      @switch (etape()) {
        @case ('PROFIL') {
          <app-profil-form (profilChoisi)="surProfilChoisi($event)" />
        }
        @case ('SELECTION') {
          @if (proposition(); as valeurProposition) {
            <app-selection-repas [proposition]="valeurProposition" (selectionValidee)="surSelectionValidee($event)" />
          }
        }
        @case ('RESULTAT') {
          @if (plan(); as valeurPlan) {
            <app-plan-semaine [plan]="valeurPlan" />
            <div class="actions-resultat">
              <button type="button" (click)="exporterPdf()">Exporter en PDF</button>
              <button type="button" class="recommencer" (click)="recommencer()">Recommencer</button>
            </div>
          }
        }
      }
    </main>
  `,
  styles: [
    `
      .planificateur {
        max-width: 64rem;
        margin: 0 auto;
        padding: 2rem 1.5rem 4rem;
      }

      header {
        margin-bottom: 2rem;
      }

      h1 {
        margin: 0;
        font-size: 2rem;
      }

      .sous-titre {
        color: var(--couleur-texte-att);
        margin: 0.25rem 0 0;
      }

      .erreur {
        background: var(--couleur-erreur-fond);
        color: var(--couleur-erreur-texte);
        border-radius: 0.5rem;
        padding: 0.75rem 1rem;
        margin-bottom: 1.5rem;
      }

      .chargement {
        color: var(--couleur-texte-att);
      }

      .actions-resultat {
        display: flex;
        gap: 1rem;
        margin-top: 2rem;
      }
    `,
  ],
})
export class PlanificateurComponent {
  private readonly api = inject(RavitoApiService);
  private readonly pdfExportService = inject(PdfExportService);

  protected readonly etape = signal<Etape>('PROFIL');
  protected readonly chargement = signal(false);
  protected readonly erreur = signal<string | null>(null);

  protected readonly profil = signal<ProfilRequest | null>(null);
  protected readonly proposition = signal<RepasProposesResponse | null>(null);
  protected readonly plan = signal<PlanSemaineResponse | null>(null);

  protected surProfilChoisi(profil: ProfilRequest): void {
    this.profil.set(profil);
    this.erreur.set(null);
    this.chargement.set(true);

    this.api.proposerRepas(profil).subscribe({
      next: (proposition) => {
        this.proposition.set(proposition);
        this.etape.set('SELECTION');
        this.chargement.set(false);
      },
      error: (erreur: HttpErrorResponse) => {
        this.erreur.set(this.messageErreur(erreur));
        this.chargement.set(false);
      },
    });
  }

  protected surSelectionValidee(selection: SelectionRepas): void {
    const profil = this.profil();
    if (!profil) {
      return;
    }

    const choix = {} as Record<JourSemaine, ChoixJourRequest>;
    JOURS.forEach((jour, index) => {
      choix[jour] = {
        petitDejeunerId: selection.petitsDejeuners[index].id,
        dejeunerId: selection.dejeuners[index].id,
      };
    });

    const requete: ComposerPlanSemaineRequest = { profil, choix };

    this.erreur.set(null);
    this.chargement.set(true);

    this.api.composerPlan(requete).subscribe({
      next: (plan) => {
        this.plan.set(plan);
        this.etape.set('RESULTAT');
        this.chargement.set(false);
      },
      error: (erreur: HttpErrorResponse) => {
        this.erreur.set(this.messageErreur(erreur));
        this.chargement.set(false);
      },
    });
  }

  protected exporterPdf(): void {
    const plan = this.plan();
    if (plan) {
      this.pdfExportService.exporterPlan(plan);
    }
  }

  protected recommencer(): void {
    this.etape.set('PROFIL');
    this.profil.set(null);
    this.proposition.set(null);
    this.plan.set(null);
    this.erreur.set(null);
  }

  private messageErreur(erreur: HttpErrorResponse): string {
    const messageServeur = (erreur.error as { message?: string } | null)?.message;
    return messageServeur ?? 'Une erreur est survenue, veuillez réessayer.';
  }
}
