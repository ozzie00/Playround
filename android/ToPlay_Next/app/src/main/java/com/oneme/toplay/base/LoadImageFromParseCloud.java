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

package com.oneme.toplay.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.oneme.toplay.base.third.ContactPhotoManager;
import com.oneme.toplay.base.third.ContactPhotoManager.DefaultImageRequest;
import com.oneme.toplay.base.third.RoundedTransformationBuilder;
import com.oneme.toplay.database.Invite;
import com.oneme.toplay.database.Photo;
import com.oneme.toplay.R;

import com.parse.ParseFile;
import com.parse.ParseUser;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public final class LoadImageFromParseCloud {

    private static int INVITE_WORKOUT_IMAGE_SIZE_WIDTH  = 48;
    private static int INVITE_WORKOUT_IMAGE_SIZE_HEIGHT = 48;

    public LoadImageFromParseCloud() {

    }

    // load user avatar in dedicate context to imageview
    public static void getAvatar(Context context, ParseUser user, ImageView imageview) {

        Transformation mtransformation = null;

        mtransformation = new RoundedTransformationBuilder()
                .borderColor(Color.WHITE)
                .borderWidthDp(1)
                .cornerRadiusDp(AppConstant.OMEPARSEUSERICONRADIUS)
                .oval(true)
                .build();


        if (user != null) {
            if (user.getParseFile(AppConstant.OMEPARSEUSERICONKEY) != null) {
                ParseFile mfile = user.getParseFile(AppConstant.OMEPARSEUSERICONKEY);
                Picasso.with(context)
                        .load(mfile.getUrl())
                        .fit()
                        .transform(mtransformation)
                        .into(imageview);
            } else {
                Drawable drawable = ContactPhotoManager.getDefaultAvatarDrawableForContact(context.getResources(),
                        false, new DefaultImageRequest(user.getUsername(), user.getUsername(), true));

                imageview.setImageDrawable(drawable);

                //Picasso.with(context)
                //        .load(R.drawable.ome_default_avatar)
                //        .fit()
                //        .transform(mtransformation)
                //        .into(imageview);
            }
        }
    }

    // load user avatar in dedicate context to imageview
    public static void getworkoutImage(Context context, Invite invite, ImageView imageview) {

        if (invite != null) {
            if (invite.getWorkoutImage() != null) {
                ParseFile mfile = invite.getWorkoutImage();
                Picasso.with(context)
                        .load(mfile.getUrl())
                        .resize(INVITE_WORKOUT_IMAGE_SIZE_WIDTH, INVITE_WORKOUT_IMAGE_SIZE_HEIGHT)
                        .centerCrop()
                        .into(imageview);
            } else {
                Picasso.with(context)
                        .load(R.drawable.ome_workoutimage_default)
                        .resize(INVITE_WORKOUT_IMAGE_SIZE_WIDTH, INVITE_WORKOUT_IMAGE_SIZE_HEIGHT)
                        .centerCrop()
                        .into(imageview);
            }
        }
    }

    // load user thumb in dedicate context to imageview
    public static void getPhotoThumb(Context context, Photo photo, ImageView imageview) {

        if (photo != null) {
            if (photo.getThumbFile() != null) {
                ParseFile mfile = photo.getThumbFile();
                Picasso.with(context)
                        .load(mfile.getUrl())
                        .resize(INVITE_WORKOUT_IMAGE_SIZE_WIDTH, INVITE_WORKOUT_IMAGE_SIZE_HEIGHT)
                        .centerCrop()
                        .into(imageview);
            } else {
                Picasso.with(context)
                        .load(R.drawable.ome_workoutimage_default)
                        .resize(INVITE_WORKOUT_IMAGE_SIZE_WIDTH, INVITE_WORKOUT_IMAGE_SIZE_HEIGHT)
                        .centerCrop()
                        .into(imageview);
            }
        }
    }

    // load user photo in dedicate context to imageview
    public static void getPhoto(Context context, Photo photo, ImageView imageview) {

        if (photo != null) {
            if (photo.getPhotoFile() != null) {
                ParseFile mfile = photo.getPhotoFile();
                Picasso.with(context)
                        .load(mfile.getUrl())
                        .centerCrop()
                        .into(imageview);
            } else {
                Picasso.with(context)
                        .load(R.drawable.ome_workoutimage_default)
                        .centerCrop()
                        .into(imageview);
            }
        }
    }



}
