import { DecimalPipe } from '@angular/common';
import { Component, effect, input, signal } from '@angular/core';
import { LIBELLES_JOUR, LIBELLES_RAYON, LIBELLES_UNITE } from '../../core/reference-data';
import type { LigneListeCoursesResponse, PlanSemaineResponse, RayonMagasin, RepasResponse } from '../../core/models';
import { RecetteModaleComponent } from '../../ui/recette-modale/recette-modale';

const PREFIXE_STOCKAGE = 'ravito.dejaChezVous.';

@Component({
  selector: 'app-plan-semaine',
  imports: [DecimalPipe, RecetteModaleComponent],
  template: `
    <section class="resultat">
      <h2>3. Votre plan de la semaine</h2>
      <p class="pour-personnes">Quantités et coût calculés pour {{ nombreDePersonnes() }} personne(s).</p>

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

            <div class="repas-bloc">
              <div>
                <span class="etiquette">Dîner</span>
                <p class="repas-nom">{{ jour.diner.nom }}</p>
              </div>
              <button type="button" class="voir-recette" (click)="afficherRecette(jour.diner)">Recette</button>
            </div>
          </article>
        }
      </div>

      <h3>Liste de courses</h3>
      <p class="astuce">Cochez ce que vous avez déjà chez vous : le coût s'ajuste automatiquement.</p>
      <div class="rayons">
        @for (rayon of rayons(); track rayon) {
          <div class="rayon">
            <h4>{{ libellesRayon[rayon] }}</h4>
            <ul>
              @for (ligne of plan().listeCourses.parRayon[rayon]; track ligne.ingredient + ligne.unite) {
                <li [class.deja-chez-vous]="estDejaChezVous(ligne)">
                  <label>
                    <input
                      type="checkbox"
                      [checked]="estDejaChezVous(ligne)"
                      (change)="basculerDejaChezVous(ligne)"
                    />
                    {{ ligne.ingredient }} — {{ ligne.quantite }} {{ libellesUnite[ligne.unite] }}
                  </label>
                </li>
              }
            </ul>
          </div>
        }
      </div>

      <p class="prix">
        Coût estimé pour la semaine : <strong>{{ prixAjuste() | number: '1.2-2' }} €</strong>
        @if (economieRealisee() > 0) {
          <span class="economie">
            (-{{ economieRealisee() | number: '1.2-2' }} € grâce à ce que vous avez déjà)
          </span>
        }
      </p>
    </section>

    <app-recette-modale [repas]="repasAffiche()" (fermer)="fermerRecette()" />
  `,
  styles: [
    `
      .pour-personnes {
        color: var(--couleur-texte-att);
        margin-top: -0.5rem;
      }

      .jours {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(11rem, 1fr));
        gap: 1rem;
        margin: 1.5rem 0 2rem;
      }

      .jour {
        border: 1px solid var(--couleur-bordure);
        border-radius: var(--rayon);
        padding: 1rem;
        background: var(--couleur-surface-transparente);
        backdrop-filter: blur(6px);
        box-shadow: var(--ombre-carte);
        transition:
          transform 0.15s ease,
          box-shadow 0.15s ease;
      }

      .jour:hover {
        transform: translateY(-2px);
        box-shadow: var(--ombre-carte-hover);
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

      .astuce {
        color: var(--couleur-texte-att);
        font-size: 0.85rem;
        margin: -0.5rem 0 0.5rem;
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
        padding-left: 0;
        list-style: none;
      }

      .rayon li {
        padding: 0.15rem 0;
      }

      .rayon li label {
        display: flex;
        align-items: baseline;
        gap: 0.5rem;
        cursor: pointer;
      }

      .rayon li input[type='checkbox'] {
        flex-shrink: 0;
        margin: 0;
        accent-color: var(--couleur-accent);
      }

      .rayon li.deja-chez-vous {
        color: var(--couleur-texte-att);
      }

      .rayon li.deja-chez-vous label {
        text-decoration: line-through;
      }

      .prix {
        font-size: 1.1rem;
      }

      .economie {
        display: block;
        font-size: 0.85rem;
        font-weight: 400;
        color: var(--couleur-texte-att);
      }
    `,
  ],
})
export class PlanSemaineComponent {
  readonly plan = input.required<PlanSemaineResponse>();
  readonly nombreDePersonnes = input.required<number>();

  protected readonly libellesJour = LIBELLES_JOUR;
  protected readonly libellesRayon = LIBELLES_RAYON;
  protected readonly libellesUnite = LIBELLES_UNITE;

  protected readonly repasAffiche = signal<RepasResponse | null>(null);

  /**
   * Les ingrédients de la liste de courses que l'utilisateur possède déjà
   * (clé = `ingredient + unite`, cohérent avec la consolidation des lignes
   * côté backend). Mémorisé dans le navigateur, par plan (voir
   * `PlanSemaineResponse.id`) : survit à un rechargement de page sur cet
   * appareil. Rien n'est envoyé au serveur pour ça — le prix ajusté est
   * recalculé côté client à chaque coche, à partir du prix par ligne déjà
   * fourni dans la réponse (voir `prixAjuste`).
   */
  protected readonly dejaChezVous = signal<ReadonlySet<string>>(new Set());

  constructor() {
    effect(() => {
      this.dejaChezVous.set(this.chargerDepuisStockage(this.plan().id));
    });
  }

  protected rayons(): RayonMagasin[] {
    return Object.keys(this.plan().listeCourses.parRayon) as RayonMagasin[];
  }

  protected afficherRecette(repas: RepasResponse): void {
    this.repasAffiche.set(repas);
  }

  protected fermerRecette(): void {
    this.repasAffiche.set(null);
  }

  protected estDejaChezVous(ligne: LigneListeCoursesResponse): boolean {
    return this.dejaChezVous().has(this.cleLigne(ligne));
  }

  protected basculerDejaChezVous(ligne: LigneListeCoursesResponse): void {
    const cle = this.cleLigne(ligne);
    const nouveauSet = new Set(this.dejaChezVous());
    if (nouveauSet.has(cle)) {
      nouveauSet.delete(cle);
    } else {
      nouveauSet.add(cle);
    }
    this.dejaChezVous.set(nouveauSet);
    this.sauvegarderDansStockage(this.plan().id, nouveauSet);
  }

  /** Somme des prix des lignes cochées, deja fournis par le backend — aucun appel serveur necessaire pour l'ajuster. */
  protected economieRealisee(): number {
    let economie = 0;
    for (const rayon of this.rayons()) {
      for (const ligne of this.plan().listeCourses.parRayon[rayon] ?? []) {
        if (this.estDejaChezVous(ligne)) {
          economie += ligne.prixEstime;
        }
      }
    }
    return economie;
  }

  protected prixAjuste(): number {
    return Math.max(0, this.plan().prixEstime.montant - this.economieRealisee());
  }

  private cleLigne(ligne: LigneListeCoursesResponse): string {
    return `${ligne.ingredient}__${ligne.unite}`;
  }

  private cleStockage(planId: string): string {
    return `${PREFIXE_STOCKAGE}${planId}`;
  }

  private chargerDepuisStockage(planId: string): Set<string> {
    try {
      const brut = localStorage.getItem(this.cleStockage(planId));
      if (!brut) {
        return new Set();
      }
      const cles: unknown = JSON.parse(brut);
      return Array.isArray(cles) ? new Set(cles) : new Set();
    } catch {
      // stockage indisponible (navigation privée, quota, etc.) : on repart d'une liste vide plutôt que de planter.
      return new Set();
    }
  }

  private sauvegarderDansStockage(planId: string, cles: ReadonlySet<string>): void {
    try {
      localStorage.setItem(this.cleStockage(planId), JSON.stringify([...cles]));
    } catch {
      // idem : la coche reste effective pour la session en cours, simplement pas mémorisée.
    }
  }
}
