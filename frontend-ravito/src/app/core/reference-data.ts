import type { Enseigne, JourSemaine, NiveauCuisine, RayonMagasin, StyleAlimentaire, UniteMesure } from './models';

/** Les 5 jours couverts par un plan, dans l'ordre — miroir de JourSemaine (domaine). */
export const JOURS: JourSemaine[] = ['LUNDI', 'MARDI', 'MERCREDI', 'JEUDI', 'VENDREDI'];

/**
 * Les 10 principales enseignes de la grande distribution alimentaire en
 * France. `couleur` sert a colorer la tuile de selection (etape 1) — une
 * teinte evocatrice de chaque marque, pas son logo officiel : reproduire
 * les vrais logos (droits d'auteur/marque deposee) n'a pas sa place dans
 * ce depot de code. `texteFonce` bascule le texte de la tuile en sombre
 * pour les couleurs de fond claires (sinon illisible en blanc sur blanc).
 */
export const ENSEIGNES: { valeur: Enseigne; libelle: string; couleur: string; texteFonce?: boolean }[] = [
  { valeur: 'CARREFOUR', libelle: 'Carrefour', couleur: '#004E9F' },
  { valeur: 'LECLERC', libelle: 'E.Leclerc', couleur: '#0033A0' },
  { valeur: 'LIDL', libelle: 'Lidl', couleur: '#0050AA' },
  { valeur: 'AUCHAN', libelle: 'Auchan', couleur: '#E2001A' },
  { valeur: 'INTERMARCHE', libelle: 'Intermarché', couleur: '#ED1C24' },
  { valeur: 'SYSTEME_U', libelle: 'Système U', couleur: '#E4032E' },
  { valeur: 'CASINO', libelle: 'Géant Casino', couleur: '#E4007C' },
  { valeur: 'CORA', libelle: 'Cora', couleur: '#FFCC00', texteFonce: true },
  { valeur: 'ALDI', libelle: 'Aldi', couleur: '#00447C' },
  { valeur: 'MONOPRIX', libelle: 'Monoprix', couleur: '#7A1150' },
];

/**
 * Le style NORMAL est affiche "Équilibré" cote frontend uniquement : le nom
 * technique (base de donnees, enum Java, JSON) ne change pas, seul le
 * libelle utilisateur est renomme — plus parlant que "Normal" a cote de
 * "Healthy" et "Gourmand".
 */
export const STYLES_ALIMENTAIRES: { valeur: StyleAlimentaire; libelle: string; icone: string }[] = [
  { valeur: 'HEALTHY', libelle: 'Healthy', icone: '🥗' },
  { valeur: 'NORMAL', libelle: 'Équilibré', icone: '⚖️' },
  { valeur: 'GOURMAND', libelle: 'Gourmand', icone: '🍰' },
];

export const LIBELLES_STYLE: Record<StyleAlimentaire, string> = Object.fromEntries(
  STYLES_ALIMENTAIRES.map((option) => [option.valeur, option.libelle]),
) as Record<StyleAlimentaire, string>;

export const NIVEAUX_CUISINE: { valeur: NiveauCuisine; libelle: string; icone: string }[] = [
  { valeur: 'DEBUTANT', libelle: 'Débutant', icone: '🌱' },
  { valeur: 'CONFIRME', libelle: 'Confirmé', icone: '👨‍🍳' },
];

/** Bornes du foyer, miroir de NombreDePersonnes (domaine). */
export const NOMBRE_DE_PERSONNES_MIN = 1;
export const NOMBRE_DE_PERSONNES_MAX = 12;
export const NOMBRE_DE_PERSONNES_DEFAUT = 2;

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
