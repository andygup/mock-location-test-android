package com.esri.template;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.esri.quickstart.EsriQuickStart;
import com.esri.quickstart.EsriQuickStart.MapType;


public class MockLocationTestActivity extends Activity {
	
	EsriQuickStart _esriQuickStartLib = null;
	Button _mockLocationButton = null;
	MockLocationTest _locationTest;
	LocationListener _locationListener;
	EditText _latitudeText;
	EditText _longitudeText;
	boolean _allowed = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		_esriQuickStartLib = new EsriQuickStart(this,R.id.map);
		_esriQuickStartLib.addLayer(MapType.STREETS, null, null, null,true);
		
		_mockLocationButton = (Button) findViewById(R.id.button1);
		_latitudeText = (EditText) findViewById(R.id.latitude);
		_latitudeText.setText("38.89");
		_longitudeText = (EditText) findViewById(R.id.longitude);
		_longitudeText.setText("-77.03");
		
    	_locationTest = new MockLocationTest();
		
		setButtonListeners();
		setLocationListener();
		setLocationManager();
    }
    
    private void setButtonListeners(){
    	_mockLocationButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				createMockLocation();
			}
		});
    }
    
    private void setLocationManager(){
    	_allowed = _locationTest.addTestProvider(this, LocationManager.GPS_PROVIDER, false, false, false, false, false, false, false, 0, android.location.Criteria.ACCURACY_FINE);
    	LocationManager locationManager = _locationTest.getLocationManager();
		locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 10000, 10, _locationListener);
    }
    
    private void setLocationListener(){
    	_locationListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
	    		_esriQuickStartLib.centerAt(location.getLatitude(), location.getLongitude(), true);
			}

			@Override
			public void onProviderDisabled(String arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderEnabled(String arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				// TODO Auto-generated method stub
				
			}
    		
    	};
    }
    
    private void createMockLocation(){
    	
    	double latitude = 0;
    	double longitude = 0;
		
		String lat = _latitudeText.getText().toString();
		String lon = _longitudeText.getText().toString();
		
		if(!lat.equals("") && !lon.equals("") && _allowed == true){
			latitude = Double.parseDouble(lat);
			longitude = Double.parseDouble(lon);
			_locationTest.addNewLocation(latitude,longitude, 3, 0, 0, 0, 0);
		}
		else{
			Toast toast = Toast.makeText(getApplicationContext(), "Invalid locations.Try again.", Toast.LENGTH_LONG);
			toast.show();
		}
    
    }

	@Override 
	protected void onDestroy() { 
		super.onDestroy();
	}
	
	@Override
	protected void onPause() {
		_esriQuickStartLib.pause();
		super.onPause();
	}
	
	@Override 	protected void onResume() {
		super.onResume(); 
		_esriQuickStartLib.unpause();
	}

}