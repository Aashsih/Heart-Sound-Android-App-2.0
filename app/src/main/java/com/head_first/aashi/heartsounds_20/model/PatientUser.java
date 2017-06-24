package com.head_first.aashi.heartsounds_20.model;

/**
 * This class represents a PatientUser and
 * extends the User class.
 *
 * This class will be extended by the type of
 * user that will interact with a Patient.
 */

public abstract class PatientUser extends User {

    public PatientUser(String id, String username, String firstName, String lastName, String email) {
        super(id, username, firstName, lastName, email);
    }
}
