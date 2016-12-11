package com.head_first.aashi.heartsounds_20.model;

import com.head_first.aashi.heartsounds_20.enums.Gender;
import com.head_first.aashi.heartsounds_20.exception.InputException;
import com.head_first.aashi.heartsounds_20.exception.InvalidDateOfBirthException;
import com.head_first.aashi.heartsounds_20.exception.InvalidNameException;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public final class Patient {
    private static DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    private String id;
    private String doctorId;
    private String deviceId;
    private String name;
    private Date dateOfBirth;
    private Gender gender;

    /**
     *This constructor should be used when gettin data from the database
     */
    public Patient(String doctorId, String deviceId,String name, Date dateOfBirth, Gender gender) {

        //The id will be received from the data base
        //this.id = (ID.add(BigInteger.ONE)).toString();
        this.doctorId = doctorId;
        this.deviceId = deviceId;
        this.setName(name);
        this.setDateOfBirth(dateOfBirth);
        this.setGender(gender);
    }

    /**
     * This constructor should only be called when the patient is created for the first time
     */
    public Patient(String deviceId,String name, Date dateOfBirth, Gender gender) {
        //deviceId can also be checked for but is skipped for now

        //The first argument needs to be obtained from the current logged in doctor.
        this("", deviceId,name, dateOfBirth, gender);
    }



    //Getters and Setters
    public final String getId() {
        return id;
    }

    public final String getDoctorId() {
        return doctorId;
    }

    public final String getDeviceId() {
        return deviceId;
    }

    public final String getName() {
        return name;
    }

    public final void setName(String name) throws InputException{
        if(name == null || name.equals("")){
            //possibly throw an exception here
            throw new InvalidNameException(name);
        }
        else{
            this.name = name;
        }
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
