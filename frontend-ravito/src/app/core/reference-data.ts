import type { Enseigne, JourSemaine, NiveauCuisine, RayonMagasin, StyleAlimentaire, UniteMesure } from './models';

/** Les 5 jours couverts par un plan, dans l'ordre — miroir de JourSemaine (domaine). */
export const JOURS: JourSemaine[] = ['LUNDI', 'MARDI', 'MERCREDI', 'JEUDI', 'VENDREDI'];

export const ENSEIGNES: { valeur: Enseigne; libelle: string }[] = [
  { valeur: 'CARREFOUR', libelle: 'Carrefour' },
  { valeur: 'LECLERC', libelle: 'Leclerc' },
  { valeur: 'LIDL', libelle: 'Lidl' },
  { valeur: 'AUCHAN', libelle: 'Auchan' },
];

export const STYLES_ALIMENTAIRES: { valeur: StyleAlimentaire; libelle: string }[] = [
  { valeur: 'HEALTHY', libelle: 'Healthy' },
  { valeur: 'NORMAL', libelle: 'Normal' },
  { valeur: 'GOURMAND', libelle: 'Gourmand' },
];

export const NIVEAUX_CUISINE: { valeur: NiveauCuisine; libelle: string }[] = [
  { valeur: 'DEBUTANT', libelle: 'Débutant' },
  { valeur: 'CONFIRME', libelle: 'Confirmé' },
];

export const LIBELLES_JOUR: Record<JourSemaine, string> = {
  LUNDI: 'Lundi',
  MARDI: 'Mardi',
  MERCREDI: 'Mercredi',
  JEUDI: 'Jeudi',
  VENDREDI: 'Vendredi',
};

export const LIBELLES_RAYON: Record<RayonMagasin, string> = {
  FRUITS_LEGUMES: 'Fruits & légumes',
  BOUCHERIE: 'Boucherie',
  POISSONNERIE: 'Poissonnerie',
  CREMERIE: 'Crémerie',
  EPICERIE: 'Épicerie',
  SURGELES: 'Surgelés',
  BOULANGERIE: 'Boulangerie',
  BOISSONS: 'Boissons',
  AUTRE: 'Autre',
};

export const LIBELLES_UNITE: Record<UniteMesure, string> = {
  GRAMME: 'g',
  KILOGRAMME: 'kg',
  MILLILITRE: 'ml',
  LITRE: 'l',
  UNITE: 'unité(s)',
  CUILLERE_A_SOUPE: 'c. à soupe',
  CUILLERE_A_CAFE: 'c. à café',
  PINCEE: 'pincée(s)',
};
