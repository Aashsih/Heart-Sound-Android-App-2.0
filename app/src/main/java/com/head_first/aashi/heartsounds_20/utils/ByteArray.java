package com.head_first.aashi.heartsounds_20.utils;

import android.util.Log;

import java.io.ByteArrayInputStream;

/**
 * Created by Aashish Indorewala on 05-Mar-17.
 */

public class ByteArray {
    private static final int INITIAL_SIZE = 1280;
    private static final int SIZE_ICREASE_FACTOR = 2;
    private int numberOfBytes;
    private byte[] byteArray;

    public ByteArray(){
        byteArray = new byte[INITIAL_SIZE];
        numberOfBytes = 0;
    }

    public ByteArray(int size){
        if(size > 0){
            byteArray = new byte[size];
        }
        else{
            byteArray = new byte[INITIAL_SIZE];
        }
        numberOfBytes = 0;
    }

    public boolean appendBytes(byte[] bytesToAppend, int offset, int length){
        while(!((byteArray.length - numberOfBytes) < (length - offset))){
            expandCapacity();
            Log.v("expanding capacity","Expanding Capacity" + byteArray.length);
        }
        for(int i = 0; i < length - offset; i++){
            byteArray[numberOfBytes + i] = bytesToAppend[offset + i];
            numberOfBytes++;
        }
        return true;
    }

    public byte[] getByteArray(){
        byte[] bytes = new byte[numberOfBytes];
        for (int i = 0; i < numberOfBytes; i++) {
            bytes[i] = byteArray[i];
        }
        return bytes;
    }

    public int getNumberOfBytes(){
        return numberOfBytes;
    }

    private void expandCapacity(){
        byte[] newByteArray = new byte[byteArray.length * SIZE_ICREASE_FACTOR];
        for(int i = 0; i < byteArray.length; i++){
            newByteArray[i] = byteArray[i];
        }
        byteArray = newByteArray;
    }


}
