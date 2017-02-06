package com.head_first.aashi.heartsounds_20.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum FinalDiagnosis {
    NO_MURMUR("No Murmer"),PHYSIOLOGICAL_MURMUR("Physiological Murmer"), AORTIC_STENOSIS("Aortic Stenosis"),
    AORTIC_REGURGITATION("Aortic Regurgitation"), AORTIC_STENOSIS_WITH_EJECTION_CLICK("Aortic Stenosis With Ejection Click"),
    MITRAL_REGURGITATION("Mitral Regurgitation"), MITRAL_STENOSIS("Mitral Stenosis"),
    MITRAL_REGURGITATION_DUE_TO_MITRAL_VALVE_PROLAPSE_WITH_MID_SYSTOLIC_CLICK("Mitral Regurgitation Due to Mitral Valve Prolapse With Mid Systolic Click"),
    TRICUSPID_REGURGITATION("Tricuspid Regurgitation"), PULMONARY_STENOSIS("Pulmonary Stenosis"),
    PULMONARY_REGURGITATION("Pulmonary Regurgitation"), PATENT_DUCTUS_ARTERIOSUS("Patent Ductus Arteriosus"),
    FISTULA("Fistula"), VENTRICULAR_SEPTAL_DEFECT("Ventricular Septal Defect"), ATRIAL_SEPTAL_DEFECT("Atrial Septal Defect"),
    HYPERTROPHIC_CARDIOMYOPATHY_WITH_LEFT_VENTRICULAR_OUTFLOW_OBSTRUCTION("Hypertrophic Cardiomyopathy With Left Ventricular Outflow Obstruction");

    private static Map<String, FinalDiagnosis> valueToFinalDiagnosis = new HashMap<String, FinalDiagnosis>();

    static {
        for(FinalDiagnosis finalDiagnosis : FinalDiagnosis.values()){
            valueToFinalDiagnosis.put(finalDiagnosis.value,finalDiagnosis);
        }
    }

    public static FinalDiagnosis getFinalDiagnosis(String value){
        return valueToFinalDiagnosis.get(value);
    }

    private String value;

    private FinalDiagnosis(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }
}
