/*
* Copyright 2014 OneME
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

package com.oneme.toplay.database;

import android.graphics.Bitmap;

public class PlayerIcon {
    Bitmap mimage;
    String mtitle;
    String momeid;

    public PlayerIcon(Bitmap image, String title, String omeid) {
        super();
        this.mimage = image;
        this.mtitle = title;
        this.momeid = omeid;
    }
    public Bitmap getImage() {

        return mimage;
    }
    public void setImage(Bitmap image) {

        this.mimage = image;
    }
    public String getTitle() {

        return mtitle;
    }
    public void setTitle(String title) {

        this.mtitle = title;
    }

    public String getOMEID() {

        return mtitle;
    }
    public void setOMEID(String omeid) {

        this.momeid = omeid;
    }


}