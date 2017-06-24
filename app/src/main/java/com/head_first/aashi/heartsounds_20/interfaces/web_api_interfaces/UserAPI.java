package com.head_first.aashi.heartsounds_20.interfaces.web_api_interfaces;

import com.head_first.aashi.heartsounds_20.model.Doctor;

import org.json.JSONException;

/**
 * Created by Aashish Indorewala on 18-Mar-17.
 */

public interface UserAPI {
    public void requestUserDetails();
    public void changeUserPassword(String oldPassword, String newPassword, String confirmNewPassword) throws JSONException;
    public void registerUser(Doctor doctor);

}
