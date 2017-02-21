package com.head_first.aashi.heartsounds_20.utils;

import java.io.IOException;

/**
 * Created by Aashish Indorewala on 19-Feb-17.
 */

public interface AudioRecording {
    public void beginRecording() throws IOException;
    public void playRecording() throws IOException;
    public void pauseRecording();
    public void finishRecording();
    public void stopReplay();
    public void replayRecording();
    public void closeMediaRecorder();
    public void closeMediaPlayer();
}
