package ca.ualberta.team10projectw2014.controllersAndViews;


import android.os.Bundle;

import ca.ualberta.team10projectw2014.R;

import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapActivity;
import com.mapquest.android.maps.MapView;


public class MapsViewActivity extends MapActivity{

	@Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_map_view);

      // set the zoom level, center point and enable the default zoom controls 
      MapView map = (MapView) findViewById(R.id.map);
      map.getController().setZoom(10);
      map.getController().setCenter(new GeoPoint(53.5500, -113.5000));
      map.setBuiltInZoomControls(true);
    }

    // return false since no route is being displayed 
    @Override
    public boolean isRouteDisplayed() {
      return false;
    }
	
	
}
