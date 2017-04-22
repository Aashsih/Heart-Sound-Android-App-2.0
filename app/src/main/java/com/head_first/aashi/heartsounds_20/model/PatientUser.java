package com.head_first.aashi.heartsounds_20.model;

/**
 * Created by Aashish Indorewala on 20-Jan-17.
 */

public abstract class PatientUser extends User {

    public PatientUser(String id, String username, String firstName, String lastName, String email) {
        super(id, username, firstName, lastName, email);
    }
}
