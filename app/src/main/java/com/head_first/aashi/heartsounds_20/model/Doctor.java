package com.head_first.aashi.heartsounds_20.model;

/**
 * Created by Aashish Indorewala on 11-Jan-17.
 */

public class Doctor extends PatientUser {
    /**
     * The doctor needs to store information created by him/her separately.
     *
     * 1. PatientListFragment created by the doctor
     * 2. Shared PatientListFragment
     * (Note the PatientListFragment object will be storing all the HeartSounds that are associated with the patient,
     * however the Doctor needs to know which ones were created by him/her)
     * 3. HeartSounds created by the doctor for a Particular Patient
     * 4. Other HeartSounds for a patient
     *(3 and 4 will be methods available to a doctor: This feature will also be useful to decide which HeartSounds are editable)
     * 5. For any update that a doctor tries to make to the tables keep a check to verify that the doctor is only able to update
     * the Content created by him/her. If an attempt is made to update any other entry, then an exception should be thrown stating
     * not authorized.
     */
    public Doctor(String id, String username, String firstName, String lastName, String email) {
        super(id, username, firstName, lastName, email);
    }

}
