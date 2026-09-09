import { Component, input, output } from '@angular/core';
import { LIBELLES_STYLE, LIBELLES_UNITE } from '../../core/reference-data';
import type { RepasResponse } from '../../core/models';

/**
 * Pop-up de recette (nom, style, niveau, ingredients avec quantites) —
 * partagee entre l'etape 2 (previsualiser avant de choisir) et l'etape 3
 * (revoir apres coup) : meme composant, deux appelants.
 *
 * <p>Se rend invisible d'elle-meme quand aucun repas n'est fourni, pour que
 * l'appelant puisse l'inclure sans condition dans son template plutot que
 * de dupliquer un `@if` autour.
 */
@Component({
  selector: 'app-recette-modale',
  template: `
    @if (repas(); as r) {
      <div class="modale-fond" (click)="fermer.emit()">
        <div class="modale" role="dialog" aria-modal="true" (click)="$event.stopPropagation()">
          <button type="button" class="fermer" (click)="fermer.emit()" aria-label="Fermer la recette">✕</button>
          <h3>{{ r.nom }}</h3>
          <p class="modale-meta">
            {{ libellesStyle[r.style] }} · {{ r.niveauRequis === 'CONFIRME' ? 'Confirmé' : 'Débutant' }}
          </p>
          @if (note()) {
            <p class="modale-note">{{ note() }}</p>
          }
          <h4>Ingrédients</h4>
          <ul>
            @for (ingredient of r.ingredients; track ingredient.ingredient) {
              <li>{{ ingredient.ingredient }} — {{ ingredient.quantite }} {{ libellesUnite[ingredient.unite] }}</li>
            }
          </ul>
          <h4>Étapes de préparation</h4>
          <ol>
            @for (etape of r.etapesPreparation; track $index) {
              <li>{{ etape }}</li>
            }
          </ol>
        </div>
      </div>
    }
  `,
  styles: [
    `
      .modale-fond {
        position: fixed;
        inset: 0;
        background: rgba(31, 36, 33, 0.5);
        backdrop-filter: blur(3px);
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 1.5rem;
        z-index: 10;
        animation: voile-entree 0.2s ease both;
      }

      .modale {
        position: relative;
        background: var(--couleur-surface);
        border-radius: var(--rayon);
        padding: 1.75rem;
        max-width: 26rem;
        width: 100%;
        max-height: 80vh;
        overflow-y: auto;
        box-shadow: var(--ombre-carte-hover);
        animation: modale-entree 0.25s ease both;
      }

      @keyframes voile-entree {
        from {
          opacity: 0;
        }
        to {
          opacity: 1;
        }
      }

      @keyframes modale-entree {
        from {
          opacity: 0;
          transform: translateY(0.75rem) scale(0.97);
        }
        to {
          opacity: 1;
          transform: translateY(0) scale(1);
        }
      }

      .modale h3 {
        margin: 0 0 0.25rem;
      }

      .modale-meta {
        color: var(--couleur-texte-att);
        margin: 0 0 1rem;
      }

      .modale-note {
        background: var(--couleur-fond);
        border-radius: 0.5rem;
        padding: 0.6rem 0.75rem;
        font-size: 0.85rem;
        margin: 0 0 1rem;
      }

      .modale ul {
        margin: 0;
        padding-left: 1.1rem;
      }

      .modale h4 + ol {
        margin: 0;
        padding-left: 1.1rem;
      }

      .modale ol li + li {
        margin-top: 0.4rem;
      }

      .fermer {
        position: absolute;
        top: 0.75rem;
        right: 0.75rem;
        background: transparent;
        color: var(--couleur-texte-att);
        padding: 0.2rem 0.5rem;
      }
    `,
  ],
})
export class RecetteModaleComponent {
  readonly repas = input<RepasResponse | null>(null);
  readonly note = input<string | null>(null);
  readonly fermer = output<void>();

  protected readonly libellesStyle = LIBELLES_STYLE;
  protected readonly libellesUnite = LIBELLES_UNITE;
}
