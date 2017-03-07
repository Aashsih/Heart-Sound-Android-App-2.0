package com.head_first.aashi.heartsounds_20.utils;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by Aashish Indorewala on 21-Feb-17.
 */

public abstract class AudioRecorder implements AudioRecording {



    //Note: the output file format if changed, the extension of the file also needs to be chnaged
    protected static final String OUTPUT_FILE_PATH = Environment.getExternalStorageDirectory().toString()+ "/recording.3gpp";
    protected static final int OUTPUT_FILE_FORMAT = MediaRecorder.OutputFormat.THREE_GPP;

    //Media Playback tools
    protected MediaPlayer mediaPlayer;

    //Audio recording States
    //private boolean playing;
    private boolean paused;
    private boolean recording;
    private boolean stopRecording;
    private boolean stopPlaying;
    private boolean replaying;


    protected void setupMediaPlayer(boolean resetRequired)throws IOException{
        if(mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
        }
        if(resetRequired){
            mediaPlayer.reset();
            mediaPlayer.setDataSource(OUTPUT_FILE_PATH);
        }
    }


    public void closeMediaPlayer() {
        if(mediaPlayer != null){
            mediaPlayer.release();
        }
    }

    public void deleteRecordedMediaFile(){
        File file = new File(OUTPUT_FILE_PATH);
        boolean deleted = file.delete();
        if(!deleted){
            //throw exception

            Log.v("FILE I/O", "Could not delete the file");
        }
    }

    private void disablePlayModeButtons(){
        this.paused  =false;
        this.stopPlaying = false;
    }

    private void disableRecordModeButtons(){
        this.recording = false;
        this.stopRecording = false;
    }

    public void resetAudioRecorder(){
        disableRecordModeButtons();
        disablePlayModeButtons();
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
    public void pausePlaying(){
        this.paused = true;
        this.replaying = false;
        this.stopPlaying = false;
        this.disableRecordModeButtons();
    }

    @Override
    public void stopPlaying(){
        this.stopPlaying = true;
        this.paused = false;
        this.replaying = false;
        this.disableRecordModeButtons();
    }
    //Getters and Setters

    //Getters and Setters
    public MediaPlayer getMediaPlayer(){

        return this.mediaPlayer;
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
