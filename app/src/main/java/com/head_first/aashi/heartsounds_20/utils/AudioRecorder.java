package com.head_first.aashi.heartsounds_20.utils;

/**
 * Created by Aashish Indorewala on 21-Feb-17.
 */

public abstract class AudioRecorder implements AudioRecording {

    private boolean playing;
    private boolean paused;
    private boolean recording;
    private boolean stopped;
    private boolean replaying;

    //Getters and Setters

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isRecording() {
        return recording;
    }

    public void setRecording(boolean recording) {
        this.recording = recording;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public boolean isReplaying() {
        return replaying;
    }

    public void setReplaying(boolean replaying) {
        this.replaying = replaying;
    }
}
