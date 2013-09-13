/* Scott Caruso
 * MDF3 - 1309
 * Week 2 - Here I Am Camera/GPS App
 */
package com.scottcaruso.hereiam;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayActivity extends Activity 
{
	public String currentNetworkState;
	public NetworkReceiver networkReceiver;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        //Register the intentfilter so we can monitor the network here, too.
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        networkReceiver = new NetworkReceiver();
        this.registerReceiver(networkReceiver, filter);
        
        //Get the picture and location that the user generated.
        Intent incomingIntent = getIntent();
        Bundle extras = incomingIntent.getExtras();
    	Bitmap returnedBitmap = (Bitmap) extras.get("bitmap");
    	String location = (String) extras.get("location");
    	
    	//Display the picture
        setContentView(R.layout.photo_and_location_display);
        ImageView cameraImage = (ImageView) findViewById(R.id.capturedImage);
        cameraImage.setImageBitmap(returnedBitmap);
        
        //Set a status update for the user
        TextView statusUpdate = (TextView) findViewById(R.id.statusUpdate);
        statusUpdate.setText("Here I am! You found me in " + location + ". #hereiam");
        
        Button facebookButton = (Button) findViewById(R.id.facebookbutton);
        Button twitterButton = (Button) findViewById(R.id.twitterbutton);
        
        facebookButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				//Before trying to access FB or Twitter, verify that the connection still exists.
				if (currentNetworkState == "CONNECTED")
				{
					Toast toast = Toast.makeText(DisplayActivity.this, "This button will post your photo and status update to Facebook. Coming soon!", Toast.LENGTH_LONG);
					toast.show();
				} else
				{
					Toast toast = Toast.makeText(DisplayActivity.this, "Oops! You appear to have lost network connection. Please reconnect before trying to post your photo and update!", Toast.LENGTH_LONG);
					toast.show();
				}
				
			}
		});
        
        twitterButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				if (currentNetworkState == "CONNECTED")
				{
					Toast toast = Toast.makeText(DisplayActivity.this, "This button will post your photo and status update to Twitter. Coming soon!", Toast.LENGTH_LONG);
					toast.show();
				} else
				{
					Toast toast = Toast.makeText(DisplayActivity.this, "Oops! You appear to have lost network connection. Please reconnect before trying to post your photo and update!", Toast.LENGTH_LONG);
					toast.show();
				}
			}
		});
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
				e.printStackTrace();
			}
    		Log.i("Connection State",currentNetworkState);
    	}
    }
	
}
