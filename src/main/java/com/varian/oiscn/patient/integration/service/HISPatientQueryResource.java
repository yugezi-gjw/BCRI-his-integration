package com.varian.oiscn.patient.integration.service;

import com.varian.oiscn.core.patient.RegistrationVO;
import com.varian.oiscn.patient.integration.HisPatientVO;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Slf4j
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HISPatientQueryResource {
    private static final String GENDER_UNKNOWN = "0";
    private static final String GENDER_MALE = "1";
    private static final String GENDER_FEMALE = "2";
    private static Map<String, String> codePool = new HashMap<>();

    static {
        codePool.put("A", "活动性肝炎");
        codePool.put("B", "皮肤疾病");
        codePool.put("C", "HIV");
        codePool.put("D", "梅毒");
        codePool.put("Z", "粒子");
        codePool.put("Y", "无");
    }

    private static final String REQUEST_PARAMS_NAME = "patientid=";

    protected Configuration configuration;
    protected Environment environment;

    public HISPatientQueryResource(Configuration configuration, Environment environment) {
        this.configuration = configuration;
        this.environment = environment;
    }

    @Path("/his-patient/search")
    @GET
    public Response queryPatient(@QueryParam("patientid") String hisId) {
        log.debug("Query patient from HIS. HisId[{}]", hisId);
        RegistrationVO registrationVO = null;
        if (HisPatientInfoService.isOK()) {
            HisPatientVO hisPatientVO = HisPatientInfoService.callHisInterface(REQUEST_PARAMS_NAME + hisId);

            log.debug("The patient from HIS is {}.", ReflectionToStringBuilder.toString(hisPatientVO));
            if (hisPatientVO != null) {

                registrationVO = new RegistrationVO();

                registrationVO.setChineseName(hisPatientVO.getPatname());

                String patimg = hisPatientVO.getPatimg();
                if (isNotEmpty(patimg)) {
                    byte[] photoByte = Base64.decodeBase64(patimg);
                    registrationVO.setPhotoByte(photoByte);
                    registrationVO.setPhoto(patimg);
                }
                registrationVO.setNationalId(hisPatientVO.getIdnum());

                String sex = hisPatientVO.getSex();
                String gender;
                if (isNotEmpty(sex)) {
                    switch (sex) {
                        case GENDER_UNKNOWN:
                            gender = "Unknown";
                            break;
                        case GENDER_MALE:
                            gender = "Male";
                            break;
                        case GENDER_FEMALE:
                            gender = "Female";
                            break;
                        default:
                            gender = "Unknown";
                    }
                    registrationVO.setGender(gender);
                }
                registrationVO.setBirthday(hisPatientVO.getBirthday());
                registrationVO.setAge(String.valueOf(hisPatientVO.getAge()));
                registrationVO.setHisId(hisPatientVO.getPatientid());
                registrationVO.setAddress(hisPatientVO.getAddress());
                registrationVO.setTelephone(hisPatientVO.getPhone());
                registrationVO.setPatientHistory(hisPatientVO.getMedicalhistory());

                String paymenttype = hisPatientVO.getPaymenttype();
                switch (paymenttype) {
                    case "0":
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                        registrationVO.setInsuranceType(paymenttype);
                        break;
                    case "5":
                        registrationVO.setUrgent(true);
                        break;
                }

                registrationVO.setPatientSource(hisPatientVO.getPattype());
                String pattype = hisPatientVO.getPattype();
                switch (pattype) {
                    case "0":
                        registrationVO.setPatientSource("门诊");
                        break;
                    case "1":
                        registrationVO.setPatientSource("住院");
                        break;
                    default:
                        registrationVO.setPatientSource("门诊");
                }
                registrationVO.setDiagnosisDesc(hisPatientVO.getPrediagnosis());

                String specdis = hisPatientVO.getSpecdiseases();
                String positiveFlag;
                StringBuffer specBuffer = new StringBuffer();
                if (isNotEmpty(specdis)) {
                    String[] spec = specdis.split(",");
                    for (String sp : spec) {
                        specBuffer.append(codePool.get(sp)).append(",");
                    }

                    positiveFlag = specBuffer.toString();
                    positiveFlag = positiveFlag.substring(0, positiveFlag.length() - 1);
                    registrationVO.setPositiveSign(positiveFlag);

                }

                if (isNotEmpty(hisPatientVO.getEcog())) {
                    registrationVO.setEcogScore(hisPatientVO.getEcog());
                }

                if (isNotEmpty(hisPatientVO.getEcog_describe())) {
                    registrationVO.setEcogDesc(hisPatientVO.getEcog_describe());
                }

            }
        }
        log.debug("RegistrationVO is {}.", ReflectionToStringBuilder.toString(registrationVO));
        return Response.ok(registrationVO, new MediaType("application", "json", "utf-8")).build();
    }
}
