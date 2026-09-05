package com.ravito;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Point d'entree unique de l'application, et seul module du projet a
 * dependre de tout le reste : {@code application}, {@code
 * infrastructure-persistence}, {@code infrastructure-web} (qui ne se
 * connaissent pas entre eux — chacun ne depend que de {@code domain}).
 *
 * <p>Place dans le package racine {@code com.ravito} (et pas
 * {@code com.ravito.bootstrap}) pour que le component scan par defaut de
 * {@code @SpringBootApplication} (son propre package et en-dessous) couvre
 * naturellement {@code com.ravito.application}, {@code
 * com.ravito.infrastructure.persistence} et {@code com.ravito.infrastructure.web}
 * sans avoir a lister explicitement des {@code basePackages} — la meme
 * mecanique detecte aussi automatiquement les entites JPA et les
 * {@code JpaRepository} de ces sous-packages.
 */
@SpringBootApplication
public class RavitoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RavitoApplication.class, args);
    }
}
