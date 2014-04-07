package ca.ualberta.team10projectw2014.tests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import android.app.Activity;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import ca.ualberta.team10projectw2014.controllersAndViews.MainListViewActivity;
import ca.ualberta.team10projectw2014.models.ApplicationStateModel;
import ca.ualberta.team10projectw2014.models.CommentModel;
import ca.ualberta.team10projectw2014.models.LocationListenerModel;
import ca.ualberta.team10projectw2014.models.LocationModel;


public class ApplicationStateModelTests extends ActivityInstrumentationTestCase2<MainListViewActivity> {
	
	Activity activity;

	public ApplicationStateModelTests()
	{

		super(MainListViewActivity.class);
		appState = ApplicationStateModel.getInstance();

	}

	private ApplicationStateModel appState;
	
	
	@Override
	protected void setUp() throws Exception{
		super.setUp();
	}
	
	public void testSortByUserLocRandom() throws Throwable{
		activity = getActivity();
		ArrayList<CommentModel> sortableList = new ArrayList<CommentModel>();
		CommentModel starterComment = new CommentModel();
		starterComment.setAuthor("Someone");
		starterComment.setAuthorAndroidID("123");
		starterComment.setContent("A commmmmeeeeeeennnnnttttt!");
		starterComment.setImageUri(null);
		starterComment.setNumFavourites(3);
		starterComment.setParentID(null);
		starterComment.setTimestamp(Calendar.getInstance());
		starterComment.setTitle("A title.");
		starterComment.setUniqueID("me123");
		starterComment.setLocation(new LocationModel("somewhere", 0, 0));
		//Using pattern found at: http://stackoverflow.com/questions/363681/generating-random-numbers-in-a-range-with-java
		int commentsToGenerate = (int)(Math.random() * (101));
		CommentModel randComment;
		for(int i = 0; i < commentsToGenerate; i++){
			randComment = deepCopyComment(starterComment);
			randComment.getLocation().setLatitude(-90 + ((int)(Math.random() * (181))));
			randComment.getLocation().setLongitude(-180 + ((int)(Math.random() * (362))));
			sortableList.add(randComment);
		}
		appState.setFileContext(activity);
		Collections.sort(sortableList, ApplicationStateModel.locCompare);
		//Get the user's current location:
		final LocationListenerModel locationListener = new LocationListenerModel(activity);
		Location userLocation = locationListener.getLastBestLocation();
		Location closerLoc = new Location("provider");
		Location fartherLoc = new Location("provider");
		CommentModel closerComment;
		CommentModel fartherComment;
		for(int i = 0; i < (sortableList.size() - 1); i++){
			closerComment = sortableList.get(i);
			fartherComment = sortableList.get(i+1);
			closerLoc.setLatitude(closerComment.getLocation().getLatitude());
			closerLoc.setLongitude(closerComment.getLocation().getLongitude());
			fartherLoc.setLatitude(fartherComment.getLocation().getLatitude());
			fartherLoc.setLongitude(fartherComment.getLocation().getLongitude());
			fail();
			assertTrue("Comment with smaller index should be closer to user location.", (closerLoc.distanceTo(userLocation) <= fartherLoc.distanceTo(userLocation)));
		}
		activity.finish();
		setActivity(null);
	}
	
	public void testSortByDateRandom() throws Throwable{
		activity = getActivity();
		ArrayList<CommentModel> sortableList = new ArrayList<CommentModel>();
		CommentModel starterComment = new CommentModel();
		starterComment.setAuthor("Someone");
		starterComment.setAuthorAndroidID("123");
		starterComment.setContent("A commmmmeeeeeeennnnnttttt!");
		starterComment.setImageUri(null);
		starterComment.setNumFavourites(3);
		starterComment.setParentID(null);
		starterComment.setTimestamp(Calendar.getInstance());
		starterComment.setTitle("A title.");
		starterComment.setUniqueID("me123");
		starterComment.setLocation(new LocationModel("somewhere", 0, 0));
		//Using pattern found at: http://stackoverflow.com/questions/363681/generating-random-numbers-in-a-range-with-java
		int commentsToGenerate = (int)(Math.random() * (101));
		CommentModel randComment;
		for(int i = 0; i < commentsToGenerate; i++){
			randComment = deepCopyComment(starterComment);
			randComment.getLocation().setLatitude(-90 + ((int)(Math.random() * (181))));
			randComment.getLocation().setLongitude(-180 + ((int)(Math.random() * (362))));
			sortableList.add(randComment);
		}
		appState.setFileContext(activity);
		Collections.sort(sortableList, ApplicationStateModel.locCompare);
		//Get the user's current location:
		final LocationListenerModel locationListener = new LocationListenerModel(activity);
		Location userLocation = locationListener.getLastBestLocation();
		Location closerLoc = new Location("provider");
		Location fartherLoc = new Location("provider");
		CommentModel closerComment;
		CommentModel fartherComment;
		for(int i = 0; i < (sortableList.size() - 1); i++){
			closerComment = sortableList.get(i);
			fartherComment = sortableList.get(i+1);
			closerLoc.setLatitude(closerComment.getLocation().getLatitude());
			closerLoc.setLongitude(closerComment.getLocation().getLongitude());
			fartherLoc.setLatitude(fartherComment.getLocation().getLatitude());
			fartherLoc.setLongitude(fartherComment.getLocation().getLongitude());
			fail();
			assertTrue("Comment with smaller index should be closer to user location.", (closerLoc.distanceTo(userLocation) > fartherLoc.distanceTo(userLocation)));
		}
		activity.finish();
		setActivity(null);
	}
	
	public CommentModel deepCopyComment(CommentModel copiedComment){
		CommentModel copy = new CommentModel();
		copy.setAuthor(copiedComment.getAuthor());
		copy.setAuthorAndroidID(copiedComment.getAuthorAndroidID());
		copy.setContent(copiedComment.getContent());
		copy.setImageUri(copiedComment.getImageUri());
		copy.setLocation(new LocationModel(copiedComment.getLocation().getName(), copiedComment.getLocation().getLatitude(), copiedComment.getLocation().getLongitude()));
		copy.setNumFavourites(copiedComment.getNumFavourites());
		copy.setParentID(copiedComment.getParentID());
		copy.setPhotoPath(copiedComment.getPhotoPath());
		copy.setTimestamp((Calendar)copiedComment.getTimestamp().clone());
		copy.setTitle(copiedComment.getTitle());
		copy.setUniqueID(copiedComment.getUniqueID());
		return copy;
	}
}
