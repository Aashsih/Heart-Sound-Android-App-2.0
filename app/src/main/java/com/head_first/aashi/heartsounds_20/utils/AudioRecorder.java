package com.head_first.aashi.heartsounds_20.utils;

import java.io.IOException;

/**
 * Created by Aashish Indorewala on 21-Feb-17.
 */

public abstract class AudioRecorder implements AudioRecording {

    private boolean playing;
    private boolean paused;
    private boolean recording;
    private boolean stopRecording;
    private boolean stopPlaying;
    private boolean replaying;

    private void disablePlayModeButtons(){
        this.playing = false;
        this.paused  =false;
        this.stopPlaying = false;
    }

    private void disableRecordModeButtons(){
        this.recording = false;
        this.replaying = false;
        this.stopRecording = false;
    }

    @Override
    public void startRecording() throws IOException{
        this.recording = true;
        this.stopRecording = false;
        this.replaying = false;
        disablePlayModeButtons();
    }

    @Override
    public void stopRecording(){
        this.stopRecording = true;
        this.recording = false;
        this.replaying = false;
        disablePlayModeButtons();
    }

    @Override
    public void replayRecording() throws IOException{
        this.replaying = true;
        this.stopRecording = false;
        this.recording = false;
        disablePlayModeButtons();
    }

    @Override
    public void startPlaying(){
        this.playing = true;
        this.paused = false;
        this.stopPlaying = false;
        this.disableRecordModeButtons();
    }

    @Override
    public void pausePlaying(){
        this.paused = true;
        this.playing = false;
        this.stopPlaying = false;
        this.disableRecordModeButtons();
    }

    @Override
    public void stopPlaying(){
        this.stopPlaying = false;
        this.playing = false;
        this.paused = true;
        this.disableRecordModeButtons();
    }
    //Getters and Setters

    public boolean isPlaying() {
        return playing;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isRecording() {
        return recording;
    }

    public boolean isStopRecording() {
        return stopRecording;
    }

    public boolean isStopPlaying() {
        return stopPlaying;
    }

    public boolean isReplaying() {
        return replaying;
    }

}
