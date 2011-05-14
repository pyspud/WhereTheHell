package uk.co.kalgan.wherethehell;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.location.Location;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class MyPositionOverlay extends Overlay {
	
	private final int mRadius = 5;
	Location location;
	
	@Override
	public void draw (Canvas _canvas, MapView _mapView, boolean _shadow) {
		Projection projection = _mapView.getProjection();
		
		if (_shadow == false) {
			// Get current location
			Double lat = location.getLatitude()*1E6;
			Double lon = location.getLongitude()*1E6;
			GeoPoint geoPoint = new GeoPoint(lat.intValue(), lon.intValue());
			
			// Convert the location to screen pixels
			Point point = new Point();
			projection.toPixels(geoPoint, point);
			
			RectF oval = new RectF(point.x - mRadius, point.y - mRadius,
								   point.x + mRadius, point.y + mRadius);
			
			// Setup Paint
			Paint paint = new Paint();
			paint.setARGB(250, 255, 255, 255);
			paint.setAntiAlias(true);
			paint.setFakeBoldText(true);
			
			Paint backPaint = new Paint();
			backPaint.setARGB(175, 50, 50, 50);
			backPaint.setAntiAlias(true);
			
			RectF backRect = new RectF(point.x + 2 + mRadius, point.y - 3*mRadius,
									   point.x + 65,		  point.y + mRadius);
			
			// Draw the marker
			_canvas.drawOval(oval, paint);
			_canvas.drawRoundRect(backRect, 5, 5, backPaint);
			_canvas.drawText("Here I Am", point.x + 2*mRadius, point.y, paint);
		}
		super.draw(_canvas, _mapView, _shadow);
	}

	@Override
	public boolean onTap(GeoPoint _point, MapView _mapView) {
		return false;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location _location) {
		location = _location;
	}
}
