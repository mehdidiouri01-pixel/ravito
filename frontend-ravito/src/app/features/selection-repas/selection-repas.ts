import { Component, computed, input, output, signal } from '@angular/core';
import { JOURS, LIBELLES_JOUR } from '../../core/reference-data';
import type { ChoixJourRequest, JourSemaine, RepasProposesResponse, RepasResponse } from '../../core/models';
import { RecetteModaleComponent } from '../../ui/recette-modale/recette-modale';

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
  imports: [RecetteModaleComponent],
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
              <div class="select-avec-recette">
                <select (change)="choisirPetitDejeuner(jour, $any($event.target).value)">
                  <option value="" [selected]="choix()[jour].petitDejeunerId === ''">— Choisir —</option>
                  @for (repas of proposition().petitsDejeuners; track repas.id) {
                    <option [value]="repas.id" [selected]="repas.id === choix()[jour].petitDejeunerId">
                      {{ repas.nom }} ({{ repas.niveauRequis === 'CONFIRME' ? 'Confirmé' : 'Débutant' }})
                    </option>
                  }
                </select>
                <button
                  type="button"
                  class="voir-recette"
                  [disabled]="choix()[jour].petitDejeunerId === ''"
                  (click)="afficherRecette(jour, 'petitDejeunerId', proposition().petitsDejeuners)"
                >
                  Recette
                </button>
              </div>
            </label>

            <label>
              Déjeuner
              <div class="select-avec-recette">
                <select (change)="choisirDejeuner(jour, $any($event.target).value)">
                  <option value="" [selected]="choix()[jour].dejeunerId === ''">— Choisir —</option>
                  @for (repas of proposition().dejeuners; track repas.id) {
                    <option [value]="repas.id" [selected]="repas.id === choix()[jour].dejeunerId">
                      {{ repas.nom }} ({{ repas.niveauRequis === 'CONFIRME' ? 'Confirmé' : 'Débutant' }})
                    </option>
                  }
                </select>
                <button
                  type="button"
                  class="voir-recette"
                  [disabled]="choix()[jour].dejeunerId === ''"
                  (click)="afficherRecette(jour, 'dejeunerId', proposition().dejeuners)"
                >
                  Recette
                </button>
              </div>
            </label>
          </article>
        }
      </div>

      <button type="button" class="valider" [disabled]="!selectionComplete()" (click)="valider()">
        Composer mon plan de la semaine
      </button>
    </section>

    <app-recette-modale [repas]="repasAffiche()" (fermer)="fermerRecette()" />
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

      .select-avec-recette {
        display: flex;
        gap: 0.4rem;
      }

      .select-avec-recette select {
        flex: 1;
        min-width: 0;
        font: inherit;
        padding: 0.5rem 0.6rem;
        border-radius: 0.5rem;
        border: 1px solid var(--couleur-bordure);
        background: var(--couleur-fond);
        color: var(--couleur-texte);
      }

      .voir-recette {
        flex-shrink: 0;
        background: transparent;
        color: var(--couleur-accent);
        border: 1px solid var(--couleur-accent);
        padding: 0.3rem 0.6rem;
        font-size: 0.75rem;
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

  protected readonly repasAffiche = signal<RepasResponse | null>(null);

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

  /**
   * Affiche la recette du repas actuellement choisi dans la case (jour,
   * type). Rien a afficher si la case est encore vide — le bouton est de
   * toute facon desactive dans ce cas (voir le template).
   */
  protected afficherRecette(jour: JourSemaine, cle: keyof ChoixJourPartiel, catalogue: RepasResponse[]): void {
    const id = this.choix()[jour][cle];
    const repas = catalogue.find((r) => r.id === id);
    if (repas) {
      this.repasAffiche.set(repas);
    }
  }

  protected fermerRecette(): void {
    this.repasAffiche.set(null);
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
