package uk.co.kalgan.wherethehell;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
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
        
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(criteria, true);
        
        Location location = locationManager.getLastKnownLocation(provider);
        updateWithNewLocation(location);
        
        locationManager.requestLocationUpdates(provider, 2000, 10, locationListener);
    }
    
    private final LocationListener locationListener = new LocationListener() {
    	public void onLocationChanged(Location _location) {
    		updateWithNewLocation(_location);
    	}
    	public void onProviderDisabled(String _provider) {
    		updateWithNewLocation(null);
    	}
    	public void onProviderEnabled(String _provider) { }
    	public void onStatusChanged(String _provider, int _status, Bundle _extras) { }
	};
    
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