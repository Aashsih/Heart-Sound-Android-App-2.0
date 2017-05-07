package com.head_first.aashi.heartsounds_20.interfaces.web_api_interfaces;

import com.head_first.aashi.heartsounds_20.model.Patient;

import org.json.JSONException;

/**
 * Created by Aashish Indorewala on 18-Mar-17.
 */

public interface PatientAPI {
    public void sharePatient(String doctorId) throws JSONException;
    public void unSharePatient(String doctorId) throws JSONException;
    public void requestPatient(int patientId);
    public void requestPatients();
    public void createPatient(Patient patient) throws JSONException;
    public void updatePatient(Patient patient) throws JSONException;
    public void deletePatient(int patientId);
    public void getAllUsers();
}
