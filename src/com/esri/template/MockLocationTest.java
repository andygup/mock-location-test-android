package com.esri.template;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;

/**
 * A Class for testing mock locations in native Android applications.
 * @author Andy Gup
 * @version 1.0
 *
 */
public class MockLocationTest {
	private LocationManager _locationManager;
	private String _providerName;
	private Activity _activity;
	
	public MockLocationTest(){

	}
	
	/**
	 * Instantiate an instance of addTestProvider()
	 * @param activity Activity
	 * @param providerName LocationManager.GPS_PROVIDER or LocationManager.NETWORK_PROVIDER
	 * @param requiresSatellite
	 * @param requiresNetwork
	 * @param requiresCell
	 * @param hasMonetaryCost
	 * @param supportsAltitude
	 * @param supportsSpeed
	 * @param supportsBearing
	 * @param powerRequirement android.location.Criteria.POWER_LOW
	 * @param accuracy android.location.Criteria.ACCURACY_FINE
	 */
	public boolean addTestProvider(
			Activity activity,
			String providerName,boolean requiresSatellite, boolean requiresNetwork, 
			boolean requiresCell, boolean hasMonetaryCost, boolean supportsAltitude, 
			boolean supportsSpeed, boolean supportsBearing, int powerRequirement, int accuracy){
		
		_providerName = providerName;
		_activity = activity;		
		
		_locationManager = (LocationManager) _activity.getSystemService(Context.LOCATION_SERVICE);
		
		if(isAllowMockLocations(_activity) == true){
			_locationManager.addTestProvider(
					_providerName, 
					requiresNetwork, 
					requiresSatellite, 
					requiresCell, 
					hasMonetaryCost, 
					supportsAltitude, 
					supportsSpeed, 
					supportsBearing, 
					powerRequirement, 
					accuracy
			);
		}
		else{
			Log.d("MockLocationTest","MockLocationTest: ALLOW_MOCK_LOCATION is not specified in manifest.");
			Toast toast = Toast.makeText(_activity, "ALLOW_MOCK_LOCATION is not specified in manifest", Toast.LENGTH_LONG);
			toast.show();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Adds a new mock location
	 * @param latitude
	 * @param longitude
	 * @param accuracy meters
	 * @param altitude meters
	 * @param bearing degrees
	 * @param speed meters per second
	 * @param time 
	 */
	public void addNewLocation(double latitude, double longitude, float accuracy, double altitude, float bearing, float speed, long time){
		if(isAllowMockLocations(_activity) == true){
			Location location = new Location(_providerName);
			location.setLatitude(latitude);
			location.setLongitude(longitude);
			location.setAccuracy(accuracy);
			location.setAltitude(altitude);
			location.setBearing(bearing);
			location.setSpeed(speed);
			location.setTime(time);
			
			_locationManager.setTestProviderEnabled(_providerName, true);
			_locationManager.setTestProviderStatus(_providerName, LocationProvider.AVAILABLE, null, System.currentTimeMillis());
			_locationManager.setTestProviderLocation(_providerName, location);
		}
		else{
			Log.d("MockLocationTest","MockLocationTest: ALLOW_MOCK_LOCATION is not specified in manifest.");
			Toast toast = Toast.makeText(_activity, "ALLOW_MOCK_LOCATION is not specified in manifest", Toast.LENGTH_LONG);
			toast.show();
			
		}
	}
	
	public LocationManager getLocationManager(){
		return _locationManager;
	}
	
	public boolean isLocationProviderEnabled(Activity activity, String provider){
		return Secure.isLocationProviderEnabled(activity.getContentResolver(), provider);
	}
	
	public boolean isAllowMockLocations(Activity activity){
		String t = Secure.getString(activity.getContentResolver(), Secure.ALLOW_MOCK_LOCATION);

		if (t.equals("0")){
			return false; 
		}
		else{
			return true;
		}
			      	       
	}
	
	public void clearLocation(){
		_locationManager.clearTestProviderLocation(_providerName);
	}
}

