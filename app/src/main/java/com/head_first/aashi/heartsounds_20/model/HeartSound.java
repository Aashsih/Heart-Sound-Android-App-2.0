package com.head_first.aashi.heartsounds_20.model;

import com.head_first.aashi.heartsounds_20.enums.Gender;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

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
    private long id;
    private long patientId;
    private long doctorId;
    private long deviceId;
    private byte[] heartSoundData;
    private byte[] voiceCommentData;
    private Integer qualityOfRecording;


    /**
     *This constructor should be used when gettin data from the database
     */
    public HeartSound(long patientId, byte[] heartSoundData, byte[] voiceCommentData, Integer qualityOfRecording) {
        //The id will be received from the data base
        //this.id = (ID.add(BigInteger.ONE)).toString();
        this.patientId = patientId;
        this.setHeartSoundData(heartSoundData);
        this.setVoiceCommentData(voiceCommentData);
        this.setQualityOfRecording(qualityOfRecording);
    }

    /**
     * This constructor should only be called when the heart sound is created for the first time
     */
    public HeartSound(Patient patient, byte[] heartSoundData, byte[] voiceCommentData, Integer qualityOfRecording) {
        //deviceId can also be checked for but is skipped for now

        //How to validate if the correct patient object (to whom the heartsound belongs) is passed?
        this(patient.getId(), heartSoundData, voiceCommentData, qualityOfRecording);
    }

    //Getters and Setters
    public final long getId() {
        return id;
    }

    public final long getPatientId() {
        return patientId;
    }

    public final byte[] getHeartSoundData() {
        return heartSoundData;
    }

    public final void setHeartSoundData(byte[] heartSoundData) {
        //Check for some exceptions
        this.heartSoundData = heartSoundData;
    }

    public final byte[] getVoiceCommentData() {
        return voiceCommentData;
    }

    public final void setVoiceCommentData(byte[] voiceCommentData) {
        //is this field being non-empty neccessary?
        this.voiceCommentData = voiceCommentData;
    }

    public final Integer getQualityOfRecording() {
        return qualityOfRecording;
    }
    //not sure
    public final void setQualityOfRecording(Integer qualityOfRecording) {
        if(qualityOfRecording == null){
            this.qualityOfRecording = 0;
        }
        else{
            this.qualityOfRecording = qualityOfRecording;
        }
    }
}
