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
    return Array.from((fixture.nativeElement as HTMLElement).querySelectorAll<HTMLButtonElement>('.tuile-marque'));
  }

  function tuilesStyle(): HTMLButtonElement[] {
    const [grilleStyle] = Array.from(
      (fixture.nativeElement as HTMLElement).querySelectorAll('[aria-label="Style alimentaire"]'),
    );
    return Array.from(grilleStyle.querySelectorAll<HTMLButtonElement>('.tuile-choix'));
  }

  function tuilesNiveau(): HTMLButtonElement[] {
    const [grilleNiveau] = Array.from(
      (fixture.nativeElement as HTMLElement).querySelectorAll('[aria-label="Niveau en cuisine"]'),
    );
    return Array.from(grilleNiveau.querySelectorAll<HTMLButtonElement>('.tuile-choix'));
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

  it('propose Equilibre et Debutant par defaut, tuiles mises en evidence', () => {
    expect(tuilesStyle().length).toBe(3);
    expect(tuilesNiveau().length).toBe(2);

    const tuileEquilibre = tuilesStyle().find((tuile) => tuile.textContent?.includes('Équilibré'));
    const tuileDebutant = tuilesNiveau().find((tuile) => tuile.textContent?.includes('Débutant'));
    expect(tuileEquilibre?.classList.contains('selectionnee')).toBeTrue();
    expect(tuileDebutant?.classList.contains('selectionnee')).toBeTrue();

    const emis = soumettre();
    expect(emis?.style).toBe('NORMAL');
    expect(emis?.niveau).toBe('DEBUTANT');
  });

  it('selectionne un style alimentaire et un niveau au clic sur leurs tuiles', () => {
    const tuileGourmand = tuilesStyle().find((tuile) => tuile.textContent?.includes('Gourmand'))!;
    const tuileConfirme = tuilesNiveau().find((tuile) => tuile.textContent?.includes('Confirmé'))!;

    tuileGourmand.click();
    tuileConfirme.click();
    fixture.detectChanges();

    expect(tuileGourmand.classList.contains('selectionnee')).toBeTrue();
    expect(tuileConfirme.classList.contains('selectionnee')).toBeTrue();

    const emis = soumettre();
    expect(emis?.style).toBe('GOURMAND');
    expect(emis?.niveau).toBe('CONFIRME');
  });
});
