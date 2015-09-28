package com.oneme.toplay.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
//import android.util.Log;

import com.oneme.toplay.Application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataFile {

	private String fileName;
    private Context mcontext;

	public DataFile(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        mcontext = context;
        fileName = preferences.getString("active_account","");
	}

    public DataFile(Context context, String fileName) {
        mcontext      = context;
        this.fileName = fileName;
    }

	/**
	 * Method to check if the data file exists before attempting to use it
	 * @return
	 */
	public boolean doesFileExist() {
        if (mcontext==null) {
            if (Application.APPDEBUG) {
                //Log.d("DataFile", "Context is null!");
            }
        }
        if (Application.APPDEBUG) {
            //Log.d("DataFile", "fileName: " + fileName);
        }
		File myFile = mcontext.getFileStreamPath(fileName);
        if (myFile == null) {
            if (Application.APPDEBUG) {
               // Log.d("DataFile", "myFile is null!");
            }
        }
		return myFile.exists();
	}

	/**
	 * Method for deleting the tox data file
	 */
	public void deleteFile() {
        mcontext.deleteFile(fileName);
	}

    /**
     * Checks if external storage is available to read
     * @return
     * THIS NEEDS TO BE DONE PROPERLY
     */


	/**
	 * Method for loading data from a saved file and return it. Requires the
	 * context of the activity or service calling it.
	 *
	 * @return
	 */
	public byte[] loadFile() {
		FileInputStream fin = null;
		final File file     = mcontext.getFileStreamPath(fileName);
		byte[] data         = null;
		try {
			fin  = new FileInputStream(file);
			data = new byte[(int) file.length()];
			fin.read(data);

		} catch (FileNotFoundException e) {
            if (Application.APPDEBUG) {
                e.printStackTrace();
            }

		} catch (IOException e) {
            if (Application.APPDEBUG) {
                e.printStackTrace();
            }
		} finally {
			try {
				if (fin != null) {
					fin.close();
				}
			} catch (IOException ioe) {
				System.out.println("Error while closing stream: " + ioe);
			}
		}
		return data;
	}

	/**
	 * Method for saving tox data. Requires the data to be saved and the context
	 * of the activity or service calling it
	 * 
	 * @param dataToBeSaved
	 */
	public void saveFile(byte[] dataToBeSaved) {
		File myFile = mcontext.getFileStreamPath(fileName);
		try {
			myFile.createNewFile();
		} catch (IOException e1) {
            if (Application.APPDEBUG) {
                e1.printStackTrace();
            }
		}
		try {
			FileOutputStream output = new FileOutputStream(myFile);
			output.write(dataToBeSaved, 0, dataToBeSaved.length);
			output.close();

		} catch (IOException e) {
            if (Application.APPDEBUG) {
                e.printStackTrace();
            }
		}
	}
}
