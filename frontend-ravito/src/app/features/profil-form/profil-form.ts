import { Component, output, signal } from '@angular/core';
import { ENSEIGNES, NIVEAUX_CUISINE, STYLES_ALIMENTAIRES } from '../../core/reference-data';
import type { Enseigne, NiveauCuisine, ProfilRequest, StyleAlimentaire } from '../../core/models';

@Component({
  selector: 'app-profil-form',
  template: `
    <form class="profil-form" (submit)="valider($event)">
      <h2>1. Vos préférences</h2>

      <label>
        Enseigne
        <select (change)="enseigne.set($any($event.target).value)">
          @for (option of enseignes; track option.valeur) {
            <option [value]="option.valeur" [selected]="option.valeur === enseigne()">{{ option.libelle }}</option>
          }
        </select>
      </label>

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
      }

      label {
        display: flex;
        flex-direction: column;
        gap: 0.4rem;
        font-weight: 600;
        color: var(--couleur-texte-att);
      }

      select {
        font: inherit;
        padding: 0.6rem 0.75rem;
        border-radius: 0.5rem;
        border: 1px solid var(--couleur-bordure);
        background: var(--couleur-surface);
        color: var(--couleur-texte);
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

  protected readonly enseigne = signal<Enseigne>('CARREFOUR');
  protected readonly style = signal<StyleAlimentaire>('NORMAL');
  protected readonly niveau = signal<NiveauCuisine>('DEBUTANT');

  readonly profilChoisi = output<ProfilRequest>();

  protected valider(evenement: Event): void {
    evenement.preventDefault();
    this.profilChoisi.emit({
      enseigne: this.enseigne(),
      style: this.style(),
      niveau: this.niveau(),
    });
  }
}
