/* Scott Caruso
 * Java II 1308
 * Week 3 Assignment
 */
package com.scottcaruso.geolocationfunctions;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import android.util.Log;

public class RetrieveDataFromGeoNames {
	
	static String response = "";
	static BufferedInputStream bin;
	
	public static String retrieveData(String urlString)
	{
		URL dataURL;
		Log.i("URL","Data URL created.");
		try 
		{
			dataURL = new URL(urlString);
			String response = getResponse(dataURL);
			Log.i("Info","Response received: " + response);
			return response;	
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getResponse(URL url)
	{

		try 
		{
			URLConnection connection = url.openConnection();
			try {
				bin = new BufferedInputStream(connection.getInputStream());
				Log.i("Info","Starting buffered input stream.");
			} catch (Exception e) {
				Log.e("Error","Failed at BufferedInputStream");
				e.printStackTrace();
			}
			
			byte[] contentBytes = new byte[1024];
			int bytesRead = 0;
			StringBuffer responseBuffer = new StringBuffer();
			
			while((bytesRead = bin.read(contentBytes)) != -1)
			{
				response = new String(contentBytes,0,bytesRead);
				responseBuffer.append(response);
			}
			Log.i("Info","Content read and response created.");
			return responseBuffer.toString();
		
		} catch (IOException e) {
			Log.e("Error", "getURLStringResponse");
			e.printStackTrace();
		}
	
		return response;
	}
		
}
