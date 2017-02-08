package com.head_first.aashi.heartsounds_20.model;

import com.head_first.aashi.heartsounds_20.exception.InputException;
import com.head_first.aashi.heartsounds_20.exception.InvalidNameException;
import com.head_first.aashi.heartsounds_20.exception.InvalidUsernameException;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */


public abstract class User {
    /**
     * only store the necessary information like
     * Create a new class to store the following:
     * id, username, password, access token for the user session
     *
     * All other models should reflect excatly what is stored in the database so that when a JSON
     * response is obtained it can be converted into objects of those type using external libraries
     *
     */

    private long id;
    private long userPermissionId;
    private String username;
    private Password password;
    private String firstName;
    private String lastName;
    //permissions (need to decide the data type for it)

    public User(String username, String password, String firstName, String lastName) {
        //The id will be received from the data base
        //this.id = (ID.add(BigInteger.ONE)).toString();
        this.setUsername(username);
        this.setPassword(password);
        this.setFirstName(firstName);
        this.setLastName(lastName);
    }

    //Add abstract Methods here
    //Methods that involve querrying the database to get Data and common user tasks

    //Getters and Setters
    public final long getId() {
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

    public final String getFirstName() {
        return this.firstName;
    }

    public final void setFirstName(String firstName) throws InputException{
        if(firstName == null || firstName.equals("")){
            //possibly throw an exception here
            throw new InvalidNameException(firstName);
        }
        else{
            this.firstName = firstName;
        }
    }

    public final String getLastName() {
        return this.lastName;
    }

    public final void setLastName(String lastName) throws InputException{
        if(lastName == null || lastName.equals("")){
            //possibly throw an exception here
            throw new InvalidNameException(lastName);
        }
        else{
            this.lastName = lastName;
        }
    }


    public final Password getPassword() {
        return password;
    }

    public final void setPassword(String password) throws InputException{
        this.password.setEncryptedPassword(password);
    }

}
