import { DecimalPipe } from '@angular/common';
import { Component, input, signal } from '@angular/core';
import { LIBELLES_JOUR, LIBELLES_RAYON, LIBELLES_STYLE, LIBELLES_UNITE } from '../../core/reference-data';
import type { PlanSemaineResponse, RayonMagasin, RepasResponse } from '../../core/models';

@Component({
  selector: 'app-plan-semaine',
  imports: [DecimalPipe],
  template: `
    <section class="resultat">
      <h2>3. Votre plan de la semaine</h2>

      <div class="jours">
        @for (jour of plan().jours; track jour.jour) {
          <article class="jour">
            <h3>{{ libellesJour[jour.jour] }}</h3>

            <div class="repas-bloc">
              <div>
                <span class="etiquette">Petit-déjeuner</span>
                <p class="repas-nom">{{ jour.petitDejeuner.nom }}</p>
              </div>
              <button type="button" class="voir-recette" (click)="afficherRecette(jour.petitDejeuner)">
                Recette
              </button>
            </div>

            <div class="repas-bloc">
              <div>
                <span class="etiquette">Déjeuner</span>
                <p class="repas-nom">{{ jour.dejeuner.nom }}</p>
              </div>
              <button type="button" class="voir-recette" (click)="afficherRecette(jour.dejeuner)">Recette</button>
            </div>
          </article>
        }
      </div>

      <h3>Liste de courses</h3>
      <div class="rayons">
        @for (rayon of rayons(); track rayon) {
          <div class="rayon">
            <h4>{{ libellesRayon[rayon] }}</h4>
            <ul>
              @for (ligne of plan().listeCourses.parRayon[rayon]; track ligne.ingredient) {
                <li>{{ ligne.ingredient }} — {{ ligne.quantite }} {{ libellesUnite[ligne.unite] }}</li>
              }
            </ul>
          </div>
        }
      </div>

      <p class="prix">
        Coût estimé pour la semaine : <strong>{{ plan().prixEstime.montant | number: '1.2-2' }} €</strong>
      </p>
    </section>

    @if (repasAffiche(); as repas) {
      <div class="modale-fond" (click)="fermerRecette()">
        <div class="modale" role="dialog" aria-modal="true" (click)="$event.stopPropagation()">
          <button type="button" class="fermer" (click)="fermerRecette()" aria-label="Fermer la recette">✕</button>
          <h3>{{ repas.nom }}</h3>
          <p class="modale-meta">
            {{ libellesStyle[repas.style] }} · {{ repas.niveauRequis === 'CONFIRME' ? 'Confirmé' : 'Débutant' }}
          </p>
          <h4>Ingrédients</h4>
          <ul>
            @for (ingredient of repas.ingredients; track ingredient.ingredient) {
              <li>{{ ingredient.ingredient }} — {{ ingredient.quantite }} {{ libellesUnite[ingredient.unite] }}</li>
            }
          </ul>
        </div>
      </div>
    }
  `,
  styles: [
    `
      .jours {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(11rem, 1fr));
        gap: 1rem;
        margin: 1.5rem 0 2rem;
      }

      .jour {
        border: 1px solid var(--couleur-bordure);
        border-radius: 0.75rem;
        padding: 1rem;
        background: var(--couleur-surface);
      }

      .jour h3 {
        margin: 0 0 0.5rem;
      }

      .repas-bloc {
        display: flex;
        align-items: center;
        justify-content: space-between;
        gap: 0.5rem;
        margin: 0.5rem 0;
      }

      .repas-nom {
        margin: 0.15rem 0 0;
        font-size: 0.9rem;
      }

      .etiquette {
        font-size: 0.75rem;
        text-transform: uppercase;
        letter-spacing: 0.03em;
        color: var(--couleur-texte-att);
      }

      .voir-recette {
        flex-shrink: 0;
        background: transparent;
        color: var(--couleur-accent);
        border: 1px solid var(--couleur-accent);
        padding: 0.3rem 0.6rem;
        font-size: 0.75rem;
      }

      .rayons {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(13rem, 1fr));
        gap: 1.25rem;
        margin: 1rem 0 2rem;
      }

      .rayon h4 {
        margin: 0 0 0.4rem;
        color: var(--couleur-accent);
      }

      .rayon ul {
        margin: 0;
        padding-left: 1.1rem;
      }

      .prix {
        font-size: 1.1rem;
      }

      .modale-fond {
        position: fixed;
        inset: 0;
        background: rgba(31, 36, 33, 0.5);
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 1.5rem;
        z-index: 10;
      }

      .modale {
        position: relative;
        background: var(--couleur-surface);
        border-radius: 0.75rem;
        padding: 1.75rem;
        max-width: 26rem;
        width: 100%;
        max-height: 80vh;
        overflow-y: auto;
      }

      .modale h3 {
        margin: 0 0 0.25rem;
      }

      .modale-meta {
        color: var(--couleur-texte-att);
        margin: 0 0 1rem;
      }

      .modale ul {
        margin: 0;
        padding-left: 1.1rem;
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
export class PlanSemaineComponent {
  readonly plan = input.required<PlanSemaineResponse>();

  protected readonly libellesJour = LIBELLES_JOUR;
  protected readonly libellesRayon = LIBELLES_RAYON;
  protected readonly libellesUnite = LIBELLES_UNITE;
  protected readonly libellesStyle = LIBELLES_STYLE;

  protected readonly repasAffiche = signal<RepasResponse | null>(null);

  protected rayons(): RayonMagasin[] {
    return Object.keys(this.plan().listeCourses.parRayon) as RayonMagasin[];
  }

  protected afficherRecette(repas: RepasResponse): void {
    this.repasAffiche.set(repas);
  }

  protected fermerRecette(): void {
    this.repasAffiche.set(null);
  }
}
