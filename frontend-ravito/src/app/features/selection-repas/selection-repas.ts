import { Component, computed, input, output, signal } from '@angular/core';
import { JOURS, LIBELLES_JOUR } from '../../core/reference-data';
import type { ChoixJourRequest, JourSemaine, RepasProposesResponse } from '../../core/models';

interface ChoixJourPartiel {
  petitDejeunerId: string;
  dejeunerId: string;
}

const CHOIX_VIDE: ChoixJourPartiel = { petitDejeunerId: '', dejeunerId: '' };

/**
 * Une ligne par jour, chacune avec 2 listes deroulantes independantes.
 * Contrairement a l'ancienne version (cartes cliquables, "consommees" une
 * fois choisies), rien n'empeche ici de choisir le meme repas pour deux
 * jours differents, ni de remplir les jours dans un ordre quelconque —
 * chaque jour est une selection independante, pas une file d'attente.
 */
@Component({
  selector: 'app-selection-repas',
  template: `
    <section class="selection">
      <h2>2. Choisissez vos repas</h2>
      <p class="aide">
        Un menu peut être choisi plusieurs fois dans la semaine. Remplissez les jours dans l'ordre qui vous arrange.
      </p>
      <p class="compteur"><strong>{{ joursComplets() }}/5</strong> jours complets</p>

      <div class="jours">
        @for (jour of jours; track jour) {
          <article class="jour-ligne">
            <h3>{{ libellesJour[jour] }}</h3>

            <label>
              Petit-déjeuner
              <select (change)="choisirPetitDejeuner(jour, $any($event.target).value)">
                <option value="" [selected]="choix()[jour].petitDejeunerId === ''">— Choisir —</option>
                @for (repas of proposition().petitsDejeuners; track repas.id) {
                  <option [value]="repas.id" [selected]="repas.id === choix()[jour].petitDejeunerId">
                    {{ repas.nom }} ({{ repas.niveauRequis === 'CONFIRME' ? 'Confirmé' : 'Débutant' }})
                  </option>
                }
              </select>
            </label>

            <label>
              Déjeuner
              <select (change)="choisirDejeuner(jour, $any($event.target).value)">
                <option value="" [selected]="choix()[jour].dejeunerId === ''">— Choisir —</option>
                @for (repas of proposition().dejeuners; track repas.id) {
                  <option [value]="repas.id" [selected]="repas.id === choix()[jour].dejeunerId">
                    {{ repas.nom }} ({{ repas.niveauRequis === 'CONFIRME' ? 'Confirmé' : 'Débutant' }})
                  </option>
                }
              </select>
            </label>
          </article>
        }
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

      .jours {
        display: flex;
        flex-direction: column;
        gap: 0.75rem;
        margin: 1.5rem 0 2rem;
      }

      .jour-ligne {
        display: grid;
        grid-template-columns: 6rem 1fr 1fr;
        align-items: center;
        gap: 1rem;
        padding: 0.85rem 1rem;
        border-radius: 0.75rem;
        border: 1px solid var(--couleur-bordure);
        background: var(--couleur-surface);
      }

      .jour-ligne h3 {
        margin: 0;
        font-size: 1rem;
      }

      .jour-ligne label {
        display: flex;
        flex-direction: column;
        gap: 0.3rem;
        font-size: 0.8rem;
        font-weight: 600;
        color: var(--couleur-texte-att);
      }

      .jour-ligne select {
        font: inherit;
        padding: 0.5rem 0.6rem;
        border-radius: 0.5rem;
        border: 1px solid var(--couleur-bordure);
        background: var(--couleur-fond);
        color: var(--couleur-texte);
      }

      @media (max-width: 40rem) {
        .jour-ligne {
          grid-template-columns: 1fr;
        }
      }

      .valider {
        margin-top: 0.5rem;
      }
    `,
  ],
})
export class SelectionRepasComponent {
  readonly proposition = input.required<RepasProposesResponse>();
  readonly selectionValidee = output<Record<JourSemaine, ChoixJourRequest>>();

  protected readonly jours = JOURS;
  protected readonly libellesJour = LIBELLES_JOUR;

  protected readonly choix = signal<Record<JourSemaine, ChoixJourPartiel>>(
    Object.fromEntries(JOURS.map((jour) => [jour, { ...CHOIX_VIDE }])) as Record<JourSemaine, ChoixJourPartiel>,
  );

  protected readonly joursComplets = computed(
    () => this.jours.filter((jour) => this.estComplet(this.choix()[jour])).length,
  );

  protected readonly selectionComplete = computed(() => this.joursComplets() === this.jours.length);

  protected choisirPetitDejeuner(jour: JourSemaine, repasId: string): void {
    this.choix.update((actuel) => ({ ...actuel, [jour]: { ...actuel[jour], petitDejeunerId: repasId } }));
  }

  protected choisirDejeuner(jour: JourSemaine, repasId: string): void {
    this.choix.update((actuel) => ({ ...actuel, [jour]: { ...actuel[jour], dejeunerId: repasId } }));
  }

  protected valider(): void {
    if (!this.selectionComplete()) {
      return;
    }
    this.selectionValidee.emit(this.choix() as Record<JourSemaine, ChoixJourRequest>);
  }

  private estComplet(choixJour: ChoixJourPartiel): boolean {
    return choixJour.petitDejeunerId !== '' && choixJour.dejeunerId !== '';
  }
}
