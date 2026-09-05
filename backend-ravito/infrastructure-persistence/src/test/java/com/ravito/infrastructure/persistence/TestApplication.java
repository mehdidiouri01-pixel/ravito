package com.ravito.infrastructure.persistence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Contexte Spring minimal pour les tests d'integration de ce module.
 *
 * <p>Ce module n'est pas une application executable (pas de {@code main}
 * utilise en dehors des tests) — le vrai point d'entree viendra avec le
 * futur module {@code bootstrap}. Cette classe n'existe que pour donner
 * a {@code @SpringBootTest} un contexte a demarrer, avec le scan
 * d'entites/repositories/composants limite au package de ce module.
 */
@SpringBootApplication
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
