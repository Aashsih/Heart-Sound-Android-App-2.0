package com.head_first.aashi.heartsounds_20.model;

import com.head_first.aashi.heartsounds_20.interfaces.model_interfaces.IUser;
import com.head_first.aashi.heartsounds_20.web_api.WebAPI;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */


public abstract class User implements IUser{
    private String id;
    private String accessToken;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Date createdOn;
    private boolean isActive;
    private long userPermissionId;

    public User(String id, String username, String firstName, String lastName, String email){
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    //Getters
    public String getId() {
        return id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public boolean isActive() {
        return isActive;
    }

    public long getUserPermissionId() {
        return userPermissionId;
    }

    public String getUserName(){
        if(!(lastName == null || lastName.isEmpty())){
            return lastName.toUpperCase().charAt(0) + ". " + firstName;
        }
        else{
            return firstName;
        }
    }

//    Setters
//
//    public final void setUsername(String username) throws InputException{
//        if(username == null || username.equals("")){
//            //possibly throw an exception here
//            throw new InvalidUsernameException(username);
//
//        }
//        else if(false){
//            //Query database to check if the
//            //pass any non-empty string to the InvalidUsernameException constructor
//            throw new InvalidUsernameException("Username Already Exists");
//        }
//        else{
//            this.username = username;
//        }
//
//    }
//
//    public final String getFirstName() {
//        return this.firstName;
//    }
//
//    public final void setFirstName(String firstName) throws InputException{
//        if(firstName == null || firstName.equals("")){
//            //possibly throw an exception here
//            throw new InvalidNameException(firstName);
//        }
//        else{
//            this.firstName = firstName;
//        }
//    }
//
//    public final void setLastName(String lastName) throws InputException{
//        if(lastName == null || lastName.equals("")){
//            //possibly throw an exception here
//            throw new InvalidNameException(lastName);
//        }
//        else{
//            this.lastName = lastName;
//        }
//    }
//
//
//    public final void setPassword(String password) throws InputException{
//        this.password.setEncryptedPassword(password);
//    }

}
