package com.head_first.aashi.heartsounds_20.utils;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by Aashish Indorewala on 19-Feb-17.
 *
 * This class will contain all of code required for audio recording.
 *
 */

public class AudioRecorder implements AudioRecording{
    //Note: the output file format if changed, the extension of the file also needs to be chnaged
    private static final String OUTPUT_FILE_NAME = Environment.getExternalStorageDirectory().toString()+ "recording.3gpp";
    private static final int OUTUT_FILE_FORMAT = MediaRecorder.OutputFormat.THREE_GPP;

    private MediaPlayer mediaPlayer;
    private MediaRecorder mediaRecorder;

    private boolean playing;
    private boolean paused;
    private boolean recording;
    private boolean stopped;
    private boolean replaying;

    //Implemented methods
    @Override
    public void beginRecording() throws IOException {
        File outputFile = new File(OUTPUT_FILE_NAME);
        if(outputFile.exists()){
            outputFile.delete();
        }
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(OUTUT_FILE_FORMAT);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(OUTPUT_FILE_NAME);
        mediaRecorder.prepare();
        mediaRecorder.start();
    }

    @Override
    public void playRecording() throws IOException {
        closeMediaPlayer();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(OUTPUT_FILE_NAME);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    @Override
    public void pauseRecording() {


    }

    @Override
    public void finishRecording() {
        if(mediaRecorder != null){
            mediaRecorder.stop();
        }
    }

    @Override
    public void replayRecording() {

    }

    @Override
    public void stopReplay() {

    }

    @Override
    public void closeMediaRecorder() {
        if(mediaRecorder != null){
            mediaRecorder.release();
        }
    }

    @Override
    public void closeMediaPlayer() {
        if(mediaPlayer != null){
            mediaPlayer.release();
        }
    }

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
