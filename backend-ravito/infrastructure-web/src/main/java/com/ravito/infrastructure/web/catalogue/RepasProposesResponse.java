package com.ravito.infrastructure.web.catalogue;

import com.ravito.domain.catalogue.RepasProposes;

import java.util.List;

public record RepasProposesResponse(List<RepasResponse> petitsDejeuners, List<RepasResponse> dejeuners) {

    public static RepasProposesResponse depuis(RepasProposes repasProposes) {
        return new RepasProposesResponse(
                repasProposes.petitsDejeuners().stream().map(RepasResponse::depuis).toList(),
                repasProposes.dejeuners().stream().map(RepasResponse::depuis).toList());
    }
}
