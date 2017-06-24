package com.head_first.aashi.heartsounds_20.model;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**
 * This model represents one HeartSound that a Patient
 * could have.
 * A HeartSound is created for a Patient and one
 * Patient can have multiple HeartSounds.
 * Each HeartSound can have one or more MurmurRating
 * associated with it.
 */
public final class HeartSound {
    private Long HeartSoundID;
    private Long PatientID;
    private long doctorId;
    private String DeviceID;
    private String HeartSoundData;
    private String VoiceCommentData;
    private Integer QualityOfRecording;
    private Date CreatedOn;
    private boolean IsActive;
    private boolean heartSoundChanged;
    private boolean voiceCommentChanged;

    //static methods
    public static Long getIdFromString(String heartSoundString){
        if(heartSoundString == null){
            return null;
        }
        else if(heartSoundString.matches("Heart Sound [0-9]+")){
            return Long.parseLong(heartSoundString.split(" ")[2]) - 1;
        }
        return null;
    }

    public HeartSound(Long patientID){
        this.setPatientID(patientID);
    }

    //Getters

    public Long getHeartSoundID() {
        return HeartSoundID;
    }

    public long getPatientID() {
        return PatientID;
    }

    public long getDoctorId() {
        return doctorId;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public String getHeartSoundData(){
          return HeartSoundData;
    }

    public String getVoiceCommentData() {
        return VoiceCommentData;
    }

    public Integer getQualityOfRecording() {
        return QualityOfRecording;
    }

    public Date getCreatedOn() {
        return CreatedOn;
    }

    public boolean isActive() {
        return IsActive;
    }

    public boolean hasHeartSoundChanged(){
        return heartSoundChanged;
    }

    public boolean hasVoiceCommentChanged(){
        return voiceCommentChanged;
    }

    //Setters
    public void setHeartSoundID(Long heartSoundID) {
        HeartSoundID = heartSoundID;
    }

    public void setPatientID(long patientID) {
        PatientID = patientID;
    }

    public void setDoctorId(long doctorId) {
        this.doctorId = doctorId;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public void setHeartSoundData(String heartSoundData) {
        HeartSoundData = heartSoundData;
    }

    public void setVoiceCommentData(String voiceCommentData) {
        VoiceCommentData = voiceCommentData;
    }

    public void setQualityOfRecording(Integer qualityOfRecording) {
        QualityOfRecording = qualityOfRecording;
    }

    public void setCreatedOn(Date createdOn) {
        CreatedOn = createdOn;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public void setHeartSoundChanged(boolean heartSoundChanged){
        this.heartSoundChanged = heartSoundChanged;
    }

    public void setVoiceCommentChanged(boolean voiceCommentChanged){
        this.voiceCommentChanged = voiceCommentChanged;
    }

    @Override
    public String toString(){
        return "Heart Sound " + (getHeartSoundID() + 1);
    }


//The following code will be used in the future
    /**
     *This constructor should be used when getting data from the database
     */
//    public HeartSound(long PatientID, byte[] HeartSoundData, byte[] VoiceCommentData, Integer QualityOfRecording) {
//        //The HeartSoundID will be received from the data base
//        //this.HeartSoundID = (ID.add(BigInteger.ONE)).toString();
//        this.PatientID = PatientID;
//        this.setHeartSoundData(HeartSoundData);
//        this.setVoiceCommentData(VoiceCommentData);
//        this.setQualityOfRecording(QualityOfRecording);
//    }

    /**
     * This constructor should only be called when the heart sound is created for the first time
     */
//    public HeartSound(Patient patient, byte[] HeartSoundData, byte[] VoiceCommentData, Integer QualityOfRecording) {
//        //DeviceID can also be checked for but is skipped for now
//
//        //How to validate if the correct patient object (to whom the heartsound belongs) is passed?
//        this(patient.getPatientID(), HeartSoundData, VoiceCommentData, QualityOfRecording);
//    }

//    //Setters
//        public final void setHeartSoundData(byte[] HeartSoundData) {
//        //Check for some exceptions
//        this.HeartSoundData = HeartSoundData;
//    }
//
//    public final void setVoiceCommentData(byte[] VoiceCommentData) {
//        //is this field being non-empty neccessary?
//        this.VoiceCommentData = VoiceCommentData;
//    }
//
//    //not sure
//    public final void setQualityOfRecording(Integer QualityOfRecording) {
//        if(QualityOfRecording == null){
//            this.QualityOfRecording = 0;
//        }
//        else{
//            this.QualityOfRecording = QualityOfRecording;
//        }
//    }
}
