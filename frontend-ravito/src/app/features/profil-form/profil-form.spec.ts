import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProfilFormComponent } from './profil-form';
import type { ProfilRequest } from '../../core/models';

describe('ProfilFormComponent', () => {
  let fixture: ComponentFixture<ProfilFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfilFormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ProfilFormComponent);
    fixture.detectChanges();
  });

  function champNombre(): HTMLInputElement {
    return (fixture.nativeElement as HTMLElement).querySelector('input[type="number"]')!;
  }

  function saisir(valeur: string): void {
    const champ = champNombre();
    champ.value = valeur;
    champ.dispatchEvent(new Event('input'));
    fixture.detectChanges();
  }

  function soumettre(): ProfilRequest | undefined {
    let emis: ProfilRequest | undefined;
    fixture.componentInstance.profilChoisi.subscribe((profil) => (emis = profil));
    (fixture.nativeElement as HTMLElement).querySelector('form')!.dispatchEvent(new Event('submit'));
    return emis;
  }

  it('propose 2 personnes par defaut', () => {
    expect(champNombre().value).toBe('2');
    expect(soumettre()?.nombreDePersonnes).toBe(2);
  });

  it('transmet le nombre de personnes saisi', () => {
    saisir('5');

    expect(soumettre()?.nombreDePersonnes).toBe(5);
  });

  it('ramene une saisie hors bornes dans les limites du domaine', () => {
    saisir('0');
    expect(soumettre()?.nombreDePersonnes).toBe(1);

    saisir('99');
    expect(soumettre()?.nombreDePersonnes).toBe(12);
  });
});
