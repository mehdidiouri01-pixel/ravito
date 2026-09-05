package com.ravito.infrastructure.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Contexte Spring minimal pour {@code @WebMvcTest} — meme role que son
 * homonyme dans le module {@code infrastructure-persistence} : ce module
 * n'est pas executable seul, le vrai point d'entree viendra du futur
 * module {@code bootstrap}.
 */
@SpringBootApplication
public class TestApplication {
}
