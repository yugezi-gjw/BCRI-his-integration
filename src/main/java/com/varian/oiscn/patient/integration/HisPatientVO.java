package com.varian.oiscn.patient.integration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: Allen
 * @Description:
 * @Date: Created in 12/21/2017
 * @Modified By:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HisPatientVO {

    private String patname;
    private String patimg;
    private String idnum;
    private String sex;
    private Date birthday;
    private int age;
    private String patientid;
    private String address;
    private String phone;
    private String medicalhistory;
    private String paymenttype;
    private String pattype;
    private String wgname;
    private String adr;
    private String adr_1;
    private String adr_2;
    private String prediagnosis;
    private String specdiseases;
    private String ecog;
    private String ecog_describe;
    private String turmorStage;

}
