import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SelectionRepasComponent } from './selection-repas';
import type { ChoixJourRequest, JourSemaine, RepasProposesResponse, RepasResponse, TypeRepas } from '../../core/models';

function unRepas(id: string, nom: string, type: TypeRepas = 'PETIT_DEJEUNER'): RepasResponse {
  return {
    id,
    nom,
    type,
    style: 'NORMAL',
    niveauRequis: 'DEBUTANT',
    ingredients: [{ ingredient: 'ingredient de test', rayon: 'EPICERIE', quantite: 1, unite: 'UNITE' }],
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

    expect(emis?.['LUNDI']).toEqual({ petitDejeuner: { id: 'pd-1' }, dejeuner: { id: 'dj-1' }, diner: { id: 'dn-1' } });
    expect(emis?.['VENDREDI']).toEqual({ petitDejeuner: { id: 'pd-1' }, dejeuner: { id: 'dj-1' }, diner: { id: 'dn-1' } });
  });

  it('emet le type demande quand on clique sur un bouton "Générer une idée"', () => {
    let typeDemande: TypeRepas | undefined;
    fixture.componentInstance.genererIdee.subscribe((type) => (typeDemande = type));

    const boutons = Array.from(
      (fixture.nativeElement as HTMLElement).querySelectorAll<HTMLButtonElement>('.inspiration-boutons button'),
    );
    expect(boutons.length).toBe(3);
    boutons[2].click(); // Dîner

    expect(typeDemande).toBe('DINER');
  });

  it('affiche automatiquement, avec une note, le repas genere recu du conteneur', () => {
    fixture.componentRef.setInput('repasGenere', unRepas('gen-1', 'Idée surprise', 'DINER'));
    fixture.detectChanges();

    const modale = (fixture.nativeElement as HTMLElement).querySelector('.modale');
    expect(modale?.textContent).toContain('Idée surprise');
    expect(modale?.textContent).toContain('générée');
  });

  it('propose le repas genere comme option dans la liste deroulante du bon type, et peut le selectionner', () => {
    fixture.componentRef.setInput('repasGenere', unRepas('gen-1', 'Dîner surprise', 'DINER'));
    fixture.detectChanges();

    const optionsGenerees = Array.from(
      (fixture.nativeElement as HTMLElement).querySelectorAll('option[value="GENERE"]'),
    );
    // 5 jours, uniquement sur le selecteur Diner :
    expect(optionsGenerees.length).toBe(5);
    expect(optionsGenerees[0].textContent).toContain('Dîner surprise');

    const [, , lundiDiner] = selects();
    choisir(lundiDiner, 'GENERE');

    expect(lundiDiner.value).toBe('GENERE');
  });

  it('emet le repas genere en entier (pas un id) pour le jour ou il a ete choisi', () => {
    fixture.componentRef.setInput('repasGenere', unRepas('gen-1', 'Dîner surprise', 'DINER'));
    fixture.detectChanges();

    let emis: Record<JourSemaine, ChoixJourRequest> | undefined;
    fixture.componentInstance.selectionValidee.subscribe((valeur) => (emis = valeur));

    const tousLesSelects = selects();
    for (let i = 0; i < tousLesSelects.length; i += 3) {
      choisir(tousLesSelects[i], 'pd-1');
      choisir(tousLesSelects[i + 1], 'dj-1');
      choisir(tousLesSelects[i + 2], i === 0 ? 'GENERE' : 'dn-1'); // seul lundi recoit le diner genere
    }

    (fixture.nativeElement as HTMLElement).querySelector<HTMLButtonElement>('.valider')?.click();

    expect(emis?.['LUNDI'].diner).toEqual({
      genere: {
        nom: 'Dîner surprise',
        type: 'DINER',
        style: 'NORMAL',
        niveauRequis: 'DEBUTANT',
        ingredients: [{ ingredient: 'ingredient de test', rayon: 'EPICERIE', quantite: 1, unite: 'UNITE' }],
      },
    });
    expect(emis?.['MARDI'].diner).toEqual({ id: 'dn-1' });
  });
});
