package com.scottcaruso.camerafunctions;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;

public class CameraIntent extends Activity {
	
	public static Bitmap imageBitmap;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

		final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		// start the image capture Intent
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
    	getCameraPhoto(data);
    	finish();
    }
    
    private void getCameraPhoto(Intent intent) 
    {
        try {
			Bundle extras = intent.getExtras();
			imageBitmap = (Bitmap) extras.get("data");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	@Override
	public void finish() 
	{
	    Intent data = new Intent();
	    try {
			data.putExtra("bitmap", imageBitmap);
			setResult(RESULT_OK, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    super.finish();
	}

}
