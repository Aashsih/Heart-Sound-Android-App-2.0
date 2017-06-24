package com.head_first.aashi.heartsounds_20.model;

/**
 * Created by Aashish Indorewala on 11-Jan-17.
 * This model represents a Doctor and extends the PatientUser
 * class.
 * A doctor is responsible for creating/editing/sharing patients
 * and for managing their HeartSound and MurmurRating.
 */

public class Doctor extends PatientUser {

    public Doctor(String id, String username, String firstName, String lastName, String email) {
        super(id, username, firstName, lastName, email);
    }

}
