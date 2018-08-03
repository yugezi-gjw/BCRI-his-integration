package com.varian.oiscn.patient.integration.config;

import lombok.Getter;

/**
 * @Author: Allen
 * @Description:
 * @Date: Created in 12/20/2017
 * @Modified By:
 */
@Getter
public enum HisServerStatusEnum {
    NO_CONFIGURATION("101","HisPatient.Server.NoConfiguration"), // NO_CONFIGURATION
    BAD_CONFIGURATION("102", "HisPatient.Server.BadConfiguration"), // BAD_CONFIGURATION
    SERVER_NOT_OK("103", "HisPatient.Server.ServerNotOK"), // SERVER_NOT_READY
    SERVICE_NOT_AVAILABLE("104", "HisPatient.Server.ServiceNotAvailable"), // SERVICE_NOT_AVAILABLE
    SERVICE_TIME_OUT("105", "HisPatient.Server.ServiceTimeout"), // SERVICE_TIME_OUT
    SERVICE_ERROR("106", "HisPatient.Server.ServerError"), // SERVICE_ERROR
    BAD_REQUEST("107", "HisPatient.Server.BadRequest"), // BAD_REQUEST
    NORMAL("100", "");


    protected String code;
    protected String errMsg;

    HisServerStatusEnum(String code, String errMsg) {
        this.code = code;
        this.errMsg = errMsg;
    }


}
