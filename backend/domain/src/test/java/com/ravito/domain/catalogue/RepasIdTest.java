package com.ravito.domain.catalogue;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class RepasIdTest {

    @Test
    void deux_ids_nouveaux_sont_differents() {
        assertThat(RepasId.nouveau()).isNotEqualTo(RepasId.nouveau());
    }

    @Test
    void deux_ids_portant_le_meme_uuid_sont_egaux() {
        UUID uuid = UUID.randomUUID();

        assertThat(new RepasId(uuid)).isEqualTo(new RepasId(uuid));
    }

    @Test
    void refuse_un_uuid_nul() {
        assertThatNullPointerException().isThrownBy(() -> new RepasId(null));
    }
}
