package com.head_first.aashi.heartsounds_20.model;

import java.util.Date;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

/**
 * Once a HeartSound is created only the creator of the HeartSound can edit it.
 * The only thing that can be edited for a HeartSound post creation is the VoiceComment Data (this will be overridden)
 * The HeartSound Data itself should not overridden because there might be multiple MurmerRatings assosciated with it
 * and will become invalid.
 * [Make the sure that the VoiceComment and the HeartSound Data are within the defined limits]
 */
public final class HeartSound {
    //time specified in milliseconds
    public static final int VOICE_COMMENT_LENGHT = 10000;
    public static final int HEART_SOUND_LENGHT = 10000;

    private long HeartSoundID;
    private long PatientID;
    private long doctorId; //Could have
    private String DeviceID;
    private String HeartSoundData;
    private String VoiceCommentData;
    private Integer QualityOfRecording;
    private Date CreatedOn;
    private boolean IsActive;

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

    //Getters

    public long getHeartSoundID() {
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

    public byte[] getHeartSoundData() {
        return HeartSoundData.getBytes();
    }

    public byte[] getVoiceCommentData() {
        return VoiceCommentData.getBytes();
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

    @Override
    public String toString(){
        return "Heart Sound " + (getHeartSoundID() + 1);
    }

    /**
     *This constructor should be used when gettin data from the database
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
