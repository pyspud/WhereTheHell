package uk.co.kalgan.wherethehell;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class WhereTheHell extends MapActivity {
	
	MapController mapController;
	MyPositionOverlay positionOverlay;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Get the reference to map view
        MapView myMapView = (MapView)findViewById(R.id.myMapView);
        mapController = myMapView.getController();
        
        // Configure the map display
        myMapView.setSatellite(true);
        myMapView.setStreetView(true);
        myMapView.displayZoomControls(false);
        
        // Zoom in to location
        mapController.setZoom(17);
        
        // Add the overlay
        positionOverlay = new MyPositionOverlay();
        List<Overlay> overlays = myMapView.getOverlays();
        overlays.add(positionOverlay);
        
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
    	
    	String addressString = "No address found";
    	
    	if (_location != null) {
    		// Update location marker
    		positionOverlay.setLocation(_location);
    		
    		double lat = _location.getLatitude();
    		double lon = _location.getLongitude();
    		latLonString = "Lat: " + lat + "\nLon: " + lon;
    		
    		// Update map location
    		Double geoLat = lat*1E6;
    		Double geoLon = lon*1E6;
    		GeoPoint point = new GeoPoint(geoLat.intValue(), geoLon.intValue());
    		mapController.animateTo(point);
    		
    		Geocoder gc = new Geocoder(this, Locale.getDefault());
    		try {
    			List<Address> addresses = gc.getFromLocation(lat, lon, 1);
    			StringBuilder sb = new StringBuilder();
    			
    			if (addresses.size() > 0) {
    				Address address = addresses.get(0);
    				
    				for ( int i = 0; i < address.getMaxAddressLineIndex(); i++)
    					sb.append(address.getAddressLine(i)).append("\n");
    				sb.append(address.getLocality()).append("\n");
    				sb.append(address.getPostalCode()).append("\n");
    				sb.append(address.getCountryName()).append("\n");
    			}
    			addressString = sb.toString();
    		} catch (IOException e) {}
    	} else {
    		latLonString = "No location found.";
    	}
    	myLocationText.setText("Your location is:\n"+ latLonString + "\n"
    			+ addressString);
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}