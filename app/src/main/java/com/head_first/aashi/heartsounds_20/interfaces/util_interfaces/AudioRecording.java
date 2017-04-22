package com.head_first.aashi.heartsounds_20.interfaces.util_interfaces;

import android.speech.RecognitionListener;

import java.io.IOException;

/**
 * Created by Aashish Indorewala on 19-Feb-17.
 */

public interface AudioRecording{
    public void startRecording() throws IOException;
    //public void startPlaying();
    public void pausePlaying();
    public void stopPlaying();
    public void stopRecording();
    public void stopReplay();
    public void replayRecording() throws IOException;
}
