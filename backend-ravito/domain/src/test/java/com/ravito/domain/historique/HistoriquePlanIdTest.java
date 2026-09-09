package com.ravito.domain.historique;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class HistoriquePlanIdTest {

    @Test
    void deux_ids_nouveaux_sont_differents() {
        assertThat(HistoriquePlanId.nouveau()).isNotEqualTo(HistoriquePlanId.nouveau());
    }

    @Test
    void deux_ids_portant_le_meme_uuid_sont_egaux() {
        UUID uuid = UUID.randomUUID();

        assertThat(new HistoriquePlanId(uuid)).isEqualTo(new HistoriquePlanId(uuid));
    }

    @Test
    void refuse_un_uuid_nul() {
        assertThatNullPointerException().isThrownBy(() -> new HistoriquePlanId(null));
    }
}
