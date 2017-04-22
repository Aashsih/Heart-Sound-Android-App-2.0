package com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum FinalDiagnosis {
    @SerializedName("No Murmer")NO_MURMUR("No Murmer"),
    @SerializedName("Physiological Murmer")PHYSIOLOGICAL_MURMUR("Physiological Murmer"),
    @SerializedName("Aortic Stenosis")AORTIC_STENOSIS("Aortic Stenosis"),
    @SerializedName("Aortic Regurgitation")AORTIC_REGURGITATION("Aortic Regurgitation"),
    @SerializedName("Aortic Stenosis With Ejection Click")AORTIC_STENOSIS_WITH_EJECTION_CLICK("Aortic Stenosis With Ejection Click"),
    @SerializedName("Mitral Regurgitation")MITRAL_REGURGITATION("Mitral Regurgitation"),
    @SerializedName("Mitral Stenosis")MITRAL_STENOSIS("Mitral Stenosis"),
    @SerializedName("Mitral Regurgitation Due to Mitral Valve Prolapse With Mid Systolic Click")MITRAL_REGURGITATION_DUE_TO_MITRAL_VALVE_PROLAPSE_WITH_MID_SYSTOLIC_CLICK("Mitral Regurgitation Due to Mitral Valve Prolapse With Mid Systolic Click"),
    @SerializedName("Tricuspid Regurgitation")TRICUSPID_REGURGITATION("Tricuspid Regurgitation"),
    @SerializedName("Pulmonary Stenosis")PULMONARY_STENOSIS("Pulmonary Stenosis"),
    @SerializedName("Pulmonary Regurgitation")PULMONARY_REGURGITATION("Pulmonary Regurgitation"),
    @SerializedName("Patent Ductus Arteriosus")PATENT_DUCTUS_ARTERIOSUS("Patent Ductus Arteriosus"),
    @SerializedName("Fistula")FISTULA("Fistula"),
    @SerializedName("Ventricular Septal Defect")VENTRICULAR_SEPTAL_DEFECT("Ventricular Septal Defect"),
    @SerializedName("Atrial Septal Defect")ATRIAL_SEPTAL_DEFECT("Atrial Septal Defect"),
    @SerializedName("Hypertrophic Cardiomyopathy With Left Ventricular Outflow Obstruction")HYPERTROPHIC_CARDIOMYOPATHY_WITH_LEFT_VENTRICULAR_OUTFLOW_OBSTRUCTION("Hypertrophic Cardiomyopathy With Left Ventricular Outflow Obstruction");

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
