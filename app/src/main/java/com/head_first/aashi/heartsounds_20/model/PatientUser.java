package com.head_first.aashi.heartsounds_20.model;

/**
 * Created by Aashish Indorewala on 20-Jan-17.
 */

public abstract class PatientUser extends User {
    public PatientUser(String username, String password, String firstName, String lastName) {
        super(username, password, firstName, lastName);
    }
}
