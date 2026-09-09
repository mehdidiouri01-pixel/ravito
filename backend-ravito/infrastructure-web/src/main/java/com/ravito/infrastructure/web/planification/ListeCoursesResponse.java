package com.ravito.infrastructure.web.planification;

import com.ravito.domain.courses.LigneListeCourses;
import com.ravito.domain.courses.ListeCourses;
import com.ravito.domain.courses.RayonMagasin;
import com.ravito.domain.prix.LignePrixEstime;
import com.ravito.domain.prix.Prix;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * La liste de courses regroupee par rayon — c'est cette forme, pas la
 * liste a plat, que l'utilisateur veut voir a l'ecran (voir
 * {@code ListeCourses.groupParRayon}).
 */
public record ListeCoursesResponse(Map<RayonMagasin, List<LigneListeCoursesResponse>> parRayon) {

    public static ListeCoursesResponse depuis(ListeCourses listeCourses, List<LignePrixEstime> prixParLigne) {
        Map<LigneListeCourses, Prix> prixParLigneMap = prixParLigne.stream()
                .collect(Collectors.toMap(LignePrixEstime::ligne, LignePrixEstime::prix));

        Map<RayonMagasin, List<LigneListeCoursesResponse>> parRayon = listeCourses.groupParRayon().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entree -> entree.getValue().stream()
                                .map(ligne -> LigneListeCoursesResponse.depuis(ligne, prixParLigneMap.get(ligne)))
                                .toList()));
        return new ListeCoursesResponse(parRayon);
    }
}
