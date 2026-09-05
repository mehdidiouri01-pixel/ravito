package com.ravito.application.prix;

import com.ravito.domain.courses.ListeCourses;
import com.ravito.domain.prix.EstimationPrixPort;
import com.ravito.domain.prix.Prix;
import com.ravito.domain.profil.Enseigne;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstimerCoutApplicationServiceTest {

    @Mock
    private EstimationPrixPort estimationPrixPort;

    private EstimerCoutApplicationService service;

    @BeforeEach
    void setUp() {
        service = new EstimerCoutApplicationService(estimationPrixPort);
    }

    @Test
    void delegue_l_estimation_au_port_de_sortie() {
        ListeCourses listeCourses = new ListeCourses(List.of());
        Prix prixAttendu = new Prix(BigDecimal.valueOf(37.90));
        when(estimationPrixPort.estimerCout(listeCourses, Enseigne.LIDL)).thenReturn(prixAttendu);

        Prix prix = service.estimer(listeCourses, Enseigne.LIDL);

        assertThat(prix).isEqualTo(prixAttendu);
        verify(estimationPrixPort).estimerCout(listeCourses, Enseigne.LIDL);
    }

    @Test
    void refuse_une_liste_de_courses_nulle() {
        assertThatNullPointerException().isThrownBy(() -> service.estimer(null, Enseigne.AUCHAN));
    }

    @Test
    void refuse_une_enseigne_nulle() {
        ListeCourses listeCourses = new ListeCourses(List.of());

        assertThatNullPointerException().isThrownBy(() -> service.estimer(listeCourses, null));
    }
}
