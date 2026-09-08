import { TestBed } from '@angular/core/testing';
import { PdfExportService } from './pdf-export.service';
import type { PlanSemaineResponse } from './models';

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
        ingredients: [],
      },
      dejeuner: {
        id: 'dj-1',
        nom: 'Pates au thon',
        type: 'DEJEUNER',
        style: 'NORMAL',
        niveauRequis: 'DEBUTANT',
        ingredients: [],
      },
      diner: {
        id: 'dn-1',
        nom: 'Soupe de legumes et pain',
        type: 'DINER',
        style: 'NORMAL',
        niveauRequis: 'DEBUTANT',
        ingredients: [],
      },
    },
  ],
  listeCourses: { parRayon: { BOULANGERIE: [{ ingredient: 'pain', quantite: 100, unite: 'GRAMME' }] } },
  prixEstime: { montant: 5.5 },
};

describe('PdfExportService', () => {
  let service: PdfExportService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PdfExportService);
  });

  it('genere un document sans lever d\'exception', () => {
    // On ne verifie pas le contenu binaire du PDF (peu de valeur, beaucoup
    // de fragilite) — seulement que la mise en forme (jours + tableau de
    // la liste de courses + total) ne plante pas sur un plan reel.
    expect(() => service.exporterPlan(UN_PLAN, 4)).not.toThrow();
  });
});
