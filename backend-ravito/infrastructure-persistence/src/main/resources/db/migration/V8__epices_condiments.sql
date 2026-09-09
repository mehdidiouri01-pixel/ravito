-- Ajoute les epices et condiments de base (sel, poivre, huile d'olive,
-- ail) comme ingredients a part entiere des 90 repas sales du catalogue
-- (dejeuners + diners — les petits-dejeuners restent sucres/simples et ne
-- recoivent pas ces condiments). Jusqu'ici ces repas etaient "assaisonnes"
-- de facon implicite dans le texte des etapes, sans jamais apparaitre dans
-- les ingredients ni, donc, dans la liste de courses ou le prix estime —
-- c'est ce que ce fichier corrige.
--
-- Quantites exprimees pour une portion (comme le reste du catalogue, voir
-- PlanSemaine.genererListeCourses) : sel 1g, poivre 0.5g, huile d'olive
-- 5ml, ail 3g — de quoi assaisonner un plat individuel sans fausser le
-- total pour un foyer de plusieurs personnes une fois mis a l'echelle.
--
-- Chacun des 90 repas concernes recoit aussi une etape de preparation
-- supplementaire mentionnant ces condiments (voir repas_etape) : le
-- pop-up de recette devient un peu plus detaille, comme demande.

-- =====================================================================
-- Catalogue de reference : prix moyens et valeurs nutritionnelles
-- =====================================================================

INSERT INTO prix_moyen_ingredient (ingredient_nom, unite, prix_unitaire) VALUES
    ('sel', 'GRAMME', 0.0015),
    ('poivre', 'GRAMME', 0.0300),
    ('huile d''olive', 'MILLILITRE', 0.0120),
    ('ail', 'GRAMME', 0.0100);

INSERT INTO valeur_nutritionnelle_ingredient (ingredient_nom, unite, calories, proteines, glucides, lipides, fibres) VALUES
    ('sel', 'GRAMME', 0.0000, 0.0000, 0.0000, 0.0000, 0.0000),
    ('poivre', 'GRAMME', 2.5100, 0.1000, 0.6400, 0.0300, 0.2500),
    ('huile d''olive', 'MILLILITRE', 8.8400, 0.0000, 0.0000, 1.0000, 0.0000),
    ('ail', 'GRAMME', 1.4900, 0.0640, 0.3300, 0.0050, 0.0210);

-- =====================================================================
-- Ingredients : sel + poivre + huile d'olive + ail sur chaque dejeuner
-- et diner (88 des 90 repas — voir plus bas les 2 exceptions fromageres
-- sans huile d'olive).
-- =====================================================================

INSERT INTO repas_ingredient (repas_id, ingredient_nom, rayon, quantite_valeur, unite) VALUES
    -- V2 + V3 NORMAL : dejeuners
    ('11111111-1111-1111-1111-111111111201', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111201', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111201', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111201', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111202', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111202', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111202', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111202', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111203', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111203', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111203', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111203', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111204', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111204', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111204', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111204', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111205', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111205', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111205', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111205', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111206', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111206', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111206', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111206', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111207', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111207', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111207', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111207', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111208', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111208', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111208', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111208', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111209', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111209', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111209', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111209', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111210', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111210', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111210', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111210', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111211', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111211', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111211', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111211', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111212', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111212', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111212', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111212', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111213', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111213', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111213', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111213', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111214', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111214', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111214', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111214', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111215', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111215', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111215', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111215', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME');

INSERT INTO repas_ingredient (repas_id, ingredient_nom, rayon, quantite_valeur, unite) VALUES
    -- V3 HEALTHY : dejeuners
    ('22222222-2222-2222-2222-222222222201', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222201', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222201', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222201', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222202', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222202', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222202', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222202', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222203', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222203', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222203', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222203', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222204', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222204', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222204', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222204', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222205', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222205', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222205', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222205', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222206', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222206', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222206', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222206', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222207', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222207', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222207', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222207', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222208', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222208', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222208', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222208', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222209', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222209', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222209', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222209', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222210', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222210', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222210', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222210', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222211', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222211', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222211', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222211', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222212', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222212', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222212', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222212', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222213', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222213', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222213', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222213', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222214', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222214', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222214', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222214', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222215', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222215', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222215', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222215', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME');

INSERT INTO repas_ingredient (repas_id, ingredient_nom, rayon, quantite_valeur, unite) VALUES
    -- V3 GOURMAND : dejeuners (sauf 333211 Raclette et 333212 Fondue
    -- savoyarde, deux plats a base de fromage fondu ou l'huile d'olive
    -- n'a pas sa place — voir plus bas, sans cette ligne pour eux).
    ('33333333-3333-3333-3333-333333333201', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333201', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333201', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('33333333-3333-3333-3333-333333333201', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333202', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333202', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333202', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('33333333-3333-3333-3333-333333333202', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333203', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333203', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333203', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('33333333-3333-3333-3333-333333333203', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333204', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333204', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333204', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('33333333-3333-3333-3333-333333333204', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333205', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333205', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333205', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('33333333-3333-3333-3333-333333333205', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333206', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333206', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333206', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('33333333-3333-3333-3333-333333333206', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333207', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333207', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333207', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('33333333-3333-3333-3333-333333333207', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333208', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333208', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333208', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('33333333-3333-3333-3333-333333333208', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333209', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333209', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333209', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('33333333-3333-3333-3333-333333333209', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333210', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333210', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333210', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('33333333-3333-3333-3333-333333333210', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333211', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333211', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333211', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333212', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333212', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333212', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333213', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333213', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333213', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('33333333-3333-3333-3333-333333333213', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333214', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333214', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333214', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('33333333-3333-3333-3333-333333333214', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333215', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333215', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333215', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('33333333-3333-3333-3333-333333333215', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME');

INSERT INTO repas_ingredient (repas_id, ingredient_nom, rayon, quantite_valeur, unite) VALUES
    -- V4 : diners NORMAL
    ('11111111-1111-1111-1111-111111111301', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111301', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111301', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111301', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111302', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111302', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111302', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111302', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111303', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111303', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111303', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111303', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111304', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111304', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111304', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111304', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111305', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111305', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111305', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111305', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111306', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111306', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111306', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111306', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111307', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111307', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111307', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111307', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111308', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111308', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111308', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111308', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111309', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111309', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111309', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111309', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111310', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111310', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111310', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111310', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111311', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111311', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111311', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111311', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111312', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111312', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111312', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111312', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111313', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111313', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111313', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111313', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111314', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111314', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111314', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111314', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111315', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111315', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111315', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111315', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME');

INSERT INTO repas_ingredient (repas_id, ingredient_nom, rayon, quantite_valeur, unite) VALUES
    -- V4 : diners HEALTHY
    ('22222222-2222-2222-2222-222222222301', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222301', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222301', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222301', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222302', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222302', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222302', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222302', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222303', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222303', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222303', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222303', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222304', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222304', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222304', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222304', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222305', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222305', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222305', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222305', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222306', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222306', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222306', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222306', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222307', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222307', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222307', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222307', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222308', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222308', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222308', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222308', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222309', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222309', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222309', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222309', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222310', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222310', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222310', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222310', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222311', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222311', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222311', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222311', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222312', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222312', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222312', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222312', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222313', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222313', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222313', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222313', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222314', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222314', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222314', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222314', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222315', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222315', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222315', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('22222222-2222-2222-2222-222222222315', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME');

INSERT INTO repas_ingredient (repas_id, ingredient_nom, rayon, quantite_valeur, unite) VALUES
    -- V4 : diners GOURMAND
    ('33333333-3333-3333-3333-333333333301', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333301', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333301', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('33333333-3333-3333-3333-333333333301', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333302', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333302', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333302', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('33333333-3333-3333-3333-333333333302', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333303', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333303', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333303', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('33333333-3333-3333-3333-333333333303', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333304', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333304', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333304', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('33333333-3333-3333-3333-333333333304', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333305', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333305', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333305', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('33333333-3333-3333-3333-333333333305', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333306', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333306', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333306', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('33333333-3333-3333-3333-333333333306', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333307', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333307', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333307', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('33333333-3333-3333-3333-333333333307', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333308', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333308', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333308', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('33333333-3333-3333-3333-333333333308', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333309', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333309', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333309', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('33333333-3333-3333-3333-333333333309', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333310', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333310', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333310', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('33333333-3333-3333-3333-333333333310', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333311', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333311', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333311', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('33333333-3333-3333-3333-333333333311', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333312', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333312', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333312', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('33333333-3333-3333-3333-333333333312', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333313', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333313', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333313', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('33333333-3333-3333-3333-333333333313', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333314', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333314', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333314', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('33333333-3333-3333-3333-333333333314', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333315', 'sel', 'EPICERIE', 1, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333315', 'poivre', 'EPICERIE', 0.5, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333315', 'huile d''olive', 'EPICERIE', 5, 'MILLILITRE'),
    ('33333333-3333-3333-3333-333333333315', 'ail', 'FRUITS_LEGUMES', 3, 'GRAMME');

-- =====================================================================
-- Etapes : une etape de preparation supplementaire par repas, mentionnant
-- explicitement les nouveaux ingredients (voir etape_ordre = ancien
-- maximum + 1, calcule depuis V6 pour chaque repas — la table garde ses
-- etapes existantes intactes, celle-ci s'ajoute juste a la suite).
-- =====================================================================

INSERT INTO repas_etape (repas_id, etape_ordre, description) VALUES
    ('11111111-1111-1111-1111-111111111201', 3, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('11111111-1111-1111-1111-111111111202', 3, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('11111111-1111-1111-1111-111111111203', 3, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('11111111-1111-1111-1111-111111111204', 3, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('11111111-1111-1111-1111-111111111205', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('11111111-1111-1111-1111-111111111206', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('11111111-1111-1111-1111-111111111207', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('11111111-1111-1111-1111-111111111208', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('11111111-1111-1111-1111-111111111209', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('11111111-1111-1111-1111-111111111210', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('11111111-1111-1111-1111-111111111211', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('11111111-1111-1111-1111-111111111212', 3, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('11111111-1111-1111-1111-111111111213', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('11111111-1111-1111-1111-111111111214', 3, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('11111111-1111-1111-1111-111111111215', 3, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),

    ('22222222-2222-2222-2222-222222222201', 3, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('22222222-2222-2222-2222-222222222202', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('22222222-2222-2222-2222-222222222203', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('22222222-2222-2222-2222-222222222204', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('22222222-2222-2222-2222-222222222205', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('22222222-2222-2222-2222-222222222206', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('22222222-2222-2222-2222-222222222207', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('22222222-2222-2222-2222-222222222208', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('22222222-2222-2222-2222-222222222209', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('22222222-2222-2222-2222-222222222210', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('22222222-2222-2222-2222-222222222211', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('22222222-2222-2222-2222-222222222212', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('22222222-2222-2222-2222-222222222213', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('22222222-2222-2222-2222-222222222214', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('22222222-2222-2222-2222-222222222215', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),

    ('33333333-3333-3333-3333-333333333201', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('33333333-3333-3333-3333-333333333202', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('33333333-3333-3333-3333-333333333203', 3, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('33333333-3333-3333-3333-333333333204', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('33333333-3333-3333-3333-333333333205', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('33333333-3333-3333-3333-333333333206', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('33333333-3333-3333-3333-333333333207', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('33333333-3333-3333-3333-333333333208', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('33333333-3333-3333-3333-333333333209', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('33333333-3333-3333-3333-333333333210', 3, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('33333333-3333-3333-3333-333333333211', 2, 'Frottez les pommes de terre chaudes d''une pointe d''ail, salez et poivrez avant de servir avec le fromage fondu.'),
    ('33333333-3333-3333-3333-333333333212', 2, 'Frottez le caquelon d''ail avant utilisation, puis salez et poivrez la fondue a votre gout.'),
    ('33333333-3333-3333-3333-333333333213', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('33333333-3333-3333-3333-333333333214', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('33333333-3333-3333-3333-333333333215', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),

    ('11111111-1111-1111-1111-111111111301', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('11111111-1111-1111-1111-111111111302', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('11111111-1111-1111-1111-111111111303', 3, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('11111111-1111-1111-1111-111111111304', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('11111111-1111-1111-1111-111111111305', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('11111111-1111-1111-1111-111111111306', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('11111111-1111-1111-1111-111111111307', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('11111111-1111-1111-1111-111111111308', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('11111111-1111-1111-1111-111111111309', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('11111111-1111-1111-1111-111111111310', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('11111111-1111-1111-1111-111111111311', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('11111111-1111-1111-1111-111111111312', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('11111111-1111-1111-1111-111111111313', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('11111111-1111-1111-1111-111111111314', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('11111111-1111-1111-1111-111111111315', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),

    ('22222222-2222-2222-2222-222222222301', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('22222222-2222-2222-2222-222222222302', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('22222222-2222-2222-2222-222222222303', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('22222222-2222-2222-2222-222222222304', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('22222222-2222-2222-2222-222222222305', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('22222222-2222-2222-2222-222222222306', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('22222222-2222-2222-2222-222222222307', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('22222222-2222-2222-2222-222222222308', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('22222222-2222-2222-2222-222222222309', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('22222222-2222-2222-2222-222222222310', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('22222222-2222-2222-2222-222222222311', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('22222222-2222-2222-2222-222222222312', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('22222222-2222-2222-2222-222222222313', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('22222222-2222-2222-2222-222222222314', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('22222222-2222-2222-2222-222222222315', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),

    ('33333333-3333-3333-3333-333333333301', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('33333333-3333-3333-3333-333333333302', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('33333333-3333-3333-3333-333333333303', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('33333333-3333-3333-3333-333333333304', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('33333333-3333-3333-3333-333333333305', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('33333333-3333-3333-3333-333333333306', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('33333333-3333-3333-3333-333333333307', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('33333333-3333-3333-3333-333333333308', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('33333333-3333-3333-3333-333333333309', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('33333333-3333-3333-3333-333333333310', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('33333333-3333-3333-3333-333333333311', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('33333333-3333-3333-3333-333333333312', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('33333333-3333-3333-3333-333333333313', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('33333333-3333-3333-3333-333333333314', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.'),
    ('33333333-3333-3333-3333-333333333315', 2, 'Salez, poivrez, ajoutez l''ail emince et arrosez d''un filet d''huile d''olive avant de servir.');

-- =====================================================================
-- Historique : les plans composes avant cette migration n'ont pas ces
-- nouveaux ingredients/etapes dans leur snapshot fige (voir
-- PlanSemaineHistorise) — rien a corriger la, c'est le comportement
-- attendu (un plan historise reste tel qu'il etait au moment ou il a ete
-- compose, meme si le catalogue evolue ensuite).
-- =====================================================================
