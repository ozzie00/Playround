/*
* Copyright 2015 OneME
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.oneme.toplay.base.third;

import android.os.Environment;
import android.util.Log;

import com.oneme.toplay.Application;
import com.oneme.toplay.base.AppConstant;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;



public class GetOutputMediaFile {

    private final String TAG = "GetOutputMediaFile";

    public static File newFile(int type){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "");

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date().getTime());
        File mediaFile;
        if (type == AppConstant.OMEPARSEAVATARFILETYPEPNG){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".png");
            if (Application.APPDEBUG) {
                Log.d("  ", mediaFile.toString() );
            }
        } else {
            return null;
        }

        return mediaFile;
    }
}
