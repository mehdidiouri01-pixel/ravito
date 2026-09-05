CREATE TABLE repas (
    id             UUID PRIMARY KEY,
    nom            VARCHAR(255) NOT NULL,
    type           VARCHAR(20)  NOT NULL,
    style          VARCHAR(20)  NOT NULL,
    niveau_requis  VARCHAR(20)  NOT NULL
);

CREATE INDEX idx_repas_type_style ON repas (type, style);

CREATE TABLE repas_ingredient (
    repas_id         UUID           NOT NULL REFERENCES repas (id) ON DELETE CASCADE,
    ingredient_nom   VARCHAR(255)   NOT NULL,
    rayon            VARCHAR(30)    NOT NULL,
    quantite_valeur  NUMERIC(12, 3) NOT NULL,
    unite            VARCHAR(20)    NOT NULL
);

CREATE INDEX idx_repas_ingredient_repas_id ON repas_ingredient (repas_id);

CREATE TABLE prix_moyen_ingredient (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ingredient_nom  VARCHAR(255)   NOT NULL,
    unite           VARCHAR(20)    NOT NULL,
    prix_unitaire   NUMERIC(10, 4) NOT NULL,
    CONSTRAINT uq_prix_moyen_ingredient UNIQUE (ingredient_nom, unite)
);
