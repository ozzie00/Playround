package com.oneme.toplay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.oneme.toplay.QR.Contents;
import com.oneme.toplay.QR.QRCodeEncode;
import com.oneme.toplay.base.AppConstant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DialogID extends DialogFragment {

    DialogIDListener mListener;
    Context mContext;

    public DialogID(Context context) {
        mContext = context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (DialogIDListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DHTDialogFragment");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater     = getActivity().getLayoutInflater();
        final View view             = inflater.inflate(R.layout.dialog_id, null);
        builder.setView(view);
        builder.setPositiveButton(getString(R.string.button_ok), new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int ID) {
                mListener.onDialogClick(DialogID.this);
            }
        });
        builder.setNeutralButton(getString(R.string.dialog_id), new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int ID) {
                /* Copy ID to clipboard */
                SharedPreferences sharedPreferences
                        = PreferenceManager.getDefaultSharedPreferences(mContext);
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) mContext
                        .getSystemService(mContext.CLIPBOARD_SERVICE);
                clipboard.setText(sharedPreferences.getString(AppConstant.OMEPARSEUSERIDKEY, ""));
            }
        });

        // Generate or load QR image of  ID
        File file = new File(Environment.getExternalStorageDirectory().getPath() + AppConstant.OMETOPLAYUSERQRCODEPATH);//"/Antox/");
        if (!file.exists()) {
            file.mkdirs();
        }

        File noMedia = new File(Environment.getExternalStorageDirectory().getPath() + AppConstant.OMETOPLAYUSERQRCODEPATH
                + AppConstant.OMETOPLAYUSERQRCODNOMEDIA);//+ "/Antox/", ".nomedia");
        if (!noMedia.exists()) {
            try {
                noMedia.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        file = new File(Environment.getExternalStorageDirectory().getPath() + AppConstant.OMETOPLAYUSERQRCODEFILE);// "/Antox/userkey_qr.png");
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        generateQR(pref.getString(AppConstant.OMEPARSEUSERIDKEY, ""));

        Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());

        ImageButton qrCode = (ImageButton) view.findViewById(R.id.qr_image);
        qrCode.setImageBitmap(bmp);
        qrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath()
                        + AppConstant.OMETOPLAYUSERQRCODEFILE)));//"/Antox/userkey_qr.png")));
                shareIntent.setType("image/jpeg");
                view.getContext().startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.share_with)));
            }
        });

        return builder.create();
    }

    private void generateQR(String userKey) {
        String qrData = AppConstant.OMEPARSEUSERIDHEAER + userKey;
        int qrCodeSize = 400;

        QRCodeEncode qrCodeEncoder = new QRCodeEncode(qrData, null,
                Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), qrCodeSize);

        FileOutputStream out;
        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            out           = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()
                    + AppConstant.OMETOPLAYUSERQRCODEFILE);//"/Antox/userkey_qr.png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface DialogIDListener {
        public void onDialogClick(DialogFragment fragment);
    }
}
