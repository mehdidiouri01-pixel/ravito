import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SelectionRepasComponent } from './selection-repas';
import type { ChoixJourRequest, JourSemaine, RepasProposesResponse } from '../../core/models';

function unRepas(id: string, nom: string) {
  return {
    id,
    nom,
    type: 'PETIT_DEJEUNER' as const,
    style: 'NORMAL' as const,
    niveauRequis: 'DEBUTANT' as const,
    ingredients: [],
  };
}

const PROPOSITION: RepasProposesResponse = {
  petitsDejeuners: [unRepas('pd-1', 'Tartines'), unRepas('pd-2', 'Céréales')],
  dejeuners: [unRepas('dj-1', 'Pâtes'), unRepas('dj-2', 'Riz')],
  diners: [unRepas('dn-1', 'Soupe'), unRepas('dn-2', 'Gratin')],
};

describe('SelectionRepasComponent', () => {
  let fixture: ComponentFixture<SelectionRepasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SelectionRepasComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SelectionRepasComponent);
    fixture.componentRef.setInput('proposition', PROPOSITION);
    fixture.detectChanges();
  });

  function selects(): HTMLSelectElement[] {
    return Array.from((fixture.nativeElement as HTMLElement).querySelectorAll('select'));
  }

  /** Simule le choix d'une option, comme le ferait un utilisateur reel. */
  function choisir(select: HTMLSelectElement, valeur: string): void {
    select.value = valeur;
    select.dispatchEvent(new Event('change'));
    fixture.detectChanges();
  }

  it('affiche une ligne par jour, avec 3 listes deroulantes chacune', () => {
    const lignes = (fixture.nativeElement as HTMLElement).querySelectorAll('.jour-ligne');
    expect(lignes.length).toBe(5);
    expect(selects().length).toBe(15);
  });

  it('desactive la validation tant que les 5 jours ne sont pas complets', () => {
    const bouton = (fixture.nativeElement as HTMLElement).querySelector<HTMLButtonElement>('.valider');
    expect(bouton?.disabled).toBeTrue();
  });

  it('accepte le meme repas pour deux jours differents (doublon autorise)', () => {
    // selects() : [lundi-petitDej, lundi-dej, lundi-diner, mardi-petitDej, ...]
    const [lundiPetitDej, , , mardiPetitDej] = selects();

    choisir(lundiPetitDej, 'pd-1');
    choisir(mardiPetitDej, 'pd-1'); // meme petit-dejeuner que lundi, sans erreur

    expect(lundiPetitDej.value).toBe('pd-1');
    expect(mardiPetitDej.value).toBe('pd-1');
  });

  it('permet de ne remplir que certains jours, dans le desordre', () => {
    // lundi(0-2), mardi(3-5), mercredi(6-8), jeudi(9-11) : 3*3 = 9
    const [, , , , , , , , , jeudiPetitDej, jeudiDejeuner, jeudiDiner] = selects();

    choisir(jeudiPetitDej, 'pd-1');
    choisir(jeudiDejeuner, 'dj-1');
    choisir(jeudiDiner, 'dn-1');

    expect((fixture.nativeElement as HTMLElement).textContent).toContain('1/5');
  });

  function boutonsRecette(): HTMLButtonElement[] {
    return Array.from((fixture.nativeElement as HTMLElement).querySelectorAll('.voir-recette'));
  }

  it('desactive le bouton Recette tant qu\'aucun repas n\'est choisi pour cette case', () => {
    expect(boutonsRecette()[0].disabled).toBeTrue(); // lundi petit-dejeuner
  });

  it('affiche la recette du repas choisi, pas celle d\'un autre jour', () => {
    const [lundiPetitDej] = selects();
    choisir(lundiPetitDej, 'pd-1'); // "Tartines"

    boutonsRecette()[0].click();
    fixture.detectChanges();

    const modale = (fixture.nativeElement as HTMLElement).querySelector('.modale');
    expect(modale?.textContent).toContain('Tartines');
  });

  it('emet la selection complete, jour par jour, une fois les 5 jours remplis', () => {
    let emis: Record<JourSemaine, ChoixJourRequest> | undefined;
    fixture.componentInstance.selectionValidee.subscribe((valeur) => (emis = valeur));

    // Remplit chaque jour : colonnes 0=petit-dejeuner, 1=dejeuner, 2=diner.
    const tousLesSelects = selects();
    for (let i = 0; i < tousLesSelects.length; i += 3) {
      choisir(tousLesSelects[i], 'pd-1');
      choisir(tousLesSelects[i + 1], 'dj-1');
      choisir(tousLesSelects[i + 2], 'dn-1');
    }

    (fixture.nativeElement as HTMLElement).querySelector<HTMLButtonElement>('.valider')?.click();

    expect(emis?.['LUNDI']).toEqual({ petitDejeunerId: 'pd-1', dejeunerId: 'dj-1', dinerId: 'dn-1' });
    expect(emis?.['VENDREDI']).toEqual({ petitDejeunerId: 'pd-1', dejeunerId: 'dj-1', dinerId: 'dn-1' });
  });
});
