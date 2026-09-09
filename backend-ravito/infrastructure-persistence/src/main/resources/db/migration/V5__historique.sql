-- Historique des plans de semaine composes. Chaque plan est fige tel qu'il
-- etait au moment de sa composition (nom et ingredients de chaque repas
-- inclus, pas seulement des identifiants) : le catalogue peut changer, un
-- repas genere n'existe nulle part ailleurs, mais le plan historise reste
-- lisible tel quel.

CREATE TABLE plan_historique (
    id                   UUID PRIMARY KEY,
    date_composition     TIMESTAMPTZ    NOT NULL,
    enseigne             VARCHAR(20)    NOT NULL,
    style                VARCHAR(20)    NOT NULL,
    niveau               VARCHAR(20)    NOT NULL,
    nombre_de_personnes  INT            NOT NULL,
    prix_estime          NUMERIC(10, 2) NOT NULL
);

CREATE INDEX idx_plan_historique_date_composition ON plan_historique (date_composition DESC);

-- Une ligne par (jour, creneau) : 15 lignes par plan. 'type' porte le
-- creneau (PETIT_DEJEUNER/DEJEUNER/DINER), 'jour_semaine' le jour.
CREATE TABLE plan_historique_repas (
    id             UUID         PRIMARY KEY,
    plan_id        UUID         NOT NULL REFERENCES plan_historique (id) ON DELETE CASCADE,
    jour_semaine   VARCHAR(20)  NOT NULL,
    nom            VARCHAR(255) NOT NULL,
    type           VARCHAR(20)  NOT NULL,
    style          VARCHAR(20)  NOT NULL,
    niveau_requis  VARCHAR(20)  NOT NULL
);

CREATE INDEX idx_plan_historique_repas_plan_id ON plan_historique_repas (plan_id);

CREATE TABLE plan_historique_repas_ingredient (
    repas_id         UUID           NOT NULL REFERENCES plan_historique_repas (id) ON DELETE CASCADE,
    ingredient_nom   VARCHAR(255)   NOT NULL,
    rayon            VARCHAR(30)    NOT NULL,
    quantite_valeur  NUMERIC(12, 3) NOT NULL,
    unite            VARCHAR(20)    NOT NULL
);

CREATE INDEX idx_plan_historique_repas_ingredient_repas_id ON plan_historique_repas_ingredient (repas_id);
