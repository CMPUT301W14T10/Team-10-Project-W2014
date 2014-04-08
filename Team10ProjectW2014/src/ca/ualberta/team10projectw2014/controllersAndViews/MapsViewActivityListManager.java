package ca.ualberta.team10projectw2014.controllersAndViews;


import java.util.ArrayList;
import ca.ualberta.team10projectw2014.models.CommentModel;
import java.util.List;
import com.mapquest.android.maps.OverlayItem;
import com.mapquest.android.maps.GeoPoint;
import ca.ualberta.team10projectw2014.models.ApplicationStateModel;
import com.mapquest.android.maps.MapView;

/**
 * Deals with the lists in MapViewActivity
 * @author  bpoulett
 * @version 1
 */
public class MapsViewActivityListManager {
	private ArrayList<CommentModel> flattenedList = new ArrayList<CommentModel>();

	public ArrayList<CommentModel> getFlattenedList() {
		return flattenedList;
	}

	public void setFlattenedList(ArrayList<CommentModel> flattenedList) {
		this.flattenedList = flattenedList;
	}

	//Taken from SCVA
	public void addCommentToList(
			ArrayList<? extends CommentModel> subCommentList) {
		if (subCommentList.size() == 0) {
			return;
		} else {
			for (int i = 0; i < subCommentList.size(); i++) {
				flattenedList.add(subCommentList.get(i));
				if (subCommentList.get(i).getSubComments().size() > 0) {
					addCommentToList(subCommentList.get(i).getSubComments());
				}
			}
		}
	}

	 /**
     * Generates the coordinates form a list of locations to be used in
     * {@link #onResume()}
     * 
     * @return locationlist the list of location coordinates
     */
	public List<OverlayItem> generateCoords(ApplicationStateModel appState,
			MapView map) {
		CommentModel headComment = appState.getSubCommentViewHead();
		List<OverlayItem> locationsList = new ArrayList<OverlayItem>();
		map.getController().setCenter(
				new GeoPoint(headComment.getLocation().getLatitude(),
						headComment.getLocation().getLongitude()));
		addCommentToList(headComment.getSubComments());
		for (int i = 0; i < flattenedList.size(); i++) {
			locationsList
					.add(new OverlayItem(
							new GeoPoint((flattenedList.get(i)).getLocation()
									.getLatitude(), (flattenedList.get(i))
									.getLocation().getLongitude()),
							flattenedList.get(i).getLocation().getName()
									.toString(), ""));
		}
		locationsList.add(new OverlayItem(new GeoPoint(headComment
				.getLocation().getLatitude(), headComment.getLocation()
				.getLongitude()), headComment.getLocation().getName()
				.toString(), ""));
		return locationsList;
	}
}