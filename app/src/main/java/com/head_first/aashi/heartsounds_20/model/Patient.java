package com.head_first.aashi.heartsounds_20.model;

import com.head_first.aashi.heartsounds_20.exception.InvalidDateOfBirthException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This model represents one Patient.
 *
 * A Patient can be created by a doctor and
 * can have one or more HeartSound associated
 * with it.
 */

public final class Patient {
    private static String DATE_FORMAT = "yyyy-MM-dd'T'hh:mm:ss.SSS";

    private long patientId;
    private String primaryDoctorId;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String gender;
    private boolean isPublic; //Public students are visible to Students
    private Date createdOn;
    private boolean isActive;

    public Patient(String primaryDoctorId){
        this.setPrimaryDoctorId(primaryDoctorId);
    }

    //Getters
    public long getPatientId() {
        return patientId;
    }

    public String getPrimaryDoctorId() {
        return primaryDoctorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getDateOfBirthInLocalDateTimeFormat(){
        if(dateOfBirth == null){
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        return simpleDateFormat.format(dateOfBirth);
    }

    //Setters
    public void setPrimaryDoctorId(String primaryDoctorId) {
        boolean validPrimaryDoctorId = User.isValidUserId(primaryDoctorId);
        if(validPrimaryDoctorId){
            this.primaryDoctorId = primaryDoctorId;
        }
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDateOfBirth(Date dateOfBirth) throws ParseException {
        this.dateOfBirth = dateOfBirth;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    //To String
    @Override
    public String toString(){
        if(lastName != null && !lastName.isEmpty()){
            return lastName.toUpperCase().charAt(0) + ". " + firstName;
        }
        else if((firstName != null && !firstName.isEmpty())){
            return firstName;
        }
        else{
            return "Patient id: " + patientId;
        }
    }

//The following code will be used in the future
    /**
     *This constructor should be used when gettin data from the database
     */
//    public Patient(long primaryDoctorId, String name, Date dateOfBirth, Gender gender) {
//
//        //The patientId will be received from the data base
//        //this.patientId = (ID.add(BigInteger.ONE)).toString();
//        this.primaryDoctorId = primaryDoctorId;
//        this.setDateOfBirth(dateOfBirth);
//        this.setGender(gender);
//    }

    /**
     * This constructor should only be called when the patient is created for the first time
     */
//    public Patient(String name, Date dateOfBirth, Gender gender) {
//        //deviceId can also be checked for but is skipped for now
//
//        //The first argument needs to be obtained from the current user logged in doctor.
//        this(0,name, dateOfBirth, gender);
//    }



//    Setters
//    public final void setDateOfBirth(Date dateOfBirth) throws InputException{
//        if(dateOfBirth == null){
//            throw new InvalidDateOfBirthException(null);
//        }
//        else if(dateOfBirth.after(new Date())){
//            throw new InvalidDateOfBirthException(dateOfBirth.toString());
//        }
//        else{
//            this.dateOfBirth = dateOfBirth;
//        }
//    }
//
//    public final void setGender(Gender gender) {
//        if(gender == null){
//            //Is selecting gender a necessity
//            gender = Gender.OTHER;
//        }
//        else{
//            this.gender = gender;
//        }
//    }


}
