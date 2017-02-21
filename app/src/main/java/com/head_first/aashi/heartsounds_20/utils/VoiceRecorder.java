package com.head_first.aashi.heartsounds_20.utils;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
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

    //Implemented methods
    @Override
    public void beginRecording() throws IOException {
        File outputFile = new File(OUTPUT_FILE_NAME);
        if(outputFile.exists()){
            outputFile.delete();
        }
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(OUTPUT_FILE_FORMAT);
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


}
