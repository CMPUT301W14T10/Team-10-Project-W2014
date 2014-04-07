package ca.ualberta.team10projectw2014.controllersAndViews;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import ca.ualberta.team10projectw2014.R;
import ca.ualberta.team10projectw2014.models.ApplicationStateModel;
import ca.ualberta.team10projectw2014.models.CommentModel;

import com.mapquest.android.maps.AnnotationView;
import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.ItemizedOverlay;
import com.mapquest.android.maps.LineOverlay;
import com.mapquest.android.maps.MapActivity;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.OverlayItem;
import com.mapquest.android.maps.PolygonOverlay;


/**
 * Displays the map with the points displaying the locations of each of sub
 * comments.
 * @author  bpoulett
 * @version 1
 */
public class MapsViewActivity extends MapActivity{

	private MapsViewActivityListManager mapsViewActivityListManager = new MapsViewActivityListManager();
	protected MapView map;
    private AnnotationView annotation;
    private List<GeoPoint> poiLocs;

	/**
	 * @uml.property  name="appState"
	 * @uml.associationEnd  
	 */
    private ApplicationStateModel appState;

	/**
     * Initializes the map
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_map_view);

      map = (MapView) findViewById(R.id.map);
      map.getController().setZoom(6);
      map.getController().setCenter(new GeoPoint(53.52676,-113.52715));
      map.setBuiltInZoomControls(true);

      // initialize the annotation to be shown later 
      annotation = new AnnotationView(map);
    }
    
	/**
	 * Gets an instance of the singleton and generates coordinates from the 
	 * list of locations
	 */
    @Override
    public void onResume(){
    	super.onResume();
    	appState = ApplicationStateModel.getInstance();
    	List<OverlayItem> locationList = mapsViewActivityListManager.generateCoords(appState, map);
        addPoiOverlay(locationList);
    }
    

    /**
     * Add an itemized overlay to map
     *  
     * @param locationList list of locations
     */
    private void addPoiOverlay(List<OverlayItem> locationList) {
    	
      List<OverlayItem> locList = locationList;

      // use a custom POI marker by referencing the bitmap file directly,
      // using the filename as the resource ID 
      Drawable icon = getResources().getDrawable(R.drawable.ic_map_marker);
      final DefaultItemizedOverlay poiOverlay = new DefaultItemizedOverlay(icon);

      // set GeoPoints and title/snippet to be used in the annotation view 
      for (int i = 0; i < locList.size();i++){
    	  poiOverlay.addItem(locList.get(i));
      }

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

    /**
     * Method that returns false since no route is being displayed 
     */
    @Override
    public boolean isRouteDisplayed() {
      return false;
    }
	
    /**
     * Returns the geopoints to be displayed on the map.
     * 
     * @return poiLocs the points of each location
     */
    public List<GeoPoint> getPoiLocs() {
		return poiLocs;
	}

    /**
     * Sets the points of each location.
     * 
     * @param poiLocs
     */
	public void setPoiLocs(List<GeoPoint> poiLocs) {
		this.poiLocs = poiLocs;
	}
	
	public ArrayList<CommentModel> getFlattenedList() {
		return mapsViewActivityListManager.getFlattenedList();
	}

	public void setFlattenedList(ArrayList<CommentModel> flattenedList) {
		mapsViewActivityListManager.setFlattenedList(flattenedList);
	}
}
