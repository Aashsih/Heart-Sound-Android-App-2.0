package com.head_first.aashi.heartsounds_20.web_api;

import android.util.Base64;

import com.head_first.aashi.heartsounds_20.model.HeartSound;
import com.head_first.aashi.heartsounds_20.model.MurmurRating;
import com.head_first.aashi.heartsounds_20.model.Patient;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * This class stores all the constants required to
 * communicate with the HeartSound Database and also
 * provides methods to prepare the required JSON object
 * for the header and body of the request.
 */

public class WebAPI {
    //Web API Date format
    public static final String[] DATE_FORMATS = new String[]{
            "yyyy-MM-dd'T'hh:mm:ss",
            "yyyy-MM-dd'T'hh:mm:ss.SSS"};

    //Web API URL
    private static final String HEART_SOUND_BASE_API_URL = "https://skyhawk.aut.ac.nz/HeartSounds";

    //User Token
    public static final String GET_USER_TOKEN_URL = HEART_SOUND_BASE_API_URL + "/Token";

    //Doctor API
    public static final String USER_BASE_URL = HEART_SOUND_BASE_API_URL + "/api/doctor/";
    public static final String GET_USER_INFO_URL = USER_BASE_URL + "/get";
    public static final String CHANGE_USER_PASSWORD_URL = USER_BASE_URL + "/ChangePassword";
    public static final String REGISTER_USER_URL = USER_BASE_URL + "/Register";
    public static final String GET_ALL_USERS_URL = USER_BASE_URL + "/all";

    //Murmur Rating API
    public static final String MURMUR_RATING_BASE_URL = HEART_SOUND_BASE_API_URL + "/api/MurmurRating/";
    public static final String GET_MURMUR_RATING_FOR_HEART_SOUND_URL = MURMUR_RATING_BASE_URL + "/HeartSound/";

    //Patient API
    public static final String PATIENT_BASE_URL = HEART_SOUND_BASE_API_URL + "/api/Patient/";
    public static final String ADD_DOCTOR_TO_PATIENT_URL = PATIENT_BASE_URL + "/AddDoctor";
    public static final String REMOVE_DOCTOR_FROM_PATIENT_URL = PATIENT_BASE_URL + "/RemoveDoctor";

    //Heart Sound API
    public static final String HEART_SOUND_BASE_URL = HEART_SOUND_BASE_API_URL + "/api/HeartSound/";
    public static final String GET_PATIENT_HEART_SOUNDS_URL = HEART_SOUND_BASE_URL + "/Patient/";

    // Headers
    public static final String AUTHORIZATION_KEY = "Authorization";
    public static final String AUTHORIZATION_VALUE_BEARER_TOKEN = "bearer ";
    public static final String CONTENT_TYPE_KEY = "Content-Type";
    public static final String CONTENT_TYPE_URLENCODED_VALUE = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_JSON_VALUE = "application/json";

    //User Details
    public static final String ACCESS_TOKEN = "access_token";
    public static final String FIRST_NAME = "FirstName";
    public static final String LAST_NAME = "LastName";
    public static final String FIRST_NAME_LOWER_CASE = "firstName";
    public static final String LAST_NAME_LOWER_CASE = "lastName";
    public static final String USER_NAME = "userName";
    public static final String EMAIL = "Email";
    public static final String EMAIL_LOWER_CASE = "email";

    //Password
    public static final String PASSWORD = "Password";
    public static final String PASSWORD_LOWER_CASE = "password";
    public static final String OLD_PASSWORD = "OldPassword";
    public static final String NEW_PASSWORD = "NewPassword";
    public static final String CONFIRM_PASSWORD = "ConfirmPassword";

    //Doctor
    public static final String DOCTOR_ID = "DoctorID";
    public static final String DOCTOR_ID_LOWER_CASE = "doctorId";

    //Patient
    public static final String PATIENT_ID = "PatientID";
    public static final String PATIENT_ID_LOWER_CASE = "patientId";
    public static final String PATIENT_PRIMARY_DOCTOR_ID = "primaryDoctorId";
    public static final String PATIENT_FIRST_NAME = "firstName";
    public static final String PATIENT_LAST_NAME = "lastName";
    public static final String DATE_OF_BIRTH = "dateOfBirth";
    public static final String GENDER = "gender";

    //HeartSound
    public static final String HEARTSOUND_ID = "HeartSoundID";
    public static final String DEVICE_ID = "deviceId";
    public static final String HEART_SOUND_DATA = "HeartSoundData";
    public static final String VOICE_COMMENT_DATA = "VoiceCommentData";
    public static final String QUALITY_OF_RECORDING = "QualityOfRecording";


    //MurmurRating
    public static final String MURMUR_RATING_ID = "MurmurRatingID";
    public static final String CARDIAC_PHASE = "CardiacPhase";
    public static final String DURATION_OF_MURMUR = "DurationOfMurmur";
    public static final String LOCATION_MOST_INTENSE = "LocationMostIntense";
    public static final String RADIATION = "Radiation";
    public static final String CHARACTER = "Character";
    public static final String ADDED_SOUNDS = "AddedSounds";
    public static final String S1 = "S1";
    public static final String S2 = "S2";
    public static final String CHANGE_WITH_BREATHING = "ChangeWithBreathing";
    public static final String VALSALVA = "Valsalva";
    public static final String LEFT_LATERAL_POSITION = "LeftLateralPosition";
    public static final String SITTING_FORWARD = "SittingForward";
    public static final String RATING_INFO = "RATING_INFO";

    //Other
    public static final String GRANT_TYPE = "grant_type";
    public static final String CREATED_ON = "CreatedOn";
    public static final String CREATED_ON_LOWER_CASE = "createdOn";
    public static final String IS_ACTIVE = "IsActive";
    public static final String IS_ACTIVE_LOWER_CASE = "isActive";
    public static final String IS_PUBLIC = "isPublic";

    //Headers

    /**
     * This method is used to retrieve key value pair
     * of the header for the access token
     *
     * @return HashMap of the access token Header
     */
    public static final HashMap<String, String> prepareAccessTokenRequestHeader(){
        HashMap<String, String> header = new HashMap<>();
        header.put(CONTENT_TYPE_KEY, CONTENT_TYPE_URLENCODED_VALUE);
        return header;
    }

    /**
     * This method is used to retrieve key value pair
     * of the header for the access token provided as
     * a parameter
     *
     * @param accessToken access token required for the header
     * @return HashMap of the access token Header
     */
    public static final HashMap<String, String> prepareAccessTokenHeader(String accessToken){
        HashMap<String, String> header = new HashMap<>();
        header.put(AUTHORIZATION_KEY, AUTHORIZATION_VALUE_BEARER_TOKEN + accessToken);
        return header;
    }

    /**
     * This method is used to retrieve key value pair
     * of the header for the a JSON request
     *
     * @param accessToken access token required to make the request
     * @return HashMap of the header required to make the request
     */
    public static final HashMap<String, String> prepareJsonRequestHeader(String accessToken){
        //Get access token
        HashMap<String, String> header = prepareAccessTokenHeader(accessToken);
        return header;
    }

    //Request Body
    /**
     * This method is used to retrieve a HashMap of the key
     * value pairs required for making a request to
     * retrieve a user's access token
     * @param username username of the user
     * @param password password of the user
     * @return HashMap of the request body to retrieve
     *         user's access token
     */
    public static final HashMap<String, String> addAccessTokenRequestParams(String username, String password){
        HashMap<String, String> params = new HashMap<>();
        params.put(GRANT_TYPE, PASSWORD_LOWER_CASE);
        params.put(USER_NAME, username);
        params.put(PASSWORD_LOWER_CASE, password);
        return params;
    }

    /**
     * This method is used to retrieve a JSONObject of the key
     * value pairs required for making a request to
     * change the password of the user
     *
     * @param oldPassword of the user
     * @param newPassword of the user
     * @param confirmNewPassword of the user
     * @return JSONObject representing a request to change user's password
     * @throws JSONException
     */
    public static final JSONObject addChangePasswordParams(String oldPassword, String newPassword, String confirmNewPassword) throws JSONException {
        JSONObject params = new JSONObject();
        params.put(OLD_PASSWORD, oldPassword);
        params.put(NEW_PASSWORD, newPassword);
        params.put(CONFIRM_PASSWORD, confirmNewPassword);
        return params;
    }

    /**
     * This method is used to retrieve a JSONObject of the key
     * value pairs required for making a request to
     * share or unshare a patient with another doctor
     *
     * @param patientId of the patient to be shared/unshared
     * @param doctorId of the doctor with which the patient
     *                 is to be shared/unshared
     * @return JSONObject representing a request body for sharing
     *          unsharing a patient with another doctor
     * @throws JSONException
     */
    public static final JSONObject addShareUnsharePatientParams(int patientId, String doctorId) throws JSONException {
        JSONObject params = new JSONObject();
        params.put(PATIENT_ID_LOWER_CASE, patientId);
        params.put(DOCTOR_ID_LOWER_CASE, doctorId);
        return params;
    }

    /**
     * This method is used to retrieve a JSONObject of the key
     * value pairs required for making a request to
     * create a new Patient
     *
     * @param patient contains information about the patient to be
     *                created
     * @return JSONObject representing the request body for creating
     *          a new Patient
     * @throws JSONException
     */
    public static final JSONObject addCreatePatientParams(Patient patient) throws JSONException {
        JSONObject params = new JSONObject();
        params.put(FIRST_NAME_LOWER_CASE, patient.getFirstName());
        params.put(LAST_NAME_LOWER_CASE, patient.getLastName());
        params.put(DATE_OF_BIRTH, patient.getDateOfBirthInLocalDateTimeFormat());
        params.put(GENDER, patient.getGender());
        params.put(PATIENT_PRIMARY_DOCTOR_ID, patient.getPrimaryDoctorId());
        params.put(IS_PUBLIC, patient.isPublic());
        return params;
    }

    /**
     * This method is used to retrieve a JSONObject of the key
     * value pairs required for making a request to
     * update a Patient's information
     *
     * @param patient contains the information that needs to be
     *                updated for the Patient
     * @return JSONObject representing the request body for making
     *          an update to the Patient's information
     * @throws JSONException
     */
    public static final JSONObject addUpdatePatientParams(Patient patient) throws JSONException {
        JSONObject params = addCreatePatientParams(patient);
        params.put(PATIENT_ID_LOWER_CASE, patient.getPatientId());
        params.put(IS_ACTIVE_LOWER_CASE, true);//This should not be editable in this request
        return params;
    }

    /**
     * This method is used to retrieve a JSONObject of the key
     * value pairs required for making a request to
     * create a new HeartSound
     *
     * @param heartSound contains the information for the new HeartSound
     * @param patientId of the Patient for which the HeartSound is to be
     *                  created
     * @return JSONObjcet representing the request body for creating a
     *          new HeartSound
     * @throws JSONException
     */
    public static final JSONObject addCreateHeartSoundParams(HeartSound heartSound, int patientId) throws JSONException {
        JSONObject params = addCommonHeartSoundParams(heartSound);
        params.put(PATIENT_ID_LOWER_CASE, patientId);
        return params;
    }

    /**
     * This method is used to retrieve a JSONObject of the key
     * value pairs required for making a request to
     * update a HeartSound's QualityOfRecording
     *
     * @param heartSound contains the information that the HeartSound
     *                   needs to be updated with
     * @return JSONObject representing the request body for updating
     *          the HeartSound
     * @throws JSONException
     */
    public static final JSONObject addUpdateQualityOfRecordingParams(HeartSound heartSound) throws JSONException {
        JSONObject params = new JSONObject();
        params.put(HEARTSOUND_ID, heartSound.getHeartSoundID().intValue());
        params.put(HEART_SOUND_DATA, heartSound.getHeartSoundData());
        params.put(VOICE_COMMENT_DATA, heartSound.getVoiceCommentData());
        params.put(DEVICE_ID, heartSound.getDeviceID());
        params.put(QUALITY_OF_RECORDING, heartSound.getQualityOfRecording());
        params.put(IS_ACTIVE, heartSound.isActive());
        return params;
    }

    /**
     * This method is used to retrieve a JSONObject of the key
     * value pairs required for making a request to
     * update a HeartSound's HeartSound
     *
     * @param heartSound contains the information that the HeartSound
     *                   needs to be updated with
     * @return JSONObject representing the request body for updating
     *          the HeartSound
     * @throws JSONException
     */
    public static final JSONObject addUpdateHeartSoundParams(HeartSound heartSound) throws JSONException {
        JSONObject params = new JSONObject();
        params.put(HEARTSOUND_ID, heartSound.getHeartSoundID().intValue());
        params.put(HEART_SOUND_DATA, heartSound.getHeartSoundData());
        params.put(VOICE_COMMENT_DATA, heartSound.getVoiceCommentData());
        params.put(DEVICE_ID, heartSound.getDeviceID());
        params.put(QUALITY_OF_RECORDING, heartSound.getQualityOfRecording());
        params.put(IS_ACTIVE, heartSound.isActive());
        return params;
    }

    /**
     * This method is used to retrieve a JSONObject of the key
     * value pairs required for making a request to
     * update a HeartSound's VoiceComment
     *
     * @param heartSound contains the information that the HeartSound
     *                   needs to be updated with
     * @return JSONObject representing the request body for updating
     *          the HeartSound
     * @throws JSONException
     */
    public static final JSONObject addUpdateVoiceCommentParams(HeartSound heartSound) throws JSONException {
        JSONObject params = new JSONObject();
        params.put(HEARTSOUND_ID, heartSound.getHeartSoundID().intValue());
        params.put(HEART_SOUND_DATA, heartSound.getHeartSoundData());
        params.put(VOICE_COMMENT_DATA, heartSound.getVoiceCommentData());
        params.put(DEVICE_ID, heartSound.getDeviceID());
        params.put(QUALITY_OF_RECORDING, heartSound.getQualityOfRecording());
        params.put(IS_ACTIVE, heartSound.isActive());
        return params;
    }

    /**
     * This method is used to retrieve a JSONObject of the key
     * value pairs required for making a request to
     * add the common HeartSounds params required for each
     * HeartSound request
     *
     * @param heartSound contains the information that the HeartSound
     *                   needs to be updated with
     * @return JSONObject representing the request body for some
     *          of the common parameters for each HeartSound request
     * @throws JSONException
     */
    private static final JSONObject addCommonHeartSoundParams(HeartSound heartSound) throws JSONException {
        JSONObject params = new JSONObject();
        params.put(HEART_SOUND_DATA, heartSound.getHeartSoundData());
        params.put(VOICE_COMMENT_DATA, heartSound.getVoiceCommentData());
        params.put(DEVICE_ID, heartSound.getDeviceID());
        params.put(QUALITY_OF_RECORDING, heartSound.getQualityOfRecording());
        return params;
    }

    /**
     * This method is used to retrieve a JSONObject of the key
     * value pairs required for making a request to
     * create a new MurmurRating
     *
     * @param murmurRating contains the information to create a new
     *                     MurmurRating
     * @return JSONObject representing the request body to create a
     *          new MurmurRating
     * @throws JSONException
     */
    public static final JSONObject addCreateMurmurRatingParams(MurmurRating murmurRating) throws JSONException {
        JSONObject params = new JSONObject();
        params.put(DOCTOR_ID, murmurRating.getDoctorID());
        params.put(HEARTSOUND_ID, murmurRating.getHeartSoundID());
        params.put(CARDIAC_PHASE, murmurRating.getCardiacPhase().toString());
        params.put(DURATION_OF_MURMUR, murmurRating.getDurationOfMurmur().toString());
        params.put(LOCATION_MOST_INTENSE, murmurRating.getLocationMostIntense().toString());
        params.put(RADIATION, murmurRating.getRadiation().toString());
        params.put(CHARACTER, murmurRating.getCharacter().toString());
        params.put(ADDED_SOUNDS, murmurRating.getAddedSounds().toString());
        params.put(S1, murmurRating.getS1().toString());
        params.put(S2, murmurRating.getS2().toString());
        params.put(CHANGE_WITH_BREATHING, murmurRating.getChangeWithBreathing().toString());
        params.put(VALSALVA, murmurRating.getValsalva().toString());
        params.put(LEFT_LATERAL_POSITION, murmurRating.getLeftLateralPosition().toString());
        params.put(SITTING_FORWARD, murmurRating.getSittingForward().toString());
        params.put(RATING_INFO, murmurRating.getRatingInfo());
        return params;
    }

    /**
     * This method is used to retrieve a JSONObject of the key
     * value pairs required for making a request to
     * update an existing MurmurRating
     *
     * @param murmurRating contains the information that the MurmurRating
     *                     needs to be updated with
     * @return JSONObject representing the request body for updating an
     *          existing MurmuRating
     * @throws JSONException
     */
    public static final JSONObject addUpdateMurmurRatingParams(MurmurRating murmurRating) throws JSONException {
        JSONObject params = addCreateMurmurRatingParams(murmurRating);
        params.put(MURMUR_RATING_ID, murmurRating.getMurmurRatingID());
        params.put(CREATED_ON, murmurRating.getCreatedOn().toString());
        params.put(IS_ACTIVE, new String("" + murmurRating.isActive()));
        return params;
    }
}
