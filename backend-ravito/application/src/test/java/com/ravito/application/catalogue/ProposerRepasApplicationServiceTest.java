package com.ravito.application.catalogue;

import com.ravito.domain.catalogue.CatalogueInsuffisantException;
import com.ravito.domain.catalogue.CatalogueRepasPort;
import com.ravito.domain.catalogue.Repas;
import com.ravito.domain.catalogue.RepasId;
import com.ravito.domain.catalogue.RepasProposes;
import com.ravito.domain.catalogue.TypeRepas;
import com.ravito.domain.courses.Ingredient;
import com.ravito.domain.courses.IngredientQuantite;
import com.ravito.domain.courses.Quantite;
import com.ravito.domain.courses.RayonMagasin;
import com.ravito.domain.courses.UniteMesure;
import com.ravito.domain.profil.Enseigne;
import com.ravito.domain.profil.NiveauCuisine;
import com.ravito.domain.profil.NombreDePersonnes;
import com.ravito.domain.profil.ProfilUtilisateur;
import com.ravito.domain.profil.StyleAlimentaire;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProposerRepasApplicationServiceTest {

    private static final ProfilUtilisateur PROFIL =
            new ProfilUtilisateur(Enseigne.CARREFOUR, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT, NombreDePersonnes.une());

    @Mock
    private CatalogueRepasPort catalogueRepasPort;

    private ProposerRepasApplicationService service;

    @BeforeEach
    void setUp() {
        service = new ProposerRepasApplicationService(catalogueRepasPort);
    }

    @Test
    void interroge_le_port_avec_le_type_et_le_style_du_profil() {
        when(catalogueRepasPort.rechercherParTypeEtStyle(TypeRepas.PETIT_DEJEUNER, StyleAlimentaire.NORMAL))
                .thenReturn(cinqRepas(TypeRepas.PETIT_DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT));
        when(catalogueRepasPort.rechercherParTypeEtStyle(TypeRepas.DEJEUNER, StyleAlimentaire.NORMAL))
                .thenReturn(cinqRepas(TypeRepas.DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT));

        service.proposer(PROFIL);

        verify(catalogueRepasPort).rechercherParTypeEtStyle(TypeRepas.PETIT_DEJEUNER, StyleAlimentaire.NORMAL);
        verify(catalogueRepasPort).rechercherParTypeEtStyle(TypeRepas.DEJEUNER, StyleAlimentaire.NORMAL);
    }

    @Test
    void ecarte_les_repas_dont_le_niveau_ou_le_style_ne_convient_pas_au_profil() {
        List<Repas> petitsDejeuners = new ArrayList<>(
                cinqRepas(TypeRepas.PETIT_DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT));
        // un repas trop avance pour un profil DEBUTANT :
        petitsDejeuners.add(unRepas(TypeRepas.PETIT_DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.CONFIRME));
        // un repas d'un autre style, que le port n'aurait jamais du renvoyer :
        petitsDejeuners.add(unRepas(TypeRepas.PETIT_DEJEUNER, StyleAlimentaire.HEALTHY, NiveauCuisine.DEBUTANT));

        when(catalogueRepasPort.rechercherParTypeEtStyle(TypeRepas.PETIT_DEJEUNER, StyleAlimentaire.NORMAL))
                .thenReturn(petitsDejeuners);
        when(catalogueRepasPort.rechercherParTypeEtStyle(TypeRepas.DEJEUNER, StyleAlimentaire.NORMAL))
                .thenReturn(cinqRepas(TypeRepas.DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT));

        RepasProposes proposition = service.proposer(PROFIL);

        assertThat(proposition.petitsDejeuners()).hasSize(5);
        assertThat(proposition.petitsDejeuners())
                .allMatch(repas -> repas.estCompatibleAvec(PROFIL));
    }

    @Test
    void propage_l_exception_si_moins_de_cinq_repas_compatibles() {
        List<Repas> quatreRepas = IntStream.range(0, 4)
                .mapToObj(i -> unRepas(TypeRepas.PETIT_DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT))
                .toList();

        when(catalogueRepasPort.rechercherParTypeEtStyle(TypeRepas.PETIT_DEJEUNER, StyleAlimentaire.NORMAL))
                .thenReturn(quatreRepas);
        when(catalogueRepasPort.rechercherParTypeEtStyle(TypeRepas.DEJEUNER, StyleAlimentaire.NORMAL))
                .thenReturn(cinqRepas(TypeRepas.DEJEUNER, StyleAlimentaire.NORMAL, NiveauCuisine.DEBUTANT));

        assertThatExceptionOfType(CatalogueInsuffisantException.class)
                .isThrownBy(() -> service.proposer(PROFIL));
    }

    private static List<Repas> cinqRepas(TypeRepas type, StyleAlimentaire style, NiveauCuisine niveauRequis) {
        return IntStream.range(0, 5)
                .mapToObj(i -> unRepas(type, style, niveauRequis))
                .toList();
    }

    private static Repas unRepas(TypeRepas type, StyleAlimentaire style, NiveauCuisine niveauRequis) {
        IngredientQuantite ingredient = new IngredientQuantite(
                new Ingredient("ingredient de test", RayonMagasin.EPICERIE),
                new Quantite(BigDecimal.ONE, UniteMesure.UNITE));
        return new Repas(RepasId.nouveau(), "repas de test", type, style, niveauRequis, List.of(ingredient));
    }
}
