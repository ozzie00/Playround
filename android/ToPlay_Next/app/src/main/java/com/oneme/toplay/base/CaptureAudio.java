package com.oneme.toplay.base;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.util.Log;

import com.oneme.toplay.Application;

import com.oneme.toplay.service.Singleton;

public class CaptureAudio { // extends AsyncTask<String, Void, Void> {
    /*
    private int bufferSizeBytes;

    @Override
    protected Void doInBackground(String... params) {
        Singleton mSingleton    = Singleton.getInstance();
        AudioRecord audioRecord = findAudioRecord();
        audioRecord.startRecording();

        while(true) {
            if(isCancelled())
                break;

            try {
                byte[] buffer = new byte[bufferSizeBytes];
                int read = audioRecord.read(buffer, 0, bufferSizeBytes);
                mSingleton.jTox.avSendAudio(Integer.parseInt(params[0]), buffer);
                if (Application.APPDEBUG) {
                    Log.d("Mic", "Sending audio to:" + params[0]);
                }
            } catch (Exception e) {
            }
        }

        audioRecord.stop();
        audioRecord.release();

        return null;
    }

    private static int[] mSampleRates = new int[] { 8000, 11025, 22050, 44100 };

    private AudioRecord findAudioRecord() {
        for (int rate : mSampleRates) {
            for (short audioFormat : new short[] { AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT }) {
                for (short channelConfig : new short[] { AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_IN_STEREO }) {
                    try {
                        if (Application.APPDEBUG) {
                            Log.d("CaptureAudio", "Attempting rate " + rate + "Hz, bits: " + audioFormat + ", channel: "
                                    + channelConfig);
                        }
                        int bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig, audioFormat);

                        if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
                            // check if we can instantiate and have a success
                            AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, rate, channelConfig, audioFormat, bufferSize);

                            if (recorder.getState() == AudioRecord.STATE_INITIALIZED) {
                                bufferSizeBytes = bufferSize;
                                return recorder;
                            }

                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
        return null;
    }
    */
}
