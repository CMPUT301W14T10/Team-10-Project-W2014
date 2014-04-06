package ca.ualberta.team10projectw2014.controllersAndViews;


import java.util.ArrayList;
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


public class MapsViewActivity extends MapActivity{

	protected MapView map;
    private AnnotationView annotation;
    private List<GeoPoint> poiLocs;
    private ApplicationStateModel appState;

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
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	appState = ApplicationStateModel.getInstance();
    	List<OverlayItem> locationList = generateCoords();
        addPoiOverlay(locationList);
    }
    
    private List<OverlayItem> generateCoords(){
    	//Get list of current stuff from AppState
    	//Take coordinates and descriptions from that list to create new GeoPoints
    	
    	List<CommentModel> commentList = appState.getSubCommentViewHead().getSubComments();
    	List<OverlayItem> locationsList = new ArrayList<OverlayItem>();
    	CommentModel headComment = appState.getSubCommentViewHead();
    	map.getController().setCenter(new GeoPoint(headComment.getLocation().getLatitude(),headComment.getLocation().getLongitude()));
    	locationsList.add(new OverlayItem(new GeoPoint (headComment.getLocation().getLatitude(), headComment.getLocation().getLongitude()), headComment.getLocation().getName().toString(), ""));
    	for(int i = 0; i < commentList.size();i++){
    		locationsList.add(new OverlayItem(new GeoPoint (commentList.get(i).getLocation().getLatitude(), commentList.get(i).getLocation().getLongitude()), commentList.get(i).getLocation().getName().toString(), ""));
    	}
    	return locationsList;
    }
	
    // add an itemized overlay to map 
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

    // return false since no route is being displayed 
    @Override
    public boolean isRouteDisplayed() {
      return false;
    }
	
	
}
