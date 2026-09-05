package com.ravito;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test de bout en bout : demarre le contexte Spring complet (application +
 * infrastructure-persistence + infrastructure-web cables ensemble, comme en
 * production) contre un vrai PostgreSQL, et verifie les 3 fonctionnalites du
 * domaine en enchainant de vrais appels HTTP — c'est la premiere fois que
 * tout l'hexagone tourne assemble d'un bout a l'autre.
 *
 * <p>S'appuie sur les donnees de demo semees par Flyway
 * (V2__donnees_demo.sql, module infrastructure-persistence) : 5
 * petits-dejeuners + 5 dejeuners, style NORMAL, niveau DEBUTANT.
 */
@SpringBootTest(classes = RavitoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class RavitoApplicationIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void propose_compose_un_plan_et_estime_son_cout_de_bout_en_bout() throws Exception {
        // 1. Proposition de repas pour le profil de demo (NORMAL/DEBUTANT)
        String requeteProfil = """
                {"enseigne":"CARREFOUR","style":"NORMAL","niveau":"DEBUTANT"}
                """;
        ResponseEntity<String> propositionReponse = restTemplate.postForEntity(
                "/api/repas/propositions", entiteJson(requeteProfil), String.class);

        assertThat(propositionReponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode proposition = objectMapper.readTree(propositionReponse.getBody());
        // Au moins 5 (l'invariant du domaine) : la taille exacte depend du
        // contenu du catalogue seme par Flyway (V2/V3), pas de ce test.
        assertThat(proposition.get("petitsDejeuners").size()).isGreaterThanOrEqualTo(5);
        assertThat(proposition.get("dejeuners").size()).isGreaterThanOrEqualTo(5);

        // 2. Composition d'un plan de semaine avec le premier petit-dejeuner et
        // le premier dejeuner proposes, repetes sur les 5 jours (suffisant pour
        // ce test de plomberie de bout en bout).
        UUID petitDejeunerId = UUID.fromString(proposition.get("petitsDejeuners").get(0).get("id").asText());
        UUID dejeunerId = UUID.fromString(proposition.get("dejeuners").get(0).get("id").asText());

        String choixJour = """
                {"petitDejeunerId":"%s","dejeunerId":"%s"}
                """.formatted(petitDejeunerId, dejeunerId);
        String requetePlan = """
                {"profil":%s,"choix":{"LUNDI":%s,"MARDI":%s,"MERCREDI":%s,"JEUDI":%s,"VENDREDI":%s}}
                """.formatted(requeteProfil, choixJour, choixJour, choixJour, choixJour, choixJour);

        ResponseEntity<String> planReponse = restTemplate.postForEntity(
                "/api/plans-semaine", entiteJson(requetePlan), String.class);

        assertThat(planReponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode plan = objectMapper.readTree(planReponse.getBody());
        assertThat(plan.get("jours")).hasSize(5);
        // 5 jours x (1 ingredient petit-dej + 1 ingredient dejeuner), consolides :
        // au moins une ligne de liste de courses, et un prix strictement positif.
        assertThat(plan.get("listeCourses").get("parRayon")).isNotEmpty();
        assertThat(plan.get("prixEstime").get("montant").decimalValue())
                .isGreaterThan(java.math.BigDecimal.ZERO);
    }

    private HttpEntity<String> entiteJson(String corps) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(corps, headers);
    }
}
