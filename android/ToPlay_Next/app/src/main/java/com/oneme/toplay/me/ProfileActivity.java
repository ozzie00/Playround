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

package com.oneme.toplay.me;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.oneme.toplay.base.LoadImageFromParseCloud;
import com.oneme.toplay.base.third.RoundedTransformationBuilder;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import com.oneme.toplay.Application;
import com.oneme.toplay.R;
import com.oneme.toplay.base.AppConstant;
import com.oneme.toplay.base.Constants;
import com.oneme.toplay.base.FileDialog;
import com.oneme.toplay.addfriend.ShowQRcodeActivity;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;


/**
 * Activity that displays the settings screen.
 */
public class ProfileActivity extends ActionBarActivity {

    private final String TAG = "ProfileActivity";

    private LinearLayout msettingLinerLayout;
    private Button mloginButton;

    private TextView maliasText;

    private ImageView mavatarImage;

    private String mUsername     = null;


    private TextView mplayroundidText;

    private String mAbout     = null;

    public String photoPath;

    public Uri imageuri = null;

    public Uri userOldAvatarUri = null;

    private static final int SELECT_PICTURE = 1;

    private String selectedImagePath;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;

    private ParseFile userIconFile;

    private ParseUser muser                = ParseUser.getCurrentUser();
    private Transformation mtransformation = null;

    private List<Float> availableOptions = Application.getConfigHelper().getSearchDistanceAvailableOptions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ome_activity_me_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();

        if (muser != null) {
            mUsername     = muser.getUsername();
            addaliasText();
            addplayroundid();
           // addUsernameText();
        } else {
            //Toast.makeText(SettingActivity.this, getResources().getString(R.string.OMEPARSEINVITELOGINALERT),
            //         Toast.LENGTH_SHORT).show();
        }

        mtransformation = new RoundedTransformationBuilder()
                .borderColor(Color.WHITE)
                .borderWidthDp(1)
                .cornerRadiusDp(AppConstant.OMEPARSEUSERICONRADIUS)
                .oval(false)
                .build();

        mavatarImage    = (ImageView)findViewById(R.id.profile_avatar_image);

        if (muser != null) {

            LoadImageFromParseCloud.getAvatar(ProfileActivity.this, muser, mavatarImage);

            //ParseFile mfile = muser.getParseFile(AppConstant.OMEPARSEUSERICONKEY);

            //Picasso.with(ProfileActivity.this)
            //        .load(mfile.getUrl())
            //        .fit()
            //        .transform(mtransformation)
            //        .into(mavatarImage);
        }

        RelativeLayout avatar = (RelativeLayout) findViewById(R.id.profile_avatar_block);
        avatar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                final CharSequence items[];
                items = new CharSequence[] {
                        getResources().getString(R.string.OMEPARSEMEPROFILECHOOSEIMAGE),
                        getResources().getString(R.string.OMEPARSEMEPROFILETAKEPHOTO),
                };
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, Constants.IMAGE_RESULT);
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

                                    mavatarImage.setImageBitmap(BitmapFactory.decodeFile(photoPath));

                                    // saveScaledPhoto(BitmapFactory.decodeFile(photoPath));
                                }
                                //cameraIntent.setData(imageuri);
                                startActivityForResult(cameraIntent, Constants.PHOTO_RESULT);
                                break;

                        }
                    }
                });
                builder.create().show();
            }
        });



      //  Bitmap snypImageScaled = Bitmap.createScaledBitmap(, 48, 48
      //          * BitmapFactory.decodeFile(photoPath).getHeight() / BitmapFactory.decodeFile(photoPath).getWidth(), false);

        //mavatarImage.setImageBitmap(snypImageScaled);//BitmapFactory.decodeFile(photoPath));



        RelativeLayout qrcode = (RelativeLayout) findViewById(R.id.profile_QR_block);
        qrcode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent invokeShowQRcodeActivityIntent = new Intent(ProfileActivity.this, ShowQRcodeActivity.class);
                startActivity(invokeShowQRcodeActivityIntent);
            }
        });





    }

    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
            }
        }
    }

    */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.IMAGE_RESULT  && resultCode == Activity.RESULT_OK) {
            Uri uri                 = data.getData();
            String path             = null;
            String[] filePathColumn = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME};
            String filePath         = null;
            String fileName         = null;
            CursorLoader loader     = new CursorLoader(ProfileActivity.this, uri, filePathColumn, null, null, null);
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

                saveScaledPhoto(mavatar);

                ParseUser.getCurrentUser().saveEventually();

                Bitmap mavatarScaled = Bitmap.createScaledBitmap(mavatar, 96, 96
                        * mavatar.getHeight() / mavatar.getWidth(), false);
                mavatarImage.setImageBitmap(mavatarScaled);



               // mSingleton.sendFileSendRequest(path, this.activeKey, getActivity());
            }
        }

        if(requestCode==Constants.PHOTO_RESULT && resultCode==Activity.RESULT_OK){

            if (photoPath!=null) {

                Bitmap mavatar = BitmapFactory.decodeFile(photoPath);

                saveScaledPhoto(mavatar);

                ParseUser.getCurrentUser().saveEventually();

                Bitmap mavatarScaled = Bitmap.createScaledBitmap(mavatar, 96, 96
                        * mavatar.getHeight() / mavatar.getWidth(), false);
                mavatarImage.setImageBitmap(mavatarScaled);

                photoPath = null;
            }

        }
    }
    /*

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        Bitmap bitmap   = null;
        String path     = "";

        if (requestCode == PICK_FROM_FILE) {
            imageuri = data.getData();
            path = getRealPathFromURI(imageuri); //from Gallery

            if (path == null) {
                path = imageuri.getPath(); //from File Manager
            }

            if (path != null) {
                bitmap = BitmapFactory.decodeFile(path);

                saveScaledPhoto(bitmap);
            }

        } else {
            path    = imageuri.getPath();
            bitmap  = BitmapFactory.decodeFile(path);
            saveScaledPhoto(bitmap);
        }

        mavatarImage.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 48, 48
                * bitmap.getHeight() / bitmap.getWidth(), false));//bitmap);
    }

*/


    private Bitmap getBitmapFromUri(Uri uri) throws IOException {

        if (uri != null) {
            try {
                ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
                FileDescriptor fileDescriptor             = parcelFileDescriptor.getFileDescriptor();
                Bitmap image                              = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                parcelFileDescriptor.close();
                return image;
            } catch (IOException ioe) {

                return null;
            }
        }
        return null;
    }

    private void saveScaledPhoto(Bitmap avatarImage) {

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
        userIconFile = new ParseFile(AppConstant.OMEPARSEUSERICONFILENAME, scaledData);

       // userIconFile.saveEventually();
        userIconFile.saveInBackground(new SaveCallback() {

            public void done(ParseException e) {
                if (e == null) {
                    ParseUser.getCurrentUser().put(AppConstant.OMEPARSEUSERICONKEY, userIconFile);
                } else {

                    Toast.makeText(ProfileActivity.this,
                            "Error saving: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // define username text
    private void addaliasText() {

        maliasText = (TextView) findViewById(R.id.profile_alias_text);
        maliasText.setText(mUsername);
    }

    // define username text
    private void addplayroundid() {
        mplayroundidText = (TextView) findViewById(R.id.profile_ID_text);
        mplayroundidText.setText(mUsername);
    }


    /*
    @Override
    public Intent getSupportParentActivityIntent() {

        Intent parentIntent = getIntent();

        //getting the parent class name
        String className = parentIntent.getStringExtra(AppConstant.OMEPARSEPARENTCLASSNAME);

        Intent newIntent = null;

        try {
            //you need to define the class with package name
            newIntent = new Intent(ProfileActivity.this, Class.forName(className));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return newIntent;
    }
    */
}
