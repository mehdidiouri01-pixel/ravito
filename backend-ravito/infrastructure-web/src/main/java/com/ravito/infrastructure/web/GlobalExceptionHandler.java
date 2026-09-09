package com.ravito.infrastructure.web;

import com.ravito.domain.catalogue.CatalogueInsuffisantException;
import com.ravito.domain.catalogue.GenerationRepasImpossibleException;
import com.ravito.domain.catalogue.RepasIntrouvableException;
import com.ravito.domain.historique.HistoriquePlanIntrouvableException;
import com.ravito.domain.planification.PlanSemaineInvalideException;
import com.ravito.domain.planification.RepasGenereInvalideException;
import com.ravito.domain.planification.RepasIncompatibleException;
import com.ravito.domain.prix.EstimationImpossibleException;
import com.ravito.infrastructure.web.planification.ChoixRepasInvalideException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduit les exceptions du domaine en reponses HTTP. Volontairement place
 * ici, pas dans le domaine ni l'application : le choix d'un code HTTP
 * precis (404 vs 409 vs 422) est un detail du protocole de transport, pas
 * une regle metier.
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(RepasIntrouvableException.class)
    ResponseEntity<ErrorResponse> repasIntrouvable(RepasIntrouvableException exception) {
        return reponse(HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler(HistoriquePlanIntrouvableException.class)
    ResponseEntity<ErrorResponse> historiquePlanIntrouvable(HistoriquePlanIntrouvableException exception) {
        return reponse(HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler(CatalogueInsuffisantException.class)
    ResponseEntity<ErrorResponse> catalogueInsuffisant(CatalogueInsuffisantException exception) {
        return reponse(HttpStatus.CONFLICT, exception);
    }

    @ExceptionHandler(GenerationRepasImpossibleException.class)
    ResponseEntity<ErrorResponse> generationImpossible(GenerationRepasImpossibleException exception) {
        return reponse(HttpStatus.CONFLICT, exception);
    }

    @ExceptionHandler({RepasIncompatibleException.class, PlanSemaineInvalideException.class, RepasGenereInvalideException.class})
    ResponseEntity<ErrorResponse> planInvalide(RuntimeException exception) {
        return reponse(HttpStatus.UNPROCESSABLE_ENTITY, exception);
    }

    @ExceptionHandler(ChoixRepasInvalideException.class)
    ResponseEntity<ErrorResponse> choixRepasInvalide(ChoixRepasInvalideException exception) {
        return reponse(HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler(EstimationImpossibleException.class)
    ResponseEntity<ErrorResponse> estimationImpossible(EstimationImpossibleException exception) {
        return reponse(HttpStatus.UNPROCESSABLE_ENTITY, exception);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> requeteInvalide(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(erreur -> erreur.getField() + " : " + erreur.getDefaultMessage())
                .reduce((a, b) -> a + " ; " + b)
                .orElse("Requete invalide");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(message));
    }

    private static ResponseEntity<ErrorResponse> reponse(HttpStatus statut, RuntimeException exception) {
        return ResponseEntity.status(statut).body(new ErrorResponse(exception.getMessage()));
    }
}
