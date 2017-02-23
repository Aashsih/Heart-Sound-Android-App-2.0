package com.head_first.aashi.heartsounds_20.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by Aashish Indorewala on 19-Feb-17.
 *
 * This class will contain all of code required for audio recording.
 *
 */

public class VoiceRecorder extends AudioRecorder{
    //Note: the output file format if changed, the extension of the file also needs to be chnaged
    private static final String OUTPUT_FILE_NAME = Environment.getExternalStorageDirectory().toString()+ "/recording.3gpp";
    private static final int OUTPUT_FILE_FORMAT = MediaRecorder.OutputFormat.THREE_GPP;

    private MediaPlayer mediaPlayer;
    private MediaRecorder mediaRecorder;


    private void setupMediaRecorder(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(OUTPUT_FILE_FORMAT);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(OUTPUT_FILE_NAME);
    }

    private void setupMediaPlayer()throws IOException{
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(OUTPUT_FILE_NAME);
    }

    private void closeMediaRecorder() {
        if(mediaRecorder != null){
            mediaRecorder.release();
        }
    }

    private void closeMediaPlayer() {
        if(mediaPlayer != null){
            mediaPlayer.release();
        }
    }

    //Implemented methods
    @Override
    public void startRecording() throws IOException {
        super.startRecording();
        File outputFile = new File(OUTPUT_FILE_NAME);
        if(outputFile.exists()){
            outputFile.delete();
        }
        setupMediaRecorder();
        mediaRecorder.prepare();
        mediaRecorder.start();
    }

    @Override
    public void startPlaying() {
        super.startPlaying();
    }

    @Override
    public void pausePlaying() {
        super.pausePlaying();
    }

    @Override
    public void stopRecording() {
        super.stopRecording();
        if(mediaRecorder != null){
            mediaRecorder.stop();
        }
    }

    @Override
    public void replayRecording() throws IOException{
        super.replayRecording();
        closeMediaPlayer();
        setupMediaPlayer();
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    @Override
    public void stopReplay() {

    }


    //Getters and Setters
    public MediaPlayer getMediaPlayer(){
        return this.mediaPlayer;
    }

    public MediaRecorder getMediaRecorder(){
        return this.mediaRecorder;
    }

}
