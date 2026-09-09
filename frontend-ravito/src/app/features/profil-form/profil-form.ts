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

      <div class="champ">
        <span class="etiquette-champ">Enseigne</span>
        <div class="grille-tuiles" role="radiogroup" aria-label="Enseigne">
          @for (option of enseignes; track option.valeur) {
            <button
              type="button"
              class="tuile-choix tuile-marque"
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

      <div class="champ">
        <span class="etiquette-champ">Style alimentaire</span>
        <div class="grille-tuiles" role="radiogroup" aria-label="Style alimentaire">
          @for (option of stylesAlimentaires; track option.valeur) {
            <button
              type="button"
              class="tuile-choix tuile-neutre"
              [class.selectionnee]="option.valeur === style()"
              role="radio"
              [attr.aria-checked]="option.valeur === style()"
              (click)="style.set(option.valeur)"
            >
              <span class="tuile-icone">{{ option.icone }}</span>
              {{ option.libelle }}
            </button>
          }
        </div>
      </div>

      <div class="champ">
        <span class="etiquette-champ">Niveau en cuisine</span>
        <div class="grille-tuiles" role="radiogroup" aria-label="Niveau en cuisine">
          @for (option of niveauxCuisine; track option.valeur) {
            <button
              type="button"
              class="tuile-choix tuile-neutre"
              [class.selectionnee]="option.valeur === niveau()"
              role="radio"
              [attr.aria-checked]="option.valeur === niveau()"
              (click)="niveau.set(option.valeur)"
            >
              <span class="tuile-icone">{{ option.icone }}</span>
              {{ option.libelle }}
            </button>
          }
        </div>
      </div>

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
      .champ {
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

      .grille-tuiles {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(6rem, 1fr));
        gap: 0.5rem;
      }

      .tuile-choix {
        padding: 0.6rem 0.4rem;
        border-radius: 0.5rem;
        border: 2px solid transparent;
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

      .tuile-choix:hover {
        transform: translateY(-2px);
        box-shadow: var(--ombre-carte-hover);
      }

      .tuile-icone {
        display: block;
        font-size: 1.1rem;
        margin-bottom: 0.15rem;
      }

      /* Tuiles d'enseigne : couleur de marque en fond (voir ENSEIGNES), texte
         blanc par defaut, bascule en sombre pour les couleurs claires. */
      .tuile-marque {
        color: #fff;
      }

      .tuile-marque.texte-fonce {
        color: #1f2421;
      }

      .tuile-marque.selectionnee {
        border-color: var(--couleur-texte);
        box-shadow: var(--ombre-carte-hover);
      }

      /* Tuiles neutres (style alimentaire, niveau) : pas de marque a evoquer,
         simple fond de l'appli avec mise en evidence a la selection. */
      .tuile-neutre {
        background: var(--couleur-fond);
        color: var(--couleur-texte);
        border-color: var(--couleur-bordure);
      }

      .tuile-neutre.selectionnee {
        border-color: var(--couleur-accent);
        background: var(--couleur-surface-transparente);
      }

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

      button[type='submit'] {
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
