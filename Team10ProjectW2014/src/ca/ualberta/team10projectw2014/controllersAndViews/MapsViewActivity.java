package ca.ualberta.team10projectw2014.controllersAndViews;


import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import ca.ualberta.team10projectw2014.R;

import com.mapquest.android.maps.AnnotationView;
import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.ItemizedOverlay;
import com.mapquest.android.maps.LineOverlay;
import com.mapquest.android.maps.MapActivity;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.OverlayItem;
import com.mapquest.android.maps.PolygonOverlay;


public class MapsViewActivity extends MapActivity{

	protected MapView map;
    AnnotationView annotation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_map_view);

      map = (MapView) findViewById(R.id.map);
      map.getController().setZoom(4);
      map.getController().setCenter(new GeoPoint(53.52676,-113.52715));
      map.setBuiltInZoomControls(true);

      // initialize the annotation to be shown later 
      annotation = new AnnotationView(map);

      addPoiOverlay();
    }
	
    // add an itemized overlay to map 
    private void addPoiOverlay() {

      // use a custom POI marker by referencing the bitmap file directly,
      // using the filename as the resource ID 
      Drawable icon = getResources().getDrawable(R.drawable.ic_map_marker);
      final DefaultItemizedOverlay poiOverlay = new DefaultItemizedOverlay(icon);

      // set GeoPoints and title/snippet to be used in the annotation view 
      OverlayItem poi1 = new OverlayItem(new GeoPoint (53.52676,-113.52715), "Computing Science Centre", "");
      poiOverlay.addItem(poi1);

      // add a tap listener for the POI overlay 
      poiOverlay.setTapListener(new ItemizedOverlay.OverlayTapListener() {
      @Override
        public void onTap(GeoPoint pt, MapView mapView) {
           // when tapped, show the annotation for the overlayItem 
           int lastTouchedIndex = poiOverlay.getLastFocusedIndex();
           if(lastTouchedIndex>-1){
               OverlayItem tapped = poiOverlay.getItem(lastTouchedIndex);
               annotation.showAnnotationView(tapped);
           }
         }
      });

      map.getOverlays().add(poiOverlay);
    }

    // return false since no route is being displayed 
    @Override
    public boolean isRouteDisplayed() {
      return false;
    }
	
	
}
