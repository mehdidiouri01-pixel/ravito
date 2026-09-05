package com.ravito.infrastructure.persistence.prix;

import com.ravito.domain.profil.Enseigne;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

/**
 * Coefficient multiplicateur applique au sous-total (somme des prix moyens)
 * selon l'enseigne choisie — configure en YAML plutot qu'en base : seulement
 * 4 valeurs fixes, une table degagerait plus de complexite que de valeur.
 *
 * <pre>
 * ravito:
 *   prix:
 *     coefficients:
 *       CARREFOUR: 1.00
 *       LECLERC: 0.95
 *       LIDL: 0.85
 *       AUCHAN: 0.98
 * </pre>
 */
@Component
@ConfigurationProperties(prefix = "ravito.prix")
public class CoefficientEnseigneProperties {

    private Map<Enseigne, BigDecimal> coefficients = new EnumMap<>(Enseigne.class);

    public Map<Enseigne, BigDecimal> getCoefficients() {
        return coefficients;
    }

    public void setCoefficients(Map<Enseigne, BigDecimal> coefficients) {
        this.coefficients = coefficients;
    }

    /**
     * @return le coefficient configure pour l'enseigne, ou 1 (neutre) si
     * absent de la configuration plutot que de faire planter l'estimation.
     */
    BigDecimal pour(Enseigne enseigne) {
        return coefficients.getOrDefault(enseigne, BigDecimal.ONE);
    }
}
