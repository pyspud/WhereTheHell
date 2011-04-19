package uk.co.kalgan.wherethehell;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class WhereTheHell extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        LocationManager locationManager;
        String context = Context.LOCATION_SERVICE;
        locationManager = (LocationManager)getSystemService(context);
        
        String provider = LocationManager.GPS_PROVIDER;
        Location location = locationManager.getLastKnownLocation(provider);
        
        updateWithNewLocation(location);
    }
    
    private void updateWithNewLocation(Location _location) {
    	String latLonString;
    	TextView myLocationText;
    	
    	myLocationText = (TextView)findViewById(R.id.myLocationText);
    	
    	if (_location != null) {
    		double lat = _location.getLatitude();
    		double lon = _location.getLongitude();
    		latLonString = "Lat: " + lat + "\nLon: " + lon;
    	} else {
    		latLonString = "No location found.";
    	}
    	myLocationText.setText("Your location is:\n"+ latLonString);
    }
}