package com.head_first.aashi.heartsounds_20.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.head_first.aashi.heartsounds_20.controller.fragment.StethoscopeInteractionFragment;
import com.head_first.aashi.heartsounds_20.model.HeartSound;
import com.mmm.healthcare.scope.AudioType;
import com.mmm.healthcare.scope.ConfigurationFactory;
import com.mmm.healthcare.scope.Errors;
import com.mmm.healthcare.scope.IBluetoothManager;
import com.mmm.healthcare.scope.IStethoscopeListener;
import com.mmm.healthcare.scope.Stethoscope;
import com.mmm.healthcare.scope.StethoscopeTrack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Aashish Indorewala on 05-Mar-17.
 * This class represents a 3M Littman Stethoscope, used to record Heart sounds and Voice Comments
 * made by doctors.
 */

public class StethoscopeInteraction implements IStethoscopeListener{
    private static final String OUTPUT_FILE_PATH = Environment.getExternalStorageDirectory().toString()+ "/stethoscopeRecording";;
    private static final int MINIMUM_NUMBER_OF_BYTES_PER_TRANSACTION = 128;

    private Stethoscope stethoscope;
    private StethoscopeInteractionFragment stethoscopeInteractionFragment;
    private ByteArray stethoscopeData;
    private boolean stethoscopeInteractionInProgress;

    public StethoscopeInteraction(StethoscopeInteractionFragment fragment){
        this.stethoscopeInteractionFragment = fragment;
    }

    /**
     * This method connects to the first available stethoscope
     * @param context
     * @return
     */
    public boolean connectToAvailableStethoscope(Context context){
        // Populate stethoscopeSelector with paired stethoscopes.
        ConfigurationFactory.setContext(context);
        IBluetoothManager bluetoothManager = ConfigurationFactory.getBluetoothManager();
        Vector<Stethoscope> scope = bluetoothManager.getPairedDevices();
        if (scope.size() > 0) {
            stethoscope = scope.get(0);
            try {
                stethoscope.connect();
                Toast.makeText(context, "Connected to " + stethoscope.getSerialNumber(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(context, "Could not connect to stethoscope", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return stethoscope.isConnected();
        }
        else{
            Toast.makeText(context, "No paired stethoscope found", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /**
     * Connect to a particular stethoscope
     * @param context
     * @param stethoscopeSerialNumber
     * @return true, if connection to the stethoscope was successful
     *         false, if unsuccessful
     */
    public boolean connectToStethoscope(Context context ,String stethoscopeSerialNumber){
        return false;
    }

    /**
     * If the Stethoscope is connected, then this method returns the number of
     * tracks that are available in the stethoscope
     * otherwise, it returns 0
     * @return Number of available tracks on the stethoscope
     */
    public int getNumberOfAvailableTracks(){
        if(stethoscope == null || !stethoscope.isConnected()){
            return 0;
        }
        else{
            return stethoscope.getStethoscopeTracks().size();
        }
    }

    /**
     * This method can be used to get the names of all available tracks on the stethoscope
     * @return List of Names of the tracks that are currently available in the stethoscope
     */
    public List<String> getAllAvailableTrackNames(){
        List<StethoscopeTrack> stethoscopeTracks = getAllAvailableStethoscopeTracks();
        List<String> availableTrackNames = null;
        if(stethoscopeTracks != null){
            availableTrackNames = new ArrayList<>();
            for(StethoscopeTrack stethoscopeTrack : stethoscopeTracks){
                availableTrackNames.add(stethoscopeTrack.getName());
            }
        }
        return availableTrackNames;
    }

    /**
     * This method is used to fetch the track id corresponding to the entered track name
     * @param trackName name of the track for which the track id is required
     * @return TrackId for the specified track if the track name is valid,
     *          otherwise, return null
     */
    public Integer getTrackId(String trackName){
        List<StethoscopeTrack> stethoscopeTracks = getAllAvailableStethoscopeTracks();
        if(stethoscopeTracks != null){
            for(StethoscopeTrack stethoscopeTrack : stethoscopeTracks){
                if(stethoscopeTrack.getName().toLowerCase().equals(trackName.toLowerCase())){
                    return stethoscopeTrack.getIndex();
                }
            }
        }
        return null;
    }

    /**
     * @return true, if stethoscope is connected
     *         false, otherwise.
     */
    public boolean isStethoscopeConnected(){
        return (stethoscope == null)? false : stethoscope.isConnected();
    }

    /**
     * This method is used to download the specified track from the stethoscope
     * and then upload it to the database
     * @param trackIndex Index of the track that needs to be downloaded
     * @param audioType The audioType could be either Body (HeartSound) or Voice Comment
     * @return true, if download was successful
     *         false, otherwise
     */
    public boolean downloadTrackFromStethoscope(int trackIndex, AudioType audioType){
        boolean downloadStarted = false;
        if(stethoscope == null || !stethoscope.isConnected() || audioType == null){
            return false;
        }
        else{
            downloadStarted = downloadHeartSoundTrack(trackIndex, audioType);
        }
        return downloadStarted;
    }

    /**
     * This method is used to upload the specified track from the app to the stethoscope
     * @param trackIndex Index of the track that needs to be downloaded
     * @param audioType The audioType could be either Body (HeartSound) or Voice Comment
     * @return
     */
    public boolean uploadTrackFromStethoscope(int trackIndex, AudioType audioType){
        boolean uploadStarted = false;
        if(stethoscope == null || !stethoscope.isConnected() || audioType == null){
            return false;
        }
        else{
            uploadStarted = uploadHeartSoundTrack(trackIndex, audioType);
        }
        return uploadStarted;
    }

    /**
     * This method is used to stop any current interaction between the app and the stethoscope
     */
    public void stopStethoscopeInteraction(){
        stethoscopeInteractionInProgress = false;
        stethoscopeInteractionFragment.finishStethoscopeInteraction();
    }

    /**
     * This method is used to stop any current interaction between the app and the stethoscope
     */
    public void startStethoscopeInteraction(){
        stethoscopeInteractionInProgress = true;
    }

    /**
     *
     * @return true, if app is interacting with the stethoscop
     *         false, otherwise
     */
    public boolean isStethoscopeInteractionInProgress(){
        return stethoscopeInteractionInProgress;
    }


    private List<StethoscopeTrack> getAllAvailableStethoscopeTracks(){
        if(stethoscope == null || !stethoscope.isConnected()){
            return null;
        }
        return stethoscope.getStethoscopeTracks();
    }

    private boolean downloadHeartSoundTrack(int trackIndex, AudioType audioType){
        boolean downloadStarted = false;
        if(stethoscope == null || !stethoscope.isConnected()){
            return false;
        }
        else{
            stethoscopeData = new ByteArray();
            stethoscope.startDownloadTrack(trackIndex, audioType);
            startStethoscopeInteraction();
            new Thread(new TrackDownloader()).start();
//            new java.util.Timer().schedule(
//                    new java.util.TimerTask() {
//                        @Override
//                        public void run() {
//                            stethoscopeInteraction = false;
//                        }
//                    },
//                    audioType == AudioType.Body? HeartSound.HEART_SOUND_LENGHT : HeartSound.VOICE_COMMENT_LENGHT
//            );
            downloadStarted = true;
        }
        return downloadStarted;
    }

    private boolean uploadHeartSoundTrack(int trackIndex, AudioType audioType){
        boolean uploadStarted = false;
        if(stethoscope == null || !stethoscope.isConnected()){
            return false;
        }
        else{
            stethoscopeData = new ByteArray();
            stethoscope.startUploadTrack(trackIndex, audioType);
            startStethoscopeInteraction();
            new Thread(new TrackUploader()).start();
            uploadStarted = true;
        }
        return uploadStarted;
    }

    @Override
    public void disconnected() {
        stethoscope.stopUploadAndDownloadTrack();
        stopStethoscopeInteraction();
    }

    @Override
    public void error(Errors errors, String s) {
        stethoscope.stopUploadAndDownloadTrack();
        stopStethoscopeInteraction();
    }

    @Override
    public void endOfOutputStream() {
        stethoscope.stopUploadAndDownloadTrack();
        stopStethoscopeInteraction();
    }

    @Override
    public void endOfInputStream() {
        stethoscope.stopUploadAndDownloadTrack();
        stopStethoscopeInteraction();
        //upload the data in stethoscopeData to the DB

        //For now store it in a file. Delete the following code later

    }

    @Override
    public void outOfRange(boolean b) {

    }

    @Override
    public void mButtonDown(boolean b) {

    }

    @Override
    public void mButtonUp() {

    }

    @Override
    public void plusButtonDown(boolean b) {

    }

    @Override
    public void plusButtonUp() {

    }

    @Override
    public void minusButtonDown(boolean b) {

    }

    @Override
    public void minusButtonUp() {

    }

    @Override
    public void filterButtonDown(boolean b) {

    }

    @Override
    public void filterButtonUp() {

    }

    @Override
    public void onAndOffButtonDown(boolean b) {

    }

    @Override
    public void onAndOffButtonUp() {

    }

    @Override
    public void lowBatteryLevel() {

    }

    @Override
    public void underrunOrOverrunError(boolean b) {

    }

    //The File name used needs to be changed
    private class TrackDownloader implements Runnable{

        @Override
        public void run() {
            int x = 0;
            FileOutputStream fileOutputStream = null;
            InputStream stethoscopeInputStream = null;
            int bytesRead = 1;//any value grater than 0 should be fine
            try {
                File outputFile = new File(OUTPUT_FILE_PATH);
                outputFile.createNewFile();
                fileOutputStream = new FileOutputStream(outputFile);
                stethoscopeInputStream = stethoscope.getAudioInputStream();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            while(stethoscopeInteractionInProgress){
                byte[] buffer = new byte[MINIMUM_NUMBER_OF_BYTES_PER_TRANSACTION];
                try {
                    bytesRead = stethoscopeInputStream.read(buffer, 0, buffer.length);
                    if(bytesRead <= 0){
                        Thread.sleep(100);
                    }
                    else{
                        fileOutputStream.write(buffer, 0, buffer.length);
                        x++;
                    }
                }
                catch (IOException exception) {
                    exception.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.v("Tag", "Number of bytes downloaded = " + x*128);
            stethoscope.stopDownloadTrack();
            stopStethoscopeInteraction();
        }
    }

    //The File name used needs to be changed
    private class TrackUploader implements Runnable{

        @Override
        public void run() {
            int x = 0;
            byte[] buffer = new byte[MINIMUM_NUMBER_OF_BYTES_PER_TRANSACTION];
            FileInputStream fileInputStream = null;
            OutputStream stethoscopeOutputStream = stethoscope.getAudioOutputStream();
            try {
                fileInputStream = new FileInputStream(new File(OUTPUT_FILE_PATH));
                //For now the stethoscopeData byteArray is used, but this might change if the bytes are being read from a file.
                while(stethoscopeInteractionInProgress &&
                        (fileInputStream.read(buffer, 0, buffer.length)) > 0){
                    try {
                        stethoscopeOutputStream.write(buffer, 0, buffer.length);
                        Log.v("uploading buffer","buffer = " + buffer.toString());
                        x++;
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException exception) {
                exception.printStackTrace();
            }
            Log.v("Tag", "Number of bytes uploaded = " + x*128);
            stethoscope.stopUploadAndDownloadTrack();
            stopStethoscopeInteraction();
        }

//        private int copyBytes(byte[] source, byte[] destination, int offset, int numberOfBytesToCopy){
//            if(destination.length < numberOfBytesToCopy || source.length < offset){
//                return 0;
//            }
//            else {
//                int numberOfBytesRead = 0;
//                for(; numberOfBytesRead < numberOfBytesToCopy && (offset + numberOfBytesRead) < source.length; numberOfBytesRead++){
//                    destination[numberOfBytesRead] = source[offset + numberOfBytesRead];
//                }
//                return numberOfBytesRead;
//            }
//        }
    }

}
