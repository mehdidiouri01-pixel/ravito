import { DecimalPipe } from '@angular/common';
import { Component, input } from '@angular/core';
import { LIBELLES_JOUR, LIBELLES_RAYON, LIBELLES_UNITE } from '../../core/reference-data';
import type { PlanSemaineResponse, RayonMagasin } from '../../core/models';

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
            <p><span class="etiquette">Petit-déjeuner</span>{{ jour.petitDejeuner.nom }}</p>
            <p><span class="etiquette">Déjeuner</span>{{ jour.dejeuner.nom }}</p>
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

      .jour p {
        margin: 0.35rem 0;
        display: flex;
        flex-direction: column;
        font-size: 0.9rem;
      }

      .etiquette {
        font-size: 0.75rem;
        text-transform: uppercase;
        letter-spacing: 0.03em;
        color: var(--couleur-texte-att);
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
    `,
  ],
})
export class PlanSemaineComponent {
  readonly plan = input.required<PlanSemaineResponse>();

  protected readonly libellesJour = LIBELLES_JOUR;
  protected readonly libellesRayon = LIBELLES_RAYON;
  protected readonly libellesUnite = LIBELLES_UNITE;

  protected rayons(): RayonMagasin[] {
    return Object.keys(this.plan().listeCourses.parRayon) as RayonMagasin[];
  }
}
