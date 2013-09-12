package com.scottcaruso.hereiam;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.scottcaruso.camerafunctions.CameraIntent;
import com.scottcaruso.geolocationfunctions.DataRetrievalService;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity {
	
	public static LocationManager lm;
	public static String lat;
	public static String lon;
	public static String location;
	public static String response;
	public String currentNetworkState;
	public NetworkReceiver networkReceiver;
	
    @SuppressLint("HandlerLeak")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button findMeButton = (Button) findViewById(R.id.findmebutton);
        findMeButton.setOnClickListener(new View.OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				if (currentNetworkState == "CONNECTED")
				{
					Intent cameraActivity = new Intent(MainActivity.this,CameraIntent.class);
					startActivityForResult(cameraActivity, 0);
					runGeolocation();
				} else
				{
					Toast toast = Toast.makeText(MainActivity.this, "You currently do not have a network connection, so we cannot confidently say where you are! Please connect to a wireless network!", Toast.LENGTH_LONG);
					toast.show();
				}
			}
		});
		
		boolean areWeConnectedToGoogle = servicesConnected();
		if (areWeConnectedToGoogle == true)
		{
			Log.i("Google Play","Services Connected");
		}
		
    }

    @Override
    protected void onStart ()
    {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        networkReceiver = new NetworkReceiver();
        this.registerReceiver(networkReceiver, filter);
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
    	if (requestCode == 0)
    	{
    		Log.i("Request Code","Coming back from camera.");
    		Bundle extras = data.getExtras();
        	Bitmap returnedBitmap = (Bitmap) extras.get("bitmap");
        	if (lat == null || lon == null)
        	{
				Toast toast = Toast.makeText(MainActivity.this, "There was a problem retrieving your Geo data. Please check that your GPS is active and try again later.", Toast.LENGTH_SHORT);
				toast.show();
        	} else
        	{
        		if (returnedBitmap != null)
        		{
        			extras.putString("location", location);
        			Intent showDisplay = new Intent(this, DisplayActivity.class);
        			showDisplay.putExtras(extras);
        			startActivityForResult(showDisplay, 1);
        		}
        	}
    	} else if (requestCode == 1)
    	{
    		Log.i("Request Code","Coming back from display.");
    	} else
    	{
    		Log.i("Error","No request code found for the deleted activity.");
    	}
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
    
    public void obtainGeoData()
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
    	Double latDouble = thisLocation.getLatitude();
    	Double lonDouble = thisLocation.getLongitude();
    	lat = String.valueOf(latDouble);
    	lon = String.valueOf(lonDouble);
    	Log.i("Current Location","Lat = "+lat+"  Lon = "+lon);
    }
    
    public class NetworkReceiver extends BroadcastReceiver
    {
    	@Override
    	public void onReceive(Context context, Intent intent)
    	{
    		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    		try {
				NetworkInfo ni = cm.getActiveNetworkInfo();
				currentNetworkState = ni.getState().toString();
			} catch (Exception e) {
				currentNetworkState = "Not connected";
			}
    		Log.i("Connection State",currentNetworkState);
    	}
    }
    
    public void runGeolocation()
    {
    	 startLocationManager();
         if (lm != null)
         {
         	obtainGeoData();
         	Handler retrievalHandler = new Handler()
 			{
 				@Override
 				public void handleMessage(Message msg) 
 				{
 					if (msg.arg1 == RESULT_OK)
 					{
 						try {
 							response = (String) msg.obj;
 						} catch (Exception e) {
 							Log.e("Error","There was a problem retrieving the json Response.");
 						}
 						String nullResponse = "{\"results\":[],\"count\":0}";
 						if (response.equals(nullResponse))
 						{
 							Toast toast = Toast.makeText(MainActivity.this, "There was a problem retrieving Geo data. Please try again later.", Toast.LENGTH_SHORT);
 							toast.show();
 						} else
 						{
 							try {
								JSONObject result = new JSONObject(response);
								JSONArray resultArray = result.getJSONArray("geonames");
								JSONObject thisLocation = resultArray.getJSONObject(0);
								String nearestPlace = thisLocation.getString("toponymName");
								String state = thisLocation.getString("adminCode1");
								location = nearestPlace+", "+state;
								Log.i("Location",location);
							} catch (JSONException e) {
								Log.e("Error","Problem parsing JSON data!");
								e.printStackTrace();
							}
 							
 						}
 					}
 				}
 			};
 			Messenger apiMessenger = new Messenger(retrievalHandler);
 			
 			Intent startDataService = new Intent(this, DataRetrievalService.class);
 			startDataService.putExtra(DataRetrievalService.MESSENGER_KEY, apiMessenger);
 			startDataService.putExtra(DataRetrievalService.LON_KEY,lon);
 			startDataService.putExtra(DataRetrievalService.LAT_KEY,lat);
 			this.startService(startDataService);
         }
    }
  
}
