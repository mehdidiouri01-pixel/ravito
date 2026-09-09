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
    etapesPreparation: ['Etape de test'],
    valeursNutritionnelles: { calories: 250, proteines: 12, glucides: 30, lipides: 8, fibres: 4 },
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

  function boutonsChoix(): HTMLButtonElement[] {
    return Array.from((fixture.nativeElement as HTMLElement).querySelectorAll<HTMLButtonElement>('.choix-repas-bouton'));
  }

  function tuilesChoix(): HTMLButtonElement[] {
    return Array.from((fixture.nativeElement as HTMLElement).querySelectorAll<HTMLButtonElement>('.choix-tuile'));
  }

  /** Ouvre la pop-up de choix de la case (index dans boutonsChoix()) et clique la tuile dont le texte contient `nom`. */
  function choisirParTuile(indexBouton: number, nom: string): void {
    boutonsChoix()[indexBouton].click();
    fixture.detectChanges();

    const tuile = tuilesChoix().find((t) => t.textContent?.includes(nom));
    expect(tuile).withContext(`tuile "${nom}" introuvable`).toBeDefined();
    tuile!.click();
    fixture.detectChanges();
  }

  it('affiche une ligne par jour, avec 3 cases de choix chacune', () => {
    const lignes = (fixture.nativeElement as HTMLElement).querySelectorAll('.jour-ligne');
    expect(lignes.length).toBe(5);
    expect(boutonsChoix().length).toBe(15);
  });

  it('desactive la validation tant que les 5 jours ne sont pas complets', () => {
    const bouton = (fixture.nativeElement as HTMLElement).querySelector<HTMLButtonElement>('.valider');
    expect(bouton?.disabled).toBeTrue();
  });

  it('accepte le meme repas pour deux jours differents (doublon autorise)', () => {
    // boutonsChoix() : [lundi-petitDej, lundi-dej, lundi-diner, mardi-petitDej, ...]
    choisirParTuile(0, 'Tartines'); // lundi petit-dejeuner
    choisirParTuile(3, 'Tartines'); // mardi petit-dejeuner, meme repas que lundi, sans erreur

    expect(boutonsChoix()[0].textContent).toContain('Tartines');
    expect(boutonsChoix()[3].textContent).toContain('Tartines');
  });

  it('permet de ne remplir que certains jours, dans le desordre', () => {
    // lundi(0-2), mardi(3-5), mercredi(6-8), jeudi(9-11) : 3*3 = 9
    choisirParTuile(9, 'Tartines'); // jeudi petit-dejeuner
    choisirParTuile(10, 'Pâtes'); // jeudi dejeuner
    choisirParTuile(11, 'Soupe'); // jeudi diner

    expect((fixture.nativeElement as HTMLElement).textContent).toContain('1/5');
  });

  function boutonsRecette(): HTMLButtonElement[] {
    return Array.from((fixture.nativeElement as HTMLElement).querySelectorAll('.voir-recette'));
  }

  it('desactive le bouton Recette tant qu\'aucun repas n\'est choisi pour cette case', () => {
    expect(boutonsRecette()[0].disabled).toBeTrue(); // lundi petit-dejeuner
  });

  it('ferme la pop-up de choix sans rien selectionner au clic sur "— Aucun —" ou en dehors', () => {
    boutonsChoix()[0].click();
    fixture.detectChanges();
    expect((fixture.nativeElement as HTMLElement).querySelector('.choix-modale')).not.toBeNull();

    (fixture.nativeElement as HTMLElement).querySelector<HTMLButtonElement>('.fermer')?.click();
    fixture.detectChanges();

    expect((fixture.nativeElement as HTMLElement).querySelector('.choix-modale')).toBeNull();
    expect(boutonsChoix()[0].textContent).toContain('— Choisir —');
  });

  it('affiche la recette du repas choisi, pas celle d\'un autre jour', () => {
    choisirParTuile(0, 'Tartines'); // lundi petit-dejeuner

    boutonsRecette()[0].click();
    fixture.detectChanges();

    const modale = (fixture.nativeElement as HTMLElement).querySelector('.modale');
    expect(modale?.textContent).toContain('Tartines');
  });

  it('emet la selection complete, jour par jour, une fois les 5 jours remplis', () => {
    let emis: Record<JourSemaine, ChoixJourRequest> | undefined;
    fixture.componentInstance.selectionValidee.subscribe((valeur) => (emis = valeur));

    // Remplit chaque jour : colonnes 0=petit-dejeuner, 1=dejeuner, 2=diner.
    for (let i = 0; i < 15; i += 3) {
      choisirParTuile(i, 'Tartines');
      choisirParTuile(i + 1, 'Pâtes');
      choisirParTuile(i + 2, 'Soupe');
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

  it('propose le repas genere comme tuile dans le choix du bon type, et peut le selectionner', () => {
    fixture.componentRef.setInput('repasGenere', unRepas('gen-1', 'Dîner surprise', 'DINER'));
    fixture.detectChanges();

    // Present dans le choix Diner de lundi (index 2) :
    boutonsChoix()[2].click();
    fixture.detectChanges();
    expect(tuilesChoix().some((t) => t.textContent?.includes('Dîner surprise'))).toBeTrue();
    (fixture.nativeElement as HTMLElement).querySelector<HTMLButtonElement>('.fermer')?.click();
    fixture.detectChanges();

    // Present aussi dans le choix Diner de mercredi (index 8), pas seulement lundi :
    boutonsChoix()[8].click();
    fixture.detectChanges();
    expect(tuilesChoix().some((t) => t.textContent?.includes('Dîner surprise'))).toBeTrue();
    (fixture.nativeElement as HTMLElement).querySelector<HTMLButtonElement>('.fermer')?.click();
    fixture.detectChanges();

    choisirParTuile(2, 'Dîner surprise'); // lundi diner

    expect(boutonsChoix()[2].textContent).toContain('Dîner surprise');
  });

  it('ne perd pas (ni ne fait taire) le choix d\'une idee generee quand une nouvelle idee est generee ailleurs', () => {
    // Regression : l'option d'une idee generee utilisait auparavant une
    // valeur sentinel generique ('GENERE'), partagee par toutes les cases —
    // generer une nouvelle idee la faisait disparaitre (ou pire, basculer
    // silencieusement) pour les jours qui avaient deja choisi la precedente.
    fixture.componentRef.setInput('repasGenere', unRepas('gen-1', 'Dîner A', 'DINER'));
    fixture.detectChanges();

    choisirParTuile(2, 'Dîner A'); // lundi diner
    expect(boutonsChoix()[2].textContent).toContain('Dîner A');

    // On genere une idee differente (meme type) sans la choisir nulle part :
    fixture.componentRef.setInput('repasGenere', unRepas('gen-2', 'Dîner B', 'DINER'));
    fixture.detectChanges();

    // Lundi reste sur l'idee A qu'il avait choisie, pas sur la B toute fraiche :
    expect(boutonsChoix()[2].textContent).toContain('Dîner A');
  });

  it('emet le repas genere en entier (pas un id) pour le jour ou il a ete choisi', () => {
    fixture.componentRef.setInput('repasGenere', unRepas('gen-1', 'Dîner surprise', 'DINER'));
    fixture.detectChanges();

    let emis: Record<JourSemaine, ChoixJourRequest> | undefined;
    fixture.componentInstance.selectionValidee.subscribe((valeur) => (emis = valeur));

    for (let i = 0; i < 15; i += 3) {
      choisirParTuile(i, 'Tartines');
      choisirParTuile(i + 1, 'Pâtes');
      choisirParTuile(i + 2, i === 0 ? 'Dîner surprise' : 'Soupe'); // seul lundi recoit le diner genere
    }

    (fixture.nativeElement as HTMLElement).querySelector<HTMLButtonElement>('.valider')?.click();

    expect(emis?.['LUNDI'].diner).toEqual({
      genere: {
        nom: 'Dîner surprise',
        type: 'DINER',
        style: 'NORMAL',
        niveauRequis: 'DEBUTANT',
        ingredients: [{ ingredient: 'ingredient de test', rayon: 'EPICERIE', quantite: 1, unite: 'UNITE' }],
        etapesPreparation: ['Etape de test'],
      },
    });
    expect(emis?.['MARDI'].diner).toEqual({ id: 'dn-1' });
  });
});
