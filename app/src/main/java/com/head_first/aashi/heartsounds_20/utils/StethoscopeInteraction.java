package com.head_first.aashi.heartsounds_20.utils;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import com.head_first.aashi.heartsounds_20.controller.activities.PatientHeartSoundActivity;
import com.head_first.aashi.heartsounds_20.controller.fragment.StethoscopeInteractionFragment;
import com.head_first.aashi.heartsounds_20.model.HeartSound;
import com.mmm.healthcare.scope.AudioType;
import com.mmm.healthcare.scope.ConfigurationFactory;
import com.mmm.healthcare.scope.Errors;
import com.mmm.healthcare.scope.IBluetoothManager;
import com.mmm.healthcare.scope.IStethoscopeListener;
import com.mmm.healthcare.scope.Stethoscope;
import com.mmm.healthcare.scope.StethoscopeException;
import com.mmm.healthcare.scope.StethoscopeTrack;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
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
    private static final double WAITING_TIME_TO_UPLOAD_ONE_BYTE_TO_STETHOSCOPE = 0.04170373665;

    private Stethoscope stethoscope;
    private StethoscopeInteractionFragment stethoscopeInteractionFragment;
    private boolean stethoscopeInteractionInProgress;
    private List<byte[]> byteArrayList;
    ByteArrayOutputStream byteArrayOutputStream;
    private AudioType audioType;


    public StethoscopeInteraction(StethoscopeInteractionFragment fragment){
        this.stethoscopeInteractionFragment = fragment;
    }

    /**
     * This method connects to the first available stethoscope
     * @param context
     * @return
     */
    public boolean connectToAvailableStethoscope(Context context)throws IOException{
        boolean result = false;
        // Populate stethoscopeSelector with paired stethoscopes.
        ConfigurationFactory.setContext(context);
        IBluetoothManager bluetoothManager = ConfigurationFactory.getBluetoothManager();
        Vector<Stethoscope> scope = bluetoothManager.getPairedDevices();
        if (scope.size() > 0) {
            result = connectToStethoscope(scope.get(0));
            stethoscope.addStethoscopeListener(this);
        }
        return result;
    }

    public boolean connectToStethoscope(@NonNull Stethoscope stethoscope) throws IOException {
        this.stethoscope = stethoscope;
        this.stethoscope.connect();
        return this.stethoscope.isConnected();
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
     * Disconnects the application from a connected stethoscope
     */
    public void disconnectFromStethoscope(){
        if(isStethoscopeConnected()){
            stethoscope.disconnect();
        }
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
        this.audioType = audioType;
        if(stethoscope == null || !stethoscope.isConnected() || audioType == null){
            return false;
        }
        else{
            downloadStarted = downloadHeartSoundTrack(trackIndex);
        }
        return downloadStarted;
    }

    /**
     * This method is used to upload the specified track from the app to the stethoscope
     * @param trackIndex Index of the track that needs to be downloaded
     * @param audioType The audioType could be either Body (HeartSound) or Voice Comment
     * @return
     */
    public boolean uploadTrackToStethoscope(int trackIndex, AudioType audioType){
        boolean uploadStarted = false;
        this.audioType = audioType;
        if(stethoscope == null || !stethoscope.isConnected() || audioType == null){
            return false;
        }
        else{
            uploadStarted = uploadHeartSoundTrack(trackIndex);
        }
        return uploadStarted;
    }

    public boolean playDataFromDeviceOnStethoscope(@NonNull AudioType audioType){
        boolean uploadStarted = false;
        this.audioType = audioType;
        if(stethoscope == null || !stethoscope.isConnected()){
            return false;
        }
        else{
            uploadStarted = playDataOnStethoscope();
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

    public String getStethoscopeSerialNumber(){
        return (stethoscope.isConnected())? stethoscope.getSerialNumber(): null;
    }

    @Nullable
    private List<StethoscopeTrack> getAllAvailableStethoscopeTracks(){
        if(stethoscope == null || !stethoscope.isConnected()){
            return null;
        }
        return stethoscope.getStethoscopeTracks();
    }

    private boolean downloadHeartSoundTrack(int trackIndex){
        boolean downloadStarted = false;
        if(stethoscope == null || !stethoscope.isConnected()){
            return false;
        }
        else{
            try{
                //Getting the stethoscopeTrack and making sure it is not locked (This is an expensive check and might be chnaged)
                stethoscope.getStethoscopeTracks().get(trackIndex).setIsLocked(false);
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
            }catch(StethoscopeException ex){

            }
        }
        return downloadStarted;
    }

    private boolean uploadHeartSoundTrack(int trackIndex){
        boolean uploadStarted = false;
        if(stethoscope == null || !stethoscope.isConnected() || trackIndex < 0){
            return false;
        }
        else{
            //Getting the stethoscopeTrack and making sure it is not locked (This is an expensive check and might be chnaged)
            StethoscopeTrack stethoscopeTrack = stethoscope.getStethoscopeTracks().get(trackIndex);
            if(stethoscopeTrack.getIsLocked()){
                stethoscopeTrack.setIsLocked(false);
            }
            stethoscope.startUploadTrack(trackIndex, audioType);
            //if you want to play on stethoscope then use next line
            //stethoscope.startAudioOutput();
            startStethoscopeInteraction();
            new Thread(new TrackUploader(true)).start();
            uploadStarted = true;
        }
        return uploadStarted;
    }

    private boolean playDataOnStethoscope(){
        boolean uploadStarted = false;
        if(stethoscope == null || !stethoscope.isConnected()){
            return false;
        }
        else{
            //play on stethoscope
            stethoscope.startAudioOutput();
            startStethoscopeInteraction();
            new Thread(new TrackUploader(false)).start();
            uploadStarted = true;
        }
        return uploadStarted;
    }

    private void setHeartSound(){
        HeartSound heartSound = ((PatientHeartSoundActivity)stethoscopeInteractionFragment.getActivity()).getHeartSoundObject();
        if(audioType == AudioType.Body){
            StringBuffer stringBuffer = new StringBuffer();
            for(byte[] byteArray : byteArrayList){
                for(byte aByte : byteArray){
                    stringBuffer.append(aByte);
                }

            }
            //heartSound.setHeartSoundData(Base64.encodeToString(stringBuffer.toString().getBytes(), Base64.DEFAULT));
            heartSound.setHeartSoundData(Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
            heartSound.setDeviceID(stethoscope.getSerialNumber());
            heartSound.setHeartSoundChanged(true);
        }
        else{
            StringBuffer stringBuffer = new StringBuffer();
            for(byte[] byteArray : byteArrayList){
                for(byte aByte : byteArray){
                    stringBuffer.append(aByte);
                }

            }
            //heartSound.setVoiceCommentData(Base64.encodeToString(stringBuffer.toString().getBytes(), Base64.DEFAULT));
            heartSound.setVoiceCommentData(Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
            heartSound.setVoiceCommentChanged(true);
        }
        byteArrayList = null;

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
        setHeartSound();
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
        private InputStream stethoscopeInputStream;
        //private FileOutputStream fileOutputStream;

        @Override
        public void run() {
            int x = 0;
            int bytesRead = 1;//any value grater than 0 should be fine
            try {
                File outputFile = new File(OUTPUT_FILE_PATH);
                outputFile.createNewFile();
                //fileOutputStream = new FileOutputStream(outputFile);
                stethoscopeInputStream = stethoscope.getAudioInputStream();
                byteArrayOutputStream = new ByteArrayOutputStream();
                byteArrayList = new ArrayList<>();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            while(stethoscopeInteractionInProgress){
                byte[] buffer = new byte[MINIMUM_NUMBER_OF_BYTES_PER_TRANSACTION];
                try {
                    bytesRead = stethoscopeInputStream.read(buffer);
                    if(bytesRead <= 0){
                        Thread.sleep(100);
                    }
                    else{
                        //fileOutputStream.write(buffer);
                        byteArrayList.add(buffer);
                        byteArrayOutputStream.write(buffer);
                        x++;
                    }
                }
                catch (IOException exception) {
                    exception.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
//            try {
//                //fileOutputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            Log.v("Tag", "Number of bytes downloaded = " + x*128);
            //setHeartSound();
            stethoscope.stopDownloadTrack();
//            stopStethoscopeInteraction();

        }


    }

    //The File name used needs to be changed
    private class TrackUploader implements Runnable{
        private FileInputStream fileInputStream;
        private OutputStream stethoscopeOutputStream;
        private boolean uploadToStethoscope;


        public TrackUploader(boolean uploadToStethoscope){
            this.uploadToStethoscope = uploadToStethoscope;
        }
        @Override
        public void run() {
            int x = 0;
            //byte[] buffer = new byte[MINIMUM_NUMBER_OF_BYTES_PER_TRANSACTION];
            ByteArray byteArray = new ByteArray();
            byte[] buffer = null;
            if(audioType == AudioType.Body){
                buffer = Base64.decode(((PatientHeartSoundActivity)stethoscopeInteractionFragment.getActivity()).getHeartSoundObject().getHeartSoundData().getBytes(), Base64.DEFAULT);
            }
            else{
                buffer = Base64.decode(((PatientHeartSoundActivity)stethoscopeInteractionFragment.getActivity()).getHeartSoundObject().getVoiceCommentData().getBytes(), Base64.DEFAULT);
            }
            try {
//                fileInputStream = new FileInputStream(new File(OUTPUT_FILE_PATH));
//                while((fileInputStream.read(buffer)) > 0){
//
//                }
                stethoscopeOutputStream = stethoscope.getAudioOutputStream();
                //while(stethoscopeInteractionInProgress){// && (fileInputStream.read(buffer)) > 0){
                stethoscopeOutputStream.write(buffer);
                //stethoscopeOutputStream.close();
                    Thread.sleep((long)Math.ceil(WAITING_TIME_TO_UPLOAD_ONE_BYTE_TO_STETHOSCOPE * buffer.length));
                    x++;
                //}
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException exception) {
                exception.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.v("Tag", "Number of bytes uploaded = " + buffer.length);
            if(uploadToStethoscope){
                stethoscope.stopUploadAndDownloadTrack();
            }
            else{
                stopStethoscopeInteraction();
            }
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
