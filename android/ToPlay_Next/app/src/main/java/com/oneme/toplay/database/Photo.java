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
package com.oneme.toplay.database;

import com.oneme.toplay.base.AppConstant;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

//
// define data model for venue
//
@ParseClassName(AppConstant.OMETOPLAYPHOTOCLASSKEY)
public final class Photo extends ParseObject {

    private static final String TAG = "Photo";

    public ParseFile getPhotoFile() {
        return getParseFile(AppConstant.OMETOPLAYPHOTOPICTUREKEY);
    }

    public void setPhotoFile(ParseFile file) {
        put(AppConstant.OMETOPLAYPHOTOPICTUREKEY, file);
    }

    public ParseFile getThumbFile() {
        return getParseFile(AppConstant.OMETOPLAYPHOTOTHUMBNAILKEY);
    }

    public void setThumbFile(ParseFile file) {
        put(AppConstant.OMETOPLAYPHOTOTHUMBNAILKEY, file);
    }

    public String getUploadedby() {
        return getString(AppConstant.OMETOPLAYPHOTOUPLOADEDBYKEY);
    }

    public void setUploadedby(String value) {
        put(AppConstant.OMETOPLAYPHOTOUPLOADEDBYKEY, value);
    }



    public static ParseQuery<Photo> getQuery() {
        return ParseQuery.getQuery(Photo.class);
    }

}