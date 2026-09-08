-- Ajoute le diner comme 3e type de repas : 15 diners minimum par style
-- (NORMAL/HEALTHY/GOURMAND), meme repartition que pour petit-dejeuner et
-- dejeuner en V3 (10 DEBUTANT + 5 CONFIRME). Convention d'id reprise a
-- l'identique : prefixe de style (1111.../2222.../3333...), segment de
-- type '3xx' pour DINER (a la suite de '1xx' petit-dejeuner et '2xx'
-- dejeuner).
--
-- La plupart des ingredients reutilisent ceux deja prix en V2/V3 (memes
-- couples ingredient/unite, donc pas de nouvelle ligne de prix pour eux) ;
-- les quelques ingredients propres aux diners (poisson pane, courgette,
-- lait de coco, tofu, etc.) recoivent leur propre ligne de prix a la fin
-- de ce fichier.

-- =====================================================================
-- NORMAL : 15 diners
-- =====================================================================

INSERT INTO repas (id, nom, type, style, niveau_requis) VALUES
    ('11111111-1111-1111-1111-111111111301', 'Soupe de legumes et pain', 'DINER', 'NORMAL', 'DEBUTANT'),
    ('11111111-1111-1111-1111-111111111302', 'Omelette jambon', 'DINER', 'NORMAL', 'DEBUTANT'),
    ('11111111-1111-1111-1111-111111111303', 'Pates a la carbonara', 'DINER', 'NORMAL', 'DEBUTANT'),
    ('11111111-1111-1111-1111-111111111304', 'Riz cantonais', 'DINER', 'NORMAL', 'DEBUTANT'),
    ('11111111-1111-1111-1111-111111111305', 'Croque-monsieur et salade verte', 'DINER', 'NORMAL', 'DEBUTANT'),
    ('11111111-1111-1111-1111-111111111306', 'Poisson pane et puree', 'DINER', 'NORMAL', 'DEBUTANT'),
    ('11111111-1111-1111-1111-111111111307', 'Quiche jambon fromage', 'DINER', 'NORMAL', 'DEBUTANT'),
    ('11111111-1111-1111-1111-111111111308', 'Wrap poulet crudites', 'DINER', 'NORMAL', 'DEBUTANT'),
    ('11111111-1111-1111-1111-111111111309', 'Gratin de courgettes', 'DINER', 'NORMAL', 'DEBUTANT'),
    ('11111111-1111-1111-1111-111111111310', 'Boulettes sauce tomate', 'DINER', 'NORMAL', 'DEBUTANT'),
    ('11111111-1111-1111-1111-111111111311', 'Curry de poulet au lait de coco', 'DINER', 'NORMAL', 'CONFIRME'),
    ('11111111-1111-1111-1111-111111111312', 'Osso bucco et riz', 'DINER', 'NORMAL', 'CONFIRME'),
    ('11111111-1111-1111-1111-111111111313', 'Parmentier de canard', 'DINER', 'NORMAL', 'CONFIRME'),
    ('11111111-1111-1111-1111-111111111314', 'Risotto aux crevettes', 'DINER', 'NORMAL', 'CONFIRME'),
    ('11111111-1111-1111-1111-111111111315', 'Filet de boeuf sauce champignons', 'DINER', 'NORMAL', 'CONFIRME');

INSERT INTO repas_ingredient (repas_id, ingredient_nom, rayon, quantite_valeur, unite) VALUES
    ('11111111-1111-1111-1111-111111111301', 'soupe', 'EPICERIE', 400, 'MILLILITRE'),
    ('11111111-1111-1111-1111-111111111301', 'pain', 'BOULANGERIE', 50, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111302', 'oeufs', 'CREMERIE', 3, 'UNITE'),
    ('11111111-1111-1111-1111-111111111302', 'jambon', 'BOUCHERIE', 2, 'UNITE'),

    ('11111111-1111-1111-1111-111111111303', 'pates', 'EPICERIE', 150, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111303', 'lardons', 'BOUCHERIE', 100, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111304', 'riz', 'EPICERIE', 150, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111304', 'jambon', 'BOUCHERIE', 2, 'UNITE'),

    ('11111111-1111-1111-1111-111111111305', 'pain de mie', 'BOULANGERIE', 100, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111305', 'fromage rape', 'CREMERIE', 40, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111306', 'poisson pane', 'SURGELES', 1, 'UNITE'),
    ('11111111-1111-1111-1111-111111111306', 'pommes de terre', 'FRUITS_LEGUMES', 250, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111307', 'pate feuilletee', 'EPICERIE', 1, 'UNITE'),
    ('11111111-1111-1111-1111-111111111307', 'jambon', 'BOUCHERIE', 2, 'UNITE'),

    ('11111111-1111-1111-1111-111111111308', 'tortilla wrap', 'EPICERIE', 2, 'UNITE'),
    ('11111111-1111-1111-1111-111111111308', 'poulet', 'BOUCHERIE', 150, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111309', 'courgette', 'FRUITS_LEGUMES', 300, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111309', 'fromage rape', 'CREMERIE', 50, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111310', 'steak hache', 'BOUCHERIE', 150, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111310', 'tomate', 'FRUITS_LEGUMES', 150, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111311', 'poulet', 'BOUCHERIE', 200, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111311', 'lait de coco', 'EPICERIE', 200, 'MILLILITRE'),

    ('11111111-1111-1111-1111-111111111312', 'veau', 'BOUCHERIE', 250, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111312', 'riz', 'EPICERIE', 150, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111313', 'cuisse de canard confite', 'BOUCHERIE', 1, 'UNITE'),
    ('11111111-1111-1111-1111-111111111313', 'pommes de terre', 'FRUITS_LEGUMES', 250, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111314', 'riz arborio', 'EPICERIE', 150, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111314', 'crevettes', 'POISSONNERIE', 150, 'GRAMME'),

    ('11111111-1111-1111-1111-111111111315', 'boeuf', 'BOUCHERIE', 200, 'GRAMME'),
    ('11111111-1111-1111-1111-111111111315', 'champignons', 'FRUITS_LEGUMES', 120, 'GRAMME');

-- =====================================================================
-- HEALTHY : 15 diners
-- =====================================================================

INSERT INTO repas (id, nom, type, style, niveau_requis) VALUES
    ('22222222-2222-2222-2222-222222222301', 'Soupe miso tofu', 'DINER', 'HEALTHY', 'DEBUTANT'),
    ('22222222-2222-2222-2222-222222222302', 'Salade de lentilles et feta', 'DINER', 'HEALTHY', 'DEBUTANT'),
    ('22222222-2222-2222-2222-222222222303', 'Poisson vapeur brocolis', 'DINER', 'HEALTHY', 'DEBUTANT'),
    ('22222222-2222-2222-2222-222222222304', 'Bowl quinoa avocat', 'DINER', 'HEALTHY', 'DEBUTANT'),
    ('22222222-2222-2222-2222-222222222305', 'Salade de pois chiches concombre', 'DINER', 'HEALTHY', 'DEBUTANT'),
    ('22222222-2222-2222-2222-222222222306', 'Wok de tofu aux legumes', 'DINER', 'HEALTHY', 'DEBUTANT'),
    ('22222222-2222-2222-2222-222222222307', 'Soupe de potiron', 'DINER', 'HEALTHY', 'DEBUTANT'),
    ('22222222-2222-2222-2222-222222222308', 'Salade de quinoa aux legumes', 'DINER', 'HEALTHY', 'DEBUTANT'),
    ('22222222-2222-2222-2222-222222222309', 'Curry de pois chiches', 'DINER', 'HEALTHY', 'DEBUTANT'),
    ('22222222-2222-2222-2222-222222222310', 'Poulet grille et brocolis', 'DINER', 'HEALTHY', 'DEBUTANT'),
    ('22222222-2222-2222-2222-222222222311', 'Saumon en papillote et legumes', 'DINER', 'HEALTHY', 'CONFIRME'),
    ('22222222-2222-2222-2222-222222222312', 'Wok de crevettes et legumes', 'DINER', 'HEALTHY', 'CONFIRME'),
    ('22222222-2222-2222-2222-222222222313', 'Poke bowl saumon avocat', 'DINER', 'HEALTHY', 'CONFIRME'),
    ('22222222-2222-2222-2222-222222222314', 'Filet de cabillaud et quinoa', 'DINER', 'HEALTHY', 'CONFIRME'),
    ('22222222-2222-2222-2222-222222222315', 'Curry de pois chiches et epinards', 'DINER', 'HEALTHY', 'CONFIRME');

INSERT INTO repas_ingredient (repas_id, ingredient_nom, rayon, quantite_valeur, unite) VALUES
    ('22222222-2222-2222-2222-222222222301', 'miso', 'EPICERIE', 15, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222301', 'tofu', 'FRUITS_LEGUMES', 100, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222302', 'lentilles', 'EPICERIE', 150, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222302', 'feta', 'CREMERIE', 60, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222303', 'cabillaud', 'POISSONNERIE', 150, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222303', 'brocolis', 'FRUITS_LEGUMES', 150, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222304', 'quinoa', 'EPICERIE', 100, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222304', 'avocat', 'FRUITS_LEGUMES', 1, 'UNITE'),

    ('22222222-2222-2222-2222-222222222305', 'pois chiches', 'EPICERIE', 150, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222305', 'concombre', 'FRUITS_LEGUMES', 100, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222306', 'tofu', 'FRUITS_LEGUMES', 150, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222306', 'courgette', 'FRUITS_LEGUMES', 200, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222307', 'potiron', 'FRUITS_LEGUMES', 300, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222307', 'lait de coco', 'EPICERIE', 100, 'MILLILITRE'),

    ('22222222-2222-2222-2222-222222222308', 'quinoa', 'EPICERIE', 100, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222308', 'tomate', 'FRUITS_LEGUMES', 100, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222309', 'pois chiches', 'EPICERIE', 150, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222309', 'lait de coco', 'EPICERIE', 150, 'MILLILITRE'),

    ('22222222-2222-2222-2222-222222222310', 'poulet', 'BOUCHERIE', 180, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222310', 'brocolis', 'FRUITS_LEGUMES', 150, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222311', 'saumon', 'POISSONNERIE', 150, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222311', 'courgette', 'FRUITS_LEGUMES', 150, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222312', 'crevettes', 'POISSONNERIE', 150, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222312', 'brocolis', 'FRUITS_LEGUMES', 150, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222313', 'saumon', 'POISSONNERIE', 130, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222313', 'avocat', 'FRUITS_LEGUMES', 1, 'UNITE'),

    ('22222222-2222-2222-2222-222222222314', 'cabillaud', 'POISSONNERIE', 150, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222314', 'quinoa', 'EPICERIE', 100, 'GRAMME'),

    ('22222222-2222-2222-2222-222222222315', 'pois chiches', 'EPICERIE', 150, 'GRAMME'),
    ('22222222-2222-2222-2222-222222222315', 'epinards', 'FRUITS_LEGUMES', 100, 'GRAMME');

-- =====================================================================
-- GOURMAND : 15 diners
-- =====================================================================

INSERT INTO repas (id, nom, type, style, niveau_requis) VALUES
    ('33333333-3333-3333-3333-333333333301', 'Mac and cheese gratine', 'DINER', 'GOURMAND', 'DEBUTANT'),
    ('33333333-3333-3333-3333-333333333302', 'Escalope de veau a la creme', 'DINER', 'GOURMAND', 'DEBUTANT'),
    ('33333333-3333-3333-3333-333333333303', 'Poulet roti aux herbes', 'DINER', 'GOURMAND', 'DEBUTANT'),
    ('33333333-3333-3333-3333-333333333304', 'Gratin dauphinois au jambon', 'DINER', 'GOURMAND', 'DEBUTANT'),
    ('33333333-3333-3333-3333-333333333305', 'Tarte salee jambon fromage', 'DINER', 'GOURMAND', 'DEBUTANT'),
    ('33333333-3333-3333-3333-333333333306', 'Saucisses grillees et pommes de terre', 'DINER', 'GOURMAND', 'DEBUTANT'),
    ('33333333-3333-3333-3333-333333333307', 'Cote de porc a la moutarde', 'DINER', 'GOURMAND', 'DEBUTANT'),
    ('33333333-3333-3333-3333-333333333308', 'Poelee de boeuf aux champignons', 'DINER', 'GOURMAND', 'DEBUTANT'),
    ('33333333-3333-3333-3333-333333333309', 'Croziflette', 'DINER', 'GOURMAND', 'DEBUTANT'),
    ('33333333-3333-3333-3333-333333333310', 'Rissoles de saumon', 'DINER', 'GOURMAND', 'DEBUTANT'),
    ('33333333-3333-3333-3333-333333333311', 'Magret de canard sauce miel', 'DINER', 'GOURMAND', 'CONFIRME'),
    ('33333333-3333-3333-3333-333333333312', 'Saint-Jacques a la creme', 'DINER', 'GOURMAND', 'CONFIRME'),
    ('33333333-3333-3333-3333-333333333313', 'Filet mignon en croute', 'DINER', 'GOURMAND', 'CONFIRME'),
    ('33333333-3333-3333-3333-333333333314', 'Canard confit aux pommes', 'DINER', 'GOURMAND', 'CONFIRME'),
    ('33333333-3333-3333-3333-333333333315', 'Risotto au parmesan', 'DINER', 'GOURMAND', 'CONFIRME');

INSERT INTO repas_ingredient (repas_id, ingredient_nom, rayon, quantite_valeur, unite) VALUES
    ('33333333-3333-3333-3333-333333333301', 'pates', 'EPICERIE', 150, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333301', 'fromage rape', 'CREMERIE', 80, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333302', 'veau', 'BOUCHERIE', 180, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333302', 'creme fraiche', 'CREMERIE', 100, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333303', 'poulet', 'BOUCHERIE', 200, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333303', 'herbes de provence', 'EPICERIE', 5, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333304', 'pommes de terre', 'FRUITS_LEGUMES', 300, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333304', 'jambon', 'BOUCHERIE', 2, 'UNITE'),

    ('33333333-3333-3333-3333-333333333305', 'pate feuilletee', 'EPICERIE', 1, 'UNITE'),
    ('33333333-3333-3333-3333-333333333305', 'fromage rape', 'CREMERIE', 60, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333306', 'saucisse fumee', 'BOUCHERIE', 150, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333306', 'pommes de terre', 'FRUITS_LEGUMES', 250, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333307', 'cote de porc', 'BOUCHERIE', 200, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333307', 'moutarde', 'EPICERIE', 20, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333308', 'boeuf', 'BOUCHERIE', 200, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333308', 'champignons', 'FRUITS_LEGUMES', 120, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333309', 'reblochon', 'CREMERIE', 150, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333309', 'lardons', 'BOUCHERIE', 100, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333310', 'saumon', 'POISSONNERIE', 150, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333310', 'creme fraiche', 'CREMERIE', 80, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333311', 'magret de canard', 'BOUCHERIE', 180, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333311', 'miel', 'EPICERIE', 20, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333312', 'saint-jacques', 'POISSONNERIE', 150, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333312', 'creme fraiche', 'CREMERIE', 100, 'GRAMME'),

    ('33333333-3333-3333-3333-333333333313', 'filet mignon', 'BOUCHERIE', 200, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333313', 'pate feuilletee', 'EPICERIE', 1, 'UNITE'),

    ('33333333-3333-3333-3333-333333333314', 'cuisse de canard confite', 'BOUCHERIE', 1, 'UNITE'),
    ('33333333-3333-3333-3333-333333333314', 'pommes', 'FRUITS_LEGUMES', 2, 'UNITE'),

    ('33333333-3333-3333-3333-333333333315', 'riz arborio', 'EPICERIE', 150, 'GRAMME'),
    ('33333333-3333-3333-3333-333333333315', 'parmesan', 'CREMERIE', 60, 'GRAMME');

-- =====================================================================
-- Prix moyens des ingredients propres aux diners (tous les autres
-- ingredients ci-dessus reutilisent un couple ingredient/unite deja
-- price en V2 ou V3).
-- =====================================================================

INSERT INTO prix_moyen_ingredient (ingredient_nom, unite, prix_unitaire) VALUES
    ('poisson pane', 'UNITE', 1.5000),
    ('tortilla wrap', 'UNITE', 0.4000),
    ('courgette', 'GRAMME', 0.0030),
    ('lait de coco', 'MILLILITRE', 0.0035),
    ('tofu', 'GRAMME', 0.0140),
    ('feta', 'GRAMME', 0.0180),
    ('cabillaud', 'GRAMME', 0.0280),
    ('potiron', 'GRAMME', 0.0025),
    ('herbes de provence', 'GRAMME', 0.0400),
    ('cote de porc', 'GRAMME', 0.0160),
    ('moutarde', 'GRAMME', 0.0090),
    ('parmesan', 'GRAMME', 0.0250);
