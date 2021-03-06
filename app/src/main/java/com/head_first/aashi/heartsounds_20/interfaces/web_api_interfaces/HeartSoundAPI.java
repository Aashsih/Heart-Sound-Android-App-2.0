package com.head_first.aashi.heartsounds_20.interfaces.web_api_interfaces;

import com.head_first.aashi.heartsounds_20.model.HeartSound;

import org.json.JSONException;

/**
 * Created by Aashish Indorewala on 18-Mar-17.
 */

public interface HeartSoundAPI {
    public void requestPatientHeartSounds(int patientId);
    public void requestHeartSound(int heartSoundId);
    public void createHeartSound(HeartSound heartSound);
    public void updateHeartSound(HeartSound heartSound);
    public void updateQualityOfRecording(HeartSound heartSound) throws JSONException;
    public void deleteHeartSound(int heartSoundId);
}
