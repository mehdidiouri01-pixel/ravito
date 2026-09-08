import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PlanSemaineComponent } from './plan-semaine';
import type { PlanSemaineResponse } from '../../core/models';

const UN_PLAN: PlanSemaineResponse = {
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
      },
      dejeuner: {
        id: 'dj-1',
        nom: 'Pates au thon',
        type: 'DEJEUNER',
        style: 'NORMAL',
        niveauRequis: 'DEBUTANT',
        ingredients: [{ ingredient: 'pates', rayon: 'EPICERIE', quantite: 150, unite: 'GRAMME' }],
      },
    },
  ],
  listeCourses: { parRayon: { BOULANGERIE: [{ ingredient: 'pain', quantite: 100, unite: 'GRAMME' }] } },
  prixEstime: { montant: 5.5 },
};

describe('PlanSemaineComponent', () => {
  let fixture: ComponentFixture<PlanSemaineComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlanSemaineComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(PlanSemaineComponent);
    fixture.componentRef.setInput('plan', UN_PLAN);
    fixture.componentRef.setInput('nombreDePersonnes', 2);
    fixture.detectChanges();
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
});
