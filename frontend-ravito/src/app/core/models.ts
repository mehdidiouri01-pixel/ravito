/**
 * Types miroir des DTOs REST du backend (voir infrastructure-web). Volontairement
 * distincts de tout modèle "domaine" côté frontend : ce ne sont que des formes de
 * données, sans comportement — le frontend n'a pas de règles métier à protéger,
 * contrairement au backend (voir la Javadoc de PlanSemaine, RepasProposes, etc.).
 */

export type Enseigne = 'CARREFOUR' | 'LECLERC' | 'LIDL' | 'AUCHAN';
export type StyleAlimentaire = 'HEALTHY' | 'NORMAL' | 'GOURMAND';
export type NiveauCuisine = 'DEBUTANT' | 'CONFIRME';
export type TypeRepas = 'PETIT_DEJEUNER' | 'DEJEUNER' | 'DINER';
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
  etapesPreparation: string[];
}

export interface RepasProposesResponse {
  petitsDejeuners: RepasResponse[];
  dejeuners: RepasResponse[];
  diners: RepasResponse[];
}

export interface IngredientQuantiteRequest {
  ingredient: string;
  rayon: RayonMagasin;
  quantite: number;
  unite: UniteMesure;
}

/**
 * Un repas genere (voir GenererRepasRequest) transmis en entier : il
 * n'existe dans aucune table, donc pas d'id a fournir — le backend en
 * genere un nouveau et revalide chaque ingredient contre son catalogue
 * connu (voir ChoixRepasRequest).
 */
export interface RepasGenereRequest {
  nom: string;
  type: TypeRepas;
  style: StyleAlimentaire;
  niveauRequis: NiveauCuisine;
  ingredients: IngredientQuantiteRequest[];
  etapesPreparation: string[];
}

/**
 * Le choix pour un seul repas d'une journee : soit un id du catalogue, soit
 * un repas genere transmis en entier — jamais les deux, jamais aucun des
 * deux (voir ChoixRepasRequest cote backend, infrastructure-web).
 */
export type ChoixRepasRequest = { id: string; genere?: undefined } | { id?: undefined; genere: RepasGenereRequest };

export interface ChoixJourRequest {
  petitDejeuner: ChoixRepasRequest;
  dejeuner: ChoixRepasRequest;
  diner: ChoixRepasRequest;
}

export interface GenererRepasRequest {
  profil: ProfilRequest;
  type: TypeRepas;
}

export interface ComposerPlanSemaineRequest {
  profil: ProfilRequest;
  choix: Record<JourSemaine, ChoixJourRequest>;
}

export interface JourResponse {
  jour: JourSemaine;
  petitDejeuner: RepasResponse;
  dejeuner: RepasResponse;
  diner: RepasResponse;
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
