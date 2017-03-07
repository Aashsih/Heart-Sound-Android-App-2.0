package com.head_first.aashi.heartsounds_20.utils;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;

/**
 * Created by Aashish Indorewala on 19-Feb-17.
 *
 * This class will contain all of code required for audio recording.
 *
 */

public class VoiceRecorder extends AudioRecorder{



    private MediaRecorder mediaRecorder;
    private int pauseLocation;

    protected void setupMediaRecorder(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(OUTPUT_FILE_FORMAT);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(OUTPUT_FILE_PATH);
    }


    public void closeMediaRecorder() {
        if(mediaRecorder != null){
            mediaRecorder.release();
        }
    }

    //Implemented methods
    @Override
    public void startRecording() throws IOException {
        super.startRecording();
        File outputFile = new File(OUTPUT_FILE_PATH);
        if(outputFile.exists()){
            outputFile.delete();
        }
        setupMediaRecorder();
        mediaRecorder.prepare();
        mediaRecorder.start();
    }

    @Override
    public void pausePlaying() {
        super.pausePlaying();
        mediaPlayer.pause();
        pauseLocation = mediaPlayer.getCurrentPosition();
    }

    @Override
    public void stopRecording() {
        if(mediaRecorder != null && this.isRecording()){
            mediaRecorder.stop();
        }
        super.stopRecording();
    }

    @Override
    public void replayRecording() throws IOException{
        //closeMediaPlayer();
        if(this.isPaused()){
            setupMediaPlayer(false);
            mediaPlayer.seekTo(pauseLocation);
            mediaPlayer.start();
        }
        else{
            setupMediaPlayer(true);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        super.replayRecording();
    }

    @Override
    public void stopReplay() {
        if(mediaPlayer != null && (this.isReplaying() || this.isPaused())){
            mediaPlayer.stop();
        }
        super.stopPlaying();
    }

}
