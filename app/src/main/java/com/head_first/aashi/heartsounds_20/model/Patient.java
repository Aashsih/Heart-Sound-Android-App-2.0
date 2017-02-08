package com.head_first.aashi.heartsounds_20.model;

import com.head_first.aashi.heartsounds_20.enums.Gender;
import com.head_first.aashi.heartsounds_20.exception.InputException;
import com.head_first.aashi.heartsounds_20.exception.InvalidDateOfBirthException;
import com.head_first.aashi.heartsounds_20.exception.InvalidNameException;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public final class Patient {
    private static DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    private long id;
    private long doctorId;
    private Date dateOfBirth;
    private Gender gender;
    private boolean isPublic; //Public students are visible to Students

    /**
     *This constructor should be used when gettin data from the database
     */
    public Patient(long doctorId, String name, Date dateOfBirth, Gender gender) {

        //The id will be received from the data base
        //this.id = (ID.add(BigInteger.ONE)).toString();
        this.doctorId = doctorId;
        this.setDateOfBirth(dateOfBirth);
        this.setGender(gender);
    }

    /**
     * This constructor should only be called when the patient is created for the first time
     */
    public Patient(String name, Date dateOfBirth, Gender gender) {
        //deviceId can also be checked for but is skipped for now

        //The first argument needs to be obtained from the current user logged in doctor.
        this(0,name, dateOfBirth, gender);
    }



    //Getters and Setters
    public final long getId() {
        return id;
    }

    public final long getDoctorId() {
        return doctorId;
    }


    public final Date getDateOfBirth() {
        return dateOfBirth;
    }

    public final void setDateOfBirth(Date dateOfBirth) throws InputException{
        if(dateOfBirth == null){
            throw new InvalidDateOfBirthException(null);
        }
        else if(dateOfBirth.after(new Date())){
            throw new InvalidDateOfBirthException(dateOfBirth.toString());
        }
        else{
            this.dateOfBirth = dateOfBirth;
        }
    }

    public final Gender getGender() {
        return gender;
    }

    public final void setGender(Gender gender) {
        if(gender == null){
            //Is selecting gender a necessity
            gender = Gender.OTHER;
        }
        else{
            this.gender = gender;
        }
    }


}
