package com.scottcaruso.hereiam;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.scottcaruso.camerafunctions.CameraIntent;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;

public class MainActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
		Intent cameraActivity = new Intent(this,CameraIntent.class);
		startActivityForResult(cameraActivity, 0);
		
		boolean areWeConnectedToGoogle = servicesConnected();
		if (areWeConnectedToGoogle == true)
		{
			Log.i("Google Play","Services Connected");
		}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
    	Bundle extras = data.getExtras();
    	ImageView mainView = (ImageView) findViewById(R.id.mainImage);
    	Bitmap returnedBitmap = (Bitmap) extras.get("bitmap");
    	if (returnedBitmap == null)
    	{
    		Log.i("Test","Photo cancelled!");
    	}
    	mainView.setImageBitmap(returnedBitmap);
    	mainView.setVisibility(0);
    	//Log.i("Test","Bitmap returned!");
    }
    
    private boolean servicesConnected()
    {
    	int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
    	if (resultCode == 0)
    	{
    		return true;
    	} else
    	{
    		return false;
    	}
    }
    
}
