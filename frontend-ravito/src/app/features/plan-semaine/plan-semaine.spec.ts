import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PlanSemaineComponent } from './plan-semaine';
import type { PlanSemaineResponse } from '../../core/models';

const UN_PLAN: PlanSemaineResponse = {
  id: 'plan-1',
  jours: [
    {
      jour: 'LUNDI',
      petitDejeuner: {
        id: 'pd-1',
        nom: 'Tartines beurre confiture',
        type: 'PETIT_DEJEUNER',
        style: 'NORMAL',
        niveauRequis: 'DEBUTANT',
        ingredients: [{ ingredient: 'pain', rayon: 'BOULANGERIE', quantite: 100, unite: 'GRAMME' }],
        etapesPreparation: ['Etape de test'],
        valeursNutritionnelles: { calories: 250, proteines: 12, glucides: 30, lipides: 8, fibres: 4 },
      },
      dejeuner: {
        id: 'dj-1',
        nom: 'Pates au thon',
        type: 'DEJEUNER',
        style: 'NORMAL',
        niveauRequis: 'DEBUTANT',
        ingredients: [{ ingredient: 'pates', rayon: 'EPICERIE', quantite: 150, unite: 'GRAMME' }],
        etapesPreparation: ['Etape de test'],
        valeursNutritionnelles: { calories: 250, proteines: 12, glucides: 30, lipides: 8, fibres: 4 },
      },
      diner: {
        id: 'dn-1',
        nom: 'Soupe de legumes et pain',
        type: 'DINER',
        style: 'NORMAL',
        niveauRequis: 'DEBUTANT',
        ingredients: [{ ingredient: 'soupe', rayon: 'EPICERIE', quantite: 400, unite: 'MILLILITRE' }],
        etapesPreparation: ['Etape de test'],
        valeursNutritionnelles: { calories: 250, proteines: 12, glucides: 30, lipides: 8, fibres: 4 },
      },
    },
  ],
  listeCourses: {
    parRayon: {
      BOULANGERIE: [{ ingredient: 'pain', quantite: 100, unite: 'GRAMME', prixEstime: 0.8 }],
      EPICERIE: [{ ingredient: 'pates', quantite: 150, unite: 'GRAMME', prixEstime: 1.2 }],
    },
  },
  prixEstime: { montant: 5.5 },
};

describe('PlanSemaineComponent', () => {
  let fixture: ComponentFixture<PlanSemaineComponent>;

  beforeEach(async () => {
    localStorage.clear();

    await TestBed.configureTestingModule({
      imports: [PlanSemaineComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(PlanSemaineComponent);
    fixture.componentRef.setInput('plan', UN_PLAN);
    fixture.componentRef.setInput('nombreDePersonnes', 2);
    fixture.detectChanges();
  });

  afterEach(() => {
    localStorage.clear();
  });

  it('ne montre aucune recette par defaut', () => {
    const element = fixture.nativeElement as HTMLElement;
    expect(element.querySelector('.modale')).toBeNull();
  });

  it('affiche la recette du repas cliqué, avec ses ingrédients', () => {
    const element = fixture.nativeElement as HTMLElement;
    const boutonRecette = element.querySelector<HTMLButtonElement>('.voir-recette');

    boutonRecette?.click();
    fixture.detectChanges();

    const modale = element.querySelector('.modale');
    expect(modale?.textContent).toContain('Tartines beurre confiture');
    expect(modale?.textContent).toContain('pain');
  });

  it('ferme la recette au clic sur le bouton de fermeture', () => {
    const element = fixture.nativeElement as HTMLElement;
    element.querySelector<HTMLButtonElement>('.voir-recette')?.click();
    fixture.detectChanges();

    element.querySelector<HTMLButtonElement>('.fermer')?.click();
    fixture.detectChanges();

    expect(element.querySelector('.modale')).toBeNull();
  });

  it('affiche le prix total tant que rien n\'est coché', () => {
    const element = fixture.nativeElement as HTMLElement;
    expect(element.querySelector('.prix')?.textContent).toContain('5.50');
    expect(element.querySelector('.economie')).toBeNull();
  });

  it('grise et barre un ingredient coche, et ajuste le prix en consequence', () => {
    const element = fixture.nativeElement as HTMLElement;
    const caseAcocher = element.querySelector<HTMLInputElement>('.rayon li input[type="checkbox"]');

    caseAcocher!.checked = true;
    caseAcocher?.dispatchEvent(new Event('change'));
    fixture.detectChanges();

    const ligne = element.querySelector('.rayon li');
    expect(ligne?.classList.contains('deja-chez-vous')).toBeTrue();
    // pain a 0.80€ retire du total de 5.50€ : 4.70€ restants.
    expect(element.querySelector('.prix')?.textContent).toContain('4.70');
    expect(element.querySelector('.economie')?.textContent).toContain('0.80');
  });

  it('decoche un ingredient et retrouve le prix complet', () => {
    const element = fixture.nativeElement as HTMLElement;
    const caseAcocher = element.querySelector<HTMLInputElement>('.rayon li input[type="checkbox"]');

    caseAcocher!.checked = true;
    caseAcocher?.dispatchEvent(new Event('change'));
    fixture.detectChanges();

    caseAcocher!.checked = false;
    caseAcocher?.dispatchEvent(new Event('change'));
    fixture.detectChanges();

    const ligne = element.querySelector('.rayon li');
    expect(ligne?.classList.contains('deja-chez-vous')).toBeFalse();
    expect(element.querySelector('.prix')?.textContent).toContain('5.50');
  });

  it('memorise la case cochee dans le navigateur et la retrouve apres un rechargement', () => {
    const element = fixture.nativeElement as HTMLElement;
    const caseAcocher = element.querySelector<HTMLInputElement>('.rayon li input[type="checkbox"]');
    caseAcocher!.checked = true;
    caseAcocher?.dispatchEvent(new Event('change'));
    fixture.detectChanges();

    // simule un rechargement de page : nouveau composant, meme plan (meme id).
    const nouvelleFixture = TestBed.createComponent(PlanSemaineComponent);
    nouvelleFixture.componentRef.setInput('plan', UN_PLAN);
    nouvelleFixture.componentRef.setInput('nombreDePersonnes', 2);
    nouvelleFixture.detectChanges();

    const nouvelElement = nouvelleFixture.nativeElement as HTMLElement;
    expect(nouvelElement.querySelector('.rayon li')?.classList.contains('deja-chez-vous')).toBeTrue();
    expect(nouvelElement.querySelector('.prix')?.textContent).toContain('4.70');
  });
});
