package com.head_first.aashi.heartsounds_20.model;

import java.util.Date;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public final class Patient {
    //private static DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    private long patientId;
    private String primaryDoctorId;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String gender;
    private boolean isPublic; //Public students are visible to Students
    private Date createdOn;
    private boolean isActive;

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

    //To String

    @Override
    public String toString(){
        if(!(lastName == null || lastName.isEmpty())){
            return lastName.toUpperCase().charAt(0) + ". " + firstName;
        }
        else{
            return firstName;
        }
    }


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
