import { Component, output, signal } from '@angular/core';
import {
  ENSEIGNES,
  NIVEAUX_CUISINE,
  NOMBRE_DE_PERSONNES_DEFAUT,
  NOMBRE_DE_PERSONNES_MAX,
  NOMBRE_DE_PERSONNES_MIN,
  STYLES_ALIMENTAIRES,
} from '../../core/reference-data';
import type { Enseigne, NiveauCuisine, ProfilRequest, StyleAlimentaire } from '../../core/models';

@Component({
  selector: 'app-profil-form',
  template: `
    <form class="profil-form" (submit)="valider($event)">
      <h2>1. Vos préférences</h2>

      <div class="champ-enseigne">
        <span class="etiquette-champ">Enseigne</span>
        <div class="enseignes-grille" role="radiogroup" aria-label="Enseigne">
          @for (option of enseignes; track option.valeur) {
            <button
              type="button"
              class="enseigne-tuile"
              [class.selectionnee]="option.valeur === enseigne()"
              [class.texte-fonce]="option.texteFonce"
              [style.background]="option.couleur"
              role="radio"
              [attr.aria-checked]="option.valeur === enseigne()"
              (click)="enseigne.set(option.valeur)"
            >
              {{ option.libelle }}
            </button>
          }
        </div>
      </div>

      <label>
        Style alimentaire
        <select (change)="style.set($any($event.target).value)">
          @for (option of stylesAlimentaires; track option.valeur) {
            <option [value]="option.valeur" [selected]="option.valeur === style()">{{ option.libelle }}</option>
          }
        </select>
      </label>

      <label>
        Niveau en cuisine
        <select (change)="niveau.set($any($event.target).value)">
          @for (option of niveauxCuisine; track option.valeur) {
            <option [value]="option.valeur" [selected]="option.valeur === niveau()">{{ option.libelle }}</option>
          }
        </select>
      </label>

      <label>
        Nombre de personnes
        <input
          type="number"
          [min]="minPersonnes"
          [max]="maxPersonnes"
          [value]="nombreDePersonnes()"
          (input)="surNombreDePersonnes($any($event.target).value)"
        />
        <small>Les quantités de la liste de courses seront calculées pour ce nombre de personnes.</small>
      </label>

      <button type="submit">Voir les repas proposés</button>
    </form>
  `,
  styles: [
    `
      .profil-form {
        display: flex;
        flex-direction: column;
        gap: 1.25rem;
        max-width: 24rem;
        padding: 1.5rem;
        border-radius: var(--rayon);
        border: 1px solid var(--couleur-bordure);
        background: var(--couleur-surface-transparente);
        backdrop-filter: blur(6px);
        box-shadow: var(--ombre-carte);
      }

      label,
      .champ-enseigne {
        display: flex;
        flex-direction: column;
        gap: 0.4rem;
        font-weight: 600;
        color: var(--couleur-texte-att);
      }

      .etiquette-champ {
        font-weight: 600;
        color: var(--couleur-texte-att);
      }

      .enseignes-grille {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(6rem, 1fr));
        gap: 0.5rem;
      }

      .enseigne-tuile {
        padding: 0.6rem 0.4rem;
        border-radius: 0.5rem;
        border: 2px solid transparent;
        color: #fff;
        font-family: inherit;
        font-weight: 700;
        font-size: 0.8rem;
        text-align: center;
        line-height: 1.2;
        cursor: pointer;
        box-shadow: var(--ombre-carte);
        transition:
          transform 0.12s ease,
          box-shadow 0.12s ease,
          border-color 0.12s ease;
      }

      .enseigne-tuile.texte-fonce {
        color: #1f2421;
      }

      .enseigne-tuile:hover {
        transform: translateY(-2px);
        box-shadow: var(--ombre-carte-hover);
      }

      .enseigne-tuile.selectionnee {
        border-color: var(--couleur-texte);
        box-shadow: var(--ombre-carte-hover);
      }

      select,
      input[type='number'] {
        font: inherit;
        padding: 0.6rem 0.75rem;
        border-radius: 0.5rem;
        border: 1px solid var(--couleur-bordure);
        background: var(--couleur-surface);
        color: var(--couleur-texte);
      }

      label small {
        font-weight: 400;
        font-size: 0.8rem;
        color: var(--couleur-texte-att);
      }

      button {
        align-self: flex-start;
      }
    `,
  ],
})
export class ProfilFormComponent {
  protected readonly enseignes = ENSEIGNES;
  protected readonly stylesAlimentaires = STYLES_ALIMENTAIRES;
  protected readonly niveauxCuisine = NIVEAUX_CUISINE;

  protected readonly minPersonnes = NOMBRE_DE_PERSONNES_MIN;
  protected readonly maxPersonnes = NOMBRE_DE_PERSONNES_MAX;

  protected readonly enseigne = signal<Enseigne>('CARREFOUR');
  protected readonly style = signal<StyleAlimentaire>('NORMAL');
  protected readonly niveau = signal<NiveauCuisine>('DEBUTANT');
  protected readonly nombreDePersonnes = signal<number>(NOMBRE_DE_PERSONNES_DEFAUT);

  readonly profilChoisi = output<ProfilRequest>();

  /**
   * Ramene la saisie dans les bornes acceptees par le domaine : le champ
   * number laisse taper n'importe quoi (vide, 0, 999), et le backend
   * refuserait la requete — autant corriger tout de suite plutot que
   * d'attendre un 400.
   */
  protected surNombreDePersonnes(saisie: string): void {
    const valeur = Number.parseInt(saisie, 10);
    if (Number.isNaN(valeur)) {
      return;
    }
    this.nombreDePersonnes.set(Math.min(Math.max(valeur, this.minPersonnes), this.maxPersonnes));
  }

  protected valider(evenement: Event): void {
    evenement.preventDefault();
    this.profilChoisi.emit({
      enseigne: this.enseigne(),
      style: this.style(),
      niveau: this.niveau(),
      nombreDePersonnes: this.nombreDePersonnes(),
    });
  }
}
