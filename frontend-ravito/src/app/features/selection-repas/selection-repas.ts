import { Component, computed, effect, input, output, signal } from '@angular/core';
import { JOURS, LIBELLES_JOUR } from '../../core/reference-data';
import type {
  ChoixJourRequest,
  ChoixRepasRequest,
  JourSemaine,
  RepasProposesResponse,
  RepasResponse,
  TypeRepas,
} from '../../core/models';
import { RecetteModaleComponent } from '../../ui/recette-modale/recette-modale';

type CleRepas = 'petitDejeuner' | 'dejeuner' | 'diner';

const CHAMPS_REPAS: { cle: CleRepas; libelle: string; type: TypeRepas; catalogue: (p: RepasProposesResponse) => RepasResponse[] }[] = [
  { cle: 'petitDejeuner', libelle: 'Petit-déjeuner', type: 'PETIT_DEJEUNER', catalogue: (p) => p.petitsDejeuners },
  { cle: 'dejeuner', libelle: 'Déjeuner', type: 'DEJEUNER', catalogue: (p) => p.dejeuners },
  { cle: 'diner', libelle: 'Dîner', type: 'DINER', catalogue: (p) => p.diners },
];

/**
 * Le choix de l'utilisateur pour un seul repas d'une case : vide, un id du
 * catalogue, ou un repas généré capturé tel quel au moment du choix (pas une
 * référence vivante vers le signal `repasGenere` — si l'utilisateur génère
 * une nouvelle idée après avoir choisi celle-ci, ce choix ne doit pas
 * changer sous ses pieds).
 *
 * <p>L'option d'un repas généré utilise son propre id (déjà unique, généré
 * côté serveur) comme valeur — jamais un sentinel générique du style
 * "GENERE" : un sentinel désigne "le repas généré du moment", une notion
 * qui change dès qu'on génère une nouvelle idée. Si la case A avait choisi
 * l'idée précédente pendant qu'on en génère une nouvelle pour la case B,
 * un sentinel ferait soit disparaître le choix de A (aucune option ne
 * correspond plus), soit pire, le ferait basculer silencieusement vers la
 * nouvelle idée sans que l'utilisateur l'ait demandé.
 */
type ChoixRepasPartiel = { kind: 'vide' } | { kind: 'id'; id: string } | { kind: 'genere'; repas: RepasResponse };

const CHOIX_VIDE_REPAS: ChoixRepasPartiel = { kind: 'vide' };

interface ChoixJourPartiel {
  petitDejeuner: ChoixRepasPartiel;
  dejeuner: ChoixRepasPartiel;
  diner: ChoixRepasPartiel;
}

const CHOIX_VIDE: ChoixJourPartiel = {
  petitDejeuner: CHOIX_VIDE_REPAS,
  dejeuner: CHOIX_VIDE_REPAS,
  diner: CHOIX_VIDE_REPAS,
};

const TYPES_REPAS: { type: TypeRepas; libelle: string }[] = [
  { type: 'PETIT_DEJEUNER', libelle: 'Petit-déjeuner' },
  { type: 'DEJEUNER', libelle: 'Déjeuner' },
  { type: 'DINER', libelle: 'Dîner' },
];

interface SlotEnEdition {
  jour: JourSemaine;
  cle: CleRepas;
}

/**
 * Une ligne par jour, chacune avec 3 cases de choix independantes. Rien
 * n'empeche de choisir le meme repas pour deux jours differents, ni de
 * remplir les jours dans un ordre quelconque — chaque jour est une
 * selection independante, pas une file d'attente.
 *
 * <p>Le choix lui-meme se fait via une pop-up de tuiles cliquables (voir
 * `slotOuvert`/`choix-modale` dans le template), pas une liste deroulante
 * native : garder les 15 cases compactes sur la page (un simple bouton
 * affichant la selection courante) tout en rendant le choix lui-meme
 * visuel evite d'avoir a afficher d'un coup jusqu'a 15 x 15 tuiles en
 * permanence, ce qui rendrait la page ingerable.
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

      <section class="inspiration">
        <h3>Besoin d'inspiration ?</h3>
        <p class="aide">
          Génère une idée de repas au hasard, à partir des ingrédients déjà au catalogue — elle apparaît ensuite en
          haut du choix correspondant, prête à être choisie pour n'importe quel jour.
        </p>
        <div class="inspiration-boutons">
          @for (item of typesRepas; track item.type) {
            <button type="button" (click)="genererIdee.emit(item.type)">✨ {{ item.libelle }}</button>
          }
        </div>
      </section>

      <div class="jours">
        @for (jour of jours; track jour) {
          <article class="jour-ligne">
            <h3>{{ libellesJour[jour] }}</h3>

            <div class="selecteurs">
              @for (champ of champsRepas; track champ.cle) {
                <label>
                  {{ champ.libelle }}
                  <div class="select-avec-recette">
                    <button type="button" class="choix-repas-bouton" (click)="ouvrirChoix(jour, champ.cle)">
                      {{ libelleChoixCourant(jour, champ.cle) ?? '— Choisir —' }}
                    </button>
                    <button
                      type="button"
                      class="voir-recette"
                      [disabled]="estVide(choix()[jour][champ.cle])"
                      (click)="afficherRecette(jour, champ.cle)"
                    >
                      Recette
                    </button>
                  </div>
                </label>
              }
            </div>
          </article>
        }
      </div>

      <button type="button" class="valider" [disabled]="!selectionComplete()" (click)="valider()">
        Composer mon plan de la semaine
      </button>
    </section>

    @if (slotOuvert(); as slot) {
      <div class="choix-fond" (click)="fermerChoix()">
        <div class="choix-modale" role="dialog" aria-modal="true" (click)="$event.stopPropagation()">
          <button type="button" class="fermer" (click)="fermerChoix()" aria-label="Fermer le choix">✕</button>
          <h3>{{ libellesJour[slot.jour] }} · {{ libelleChamp(slot.cle) }}</h3>
          <div class="choix-tuiles">
            <button
              type="button"
              class="choix-tuile choix-tuile-vide"
              [class.selectionnee]="estVide(choix()[slot.jour][slot.cle])"
              (click)="choisirDepuisPicker(slot.jour, slot.cle, '')"
            >
              — Aucun —
            </button>
            @for (genere of optionsGenerees(slot.jour, slot.cle, champType(slot.cle)); track genere.id) {
              <button
                type="button"
                class="choix-tuile choix-tuile-genere"
                [class.selectionnee]="estGenereSelectionne(choix()[slot.jour][slot.cle], genere.id)"
                (click)="choisirDepuisPicker(slot.jour, slot.cle, genere.id)"
              >
                <span>✨ {{ genere.nom }}</span>
                <span class="niveau">Idée générée</span>
              </button>
            }
            @for (repas of catalogueDuChamp(slot.cle); track repas.id) {
              <button
                type="button"
                class="choix-tuile"
                [class.selectionnee]="estIdSelectionne(choix()[slot.jour][slot.cle], repas.id)"
                (click)="choisirDepuisPicker(slot.jour, slot.cle, repas.id)"
              >
                <span>{{ repas.nom }}</span>
                <span class="niveau">{{ repas.niveauRequis === 'CONFIRME' ? 'Confirmé' : 'Débutant' }}</span>
              </button>
            }
          </div>
        </div>
      </div>
    }

    <app-recette-modale [repas]="repasAffiche()" [note]="noteRepasAffiche()" (fermer)="fermerRecette()" />
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

      .inspiration {
        margin-top: 1rem;
        padding: 0.85rem 1rem;
        border-radius: var(--rayon);
        border: 1px dashed var(--couleur-accent);
        background: var(--couleur-surface-transparente);
        backdrop-filter: blur(6px);
      }

      .inspiration h3 {
        margin: 0 0 0.25rem;
        font-size: 1rem;
      }

      .inspiration .aide {
        margin: 0 0 0.75rem;
        font-size: 0.85rem;
      }

      .inspiration-boutons {
        display: flex;
        flex-wrap: wrap;
        gap: 0.5rem;
      }

      .inspiration-boutons button {
        background: transparent;
        color: var(--couleur-accent);
        border: 1px solid var(--couleur-accent);
        padding: 0.4rem 0.75rem;
        font-size: 0.85rem;
      }

      .jours {
        display: flex;
        flex-direction: column;
        gap: 0.75rem;
        margin: 1.5rem 0 2rem;
      }

      .jour-ligne {
        padding: 0.85rem 1rem;
        border-radius: var(--rayon);
        border: 1px solid var(--couleur-bordure);
        background: var(--couleur-surface-transparente);
        backdrop-filter: blur(6px);
        box-shadow: var(--ombre-carte);
        transition:
          transform 0.15s ease,
          box-shadow 0.15s ease;
      }

      .jour-ligne:hover {
        transform: translateY(-2px);
        box-shadow: var(--ombre-carte-hover);
      }

      .jour-ligne h3 {
        margin: 0 0 0.6rem;
        font-size: 1rem;
      }

      /* flex-wrap plutot qu'une grille a colonnes fixes : chaque selecteur a
         une largeur minimale confortable (13rem) et retombe naturellement a
         la ligne quand la place manque, a n'importe quelle largeur d'ecran —
         plus robuste qu'un seul point de rupture fixe qui doit deviner la
         largeur exacte ou 4 colonnes cessent de tenir. */
      .selecteurs {
        display: flex;
        flex-wrap: wrap;
        gap: 1rem;
      }

      .jour-ligne label {
        display: flex;
        flex-direction: column;
        gap: 0.3rem;
        font-size: 0.8rem;
        font-weight: 600;
        color: var(--couleur-texte-att);
        flex: 1 1 13rem;
        min-width: 0;
      }

      .select-avec-recette {
        display: flex;
        gap: 0.4rem;
        min-width: 0;
      }

      .choix-repas-bouton {
        flex: 1;
        min-width: 0;
        text-align: left;
        font: inherit;
        padding: 0.5rem 0.6rem;
        border-radius: 0.5rem;
        border: 1px solid var(--couleur-bordure);
        background: var(--couleur-fond);
        color: var(--couleur-texte);
        cursor: pointer;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .choix-repas-bouton:hover {
        border-color: var(--couleur-accent);
      }

      .voir-recette {
        flex-shrink: 0;
        background: transparent;
        color: var(--couleur-accent);
        border: 1px solid var(--couleur-accent);
        padding: 0.3rem 0.6rem;
        font-size: 0.75rem;
      }

      .valider {
        margin-top: 0.5rem;
      }

      .choix-fond {
        position: fixed;
        inset: 0;
        background: rgba(31, 36, 33, 0.5);
        backdrop-filter: blur(3px);
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 1.5rem;
        z-index: 10;
        animation: choix-voile-entree 0.2s ease both;
      }

      .choix-modale {
        position: relative;
        background: var(--couleur-surface);
        border-radius: var(--rayon);
        padding: 1.75rem;
        max-width: 34rem;
        width: 100%;
        max-height: 80vh;
        overflow-y: auto;
        box-shadow: var(--ombre-carte-hover);
        animation: choix-modale-entree 0.25s ease both;
      }

      @keyframes choix-voile-entree {
        from {
          opacity: 0;
        }
        to {
          opacity: 1;
        }
      }

      @keyframes choix-modale-entree {
        from {
          opacity: 0;
          transform: translateY(0.75rem) scale(0.97);
        }
        to {
          opacity: 1;
          transform: translateY(0) scale(1);
        }
      }

      .choix-modale h3 {
        margin: 0 0 1rem;
        padding-right: 2rem;
      }

      .choix-tuiles {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(9rem, 1fr));
        gap: 0.5rem;
      }

      .choix-tuile {
        display: flex;
        flex-direction: column;
        gap: 0.2rem;
        padding: 0.6rem 0.5rem;
        border-radius: 0.5rem;
        border: 2px solid var(--couleur-bordure);
        background: var(--couleur-fond);
        color: var(--couleur-texte);
        font-family: inherit;
        font-size: 0.8rem;
        font-weight: 600;
        text-align: left;
        cursor: pointer;
        transition:
          transform 0.12s ease,
          box-shadow 0.12s ease,
          border-color 0.12s ease;
      }

      .choix-tuile:hover {
        transform: translateY(-2px);
        box-shadow: var(--ombre-carte-hover);
      }

      .choix-tuile.selectionnee {
        border-color: var(--couleur-accent);
        background: var(--couleur-surface-transparente);
      }

      .choix-tuile .niveau {
        font-weight: 400;
        font-size: 0.7rem;
        color: var(--couleur-texte-att);
      }

      .choix-tuile-vide {
        font-style: italic;
        color: var(--couleur-texte-att);
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
export class SelectionRepasComponent {
  readonly proposition = input.required<RepasProposesResponse>();
  /** Dernier repas généré par le conteneur (voir PlanificateurComponent), proposable dans le choix. */
  readonly repasGenere = input<RepasResponse | null>(null);
  readonly selectionValidee = output<Record<JourSemaine, ChoixJourRequest>>();
  readonly genererIdee = output<TypeRepas>();

  protected readonly jours = JOURS;
  protected readonly libellesJour = LIBELLES_JOUR;
  protected readonly typesRepas = TYPES_REPAS;
  protected readonly champsRepas = CHAMPS_REPAS;

  protected readonly choix = signal<Record<JourSemaine, ChoixJourPartiel>>(
    Object.fromEntries(JOURS.map((jour) => [jour, { ...CHOIX_VIDE }])) as Record<JourSemaine, ChoixJourPartiel>,
  );

  protected readonly repasAffiche = signal<RepasResponse | null>(null);
  protected readonly noteRepasAffiche = signal<string | null>(null);

  /** La case (jour, repas) dont la pop-up de choix par tuiles est actuellement ouverte, ou aucune. */
  protected readonly slotOuvert = signal<SlotEnEdition | null>(null);

  constructor() {
    // Chaque nouvelle idee generee par le conteneur s'affiche automatiquement
    // dans la pop-up de recette a titre d'apercu, et devient choisissable
    // dans le choix correspondant (voir repasGenerePour).
    effect(() => {
      const genere = this.repasGenere();
      if (genere) {
        this.repasAffiche.set(genere);
        this.noteRepasAffiche.set(
          '💡 Idée générée aléatoirement à partir du catalogue — choisissez-la dans le choix du repas concerné pour l\'ajouter à votre semaine.',
        );
      }
    });
  }

  protected readonly joursComplets = computed(
    () => this.jours.filter((jour) => this.estComplet(this.choix()[jour])).length,
  );

  protected readonly selectionComplete = computed(() => this.joursComplets() === this.jours.length);

  /** Le repas genere du moment, seulement s'il correspond au type de ce champ (pas de proposition croisee). */
  protected repasGenerePour(type: TypeRepas): RepasResponse | null {
    const genere = this.repasGenere();
    return genere && genere.type === type ? genere : null;
  }

  /**
   * Les idees generees a proposer pour une case precise : celle deja
   * choisie pour cette case (pour que l'option reste presente et
   * selectionnee meme apres qu'une idee plus recente a ete generee
   * ailleurs), et celle du moment si elle est nouvelle — deux idees
   * distinctes si l'utilisateur en a genere plusieurs sans toutes les
   * choisir, dedupliquees par id sinon.
   */
  protected optionsGenerees(jour: JourSemaine, cle: CleRepas, type: TypeRepas): RepasResponse[] {
    const options: RepasResponse[] = [];
    const choixActuel = this.choix()[jour][cle];
    if (choixActuel.kind === 'genere') {
      options.push(choixActuel.repas);
    }
    const genereDuMoment = this.repasGenerePour(type);
    if (genereDuMoment && !options.some((r) => r.id === genereDuMoment.id)) {
      options.push(genereDuMoment);
    }
    return options;
  }

  protected estVide(choix: ChoixRepasPartiel): boolean {
    return choix.kind === 'vide';
  }

  protected estIdSelectionne(choix: ChoixRepasPartiel, id: string): boolean {
    return choix.kind === 'id' && choix.id === id;
  }

  protected estGenereSelectionne(choix: ChoixRepasPartiel, id: string): boolean {
    return choix.kind === 'genere' && choix.repas.id === id;
  }

  protected champType(cle: CleRepas): TypeRepas {
    return this.champsRepas.find((c) => c.cle === cle)!.type;
  }

  protected libelleChamp(cle: CleRepas): string {
    return this.champsRepas.find((c) => c.cle === cle)!.libelle;
  }

  protected catalogueDuChamp(cle: CleRepas): RepasResponse[] {
    const champ = this.champsRepas.find((c) => c.cle === cle)!;
    return champ.catalogue(this.proposition());
  }

  /** Le nom a afficher sur le bouton de la case, ou null si rien n'est encore choisi. */
  protected libelleChoixCourant(jour: JourSemaine, cle: CleRepas): string | null {
    const choix = this.choix()[jour][cle];
    if (choix.kind === 'vide') {
      return null;
    }
    if (choix.kind === 'genere') {
      return `✨ ${choix.repas.nom}`;
    }
    return this.catalogueDuChamp(cle).find((r) => r.id === choix.id)?.nom ?? null;
  }

  protected ouvrirChoix(jour: JourSemaine, cle: CleRepas): void {
    this.slotOuvert.set({ jour, cle });
  }

  protected fermerChoix(): void {
    this.slotOuvert.set(null);
  }

  protected choisirDepuisPicker(jour: JourSemaine, cle: CleRepas, valeur: string): void {
    this.choisir(jour, cle, valeur);
    this.slotOuvert.set(null);
  }

  protected choisir(jour: JourSemaine, cle: CleRepas, valeur: string): void {
    let nouveauChoix: ChoixRepasPartiel;
    if (valeur === '') {
      nouveauChoix = { kind: 'vide' };
    } else {
      // La valeur correspond-elle a une idee generee proposee pour cette case
      // (celle du moment, ou celle deja choisie qu'on reselectionne) ? Sinon
      // c'est forcement un id du catalogue.
      const champ = this.champsRepas.find((c) => c.cle === cle)!;
      const genere = this.optionsGenerees(jour, cle, champ.type).find((r) => r.id === valeur);
      nouveauChoix = genere ? { kind: 'genere', repas: genere } : { kind: 'id', id: valeur };
    }
    this.choix.update((actuel) => ({ ...actuel, [jour]: { ...actuel[jour], [cle]: nouveauChoix } }));
  }

  /**
   * Affiche la recette du repas actuellement choisi dans la case (jour,
   * type). Rien a afficher si la case est encore vide — le bouton est de
   * toute facon desactive dans ce cas (voir le template).
   */
  protected afficherRecette(jour: JourSemaine, cle: CleRepas): void {
    const choix = this.choix()[jour][cle];
    if (choix.kind === 'id') {
      const repas = this.catalogueDuChamp(cle).find((r) => r.id === choix.id);
      if (repas) {
        this.noteRepasAffiche.set(null);
        this.repasAffiche.set(repas);
      }
    } else if (choix.kind === 'genere') {
      this.noteRepasAffiche.set('💡 Idée générée aléatoirement — sélectionnée pour ce repas.');
      this.repasAffiche.set(choix.repas);
    }
  }

  protected fermerRecette(): void {
    this.repasAffiche.set(null);
    this.noteRepasAffiche.set(null);
  }

  protected valider(): void {
    if (!this.selectionComplete()) {
      return;
    }
    const requete = Object.fromEntries(
      this.jours.map((jour) => [jour, this.versChoixJourRequest(this.choix()[jour])]),
    ) as Record<JourSemaine, ChoixJourRequest>;
    this.selectionValidee.emit(requete);
  }

  private estComplet(choixJour: ChoixJourPartiel): boolean {
    return (
      choixJour.petitDejeuner.kind !== 'vide' &&
      choixJour.dejeuner.kind !== 'vide' &&
      choixJour.diner.kind !== 'vide'
    );
  }

  private versChoixJourRequest(choixJour: ChoixJourPartiel): ChoixJourRequest {
    return {
      petitDejeuner: this.versChoixRepasRequest(choixJour.petitDejeuner),
      dejeuner: this.versChoixRepasRequest(choixJour.dejeuner),
      diner: this.versChoixRepasRequest(choixJour.diner),
    };
  }

  /** @throws si choix est 'vide' — ne doit jamais arriver ici, selectionComplete() l'a deja garanti avant l'appel. */
  private versChoixRepasRequest(choix: ChoixRepasPartiel): ChoixRepasRequest {
    if (choix.kind === 'id') {
      return { id: choix.id };
    }
    if (choix.kind === 'genere') {
      const { nom, type, style, niveauRequis, ingredients, etapesPreparation } = choix.repas;
      return {
        genere: {
          nom,
          type,
          style,
          niveauRequis,
          ingredients: ingredients.map((i) => ({
            ingredient: i.ingredient,
            rayon: i.rayon,
            quantite: i.quantite,
            unite: i.unite,
          })),
          etapesPreparation,
        },
      };
    }
    throw new Error('Choix de repas incomplet — ne devrait jamais arriver apres selectionComplete()');
  }
}
