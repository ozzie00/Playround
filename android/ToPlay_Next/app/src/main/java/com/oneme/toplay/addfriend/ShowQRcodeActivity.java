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

package com.oneme.toplay.addfriend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.oneme.toplay.Application;
import com.oneme.toplay.QR.Contents;
import com.oneme.toplay.QR.QRCodeEncode;
import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public final class ShowQRcodeActivity extends FragmentActivity  {

    static Context context;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ome_activity_show_qrcode);

        context = getApplicationContext();

        // Generate or load QR image
        File file = new File(Environment.getExternalStorageDirectory().getPath() + AppConstant.OMETOPLAYUSERQRCODEPATH);
        if (!file.exists()) {
            file.mkdirs();
        }

        File noMedia = new File(Environment.getExternalStorageDirectory().getPath() + AppConstant.OMETOPLAYUSERQRCODEPATH,
                AppConstant.OMETOPLAYUSERQRCODNOMEDIA);
        if (!noMedia.exists()) {
            try {
                noMedia.createNewFile();
            } catch (IOException e) {
                if (Application.APPDEBUG) {
                    e.printStackTrace();
                }
            }
        }

        TextView username = (TextView)findViewById(R.id.addme_username_view);

        if (ParseUser.getCurrentUser() != null) {
            username.setText(ParseUser.getCurrentUser().getUsername());
        }

        ImageView avatarImageView = (ImageView)findViewById(R.id.addme_avatar_view);

        ParseFile mavatarImageFile = null;

        if (ParseUser.getCurrentUser() != null) {
            mavatarImageFile = ParseUser.getCurrentUser().getParseFile(AppConstant.OMEPARSEUSERICONKEY);
        }

        if (mavatarImageFile != null) {
            Uri imageUri = Uri.parse(mavatarImageFile.getUrl());
            Picasso.with(ShowQRcodeActivity.this).load(imageUri.toString()).into(avatarImageView);
        }

        file                   = new File(Environment.getExternalStorageDirectory().getPath() + AppConstant.OMETOPLAYUSERQRCODEFILE);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        //Ozzie Zhang temporarily change to username from useridkey
        generateQR(pref.getString(ParseUser.getCurrentUser().getUsername(), ""));//AppConstant.OMEPARSEUSERIDKEY, ""));
        Bitmap bmp             = BitmapFactory.decodeFile(file.getAbsolutePath());

        ImageView qrCode = (ImageView) findViewById(R.id.show_qr_image);
        qrCode.setImageBitmap(bmp);
        qrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath()
                        + AppConstant.OMETOPLAYUSERQRCODEFILE)));
                shareIntent.setType(AppConstant.OMETOPLAYUSERQRCODEFILETYPE);
                startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.OMEPARSEADDCOTACTSHAREQRCODEWITH)));
            }
        });

    }

    private void generateQR(String userKey) {

        QRCodeEncode qrCodeEncoder = new QRCodeEncode(AppConstant.OMEPARSEUSERIDHEAER + userKey, null,
                Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), AppConstant.OMETOPLAYUSERQRCODESIZE);

        FileOutputStream out;
        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            out           = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()
                    + AppConstant.OMETOPLAYUSERQRCODEFILE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
        } catch (WriterException e) {
            if (Application.APPDEBUG) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            if (Application.APPDEBUG) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            if (Application.APPDEBUG) {
                e.printStackTrace();
            }
        }
    }

}
