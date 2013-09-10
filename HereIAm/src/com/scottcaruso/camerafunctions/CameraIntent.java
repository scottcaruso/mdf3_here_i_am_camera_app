package com.scottcaruso.camerafunctions;

import com.scottcaruso.hereiam.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

public class CameraIntent extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

		final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
		Uri fileUri;
		final int MEDIA_TYPE_IMAGE = 1;

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = PictureStorage.getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

		// start the image capture Intent
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

}
