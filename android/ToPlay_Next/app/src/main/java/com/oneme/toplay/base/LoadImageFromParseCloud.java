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
import android.widget.ImageView;

import com.oneme.toplay.R;
import com.oneme.toplay.base.third.RoundedTransformationBuilder;

import com.parse.ParseFile;
import com.parse.ParseUser;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public final class LoadImageFromParseCloud {

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
                Picasso.with(context)
                        .load(R.drawable.ome_default_avatar)
                        .fit()
                        .transform(mtransformation)
                        .into(imageview);
            }
        }
    }
}
