package com.example.carema;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TempCamera {

    //Variables
    Bitmap imageBitmap;
    Activity activity;
    Uri uri;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    File file;
    Boolean imageTaken;

    //Constructor
    public TempCamera(Activity activity) {
        this.activity = activity;
        imageBitmap = null;
        uri = null;
        imageTaken = false;
        file=null;
    }

    //Methods
    @SuppressLint("QueryPermissionsNeeded")
    public void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    public Boolean imageTaken() {
        return imageTaken;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public Uri getImageUri() {
        uri = Uri.parse(file.toString());
        return uri;
    }

    public File getImageFile() {
        return file;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode==-1) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageTaken = true;
            saveImage(imageBitmap);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void saveImage(Bitmap finalBitmap) {
        File myDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fName = "Temperature"+ timeStamp +".jpg";

        file = new File(myDir, fName);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}