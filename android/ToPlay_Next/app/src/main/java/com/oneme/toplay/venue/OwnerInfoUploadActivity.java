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

package com.oneme.toplay.venue;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.database.Venue;
import com.oneme.toplay.database.VenueOwner;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class OwnerInfoUploadActivity extends ActionBarActivity {


    private final String TAG= "OwnerInfoUploadActivity";

    private EditText mnameText;
    private EditText memailText;
    private EditText mphoneText;

    private String mname    = null;
    private String memail   = null;
    private String mphone   = null;

    private ImageView owneridcopyImage;

    private ImageView licensecopyImage;

    public String photoPath;

    public Uri imageuri = null;

    private ParseFile owneridcopyFile;

    private ParseFile licensecopyFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ome_activity_venue_owner_upload_info);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        owneridcopyImage = (ImageView)findViewById(R.id.owner_info_upload_id_ImageView);

        // upload owner id copy
        RelativeLayout ownerid = (RelativeLayout) findViewById(R.id.owner_info_upload_id_block);
        ownerid.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final AlertDialog.Builder builder = new AlertDialog.Builder(OwnerInfoUploadActivity.this);
                final CharSequence items[];
                items = new CharSequence[] {
                        getResources().getString(R.string.OMEPARSEVENUEOWNERCHOOSEIDCOPY),
                        getResources().getString(R.string.OMEPARSEVENUEOWNERTAKEIDCOPY),
                };
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, AppConstant.OMEPARSEVENUOWNERIDCOPYIMAGERESULT);
                                break;

                            case 1:
                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                String image_name   = AppConstant.OMETOPLAYATTACHMENTFILENAME + new Date().toString();
                                File storageDir     = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                                File file           = null;
                                try {
                                    // prefix, suffix, directory
                                    file = File.createTempFile(
                                            image_name,
                                            ".png",
                                            storageDir
                                    );
                                } catch (IOException e) {
                                    if (Application.APPDEBUG) {
                                        e.printStackTrace();
                                    }
                                }
                                if (file != null) {
                                    Uri imageUri = Uri.fromFile(file);
                                    imageuri     = imageUri;
                                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                    photoPath    = file.getAbsolutePath();

                                    owneridcopyImage.setImageBitmap(BitmapFactory.decodeFile(photoPath));

                                }
                                startActivityForResult(cameraIntent, AppConstant.OMEPARSEVENUOWNERIDCOPYPHOTORESULT);
                                break;

                        }
                    }
                });
                builder.create().show();
            }
        });

        licensecopyImage = (ImageView)findViewById(R.id.owner_info_upload_license_ImageView);

        // upload owner license copy
        RelativeLayout licensecopy = (RelativeLayout) findViewById(R.id.owner_info_upload_license_block);
        licensecopy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final AlertDialog.Builder builder = new AlertDialog.Builder(OwnerInfoUploadActivity.this);
                final CharSequence items[];
                items = new CharSequence[] {
                        getResources().getString(R.string.OMEPARSEVENUEOWNERCHOOSEIDCOPY),
                        getResources().getString(R.string.OMEPARSEVENUEOWNERTAKEIDCOPY),
                };
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, AppConstant.OMEPARSEVENUOWNERLICENSECOPYIMAGERESULT);
                                break;

                            case 1:
                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                String image_name   = AppConstant.OMETOPLAYATTACHMENTFILENAME + new Date().toString();
                                File storageDir     = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                                File file           = null;
                                try {
                                    // prefix, suffix, directory
                                    file = File.createTempFile(
                                            image_name,
                                            ".png",
                                            storageDir
                                    );
                                } catch (IOException e) {
                                    if (Application.APPDEBUG) {
                                        e.printStackTrace();
                                    }
                                }
                                if (file != null) {
                                    Uri imageUri = Uri.fromFile(file);
                                    imageuri     = imageUri;
                                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                    photoPath    = file.getAbsolutePath();

                                    licensecopyImage.setImageBitmap(BitmapFactory.decodeFile(photoPath));

                                }
                                startActivityForResult(cameraIntent, AppConstant.OMEPARSEVENUOWNERLICENSECOPYPHOTORESULT);
                                break;

                        }
                    }
                });
                builder.create().show();
            }
        });


        // add mphone
        mphoneText = (EditText) findViewById(R.id.venue_owner_upload_info_phone_Text);
        mphoneText.setHint(getResources().getString(R.string.OMEPARSEVENUEOWNERPHONEPLACEHOLD));
       // mphoneText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mphoneText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        mphoneText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                if (mphoneText.length() > 0){
                    // position the text type in the left top corner
                    mphoneText.setGravity(Gravity.LEFT | Gravity.TOP);
                }else{
                    // no text entered. Center the hint text.
                    mphoneText.setGravity(Gravity.LEFT);
                }
                mphone = arg0.toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
                mphone = s.toString();
            }
        });

        // add email
        memailText = (EditText) findViewById(R.id.venue_owner_upload_info_contact_email_Text);
        memailText.setHint(getResources().getString(R.string.OMEPARSEVENUEOWNERCONTACTPLACEHOLD));
        memailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                if (memailText.length() > 0){
                    // position the text type in the left top corner
                    memailText.setGravity(Gravity.LEFT | Gravity.TOP);
                }else{
                    // no text entered. Center the hint text.
                    memailText.setGravity(Gravity.LEFT);
                }
                memail = arg0.toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
                memail = s.toString();
            }
        });




        Button submit =(Button)findViewById(R.id.venue_owner_upload_info_submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitVenue();
                finish();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // deal with owner id copy
        if (requestCode == AppConstant.OMEPARSEVENUOWNERIDCOPYIMAGERESULT  && resultCode == Activity.RESULT_OK) {
            Uri uri                 = data.getData();
            String path             = null;
            String[] filePathColumn = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME};
            String filePath         = null;
            String fileName         = null;
            CursorLoader loader     = new CursorLoader(OwnerInfoUploadActivity.this, uri, filePathColumn, null, null, null);
            Cursor cursor           = loader.loadInBackground();
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int columnIndex   = cursor.getColumnIndexOrThrow(filePathColumn[0]);
                    filePath          = cursor.getString(columnIndex);
                    int fileNameIndex = cursor.getColumnIndexOrThrow(filePathColumn[1]);
                    fileName          = cursor.getString(fileNameIndex);
                }
            }
            try {
                path = filePath;
            } catch (Exception e) {

            }
            if (path != null) {
                Bitmap mavatar = BitmapFactory.decodeFile(path);

                saveScaledIdCopy(mavatar);

                ParseUser.getCurrentUser().saveEventually();

                Bitmap mavatarScaled = Bitmap.createScaledBitmap(mavatar, 96, 96
                        * mavatar.getHeight() / mavatar.getWidth(), false);
                owneridcopyImage.setImageBitmap(mavatarScaled);

            }
        }

        if(requestCode==AppConstant.OMEPARSEVENUOWNERIDCOPYPHOTORESULT && resultCode==Activity.RESULT_OK){

            if (photoPath!=null) {

                Bitmap mavatar = BitmapFactory.decodeFile(photoPath);

                saveScaledIdCopy(mavatar);

                ParseUser.getCurrentUser().saveEventually();

                Bitmap mavatarScaled = Bitmap.createScaledBitmap(mavatar, 96, 96
                        * mavatar.getHeight() / mavatar.getWidth(), false);
                owneridcopyImage.setImageBitmap(mavatarScaled);

                photoPath = null;
            }

        }

        // deal with owner license copy
        if (requestCode == AppConstant.OMEPARSEVENUOWNERLICENSECOPYIMAGERESULT  && resultCode == Activity.RESULT_OK) {
            Uri uri                 = data.getData();
            String path             = null;
            String[] filePathColumn = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME};
            String filePath         = null;
            String fileName         = null;
            CursorLoader loader     = new CursorLoader(OwnerInfoUploadActivity.this, uri, filePathColumn, null, null, null);
            Cursor cursor           = loader.loadInBackground();
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int columnIndex   = cursor.getColumnIndexOrThrow(filePathColumn[0]);
                    filePath          = cursor.getString(columnIndex);
                    int fileNameIndex = cursor.getColumnIndexOrThrow(filePathColumn[1]);
                    fileName          = cursor.getString(fileNameIndex);
                }
            }
            try {
                path = filePath;
            } catch (Exception e) {

            }
            if (path != null) {
                Bitmap mavatar = BitmapFactory.decodeFile(path);

                saveScaledLicense(mavatar);

                ParseUser.getCurrentUser().saveEventually();

                Bitmap mavatarScaled = Bitmap.createScaledBitmap(mavatar, 96, 96
                        * mavatar.getHeight() / mavatar.getWidth(), false);
                licensecopyImage.setImageBitmap(mavatarScaled);

            }
        }

        if(requestCode==AppConstant.OMEPARSEVENUOWNERLICENSECOPYPHOTORESULT && resultCode==Activity.RESULT_OK){

            if (photoPath!=null) {

                Bitmap mavatar = BitmapFactory.decodeFile(photoPath);

                saveScaledLicense(mavatar);

                ParseUser.getCurrentUser().saveEventually();

                Bitmap mavatarScaled = Bitmap.createScaledBitmap(mavatar, 96, 96
                        * mavatar.getHeight() / mavatar.getWidth(), false);
                licensecopyImage.setImageBitmap(mavatarScaled);

                photoPath = null;
            }

        }
    }

    private void saveScaledIdCopy(Bitmap avatarImage) {

        // Resize photo from camera byte array
        //Bitmap snypImage = BitmapFactory.decodeByteArray(data, 0, data.length);
        Bitmap mavatarImageScaled = Bitmap.createScaledBitmap(avatarImage, 96, 96
                * avatarImage.getHeight() / avatarImage.getWidth(), false);

        // Override Android default landscape orientation and save portrait
        Matrix matrix = new Matrix();
        //matrix.postRotate(90);
        Bitmap rotatedScaledMealImage = Bitmap.createBitmap(mavatarImageScaled, 0,
                0, mavatarImageScaled.getWidth(), mavatarImageScaled.getHeight(),
                matrix, true);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        rotatedScaledMealImage.compress(Bitmap.CompressFormat.PNG, 100, bos);

        byte[] scaledData = bos.toByteArray();


        // Save the scaled image to Parse
        owneridcopyFile = new ParseFile(AppConstant.OMEPARSEVENUEOWNERIDCOPYFILENAME, scaledData);

        owneridcopyFile.saveInBackground(new SaveCallback() {

            public void done(ParseException e) {
                if (e == null) {
                    ParseUser user = ParseUser.getCurrentUser();
                    ParseQuery<VenueOwner> query = VenueOwner.getQuery();
                    query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                    query.include(AppConstant.OMETOPLAYVENUEOWNERCLASSKEY);
                    query.whereEqualTo(AppConstant.OMEPARSEUSERKEY, user);
                    query.setLimit(1);

                    query.getFirstInBackground(new GetCallback<VenueOwner>() {
                        public void done(VenueOwner venueowner, ParseException pe) {
                            if (pe == null) {
                                venueowner.setIdCopyImage(owneridcopyFile);
                                venueowner.saveInBackground();
                            } else {
                                Toast.makeText(OwnerInfoUploadActivity.this, getResources().getString(R.string.OMEPARSECANNOTGETVENUEINFO), Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(OwnerInfoUploadActivity.this,
                            getResources().getString(R.string.OMEPARSECANNOTSAVEPARSEFILE),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void saveScaledLicense(Bitmap avatarImage) {

        // Resize photo from camera byte array
        //Bitmap snypImage = BitmapFactory.decodeByteArray(data, 0, data.length);
        Bitmap mavatarImageScaled = Bitmap.createScaledBitmap(avatarImage, 96, 96
                * avatarImage.getHeight() / avatarImage.getWidth(), false);

        // Override Android default landscape orientation and save portrait
        Matrix matrix = new Matrix();
        //matrix.postRotate(90);
        Bitmap rotatedScaledMealImage = Bitmap.createBitmap(mavatarImageScaled, 0,
                0, mavatarImageScaled.getWidth(), mavatarImageScaled.getHeight(),
                matrix, true);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        rotatedScaledMealImage.compress(Bitmap.CompressFormat.PNG, 100, bos);

        byte[] scaledData = bos.toByteArray();


        // Save the scaled image to Parse
        licensecopyFile = new ParseFile(AppConstant.OMEPARSEVENUEOWNERLICENSEFILENAME, scaledData);

        licensecopyFile.saveInBackground(new SaveCallback() {

            public void done(ParseException e) {
                if (e == null) {
                    ParseUser user = ParseUser.getCurrentUser();
                    ParseQuery<VenueOwner> query = VenueOwner.getQuery();
                    query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                    query.include(AppConstant.OMETOPLAYVENUEOWNERCLASSKEY);
                    query.whereEqualTo(AppConstant.OMEPARSEUSERKEY, user);
                    query.setLimit(1);

                    query.getFirstInBackground(new GetCallback<VenueOwner>() {
                        public void done(VenueOwner venueowner, ParseException pe) {
                            if (pe == null) {
                                venueowner.setLicenseImage(licensecopyFile);
                                venueowner.saveInBackground();
                            } else {
                                Toast.makeText(OwnerInfoUploadActivity.this, getResources().getString(R.string.OMEPARSECANNOTGETVENUEINFO), Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    });
                } else {

                    Toast.makeText(OwnerInfoUploadActivity.this,
                            getResources().getString(R.string.OMEPARSECANNOTSAVEPARSEFILE),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    private void submitVenue () {

        // Set up a progress dialog
        final ProgressDialog messageListLoadDialog = new ProgressDialog(OwnerInfoUploadActivity.this);
        messageListLoadDialog.show();

        // Create an invitation.
        Venue venue = new Venue();

        ParseGeoPoint mGeoPoint = new ParseGeoPoint(Double.valueOf(Application.getCurrentLatitude()), Double.valueOf(Application.getCurrentLongitude()));;
        // Set the detailed venue information
        //venue.setName(mname);
        //venue.setAddress(maddress);
        venue.setPhone(mphone);
        venue.setLocation(mGeoPoint);
        venue.setCourtNumber("1");
        venue.setIndoor("1");
        venue.setLighted("1");

        venue.setUploadedBy(ParseUser.getCurrentUser().getUsername());
        ParseACL acl = new ParseACL();

        // Give public read access
        acl.setPublicReadAccess(true);
        venue.setACL(acl);

        // Save the post
        venue.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                messageListLoadDialog.dismiss();
            }
        });

        ParseUser user = ParseUser.getCurrentUser();
        ParseQuery<VenueOwner> query = VenueOwner.getQuery();
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.include(AppConstant.OMETOPLAYVENUEOWNERCLASSKEY);
        query.whereEqualTo(AppConstant.OMEPARSEUSERKEY, user);
        query.setLimit(1);

        query.getFirstInBackground(new GetCallback<VenueOwner>() {
            public void done(VenueOwner venueowner, ParseException pe) {
                if (pe == null) {
                    venueowner.setPhone(mphone);
                    venueowner.setContactEmail(memail);
                    venueowner.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Intent invokeOwnerMainActivityIntent = new Intent(OwnerInfoUploadActivity.this, OwnerMainActivity.class);
                                startActivity(invokeOwnerMainActivityIntent);
                            } else {
                                Toast.makeText(OwnerInfoUploadActivity.this, getResources().getString(R.string.OMEPARSECANNOTSAVEPARSEFILE), Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(OwnerInfoUploadActivity.this, getResources().getString(R.string.OMEPARSECANNOTGETVENUEINFO), Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        return;

    }

}
