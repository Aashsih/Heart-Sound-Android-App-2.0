package com.head_first.aashi.heartsounds_20.utils;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.head_first.aashi.heartsounds_20.model.Doctor;
import com.head_first.aashi.heartsounds_20.model.HeartSound;
import com.head_first.aashi.heartsounds_20.model.MurmurRating;
import com.head_first.aashi.heartsounds_20.model.Patient;
import com.head_first.aashi.heartsounds_20.model.User;
import com.head_first.aashi.heartsounds_20.web_api.WebAPI;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.head_first.aashi.heartsounds_20.web_api.WebAPI.DATE_FORMATS;

/**
 * Created by Aashish Indorewala on 03-Apr-17.
 */

public class JsonObjectParser {
    private static Gson gson;
    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
        // Adapter to convert byte[] in a Java Object to a String in Json
//        gsonBuilder.registerTypeAdapter(byte[].class, new JsonSerializer<byte[]>() {
//            public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
//                return new JsonPrimitive(Base64.encodeToString(src, Base64.NO_WRAP));
//            }
//        });
        gson = gsonBuilder.create();
    }

    //static methods

    //User parsing
    public static List<User> getUserListFromJsonString(String jsonString){
        if(jsonString == null || jsonString.isEmpty()){
            return null;
        }
        Type type = new TypeToken<List<Map<String, String>>>(){}.getType();
        Collection<Map<String, String>> objectList = gson.fromJson(jsonString, type);
        List<User> userList = new ArrayList<>();
        for(Map<String, String> anObject : objectList){
            userList.add(getUserFromUserStringMap(anObject));
        }
        return userList;
    }


    public static User getUserFromJsonString(String jsonString){
        if(jsonString == null || jsonString.isEmpty()){
            return null;
        }
        User user = null;
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> userMap = gson.fromJson(jsonString, type);
        return getUserFromUserStringMap(userMap);
    }

    private static User getUserFromUserStringMap(Map<String, String> userMap){
        User user = null;
        if(userMap == null){
            user = null;
        }
        else{
            String userId = userMap.get(WebAPI.DOCTOR_ID_LOWER_CASE);
            String userName = userMap.get(WebAPI.USER_NAME);
            String firstName = userMap.get(WebAPI.FIRST_NAME_LOWER_CASE);
            String lastName = userMap.get(WebAPI.LAST_NAME_LOWER_CASE);
            String email = userMap.get(WebAPI.EMAIL_LOWER_CASE);
            if(firstName == null || lastName == null || userName == null){
                user = null;
            }
            else{
                user = new Doctor(userId, userName, firstName, lastName, email);
            }
        }

        return user;
    }

    //Patient parsing
    public static Collection<Patient> getPatientListFromJsonString(String jsonString){
        if(jsonString == null || jsonString.isEmpty()){
            return null;
        }
        Type type = new TypeToken<List<Patient>>(){}.getType();
        Collection<Patient> patientList = gson.fromJson(jsonString, type);
        return patientList;
    }

    public static Patient getPatientFromJsonString(String jsonString){
        if(jsonString == null || jsonString.isEmpty()){
            return null;
        }
        Type type = new TypeToken<Patient>(){}.getType();
        Patient patient = gson.fromJson(jsonString, type);
        return patient;
    }

    public static String getJsonStringFromPatient(Patient patient){
        if(patient == null){
            return null;
        }
        Map<String, String> patientMap = new HashMap<>();
        patientMap.put(WebAPI.PATIENT_ID_LOWER_CASE, patient.getPatientId() + "");
        patientMap.put(WebAPI.FIRST_NAME_LOWER_CASE, patient.getFirstName());
        patientMap.put(WebAPI.LAST_NAME_LOWER_CASE, patient.getLastName());
        patientMap.put(WebAPI.DATE_OF_BIRTH, (new SimpleDateFormat(WebAPI.DATE_FORMATS[1])).format(patient.getDateOfBirth()));
        patientMap.put(WebAPI.GENDER, patient.getGender());
        patientMap.put(WebAPI.PATIENT_PRIMARY_DOCTOR_ID, patient.getPrimaryDoctorId());
        patientMap.put(WebAPI.IS_PUBLIC, patient.isPublic() + "");
        patientMap.put(WebAPI.CREATED_ON_LOWER_CASE, (new SimpleDateFormat(WebAPI.DATE_FORMATS[1])).format(patient.getCreatedOn()));
        patientMap.put(WebAPI.IS_ACTIVE, patient.isActive() + "");
        return gson.toJson(patientMap);
    }

    //Heart Sound parsing
    public static HeartSound getHeartSoundFromJsonString(String jsonString){
        if(jsonString == null || jsonString.isEmpty()){
            return null;
        }
        Type type = new TypeToken<HeartSound>(){}.getType();
        return gson.fromJson(jsonString, type);
    }

    public static Collection<HeartSound> getHeartSoundListFromJsonString(String jsonString){
        if(jsonString == null || jsonString.isEmpty()){
            return null;
        }
        Type type = new TypeToken<List<HeartSound>>(){}.getType();
        return gson.fromJson(jsonString, type);
    }

    public static String getJsonFromHeartSound(HeartSound heartSound){
        if(heartSound == null){
            return null;
        }
        return gson.toJson(heartSound);
    }

    //Murmur Rating parsing
    public static MurmurRating getMurmurRatingFromJsonString(String jsonString){
        if(jsonString == null || jsonString.isEmpty()){
            return null;
        }
        Type type = new TypeToken<MurmurRating>(){}.getType();
        return gson.fromJson(jsonString, type);
    }

    public static Collection<MurmurRating> getMurmurRatingListFromJsonString(String jsonString){
        if(jsonString == null || jsonString.isEmpty()){
            return null;
        }
        Type type = new TypeToken<List<MurmurRating>>(){}.getType();
        return gson.fromJson(jsonString, type);
    }
    //Date Serializer
    private static class DateDeserializer implements JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonElement jsonElement, Type typeOF,
                                JsonDeserializationContext context) throws JsonParseException {
            for (String format : DATE_FORMATS) {
                try {
                    return new SimpleDateFormat(format, Locale.US).parse(jsonElement.getAsString());
                } catch (ParseException e) {
                }
            }
            throw new JsonParseException("Unparseable date: \"" + jsonElement.getAsString()
                    + "\". Supported formats: " + Arrays.toString(DATE_FORMATS));
        }
    }
}
