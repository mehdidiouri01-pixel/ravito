/**
 * Types miroir des DTOs REST du backend (voir infrastructure-web). Volontairement
 * distincts de tout modèle "domaine" côté frontend : ce ne sont que des formes de
 * données, sans comportement — le frontend n'a pas de règles métier à protéger,
 * contrairement au backend (voir la Javadoc de PlanSemaine, RepasProposes, etc.).
 */

export type Enseigne = 'CARREFOUR' | 'LECLERC' | 'LIDL' | 'AUCHAN';
export type StyleAlimentaire = 'HEALTHY' | 'NORMAL' | 'GOURMAND';
export type NiveauCuisine = 'DEBUTANT' | 'CONFIRME';
export type TypeRepas = 'PETIT_DEJEUNER' | 'DEJEUNER';
export type JourSemaine = 'LUNDI' | 'MARDI' | 'MERCREDI' | 'JEUDI' | 'VENDREDI';
export type RayonMagasin =
  | 'FRUITS_LEGUMES'
  | 'BOUCHERIE'
  | 'POISSONNERIE'
  | 'CREMERIE'
  | 'EPICERIE'
  | 'SURGELES'
  | 'BOULANGERIE'
  | 'BOISSONS'
  | 'AUTRE';
export type UniteMesure =
  | 'GRAMME'
  | 'KILOGRAMME'
  | 'MILLILITRE'
  | 'LITRE'
  | 'UNITE'
  | 'CUILLERE_A_SOUPE'
  | 'CUILLERE_A_CAFE'
  | 'PINCEE';

export interface ProfilRequest {
  enseigne: Enseigne;
  style: StyleAlimentaire;
  niveau: NiveauCuisine;
  /** Nombre de personnes du foyer : le backend multiplie les quantités en conséquence. */
  nombreDePersonnes: number;
}

export interface IngredientQuantiteResponse {
  ingredient: string;
  rayon: RayonMagasin;
  quantite: number;
  unite: UniteMesure;
}

export interface RepasResponse {
  id: string;
  nom: string;
  type: TypeRepas;
  style: StyleAlimentaire;
  niveauRequis: NiveauCuisine;
  ingredients: IngredientQuantiteResponse[];
}

export interface RepasProposesResponse {
  petitsDejeuners: RepasResponse[];
  dejeuners: RepasResponse[];
}

export interface ChoixJourRequest {
  petitDejeunerId: string;
  dejeunerId: string;
}

export interface ComposerPlanSemaineRequest {
  profil: ProfilRequest;
  choix: Record<JourSemaine, ChoixJourRequest>;
}

export interface JourResponse {
  jour: JourSemaine;
  petitDejeuner: RepasResponse;
  dejeuner: RepasResponse;
}

export interface LigneListeCoursesResponse {
  ingredient: string;
  quantite: number;
  unite: UniteMesure;
}

export interface ListeCoursesResponse {
  parRayon: Partial<Record<RayonMagasin, LigneListeCoursesResponse[]>>;
}

export interface PrixResponse {
  montant: number;
}

export interface PlanSemaineResponse {
  jours: JourResponse[];
  listeCourses: ListeCoursesResponse;
  prixEstime: PrixResponse;
}
