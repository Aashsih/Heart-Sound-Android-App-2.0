package com.head_first.aashi.heartsounds_20.model;

/**
 * This class is current not in use.
 *
 * This model represents one Student User.
 * A Student can view Patient, their
 * HeartSound and MurmurRating if a Doctor
 * has made the Patient public.
 * A Student is not allowed to create a
 * Patient but is allowed to create MurmurRating.
 */

public class Student extends PatientUser {

    public Student(String id, String username, String firstName, String lastName, String email) {
        super(id, username, firstName, lastName, email);
    }
}
