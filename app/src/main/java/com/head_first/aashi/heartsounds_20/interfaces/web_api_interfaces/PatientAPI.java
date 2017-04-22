package com.head_first.aashi.heartsounds_20.interfaces.web_api_interfaces;

import com.head_first.aashi.heartsounds_20.model.Patient;

/**
 * Created by Aashish Indorewala on 18-Mar-17.
 */

public interface PatientAPI {
    public void sharePatient(String doctorId);
    public void unSharePatient(String doctorId);
    public void requestPatient(int patientId);
    public void requestPatients();
    public void createPatient(Patient patient);
    public void updatePatient(Patient patient);
    public void deletePatient(int patientId);
}
