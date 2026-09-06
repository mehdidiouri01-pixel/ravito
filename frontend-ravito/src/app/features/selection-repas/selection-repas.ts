import { Component, WritableSignal, input, output, signal, computed } from '@angular/core';
import type { RepasProposesResponse, RepasResponse } from '../../core/models';

export interface SelectionRepas {
  petitsDejeuners: RepasResponse[];
  dejeuners: RepasResponse[];
}

/**
 * Choix de 5 petits-dejeuners et 5 dejeuners parmi la proposition. L'ordre
 * de selection (1er clique, 2e clique, ...) determine le jour assigne
 * (lundi, mardi, ...) — c'est le conteneur parent qui fait cette
 * correspondance, ce composant ne fait que renvoyer les deux listes dans
 * l'ordre choisi.
 */
@Component({
  selector: 'app-selection-repas',
  template: `
    <section class="selection">
      <h2>2. Choisissez vos 5 petits-déjeuners et 5 déjeuners</h2>
      <p class="aide">
        L'ordre de vos clics fixe le jour : le 1<sup>er</sup> choisi sera pour lundi, le 2<sup>e</sup> pour mardi, etc.
      </p>
      <p class="compteur">
        Petits-déjeuners : <strong>{{ petitsDejeunersChoisis().length }}/5</strong>
        — Déjeuners : <strong>{{ dejeunersChoisis().length }}/5</strong>
      </p>

      <div class="colonnes">
        <div class="colonne">
          <h3>Petits-déjeuners</h3>
          <div class="cartes">
            @for (repas of proposition().petitsDejeuners; track repas.id) {
              <button
                type="button"
                class="carte"
                [class.choisi]="estChoisi(petitsDejeunersChoisis(), repas)"
                (click)="basculer(petitsDejeunersChoisis, repas)"
              >
                @if (estChoisi(petitsDejeunersChoisis(), repas)) {
                  <span class="rang">{{ rangDeSelection(petitsDejeunersChoisis(), repas) }}</span>
                }
                <strong>{{ repas.nom }}</strong>
                <small>{{ repas.niveauRequis === 'CONFIRME' ? 'Confirmé' : 'Débutant' }}</small>
              </button>
            }
          </div>
        </div>

        <div class="colonne">
          <h3>Déjeuners</h3>
          <div class="cartes">
            @for (repas of proposition().dejeuners; track repas.id) {
              <button
                type="button"
                class="carte"
                [class.choisi]="estChoisi(dejeunersChoisis(), repas)"
                (click)="basculer(dejeunersChoisis, repas)"
              >
                @if (estChoisi(dejeunersChoisis(), repas)) {
                  <span class="rang">{{ rangDeSelection(dejeunersChoisis(), repas) }}</span>
                }
                <strong>{{ repas.nom }}</strong>
                <small>{{ repas.niveauRequis === 'CONFIRME' ? 'Confirmé' : 'Débutant' }}</small>
              </button>
            }
          </div>
        </div>
      </div>

      <button type="button" class="valider" [disabled]="!selectionComplete()" (click)="valider()">
        Composer mon plan de la semaine
      </button>
    </section>
  `,
  styles: [
    `
      .aide {
        color: var(--couleur-texte-att);
        margin-top: -0.5rem;
      }

      .compteur {
        font-weight: 600;
      }

      .colonnes {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(16rem, 1fr));
        gap: 2rem;
        margin: 1.5rem 0;
      }

      .cartes {
        display: grid;
        gap: 0.75rem;
      }

      .carte {
        position: relative;
        display: flex;
        flex-direction: column;
        gap: 0.25rem;
        text-align: left;
        padding: 0.85rem 1rem;
        border-radius: 0.75rem;
        border: 1px solid var(--couleur-bordure);
        background: var(--couleur-surface);
        color: var(--couleur-texte);
        cursor: pointer;
        transition: border-color 0.15s, transform 0.1s;
        font: inherit;
      }

      .carte:hover {
        border-color: var(--couleur-accent);
      }

      .carte.choisi {
        border-color: var(--couleur-accent);
        background: var(--couleur-accent-att);
      }

      .carte small {
        color: var(--couleur-texte-att);
      }

      .rang {
        position: absolute;
        top: -0.5rem;
        right: -0.5rem;
        width: 1.6rem;
        height: 1.6rem;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 999px;
        background: var(--couleur-accent);
        color: white;
        font-size: 0.8rem;
        font-weight: 700;
      }

      .valider {
        margin-top: 1.5rem;
      }
    `,
  ],
})
export class SelectionRepasComponent {
  readonly proposition = input.required<RepasProposesResponse>();
  readonly selectionValidee = output<SelectionRepas>();

  protected readonly petitsDejeunersChoisis = signal<RepasResponse[]>([]);
  protected readonly dejeunersChoisis = signal<RepasResponse[]>([]);

  protected readonly selectionComplete = computed(
    () => this.petitsDejeunersChoisis().length === 5 && this.dejeunersChoisis().length === 5,
  );

  protected estChoisi(liste: RepasResponse[], repas: RepasResponse): boolean {
    return liste.some((r) => r.id === repas.id);
  }

  protected rangDeSelection(liste: RepasResponse[], repas: RepasResponse): number {
    return liste.findIndex((r) => r.id === repas.id) + 1;
  }

  protected basculer(liste: WritableSignal<RepasResponse[]>, repas: RepasResponse): void {
    const actuel = liste();
    if (this.estChoisi(actuel, repas)) {
      liste.set(actuel.filter((r) => r.id !== repas.id));
    } else if (actuel.length < 5) {
      liste.set([...actuel, repas]);
    }
  }

  protected valider(): void {
    if (this.selectionComplete()) {
      this.selectionValidee.emit({
        petitsDejeuners: this.petitsDejeunersChoisis(),
        dejeuners: this.dejeunersChoisis(),
      });
    }
  }
}
