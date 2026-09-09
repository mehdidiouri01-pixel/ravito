-- Ajoute les etapes de preparation de chaque repas, affichees dans le
-- pop-up de recette apres les ingredients (cote frontend). Une ligne par
-- (repas, etape), l'ordre etant porte par etape_ordre : voir la Javadoc de
-- RepasEntity.etapesPreparation pour le detail du choix @OrderColumn.
--
-- Chaque repas deja au catalogue (V2 + V3 + V4, soit 135 repas) recoit ici
-- 2 a 3 etapes coherentes avec ses ingredients.

CREATE TABLE repas_etape (
    repas_id UUID NOT NULL REFERENCES repas (id) ON DELETE CASCADE,
    etape_ordre INT NOT NULL,
    description TEXT NOT NULL,
    PRIMARY KEY (repas_id, etape_ordre)
);

-- =====================================================================
-- V2 : petits-dejeuners et dejeuners NORMAL/DEBUTANT
-- =====================================================================

INSERT INTO repas_etape (repas_id, etape_ordre, description) VALUES
    ('11111111-1111-1111-1111-111111111101', 0, 'Coupez le pain en tranches.'),
    ('11111111-1111-1111-1111-111111111101', 1, 'Tartinez chaque tranche de beurre puis de confiture.'),

    ('11111111-1111-1111-1111-111111111102', 0, 'Versez les cereales dans un bol.'),
    ('11111111-1111-1111-1111-111111111102', 1, 'Ajoutez le lait froid et melangez.'),

    ('11111111-1111-1111-1111-111111111103', 0, 'Coupez la banane en rondelles.'),
    ('11111111-1111-1111-1111-111111111103', 1, 'Deposez-la sur le yaourt et servez.'),

    ('11111111-1111-1111-1111-111111111104', 0, 'Battez les oeufs dans un bol.'),
    ('11111111-1111-1111-1111-111111111104', 1, 'Faites-les cuire a feu doux en remuant jusqu''a ce qu''ils soient brouilles.'),
    ('11111111-1111-1111-1111-111111111104', 2, 'Servez avec les tranches de pain.'),

    ('11111111-1111-1111-1111-111111111105', 0, 'Rechauffez le pain au chocolat quelques minutes au four si besoin.'),
    ('11111111-1111-1111-1111-111111111105', 1, 'Servez-le accompagne d''un verre de jus d''orange.'),

    ('11111111-1111-1111-1111-111111111201', 0, 'Faites cuire les pates dans une grande casserole d''eau bouillante salee.'),
    ('11111111-1111-1111-1111-111111111201', 1, 'Egouttez-les puis melangez avec le thon emiette et la tomate coupee en des.'),
    ('11111111-1111-1111-1111-111111111201', 2, 'Assaisonnez et servez chaud.'),

    ('11111111-1111-1111-1111-111111111202', 0, 'Faites cuire le riz dans une casserole d''eau bouillante salee.'),
    ('11111111-1111-1111-1111-111111111202', 1, 'Faites cuire le poulet a la poele jusqu''a ce qu''il soit dore.'),
    ('11111111-1111-1111-1111-111111111202', 2, 'Servez le poulet sur un lit de riz.'),

    ('11111111-1111-1111-1111-111111111203', 0, 'Lavez et coupez la salade.'),
    ('11111111-1111-1111-1111-111111111203', 1, 'Faites cuire les oeufs durs puis coupez-les en quartiers.'),
    ('11111111-1111-1111-1111-111111111203', 2, 'Ajoutez le jambon coupe en des et melangez le tout.'),

    ('11111111-1111-1111-1111-111111111204', 0, 'Faites cuire les frites surgelees au four selon les instructions du paquet.'),
    ('11111111-1111-1111-1111-111111111204', 1, 'Faites cuire le steak hache a la poele selon la cuisson souhaitee.'),
    ('11111111-1111-1111-1111-111111111204', 2, 'Servez ensemble.'),

    ('11111111-1111-1111-1111-111111111205', 0, 'Faites rechauffer la soupe a feu doux.'),
    ('11111111-1111-1111-1111-111111111205', 1, 'Servez chaud avec des tranches de pain.');

-- =====================================================================
-- V3 : NORMAL (10 petits-dejeuners + 10 dejeuners de plus)
-- =====================================================================

INSERT INTO repas_etape (repas_id, etape_ordre, description) VALUES
    ('11111111-1111-1111-1111-111111111106', 0, 'Coupez le croissant en deux dans la longueur.'),
    ('11111111-1111-1111-1111-111111111106', 1, 'Tartinez-le de beurre et servez.'),

    ('11111111-1111-1111-1111-111111111107', 0, 'Battez les oeufs dans une assiette creuse.'),
    ('11111111-1111-1111-1111-111111111107', 1, 'Trempez les tranches de pain dans les oeufs battus.'),
    ('11111111-1111-1111-1111-111111111107', 2, 'Faites-les dorer a la poele des deux cotes.'),

    ('11111111-1111-1111-1111-111111111108', 0, 'Faites rechauffer les crepes a la poele quelques secondes de chaque cote.'),
    ('11111111-1111-1111-1111-111111111108', 1, 'Saupoudrez de sucre, pliez et servez.'),

    ('11111111-1111-1111-1111-111111111109', 0, 'Faites griller les tranches de pain.'),
    ('11111111-1111-1111-1111-111111111109', 1, 'Recouvrez-les de fromage rape et passez-les quelques minutes au four jusqu''a ce que le fromage fonde.'),

    ('11111111-1111-1111-1111-111111111110', 0, 'Tartinez chaque biscotte de confiture.'),
    ('11111111-1111-1111-1111-111111111110', 1, 'Servez aussitot pour garder le croustillant.'),

    ('11111111-1111-1111-1111-111111111111', 0, 'Faites chauffer du lait et delayez-y le chocolat en poudre.'),
    ('11111111-1111-1111-1111-111111111111', 1, 'Servez chaud avec des tartines de pain.'),

    ('11111111-1111-1111-1111-111111111112', 0, 'Faites rechauffer les gaufres au grille-pain ou au four.'),
    ('11111111-1111-1111-1111-111111111112', 1, 'Saupoudrez de sucre et servez.'),

    ('11111111-1111-1111-1111-111111111113', 0, 'Coupez la brioche en tranches.'),
    ('11111111-1111-1111-1111-111111111113', 1, 'Tartinez-la de beurre et servez.'),

    ('11111111-1111-1111-1111-111111111114', 0, 'Versez le muesli dans un bol.'),
    ('11111111-1111-1111-1111-111111111114', 1, 'Ajoutez le lait froid et melangez.'),

    ('11111111-1111-1111-1111-111111111115', 0, 'Faites infuser le the.'),
    ('11111111-1111-1111-1111-111111111115', 1, 'Tartinez les tranches de pain de miel et servez avec le the chaud.'),

    ('11111111-1111-1111-1111-111111111206', 0, 'Etalez la pate a pizza sur une plaque.'),
    ('11111111-1111-1111-1111-111111111206', 1, 'Garnissez de jambon et de fromage rape puis enfournez a four chaud jusqu''a ce que la pate soit doree.'),

    ('11111111-1111-1111-1111-111111111207', 0, 'Montez le croque-monsieur avec le pain de mie, le jambon et le fromage rape.'),
    ('11111111-1111-1111-1111-111111111207', 1, 'Faites-le dorer a la poele ou au four des deux cotes.'),

    ('11111111-1111-1111-1111-111111111208', 0, 'Faites cuire le steak hache a la poele selon la cuisson souhaitee.'),
    ('11111111-1111-1111-1111-111111111208', 1, 'Assemblez le burger dans le pain avec les garnitures de votre choix.'),

    ('11111111-1111-1111-1111-111111111209', 0, 'Coupez le pain en deux dans la longueur.'),
    ('11111111-1111-1111-1111-111111111209', 1, 'Tartinez de beurre et garnissez de jambon.'),

    ('11111111-1111-1111-1111-111111111210', 0, 'Battez les oeufs et faites-les cuire a la poele en omelette.'),
    ('11111111-1111-1111-1111-111111111210', 1, 'Servez accompagne de salade assaisonnee.'),

    ('11111111-1111-1111-1111-111111111211', 0, 'Battez les oeufs avec un peu de creme et versez sur le fond de pate avec les lardons.'),
    ('11111111-1111-1111-1111-111111111211', 1, 'Enfournez a four chaud jusqu''a ce que la quiche soit doree et prise.'),

    ('11111111-1111-1111-1111-111111111212', 0, 'Faites cuire les pates dans une grande casserole d''eau bouillante salee.'),
    ('11111111-1111-1111-1111-111111111212', 1, 'Melangez-les avec la creme fraiche et un peu de fromage rape.'),
    ('11111111-1111-1111-1111-111111111212', 2, 'Passez au four jusqu''a ce que le dessus soit gratine.'),

    ('11111111-1111-1111-1111-111111111213', 0, 'Coupez les pommes de terre en morceaux et disposez-les autour du poulet dans un plat.'),
    ('11111111-1111-1111-1111-111111111213', 1, 'Faites rotir le tout au four jusqu''a ce que le poulet soit bien dore.'),

    ('11111111-1111-1111-1111-111111111214', 0, 'Faites cuire la semoule selon les instructions, en la faisant gonfler avec de l''eau chaude.'),
    ('11111111-1111-1111-1111-111111111214', 1, 'Faites cuire le poulet a la poele ou au four.'),
    ('11111111-1111-1111-1111-111111111214', 2, 'Servez le poulet sur la semoule.'),

    ('11111111-1111-1111-1111-111111111215', 0, 'Faites cuire les pommes de terre a l''eau puis ecrasez-les en puree.'),
    ('11111111-1111-1111-1111-111111111215', 1, 'Faites revenir le steak hache a la poele.'),
    ('11111111-1111-1111-1111-111111111215', 2, 'Disposez la viande dans un plat, recouvrez de puree et passez au four jusqu''a ce que le dessus soit dore.');

-- =====================================================================
-- V3 : HEALTHY (15 petits-dejeuners + 15 dejeuners)
-- =====================================================================

INSERT INTO repas_etape (repas_id, etape_ordre, description) VALUES
    ('22222222-2222-2222-2222-222222222101', 0, 'Faites chauffer les flocons d''avoine avec du lait ou de l''eau jusqu''a obtenir une texture cremeuse.'),
    ('22222222-2222-2222-2222-222222222101', 1, 'Ajoutez le miel et servez chaud.'),

    ('22222222-2222-2222-2222-222222222102', 0, 'Mixez les fruits rouges avec le lait d''amande jusqu''a obtenir une texture lisse.'),
    ('22222222-2222-2222-2222-222222222102', 1, 'Versez dans un bol et decorez de fruits.'),

    ('22222222-2222-2222-2222-222222222103', 0, 'Faites griller les tranches de pain complet.'),
    ('22222222-2222-2222-2222-222222222103', 1, 'Ecrasez l''avocat a la fourchette et etalez-le sur le pain.'),

    ('22222222-2222-2222-2222-222222222104', 0, 'Versez le fromage blanc dans un bol.'),
    ('22222222-2222-2222-2222-222222222104', 1, 'Ajoutez les fruits rouges par-dessus.'),

    ('22222222-2222-2222-2222-222222222105', 0, 'Faites revenir les epinards a la poele quelques minutes.'),
    ('22222222-2222-2222-2222-222222222105', 1, 'Pochez les oeufs dans une eau fremissante puis servez-les sur les epinards.'),

    ('22222222-2222-2222-2222-222222222106', 0, 'Melangez les graines de chia avec le lait d''amande.'),
    ('22222222-2222-2222-2222-222222222106', 1, 'Laissez reposer au frais jusqu''a ce que le melange epaississe, puis servez.'),

    ('22222222-2222-2222-2222-222222222107', 0, 'Versez le yaourt grec dans un bol.'),
    ('22222222-2222-2222-2222-222222222107', 1, 'Ajoutez un filet de miel et servez.'),

    ('22222222-2222-2222-2222-222222222108', 0, 'Mixez la banane et les epinards avec un peu d''eau ou de lait jusqu''a obtenir une texture lisse.'),
    ('22222222-2222-2222-2222-222222222108', 1, 'Servez frais.'),

    ('22222222-2222-2222-2222-222222222109', 0, 'Tartinez le pain complet d''houmous.'),
    ('22222222-2222-2222-2222-222222222109', 1, 'Ajoutez des rondelles de concombre par-dessus.'),

    ('22222222-2222-2222-2222-222222222110', 0, 'Versez le muesli dans un bol.'),
    ('22222222-2222-2222-2222-222222222110', 1, 'Ajoutez le lait d''amande et melangez.'),

    ('22222222-2222-2222-2222-222222222111', 0, 'Faites infuser le the.'),
    ('22222222-2222-2222-2222-222222222111', 1, 'Servez accompagne de fruits secs.'),

    ('22222222-2222-2222-2222-222222222112', 0, 'Faites griller le pain complet si besoin.'),
    ('22222222-2222-2222-2222-222222222112', 1, 'Tartinez-le de miel et servez.'),

    ('22222222-2222-2222-2222-222222222113', 0, 'Coupez la banane en rondelles.'),
    ('22222222-2222-2222-2222-222222222113', 1, 'Melangez-la avec les fruits rouges dans un bol et servez frais.'),

    ('22222222-2222-2222-2222-222222222114', 0, 'Faites cuire le quinoa dans le lait d''amande jusqu''a ce qu''il soit tendre et cremeux.'),
    ('22222222-2222-2222-2222-222222222114', 1, 'Servez chaud.'),

    ('22222222-2222-2222-2222-222222222115', 0, 'Ecrasez l''avocat a la fourchette et etalez-le sur une tranche de pain.'),
    ('22222222-2222-2222-2222-222222222115', 1, 'Faites cuire un oeuf au plat et deposez-le par-dessus.'),

    ('22222222-2222-2222-2222-222222222201', 0, 'Faites cuire le quinoa puis laissez-le refroidir.'),
    ('22222222-2222-2222-2222-222222222201', 1, 'Faites cuire le poulet a la poele et coupez-le en des.'),
    ('22222222-2222-2222-2222-222222222201', 2, 'Melangez le quinoa et le poulet, assaisonnez et servez.'),

    ('22222222-2222-2222-2222-222222222202', 0, 'Faites cuire le saumon a la poele ou au four.'),
    ('22222222-2222-2222-2222-222222222202', 1, 'Coupez l''avocat en tranches et dressez le tout dans un bol.'),

    ('22222222-2222-2222-2222-222222222203', 0, 'Faites cuire les brocolis a la vapeur quelques minutes.'),
    ('22222222-2222-2222-2222-222222222203', 1, 'Faites revenir les crevettes a la poele puis melangez avec les brocolis.'),

    ('22222222-2222-2222-2222-222222222204', 0, 'Faites revenir la tomate coupee en des quelques minutes.'),
    ('22222222-2222-2222-2222-222222222204', 1, 'Ajoutez les lentilles et de l''eau, puis laissez mijoter jusqu''a ce qu''elles soient tendres.'),

    ('22222222-2222-2222-2222-222222222205', 0, 'Egouttez les pois chiches et melangez-les avec la tomate coupee en des.'),
    ('22222222-2222-2222-2222-222222222205', 1, 'Assaisonnez et servez frais.'),

    ('22222222-2222-2222-2222-222222222206', 0, 'Faites cuire le saumon et les brocolis a la vapeur jusqu''a ce qu''ils soient tendres.'),
    ('22222222-2222-2222-2222-222222222206', 1, 'Assaisonnez et servez.'),

    ('22222222-2222-2222-2222-222222222207', 0, 'Faites cuire le riz complet dans une casserole d''eau bouillante salee.'),
    ('22222222-2222-2222-2222-222222222207', 1, 'Faites cuire les brocolis a la vapeur puis melangez-les au riz.'),

    ('22222222-2222-2222-2222-222222222208', 0, 'Faites cuire le poulet a la poele et coupez-le en lanieres.'),
    ('22222222-2222-2222-2222-222222222208', 1, 'Melangez avec la salade lavee et coupee, assaisonnez et servez.'),

    ('22222222-2222-2222-2222-222222222209', 0, 'Faites cuire le quinoa puis laissez-le refroidir.'),
    ('22222222-2222-2222-2222-222222222209', 1, 'Melangez-le avec la tomate coupee en des et assaisonnez.'),

    ('22222222-2222-2222-2222-222222222210', 0, 'Coupez le concombre en des.'),
    ('22222222-2222-2222-2222-222222222210', 1, 'Melangez-le avec le thon emiette, assaisonnez et servez.'),

    ('22222222-2222-2222-2222-222222222211', 0, 'Faites cuire la patate douce coupee en des dans une casserole avec un peu d''eau.'),
    ('22222222-2222-2222-2222-222222222211', 1, 'Ajoutez les pois chiches et laissez mijoter jusqu''a ce que la patate douce soit tendre.'),

    ('22222222-2222-2222-2222-222222222212', 0, 'Faites cuire les haricots verts a l''eau ou a la vapeur.'),
    ('22222222-2222-2222-2222-222222222212', 1, 'Melangez-les avec le thon emiette, assaisonnez et servez.'),

    ('22222222-2222-2222-2222-222222222213', 0, 'Faites revenir les brocolis dans un wok bien chaud.'),
    ('22222222-2222-2222-2222-222222222213', 1, 'Ajoutez les crevettes et faites sauter le tout quelques minutes.'),

    ('22222222-2222-2222-2222-222222222214', 0, 'Faites chauffer de l''eau et delayez-y la pate miso.'),
    ('22222222-2222-2222-2222-222222222214', 1, 'Ajoutez les epinards et laissez cuire quelques minutes.'),

    ('22222222-2222-2222-2222-222222222215', 0, 'Faites cuire la patate douce coupee en morceaux au four ou a l''eau.'),
    ('22222222-2222-2222-2222-222222222215', 1, 'Faites griller le poulet a la poele jusqu''a ce qu''il soit bien cuit.');

-- =====================================================================
-- V3 : GOURMAND (15 petits-dejeuners + 15 dejeuners)
-- =====================================================================

INSERT INTO repas_etape (repas_id, etape_ordre, description) VALUES
    ('33333333-3333-3333-3333-333333333101', 0, 'Faites rechauffer les pancakes a la poele quelques instants.'),
    ('33333333-3333-3333-3333-333333333101', 1, 'Arrosez-les de sirop d''erable et servez.'),

    ('33333333-3333-3333-3333-333333333102', 0, 'Rechauffez le pain au chocolat au four quelques minutes.'),
    ('33333333-3333-3333-3333-333333333102', 1, 'Servez avec un chocolat chaud prepare avec le chocolat en poudre.'),

    ('33333333-3333-3333-3333-333333333103', 0, 'Faites dorer les tranches de pain a la poele avec un peu de beurre.'),
    ('33333333-3333-3333-3333-333333333103', 1, 'Nappez-les de caramel beurre sale et servez.'),

    ('33333333-3333-3333-3333-333333333104', 0, 'Coupez la brioche en tranches.'),
    ('33333333-3333-3333-3333-333333333104', 1, 'Parsemez de pralines roses concassees et servez.'),

    ('33333333-3333-3333-3333-333333333105', 0, 'Faites chauffer du lait et delayez-y le chocolat en poudre.'),
    ('33333333-3333-3333-3333-333333333105', 1, 'Versez dans une tasse et deposez une bonne cuillere de chantilly par-dessus.'),

    ('33333333-3333-3333-3333-333333333106', 0, 'Etalez la pate feuilletee sur une plaque.'),
    ('33333333-3333-3333-3333-333333333106', 1, 'Disposez les pommes coupees en fines lamelles par-dessus.'),
    ('33333333-3333-3333-3333-333333333106', 2, 'Enfournez a four chaud jusqu''a ce que la pate soit doree.'),

    ('33333333-3333-3333-3333-333333333107', 0, 'Rechauffez les viennoiseries au four quelques minutes si besoin.'),
    ('33333333-3333-3333-3333-333333333107', 1, 'Disposez-les sur un plateau et servez.'),

    ('33333333-3333-3333-3333-333333333108', 0, 'Faites rechauffer les gaufres au grille-pain.'),
    ('33333333-3333-3333-3333-333333333108', 1, 'Faites fondre le chocolat en poudre avec un peu de lait et nappez-en les gaufres.'),

    ('33333333-3333-3333-3333-333333333109', 0, 'Faites griller les tranches de pain de mie.'),
    ('33333333-3333-3333-3333-333333333109', 1, 'Pochez les oeufs dans une eau fremissante et deposez-les sur le pain grille.'),

    ('33333333-3333-3333-3333-333333333110', 0, 'Faites cuire les oeufs a votre convenance (brouilles ou au plat).'),
    ('33333333-3333-3333-3333-333333333110', 1, 'Servez-les accompagnes de tranches de saumon.'),

    ('33333333-3333-3333-3333-333333333111', 0, 'Faites chauffer le jus d''orange dans une poele avec un peu de sucre jusqu''a legere reduction.'),
    ('33333333-3333-3333-3333-333333333111', 1, 'Faites revenir les crepes dans ce sirop puis pliez-les et servez.'),

    ('33333333-3333-3333-3333-333333333112', 0, 'Trempez les tranches de pain dans un melange d''oeuf battu et de lait, puis faites-les dorer a la poele.'),
    ('33333333-3333-3333-3333-333333333112', 1, 'Nappez de caramel beurre sale et servez.'),

    ('33333333-3333-3333-3333-333333333113', 0, 'Coupez la brioche en tranches.'),
    ('33333333-3333-3333-3333-333333333113', 1, 'Faites-la legerement dorer a la poele avec un peu de beurre.'),

    ('33333333-3333-3333-3333-333333333114', 0, 'Etalez la pate feuilletee et saupoudrez-la genereusement de sucre.'),
    ('33333333-3333-3333-3333-333333333114', 1, 'Repliez plusieurs fois puis enfournez a four chaud jusqu''a caramelisation.'),

    ('33333333-3333-3333-3333-333333333115', 0, 'Disposez les pommes coupees en des sur la moitie de la pate feuilletee.'),
    ('33333333-3333-3333-3333-333333333115', 1, 'Repliez la pate, soudez les bords et enfournez a four chaud jusqu''a ce qu''elle soit doree.'),

    ('33333333-3333-3333-3333-333333333201', 0, 'Faites revenir les morceaux de boeuf dans une cocotte jusqu''a ce qu''ils soient dores.'),
    ('33333333-3333-3333-3333-333333333201', 1, 'Ajoutez le vin rouge et laissez mijoter a feu doux pendant plusieurs heures jusqu''a ce que la viande soit tendre.'),

    ('33333333-3333-3333-3333-333333333202', 0, 'Faites saisir le magret de canard cote peau a la poele jusqu''a ce qu''il soit dore.'),
    ('33333333-3333-3333-3333-333333333202', 1, 'Faites revenir les champignons dans la graisse rendue et servez avec le magret tranche.'),

    ('33333333-3333-3333-3333-333333333203', 0, 'Faites revenir les champignons puis reservez-les.'),
    ('33333333-3333-3333-3333-333333333203', 1, 'Faites nacrer le riz arborio puis ajoutez du bouillon louche par louche en remuant jusqu''a cuisson cremeuse.'),
    ('33333333-3333-3333-3333-333333333203', 2, 'Incorporez les champignons et servez.'),

    ('33333333-3333-3333-3333-333333333204', 0, 'Disposez les pommes de terre coupees en fines rondelles dans un plat avec de la creme et enfournez jusqu''a ce qu''elles soient fondantes.'),
    ('33333333-3333-3333-3333-333333333204', 1, 'Faites cuire la cote de boeuf a la poele selon la cuisson souhaitee et servez avec le gratin.'),

    ('33333333-3333-3333-3333-333333333205', 0, 'Faites cuire le veau a feu doux dans un bouillon jusqu''a ce qu''il soit tendre.'),
    ('33333333-3333-3333-3333-333333333205', 1, 'Liez la sauce avec la creme fraiche et servez.'),

    ('33333333-3333-3333-3333-333333333206', 0, 'Faites dorer la cuisse de canard confite a la poele jusqu''a ce que la peau soit croustillante.'),
    ('33333333-3333-3333-3333-333333333206', 1, 'Faites sauter les pommes de terre coupees en morceaux dans la graisse de canard et servez ensemble.'),

    ('33333333-3333-3333-3333-333333333207', 0, 'Faites cuire les pommes de terre coupees en rondelles avec des oignons et des lardons.'),
    ('33333333-3333-3333-3333-333333333207', 1, 'Disposez le tout dans un plat, recouvrez de reblochon coupe en deux et enfournez jusqu''a ce que le fromage soit fondu et dore.'),

    ('33333333-3333-3333-3333-333333333208', 0, 'Faites cuire la choucroute a feu doux avec un peu de bouillon pendant une bonne heure.'),
    ('33333333-3333-3333-3333-333333333208', 1, 'Ajoutez la saucisse fumee en fin de cuisson pour la rechauffer et servez.'),

    ('33333333-3333-3333-3333-333333333209', 0, 'Faites revenir le riz dans une grande poele avec des legumes.'),
    ('33333333-3333-3333-3333-333333333209', 1, 'Ajoutez du bouillon et laissez cuire, puis incorporez les crevettes en fin de cuisson jusqu''a ce qu''elles soient roses.'),

    ('33333333-3333-3333-3333-333333333210', 0, 'Faites revenir le boeuf hache avec de la sauce tomate pour preparer la bolognaise.'),
    ('33333333-3333-3333-3333-333333333210', 1, 'Montez les lasagnes en alternant plaques de pates, bolognaise et bechamel.'),
    ('33333333-3333-3333-3333-333333333210', 2, 'Enfournez a four chaud jusqu''a ce que le dessus soit gratine.'),

    ('33333333-3333-3333-3333-333333333211', 0, 'Faites cuire les pommes de terre a l''eau avec la peau.'),
    ('33333333-3333-3333-3333-333333333211', 1, 'Faites fondre le fromage a raclette sur les pommes de terre chaudes et servez.'),

    ('33333333-3333-3333-3333-333333333212', 0, 'Faites fondre le fromage a raclette dans un caquelon avec un peu de vin blanc.'),
    ('33333333-3333-3333-3333-333333333212', 1, 'Servez avec des cubes de pain a tremper.'),

    ('33333333-3333-3333-3333-333333333213', 0, 'Faites saisir le filet mignon a la poele jusqu''a la cuisson souhaitee puis reservez au chaud.'),
    ('33333333-3333-3333-3333-333333333213', 1, 'Faites revenir les champignons dans la meme poele avec un peu de creme pour preparer la sauce, puis servez sur la viande.'),

    ('33333333-3333-3333-3333-333333333214', 0, 'Faites chauffer le beurre dans une poele bien chaude.'),
    ('33333333-3333-3333-3333-333333333214', 1, 'Saisissez les Saint-Jacques une minute de chaque cote et servez aussitot.'),

    ('33333333-3333-3333-3333-333333333215', 0, 'Faites saisir le magret de canard cote peau a la poele jusqu''a ce qu''il soit dore.'),
    ('33333333-3333-3333-3333-333333333215', 1, 'Deglacez avec le jus d''orange pour preparer la sauce et servez le magret tranche nappe de sauce.');

-- =====================================================================
-- V4 : diners NORMAL (15)
-- =====================================================================

INSERT INTO repas_etape (repas_id, etape_ordre, description) VALUES
    ('11111111-1111-1111-1111-111111111301', 0, 'Faites rechauffer la soupe a feu doux.'),
    ('11111111-1111-1111-1111-111111111301', 1, 'Servez chaud avec des tranches de pain.'),

    ('11111111-1111-1111-1111-111111111302', 0, 'Battez les oeufs avec le jambon coupe en des.'),
    ('11111111-1111-1111-1111-111111111302', 1, 'Faites cuire le tout a la poele jusqu''a ce que l''omelette soit prise.'),

    ('11111111-1111-1111-1111-111111111303', 0, 'Faites cuire les pates dans une grande casserole d''eau bouillante salee.'),
    ('11111111-1111-1111-1111-111111111303', 1, 'Faites revenir les lardons a la poele.'),
    ('11111111-1111-1111-1111-111111111303', 2, 'Egouttez les pates, melangez-les avec les lardons et un oeuf battu hors du feu.'),

    ('11111111-1111-1111-1111-111111111304', 0, 'Faites cuire le riz puis laissez-le refroidir.'),
    ('11111111-1111-1111-1111-111111111304', 1, 'Faites revenir le riz avec le jambon coupe en des et un oeuf brouille dans une poele bien chaude.'),

    ('11111111-1111-1111-1111-111111111305', 0, 'Montez le croque-monsieur avec le pain de mie et le fromage rape.'),
    ('11111111-1111-1111-1111-111111111305', 1, 'Faites-le dorer a la poele ou au four et servez avec une salade verte.'),

    ('11111111-1111-1111-1111-111111111306', 0, 'Faites cuire le poisson pane au four selon les instructions du paquet.'),
    ('11111111-1111-1111-1111-111111111306', 1, 'Faites cuire les pommes de terre a l''eau puis ecrasez-les en puree.'),

    ('11111111-1111-1111-1111-111111111307', 0, 'Etalez la pate feuilletee dans un moule.'),
    ('11111111-1111-1111-1111-111111111307', 1, 'Repartissez le jambon et le fromage, recouvrez d''appareil a quiche et enfournez a four chaud jusqu''a ce qu''elle soit doree.'),

    ('11111111-1111-1111-1111-111111111308', 0, 'Faites cuire le poulet a la poele et coupez-le en lanieres.'),
    ('11111111-1111-1111-1111-111111111308', 1, 'Garnissez la tortilla wrap de poulet et de crudites puis roulez-la.'),

    ('11111111-1111-1111-1111-111111111309', 0, 'Faites cuire les courgettes coupees en rondelles a la poele quelques minutes.'),
    ('11111111-1111-1111-1111-111111111309', 1, 'Disposez-les dans un plat, recouvrez de fromage rape et enfournez jusqu''a ce que le dessus soit gratine.'),

    ('11111111-1111-1111-1111-111111111310', 0, 'Formez des boulettes avec le steak hache.'),
    ('11111111-1111-1111-1111-111111111310', 1, 'Faites-les dorer a la poele puis laissez-les mijoter dans une sauce tomate.'),

    ('11111111-1111-1111-1111-111111111311', 0, 'Faites dorer le poulet coupe en morceaux dans une poele.'),
    ('11111111-1111-1111-1111-111111111311', 1, 'Ajoutez le lait de coco et laissez mijoter jusqu''a ce que le poulet soit bien cuit.'),

    ('11111111-1111-1111-1111-111111111312', 0, 'Faites dorer les morceaux de veau dans une cocotte puis laissez mijoter a feu doux avec un peu de bouillon jusqu''a ce qu''ils soient fondants.'),
    ('11111111-1111-1111-1111-111111111312', 1, 'Faites cuire le riz separement et servez avec la viande.'),

    ('11111111-1111-1111-1111-111111111313', 0, 'Faites cuire les pommes de terre a l''eau puis ecrasez-les en puree.'),
    ('11111111-1111-1111-1111-111111111313', 1, 'Effilochez la cuisse de canard confite, disposez-la dans un plat, recouvrez de puree et passez au four.'),

    ('11111111-1111-1111-1111-111111111314', 0, 'Faites nacrer le riz arborio puis ajoutez du bouillon louche par louche en remuant jusqu''a cuisson cremeuse.'),
    ('11111111-1111-1111-1111-111111111314', 1, 'Ajoutez les crevettes en fin de cuisson jusqu''a ce qu''elles soient roses et servez.'),

    ('11111111-1111-1111-1111-111111111315', 0, 'Faites saisir le filet de boeuf a la poele selon la cuisson souhaitee.'),
    ('11111111-1111-1111-1111-111111111315', 1, 'Faites revenir les champignons dans la meme poele avec un peu de creme pour la sauce.');

-- =====================================================================
-- V4 : diners HEALTHY (15)
-- =====================================================================

INSERT INTO repas_etape (repas_id, etape_ordre, description) VALUES
    ('22222222-2222-2222-2222-222222222301', 0, 'Faites chauffer de l''eau et delayez-y la pate miso.'),
    ('22222222-2222-2222-2222-222222222301', 1, 'Ajoutez le tofu coupe en des et laissez cuire quelques minutes.'),

    ('22222222-2222-2222-2222-222222222302', 0, 'Faites cuire les lentilles a l''eau jusqu''a ce qu''elles soient tendres, puis laissez-les refroidir.'),
    ('22222222-2222-2222-2222-222222222302', 1, 'Melangez-les avec la feta emiettee, assaisonnez et servez.'),

    ('22222222-2222-2222-2222-222222222303', 0, 'Faites cuire le cabillaud et les brocolis a la vapeur jusqu''a ce qu''ils soient tendres.'),
    ('22222222-2222-2222-2222-222222222303', 1, 'Assaisonnez et servez.'),

    ('22222222-2222-2222-2222-222222222304', 0, 'Faites cuire le quinoa puis laissez-le refroidir.'),
    ('22222222-2222-2222-2222-222222222304', 1, 'Dressez-le dans un bol avec l''avocat coupe en tranches.'),

    ('22222222-2222-2222-2222-222222222305', 0, 'Egouttez les pois chiches et melangez-les avec le concombre coupe en des.'),
    ('22222222-2222-2222-2222-222222222305', 1, 'Assaisonnez et servez frais.'),

    ('22222222-2222-2222-2222-222222222306', 0, 'Faites revenir le tofu coupe en des dans un wok bien chaud jusqu''a ce qu''il soit dore.'),
    ('22222222-2222-2222-2222-222222222306', 1, 'Ajoutez la courgette coupee en rondelles et faites sauter le tout quelques minutes.'),

    ('22222222-2222-2222-2222-222222222307', 0, 'Faites cuire le potiron coupe en morceaux dans un peu d''eau jusqu''a ce qu''il soit tendre.'),
    ('22222222-2222-2222-2222-222222222307', 1, 'Mixez avec le lait de coco jusqu''a obtenir une soupe onctueuse.'),

    ('22222222-2222-2222-2222-222222222308', 0, 'Faites cuire le quinoa puis laissez-le refroidir.'),
    ('22222222-2222-2222-2222-222222222308', 1, 'Melangez-le avec la tomate coupee en des, assaisonnez et servez.'),

    ('22222222-2222-2222-2222-222222222309', 0, 'Faites chauffer les pois chiches egouttes avec le lait de coco.'),
    ('22222222-2222-2222-2222-222222222309', 1, 'Laissez mijoter quelques minutes puis assaisonnez et servez.'),

    ('22222222-2222-2222-2222-222222222310', 0, 'Faites griller le poulet a la poele jusqu''a ce qu''il soit bien cuit.'),
    ('22222222-2222-2222-2222-222222222310', 1, 'Faites cuire les brocolis a la vapeur et servez ensemble.'),

    ('22222222-2222-2222-2222-222222222311', 0, 'Disposez le saumon et la courgette coupee en rondelles dans une papillote.'),
    ('22222222-2222-2222-2222-222222222311', 1, 'Enfournez a four chaud jusqu''a ce que le saumon soit cuit.'),

    ('22222222-2222-2222-2222-222222222312', 0, 'Faites revenir les brocolis dans un wok bien chaud.'),
    ('22222222-2222-2222-2222-222222222312', 1, 'Ajoutez les crevettes et faites sauter le tout quelques minutes jusqu''a ce qu''elles soient roses.'),

    ('22222222-2222-2222-2222-222222222313', 0, 'Coupez le saumon cru en des (ou faites-le cuire selon votre gout).'),
    ('22222222-2222-2222-2222-222222222313', 1, 'Dressez-le dans un bol avec l''avocat coupe en tranches.'),

    ('22222222-2222-2222-2222-222222222314', 0, 'Faites cuire le quinoa a l''eau jusqu''a ce qu''il soit tendre.'),
    ('22222222-2222-2222-2222-222222222314', 1, 'Faites cuire le filet de cabillaud a la poele ou a la vapeur et servez sur le quinoa.'),

    ('22222222-2222-2222-2222-222222222315', 0, 'Faites chauffer les pois chiches egouttes dans une casserole.'),
    ('22222222-2222-2222-2222-222222222315', 1, 'Ajoutez les epinards et laissez cuire quelques minutes jusqu''a ce qu''ils soient fondus.');

-- =====================================================================
-- V4 : diners GOURMAND (15)
-- =====================================================================

INSERT INTO repas_etape (repas_id, etape_ordre, description) VALUES
    ('33333333-3333-3333-3333-333333333301', 0, 'Faites cuire les pates dans une grande casserole d''eau bouillante salee.'),
    ('33333333-3333-3333-3333-333333333301', 1, 'Melangez-les avec une sauce au fromage rape puis passez au four jusqu''a ce que le dessus soit gratine.'),

    ('33333333-3333-3333-3333-333333333302', 0, 'Faites dorer l''escalope de veau a la poele.'),
    ('33333333-3333-3333-3333-333333333302', 1, 'Ajoutez la creme fraiche et laissez mijoter quelques minutes pour lier la sauce.'),

    ('33333333-3333-3333-3333-333333333303', 0, 'Parsemez le poulet d''herbes de provence.'),
    ('33333333-3333-3333-3333-333333333303', 1, 'Faites-le rotir au four jusqu''a ce qu''il soit bien dore.'),

    ('33333333-3333-3333-3333-333333333304', 0, 'Disposez les pommes de terre coupees en fines rondelles dans un plat avec de la creme et le jambon coupe en des.'),
    ('33333333-3333-3333-3333-333333333304', 1, 'Enfournez jusqu''a ce que le gratin soit fondant et dore.'),

    ('33333333-3333-3333-3333-333333333305', 0, 'Etalez la pate feuilletee dans un moule.'),
    ('33333333-3333-3333-3333-333333333305', 1, 'Garnissez de jambon et de fromage rape puis enfournez a four chaud jusqu''a ce que la tarte soit doree.'),

    ('33333333-3333-3333-3333-333333333306', 0, 'Faites cuire les pommes de terre a l''eau ou au four.'),
    ('33333333-3333-3333-3333-333333333306', 1, 'Faites griller les saucisses fumees a la poele et servez ensemble.'),

    ('33333333-3333-3333-3333-333333333307', 0, 'Badigeonnez la cote de porc de moutarde.'),
    ('33333333-3333-3333-3333-333333333307', 1, 'Faites-la cuire a la poele jusqu''a ce qu''elle soit bien doree des deux cotes.'),

    ('33333333-3333-3333-3333-333333333308', 0, 'Faites saisir le boeuf coupe en lanieres dans une poele bien chaude.'),
    ('33333333-3333-3333-3333-333333333308', 1, 'Ajoutez les champignons et faites revenir le tout quelques minutes.'),

    ('33333333-3333-3333-3333-333333333309', 0, 'Faites revenir les lardons a la poele.'),
    ('33333333-3333-3333-3333-333333333309', 1, 'Melangez-les avec les crozets cuits, recouvrez de reblochon et enfournez jusqu''a ce que le fromage soit fondu.'),

    ('33333333-3333-3333-3333-333333333310', 0, 'Preparez une farce avec le saumon emiette et la creme fraiche, puis enfermez-la dans des feuilles de pate.'),
    ('33333333-3333-3333-3333-333333333310', 1, 'Faites dorer les rissoles a la poele ou au four.'),

    ('33333333-3333-3333-3333-333333333311', 0, 'Faites saisir le magret de canard cote peau a la poele jusqu''a ce qu''il soit dore.'),
    ('33333333-3333-3333-3333-333333333311', 1, 'Deglacez avec le miel pour preparer la sauce et servez le magret tranche.'),

    ('33333333-3333-3333-3333-333333333312', 0, 'Faites saisir les Saint-Jacques rapidement a la poele.'),
    ('33333333-3333-3333-3333-333333333312', 1, 'Ajoutez la creme fraiche et laissez mijoter quelques instants pour lier la sauce.'),

    ('33333333-3333-3333-3333-333333333313', 0, 'Faites saisir le filet mignon a la poele sur toutes les faces.'),
    ('33333333-3333-3333-3333-333333333313', 1, 'Enveloppez-le dans la pate feuilletee et enfournez a four chaud jusqu''a ce que la croute soit doree.'),

    ('33333333-3333-3333-3333-333333333314', 0, 'Faites dorer la cuisse de canard confite a la poele jusqu''a ce que la peau soit croustillante.'),
    ('33333333-3333-3333-3333-333333333314', 1, 'Faites revenir les pommes coupees en quartiers dans la graisse de canard et servez ensemble.'),

    ('33333333-3333-3333-3333-333333333315', 0, 'Faites nacrer le riz arborio puis ajoutez du bouillon louche par louche en remuant jusqu''a cuisson cremeuse.'),
    ('33333333-3333-3333-3333-333333333315', 1, 'Incorporez le parmesan rape hors du feu et servez.');

-- =====================================================================
-- Historique des plans : meme colonne d'etapes, cote de la table
-- plan_historique_repas (voir RepasHistoriqueEntity).
-- =====================================================================

CREATE TABLE plan_historique_repas_etape (
    repas_id UUID NOT NULL REFERENCES plan_historique_repas (id) ON DELETE CASCADE,
    etape_ordre INT NOT NULL,
    description TEXT NOT NULL,
    PRIMARY KEY (repas_id, etape_ordre)
);

-- Nettoyage des plans historises crees avant l'ajout des etapes de
-- preparation (tests manuels via curl pendant le developpement de la
-- fonctionnalite d'historique) : sans cette purge, la reconstruction en
-- domaine echouerait sur l'invariant "un repas doit comporter au moins
-- une etape de preparation" (voir Repas). Le cascade sur plan_historique
-- supprime aussi ses lignes plan_historique_repas et
-- plan_historique_repas_ingredient associees.
DELETE FROM plan_historique;
