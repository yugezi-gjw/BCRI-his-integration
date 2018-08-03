package com.varian.oiscn.patient.integration.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author: Allen
 * @Description:
 * @Date: Created in 12/20/2017
 * @Modified By:
 */
@Getter
@Setter
@NoArgsConstructor
public class HisPatientInfoConfiguration {

    @JsonProperty
    private HisPatientInfo patientInfoServer;

}
