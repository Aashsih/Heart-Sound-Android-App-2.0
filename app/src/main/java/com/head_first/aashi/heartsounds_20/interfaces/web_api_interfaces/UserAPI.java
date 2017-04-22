package com.head_first.aashi.heartsounds_20.interfaces.web_api_interfaces;

import com.head_first.aashi.heartsounds_20.model.Doctor;

/**
 * Created by Aashish Indorewala on 18-Mar-17.
 */

public interface UserAPI {
    public void requestUserDetails();
    public void changeUserPassword(String oldPassword, String newPassword);
    public void registerUser(Doctor doctor);

}
