import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import { PdfExportService } from '../core/pdf-export.service';
import { RavitoApiService } from '../core/ravito-api.service';
import type {
  ChoixJourRequest,
  ComposerPlanSemaineRequest,
  JourSemaine,
  PlanSemaineResponse,
  ProfilRequest,
  RepasProposesResponse,
  RepasResponse,
  TypeRepas,
} from '../core/models';
import { ProfilFormComponent } from '../features/profil-form/profil-form';
import { SelectionRepasComponent } from '../features/selection-repas/selection-repas';
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
      <header class="hero">
        <div class="hero-voile"></div>
        <div class="hero-contenu">
          <h1>Ravito</h1>
          <p class="sous-titre">Votre plan de repas de la semaine, en 3 étapes.</p>
        </div>
      </header>

      @if (erreur()) {
        <p class="erreur" role="alert">{{ erreur() }}</p>
      }

      @if (chargement()) {
        <p class="chargement">Chargement…</p>
      }

      @switch (etape()) {
        @case ('PROFIL') {
          <div class="etape-contenu">
            <app-profil-form (profilChoisi)="surProfilChoisi($event)" />
          </div>
        }
        @case ('SELECTION') {
          @if (proposition(); as valeurProposition) {
            <div class="etape-contenu">
              <app-selection-repas
                [proposition]="valeurProposition"
                [repasGenere]="repasGenere()"
                (selectionValidee)="surSelectionValidee($event)"
                (genererIdee)="surGenererIdee($event)"
              />
            </div>
          }
        }
        @case ('RESULTAT') {
          @if (plan(); as valeurPlan) {
            <div class="etape-contenu">
              <app-plan-semaine [plan]="valeurPlan" [nombreDePersonnes]="nombreDePersonnes()" />
              <div class="actions-resultat">
                <button type="button" (click)="exporterPdf()">Exporter en PDF</button>
                <button type="button" class="recommencer" (click)="recommencer()">Recommencer</button>
              </div>
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
        padding: 1.5rem 1.5rem 4rem;
      }

      .hero {
        position: relative;
        margin-bottom: 2rem;
        border-radius: 1.25rem;
        overflow: hidden;
        min-height: 12rem;
        display: flex;
        align-items: flex-end;
        background-image: url('https://images.unsplash.com/photo-1579113800032-c38bd7635818?auto=format&fit=crop&w=1600&q=80');
        background-size: cover;
        background-position: center;
        box-shadow: var(--ombre-carte);
      }

      .hero-voile {
        position: absolute;
        inset: 0;
        background: linear-gradient(180deg, rgba(31, 46, 34, 0.15) 0%, rgba(20, 28, 20, 0.82) 100%);
      }

      .hero-contenu {
        position: relative;
        padding: 2rem 1.75rem 1.5rem;
        color: white;
      }

      h1 {
        margin: 0;
        font-size: 2.25rem;
        color: white;
      }

      .sous-titre {
        color: rgba(255, 255, 255, 0.85);
        margin: 0.3rem 0 0;
      }

      /* Chaque changement d'etape recree ce noeud (via @switch) : l'animation
         se rejoue donc automatiquement a chaque transition, sans dependance
         au module @angular/animations. */
      .etape-contenu {
        animation: etape-entree 0.45s ease both;
      }

      @keyframes etape-entree {
        from {
          opacity: 0;
          transform: translateY(0.75rem);
        }
        to {
          opacity: 1;
          transform: translateY(0);
        }
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
  protected readonly repasGenere = signal<RepasResponse | null>(null);

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

  protected surSelectionValidee(choix: Record<JourSemaine, ChoixJourRequest>): void {
    const profil = this.profil();
    if (!profil) {
      return;
    }

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

  protected surGenererIdee(type: TypeRepas): void {
    const profil = this.profil();
    if (!profil) {
      return;
    }

    this.erreur.set(null);
    this.chargement.set(true);

    this.api.genererRepas({ profil, type }).subscribe({
      next: (repas) => {
        this.repasGenere.set(repas);
        this.chargement.set(false);
      },
      error: (erreur: HttpErrorResponse) => {
        this.erreur.set(this.messageErreur(erreur));
        this.chargement.set(false);
      },
    });
  }

  /** Le plan affiché est toujours issu d'un profil validé — 1 en repli défensif. */
  protected nombreDePersonnes(): number {
    return this.profil()?.nombreDePersonnes ?? 1;
  }

  protected exporterPdf(): void {
    const plan = this.plan();
    if (plan) {
      this.pdfExportService.exporterPlan(plan, this.nombreDePersonnes());
    }
  }

  protected recommencer(): void {
    this.etape.set('PROFIL');
    this.profil.set(null);
    this.proposition.set(null);
    this.plan.set(null);
    this.repasGenere.set(null);
    this.erreur.set(null);
  }

  private messageErreur(erreur: HttpErrorResponse): string {
    const messageServeur = (erreur.error as { message?: string } | null)?.message;
    return messageServeur ?? 'Une erreur est survenue, veuillez réessayer.';
  }
}
