# Ravito

Planification de repas sur 5 jours (lundi → vendredi), génération automatique
d'une liste de courses regroupée par rayon, et estimation du coût du panier
selon l'enseigne choisie.

Projet personnel d'apprentissage de l'**architecture hexagonale** (ports &
adapters) en Java/Spring Boot, côté backend.

## Fonctionnel

1. Choix de l'enseigne (Carrefour, Leclerc, Lidl, Auchan).
2. Choix du style alimentaire (HEALTHY, NORMAL, GOURMAND).
3. Choix du niveau en cuisine (DEBUTANT, CONFIRME).
4. L'appli propose des petits-déjeuners et déjeuners filtrés selon ce profil.
5. Sélection d'exactement 5 petits-déjeuners et 5 déjeuners, un par jour.
6. Génération de la liste de courses, ingrédients consolidés et regroupés par
   rayon.
7. Estimation du coût total du panier pour l'enseigne choisie.

## Stack

- **Backend** : Java 21, Spring Boot 3, Maven multi-modules, PostgreSQL,
  Flyway, Docker Compose.
- **Frontend** : Angular 20 (standalone components, signals).

## Architecture

Le backend ([backend-ravito/](backend-ravito)) suit l'architecture
hexagonale : le domaine ne dépend de rien, l'application orchestre les ports
sans logique métier propre, et chaque détail technique (base de données,
HTTP) est isolé dans un module d'infrastructure dédié.

```
backend-ravito/
├── domain/                      modèle métier pur (aucune dépendance framework)
├── application/                 implémente les ports d'entrée du domaine
├── infrastructure-persistence/  adapters JPA/PostgreSQL des ports de sortie
├── infrastructure-web/          adapters REST des ports d'entrée
├── bootstrap/                   module Spring Boot exécutable (composition root)
└── docker-compose.yml           PostgreSQL pour le développement local
```

Règle de dépendance : `application`, `infrastructure-persistence` et
`infrastructure-web` ne dépendent chacun que de `domain`, jamais entre eux.
`bootstrap` est le seul module qui les connaisse tous et les câble ensemble.

Voir la Javadoc des classes de `domain` pour le détail des invariants
métier (`PlanSemaine`, `RepasProposes`, etc.) et le raisonnement derrière
chaque choix d'architecture.

Le frontend ([frontend-ravito/](frontend-ravito)) est une application
Angular 20 standalone (composants + signals, sans NgModule) : un unique
conteneur (`PlanificateurComponent`) orchestre le parcours en 3 étapes
(profil → sélection de 5+5 repas → plan de la semaine) et détient tout
l'état ; les composants enfants sont purement passifs (`input`/`output`).

## Lancer le projet en local

Prérequis : JDK 21, Docker Desktop, Node.js.

**Backend**

```bash
cd backend-ravito
docker compose up -d
mvn spring-boot:run -pl bootstrap -am
```

L'API écoute sur `http://localhost:8080`. Exemple d'appel :

```bash
curl -X POST http://localhost:8080/api/repas/propositions \
  -H "Content-Type: application/json" \
  -d '{"enseigne":"CARREFOUR","style":"NORMAL","niveau":"DEBUTANT"}'
```

**Frontend**

```bash
cd frontend-ravito
npm install
npm start
```

L'application est servie sur `http://localhost:4200` (le backend doit
tourner sur le port 8080 : `proxy.conf.json` y redirige les appels `/api`).

## Tests

```bash
# Backend
cd backend-ravito
mvn test               # tests unitaires (rapides, pas de Docker requis)
mvn verify              # + tests d'intégration Testcontainers (Docker requis)

# Frontend
cd frontend-ravito
npm test
```

## État du projet

- [x] Modèle de domaine (agrégats, value objects, ports, invariants)
- [x] Couche application (implémentation des ports d'entrée)
- [x] Persistance JPA/PostgreSQL, migrations Flyway
- [x] API REST
- [x] Module Spring Boot exécutable, hexagone câblé de bout en bout
- [x] Catalogue complet (15 repas minimum par type et par style)
- [x] Frontend Angular (parcours complet, vérifié de bout en bout)
