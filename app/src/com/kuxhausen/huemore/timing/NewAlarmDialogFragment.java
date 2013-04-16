package com.kuxhausen.huemore.timing;

import com.kuxhausen.huemore.*;
import com.kuxhausen.huemore.persistence.DatabaseDefinitions;
import com.kuxhausen.huemore.persistence.DatabaseDefinitions.GroupColumns;
import com.kuxhausen.huemore.persistence.DatabaseDefinitions.MoodColumns;

import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.database.Cursor;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

public class NewAlarmDialogFragment extends DialogFragment implements
OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

	// Identifies a particular Loader being used in this component
	private static final int GROUPS_LOADER = 0, MOODS_LOADER = 1;

	SeekBar brightnessBar;
	Spinner groupSpinner, moodSpinner;
	SimpleCursorAdapter groupDataSource, moodDataSource;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		View myView = inflater.inflate(R.layout.edit_alarm_dialog, container, false);
		Bundle args = getArguments();

		//this.getDialog().setTitle("New Alarm");
		this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		Button cancelButton = (Button) myView.findViewById(R.id.cancel);
		cancelButton.setOnClickListener(this);
		Button okayButton = (Button) myView.findViewById(R.id.okay);
		okayButton.setOnClickListener(this);
		
		
		// We need to use a different list item layout for devices older than
				// Honeycomb
				int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? android.R.layout.simple_list_item_activated_1
						: android.R.layout.simple_list_item_1;
		
		/*
		 * Initializes the CursorLoader. The GROUPS_LOADER value is eventually
		 * passed to onCreateLoader().
		 */
		LoaderManager lm = getActivity().getSupportLoaderManager();
		lm.initLoader(GROUPS_LOADER, null, this);
		lm.initLoader(MOODS_LOADER, null, this);
		

		brightnessBar = (SeekBar) myView.findViewById(R.id.brightnessBar);
		
		groupSpinner = (Spinner) myView.findViewById(R.id.groupSpinner);
		String[] gColumns = { GroupColumns.GROUP, BaseColumns._ID };
		groupDataSource = new SimpleCursorAdapter(getActivity(), layout, null,
				gColumns, new int[] { android.R.id.text1 }, 0);
		groupDataSource.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		groupSpinner.setAdapter(groupDataSource);
		
		
		moodSpinner = (Spinner) myView.findViewById(R.id.moodSpinner);
		String[] mColumns = { MoodColumns.MOOD, BaseColumns._ID };
		moodDataSource = new SimpleCursorAdapter(getActivity(), layout, null,
				mColumns, new int[] { android.R.id.text1 }, 0);
		moodDataSource.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		moodSpinner.setAdapter(moodDataSource);
		
		
		
		
		return myView;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.okay:
			new AlarmReciever(getActivity(),null, 15);
			
			this.dismiss();
			break;
		case R.id.cancel:
			this.dismiss();
			break;
		}
	}

	
	/**
	 * Callback that's invoked when the system has initialized the Loader and is
	 * ready to start the query. This usually happens when initLoader() is
	 * called. The loaderID argument contains the ID value passed to the
	 * initLoader() call.
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int loaderID, Bundle arg1) {
		/*
		 * Takes action based on the ID of the Loader that's being created
		 */
		switch (loaderID) {
		case GROUPS_LOADER:
			// Returns a new CursorLoader
			String[] gColumns = { GroupColumns.GROUP, BaseColumns._ID };
			return new CursorLoader(getActivity(), // Parent activity context
					DatabaseDefinitions.GroupColumns.GROUPS_URI, // Table
					gColumns, // Projection to return
					null, // No selection clause
					null, // No selection arguments
					null // Default sort order
			);
		case MOODS_LOADER:
			// Returns a new CursorLoader
			String[] mColumns = { MoodColumns.MOOD, BaseColumns._ID };
			return new CursorLoader(getActivity(), // Parent activity context
					DatabaseDefinitions.MoodColumns.MOODS_URI, // Table
					mColumns, // Projection to return
					null, // No selection clause
					null, // No selection arguments
					null // Default sort order
			); 
		default:
			// An invalid id was passed in
			return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		/*
		 * Moves the query results into the adapter, causing the ListView
		 * fronting this adapter to re-display
		 */
		switch(loader.getId()){
			case GROUPS_LOADER: groupDataSource.changeCursor(cursor); break;
			case MOODS_LOADER: moodDataSource.changeCursor(cursor); break;
		}
		
		//registerForContextMenu(getListView());
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		/*
		 * Clears out the adapter's reference to the Cursor. This prevents
		 * memory leaks.
		 */
		// unregisterForContextMenu(getListView());
		switch(loader.getId()){
			case GROUPS_LOADER: groupDataSource.changeCursor(null); break;
			case MOODS_LOADER: moodDataSource.changeCursor(null); break;
		}
	}

}
