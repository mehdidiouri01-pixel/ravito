package com.ravito.infrastructure.web.planification;

import com.ravito.domain.courses.ListeCourses;
import com.ravito.domain.courses.RayonMagasin;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * La liste de courses regroupee par rayon — c'est cette forme, pas la
 * liste a plat, que l'utilisateur veut voir a l'ecran (voir
 * {@code ListeCourses.groupParRayon}).
 */
public record ListeCoursesResponse(Map<RayonMagasin, List<LigneListeCoursesResponse>> parRayon) {

    public static ListeCoursesResponse depuis(ListeCourses listeCourses) {
        Map<RayonMagasin, List<LigneListeCoursesResponse>> parRayon = listeCourses.groupParRayon().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entree -> entree.getValue().stream().map(LigneListeCoursesResponse::depuis).toList()));
        return new ListeCoursesResponse(parRayon);
    }
}
