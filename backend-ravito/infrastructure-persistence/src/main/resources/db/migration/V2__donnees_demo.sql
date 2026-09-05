-- Donnees de demo pour le style NORMAL / niveau DEBUTANT uniquement :
-- 5 petits-dejeuners + 5 dejeuners, de quoi valider toute la chaine
-- (proposition -> composition -> liste de courses -> estimation) de bout
-- en bout. Remplir les 15 minimum par type ET les styles HEALTHY/GOURMAND
-- est un travail de contenu a part, pas traite ici.

-- Petits-dejeuners

INSERT INTO repas (id, nom, type, style, niveau_requis) VALUES
    ('11111111-1111-1111-1111-111111111101', 'Tartines beurre confiture', 'PETIT_DEJEUNER', 'NORMAL', 'DEBUTANT'),
    ('11111111-1111-1111-1111-111111111102', 'Bol de cereales au lait', 'PETIT_DEJEUNER', 'NORMAL', 'DEBUTANT'),
    ('11111111-1111-1111-1111-111111111103', 'Yaourt et banane', 'PETIT_DEJEUNER', 'NORMAL', 'DEBUTANT'),
    ('11111111-1111-1111-1111-111111111104', 'Oeufs brouilles et pain', 'PETIT_DEJEUNER', 'NORMAL', 'DEBUTANT'),
    ('11111111-1111-1111-1111-111111111105', 'Pain au chocolat et jus d''orange', 'PETIT_DEJEUNER', 'NORMAL', 'DEBUTANT');

INSERT INTO repas_ingredient (repas_id, ingredient_nom, rayon, quantite_valeur, unite) VALUES
    ('11111111-1111-1111-1111-111111111101', 'pain',      'BOULANGERIE', 100, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111101', 'beurre',    'CREMERIE',     20, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111101', 'confiture', 'EPICERIE',     30, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111102', 'cereales',  'EPICERIE',     60, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111102', 'lait',      'CREMERIE',    200, 'MILLILITRE'),

    ('11111111-1111-1111-1111-111111111103', 'yaourt',    'CREMERIE',      2, 'UNITE'),
    ('11111111-1111-1111-1111-111111111103', 'banane',    'FRUITS_LEGUMES', 1, 'UNITE'),

    ('11111111-1111-1111-1111-111111111104', 'oeufs',     'CREMERIE',      2, 'UNITE'),
    ('11111111-1111-1111-1111-111111111104', 'pain',      'BOULANGERIE',  80, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111105', 'pain au chocolat', 'BOULANGERIE', 1, 'UNITE'),
    ('11111111-1111-1111-1111-111111111105', 'jus d''orange',    'BOISSONS',  200, 'MILLILITRE');

-- Dejeuners

INSERT INTO repas (id, nom, type, style, niveau_requis) VALUES
    ('11111111-1111-1111-1111-111111111201', 'Pates au thon', 'DEJEUNER', 'NORMAL', 'DEBUTANT'),
    ('11111111-1111-1111-1111-111111111202', 'Riz et poulet', 'DEJEUNER', 'NORMAL', 'DEBUTANT'),
    ('11111111-1111-1111-1111-111111111203', 'Salade composee', 'DEJEUNER', 'NORMAL', 'DEBUTANT'),
    ('11111111-1111-1111-1111-111111111204', 'Steak hache et frites', 'DEJEUNER', 'NORMAL', 'DEBUTANT'),
    ('11111111-1111-1111-1111-111111111205', 'Soupe et pain', 'DEJEUNER', 'NORMAL', 'DEBUTANT');

INSERT INTO repas_ingredient (repas_id, ingredient_nom, rayon, quantite_valeur, unite) VALUES
    ('11111111-1111-1111-1111-111111111201', 'pates',   'EPICERIE',       150, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111201', 'thon',    'EPICERIE',         1, 'UNITE'),
    ('11111111-1111-1111-1111-111111111201', 'tomate',  'FRUITS_LEGUMES', 100, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111202', 'riz',     'EPICERIE',       150, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111202', 'poulet',  'BOUCHERIE',      200, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111203', 'salade',  'FRUITS_LEGUMES', 100, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111203', 'oeufs',   'CREMERIE',         1, 'UNITE'),
    ('11111111-1111-1111-1111-111111111203', 'jambon',  'BOUCHERIE',        2, 'UNITE'),

    ('11111111-1111-1111-1111-111111111204', 'steak hache',      'BOUCHERIE', 150, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111204', 'frites surgelees', 'SURGELES',  200, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111205', 'soupe',   'EPICERIE',       400, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111205', 'pain',    'BOULANGERIE',     50, 'GRAMME');

-- Prix moyens (source : estimation manuelle, a affiner plus tard) pour
-- chaque couple (ingredient, unite) utilise ci-dessus.

INSERT INTO prix_moyen_ingredient (ingredient_nom, unite, prix_unitaire) VALUES
    ('pain',              'GRAMME',     0.0080),
    ('beurre',            'GRAMME',     0.0120),
    ('confiture',         'GRAMME',     0.0090),
    ('cereales',          'GRAMME',     0.0100),
    ('lait',              'MILLILITRE', 0.0011),
    ('yaourt',            'UNITE',      0.2500),
    ('banane',            'UNITE',      0.3000),
    ('oeufs',             'UNITE',      0.3200),
    ('pain au chocolat',  'UNITE',      1.1000),
    ('jus d''orange',     'MILLILITRE', 0.0035),
    ('pates',             'GRAMME',     0.0025),
    ('thon',              'UNITE',      1.8000),
    ('tomate',            'GRAMME',     0.0035),
    ('riz',               'GRAMME',     0.0030),
    ('poulet',            'GRAMME',     0.0110),
    ('salade',            'GRAMME',     0.0060),
    ('jambon',            'UNITE',      0.6000),
    ('steak hache',       'GRAMME',     0.0130),
    ('frites surgelees',  'GRAMME',     0.0045),
    ('soupe',             'MILLILITRE', 0.0025);
