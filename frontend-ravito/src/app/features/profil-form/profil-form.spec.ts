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

  function tuilesEnseigne(): HTMLButtonElement[] {
    return Array.from((fixture.nativeElement as HTMLElement).querySelectorAll<HTMLButtonElement>('.enseigne-tuile'));
  }

  it('propose Carrefour par defaut, tuile mise en evidence', () => {
    const tuiles = tuilesEnseigne();
    expect(tuiles.length).toBe(10);

    const tuileCarrefour = tuiles.find((tuile) => tuile.textContent?.trim() === 'Carrefour');
    expect(tuileCarrefour?.classList.contains('selectionnee')).toBeTrue();
    expect(soumettre()?.enseigne).toBe('CARREFOUR');
  });

  it('selectionne une enseigne au clic sur sa tuile, et deplace la mise en evidence', () => {
    const tuiles = tuilesEnseigne();
    const tuileLidl = tuiles.find((tuile) => tuile.textContent?.trim() === 'Lidl')!;

    tuileLidl.click();
    fixture.detectChanges();

    expect(tuileLidl.classList.contains('selectionnee')).toBeTrue();
    expect(tuiles.find((tuile) => tuile.textContent?.trim() === 'Carrefour')?.classList.contains('selectionnee')).toBeFalse();
    expect(soumettre()?.enseigne).toBe('LIDL');
  });
});
