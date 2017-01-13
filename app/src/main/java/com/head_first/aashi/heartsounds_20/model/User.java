package com.head_first.aashi.heartsounds_20.model;

import com.head_first.aashi.heartsounds_20.exception.InputException;
import com.head_first.aashi.heartsounds_20.exception.InvalidNameException;
import com.head_first.aashi.heartsounds_20.exception.InvalidUsernameException;

import java.math.BigInteger;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */


public abstract class User {


    private String id;
    private String username;
    private Password password;
    private String name;
    //permissions (need to decide the data type for it)

    public User(String username, String password, String name) {
        //The id will be received from the data base
        //this.id = (ID.add(BigInteger.ONE)).toString();
        this.setUsername(username);
        this.setPassword(password);
        this.setName(name);
    }

    //Add abstract Methods here
    //Methods that involve querrying the database to get Data and common user tasks

    //Getters and Setters
    public final String getId() {
        return id;
    }

    public final String getUsername() {
        return username;
    }

    public final void setUsername(String username) throws InputException{
        if(username == null || username.equals("")){
            //possibly throw an exception here
            throw new InvalidUsernameException(username);

        }
        else if(false){
            //Query database to check if the
            //pass any non-empty string to the InvalidUsernameException constructor
            throw new InvalidUsernameException("Username Already Exists");
        }
        else{
            this.username = username;
        }

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

    public final Password getPassword() {
        return password;
    }

    public final void setPassword(String password) throws InputException{
        this.password.setEncryptedPassword(password);
    }

}
