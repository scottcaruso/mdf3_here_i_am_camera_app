package com.scottcaruso.hereiam;

import java.util.List;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.scottcaruso.camerafunctions.CameraIntent;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;

public class MainActivity extends Activity {
	
	public static LocationManager lm;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        startLocationManager();
        if (lm != null)
        {
        	Criteria criteria = new Criteria();
        	criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        	criteria.setCostAllowed(false);
        	String providerName = lm.getBestProvider(criteria, true);
        	final LocationListener listener = new LocationListener() {
        		@Override
        		public void onLocationChanged(Location location) 
        		{
        			Log.i("Info","Location changed!");
        		}

				@Override
				public void onProviderDisabled(String provider) {
					Log.i("Info","Provider Disabled!");
				}

				@Override
				public void onProviderEnabled(String provider) {
					Log.i("Info","Provider Enabled!");
				}

				@Override
				public void onStatusChanged(String provider, int status,
						Bundle extras) 
				{
					Log.i("Info","Location changed!");
				}
        	};
        	lm.requestSingleUpdate(criteria, listener, null);
        	Location thisLocation = lm.getLastKnownLocation(providerName);
        	String currentLocation = thisLocation.toString();
        	Log.i("Current Location",currentLocation);
        }
        
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
    
    public void startLocationManager()
    {
    	lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }
    
}
