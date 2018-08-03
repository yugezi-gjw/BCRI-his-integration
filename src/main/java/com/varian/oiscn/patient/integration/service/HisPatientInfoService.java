package com.varian.oiscn.patient.integration.service;

import com.varian.oiscn.patient.integration.HisPatientVO;
import com.varian.oiscn.patient.integration.config.HisPatientInfoConfiguration;
import com.varian.oiscn.patient.integration.config.HisServerStatusEnum;
import com.varian.oiscn.patient.integration.exception.HisServiceException;
import com.varian.oiscn.util.JsonSerializer;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * @Author: Allen
 * @Description:
 * @Date: Created in 12/20/2017
 * @Modified By:
 */
@Slf4j
public class HisPatientInfoService {

    private static HisPatientInfoConfiguration configuration;
    private static boolean isOK = false;
    private static JsonSerializer jsonSerializer = new JsonSerializer();

    private HisPatientInfoService() {

    }

    /**
     * Init configuration
     * @param configFilePath
     */
    public static void init(String configFilePath) {
        if(isNotEmpty(configFilePath)) {
            try {
                loadConfiguration(configFilePath);
            } catch(HisServiceException e) {
                log.error(e.getMessage());
            }
            if(configuration != null) {
                HisPatientHttpClient.initConfig(configuration);
            }
            isOK = true;
        }
    }

    /**
     * Load configuratin from file
     * @param filePath
     * @return
     */
    private static HisPatientInfoConfiguration loadConfiguration(String filePath) throws HisServiceException{
        File file = new File(filePath);
        if(file != null && file.exists() && file.isFile() ) {
            try {
                configuration = new Yaml().loadAs(new FileInputStream(file), HisPatientInfoConfiguration.class);
            } catch(FileNotFoundException e) {
                log.error(e.getMessage());
                isOK = false;
                throw new HisServiceException(HisServerStatusEnum.NO_CONFIGURATION);
            }
        }
        else {
            isOK = false;
        }
        return configuration;
    }

    /**
     * Check the server status
     * @return
     */
    public static boolean isOK() {return isOK;}

    /**
     * Send HTTP request to HIS interface and get response
     * @param params
     * @return
     */
    public static HisPatientVO callHisInterface(String params) {
        HisPatientHttpClient client = null;
        HisPatientVO hisPatientVO = null;
        String json = null;
        try {
            client = new HisPatientHttpClient(params);
            json = client.sendMessage();
            if(isNotEmpty(json)) {
                hisPatientVO = (HisPatientVO) jsonSerializer.getObject(json, HisPatientVO.class);
            }
        } catch(HisServiceException e) {
            log.error(e.getMessage());
        } finally {
            try {
                if(client != null) {
                    client.close();
                }
            } catch(Exception ie) {
                log.error(ie.getMessage());
            }
        }
        return hisPatientVO;
    }

}
