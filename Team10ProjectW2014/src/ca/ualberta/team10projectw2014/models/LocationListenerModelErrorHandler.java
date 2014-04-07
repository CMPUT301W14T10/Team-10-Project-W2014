package ca.ualberta.team10projectw2014.models;


import android.content.Context;
import android.app.AlertDialog;
import ca.ualberta.team10projectw2014.R;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

/**
 * @author       Bradley Poulette <bpoulett@ualberta.ca>
 * @version      1                (current version number of program)  This class is used to deal with error handling of the location listener
 */
public class LocationListenerModelErrorHandler {
	private Context context;

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * Tells the user to turn on the GPS  Called in  {@link #LocationListenerController(Context)}   This is a direct copy from  http://stackoverflow.com/a/843716/2557554 accessed on March 9 at 2:00PM
	 */
	public void noGPSError() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(R.string.gps_appears_disabled)
				.setCancelable(false)
				.setPositiveButton(R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int id) {
								context.startActivity(new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
							}
						})
				.setNegativeButton(R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int id) {
								dialog.cancel();
							}
						});
		final AlertDialog alert = builder.create();
		alert.show();
	}
}