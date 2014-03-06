/*
The MyMemories source code (henceforth referred to as "MyMemories") is
copyrighted by Mike Walker, Adam Porter, Doug Schmidt, and Jules White
at Vanderbilt University and the University of Maryland, Copyright (c)
2014, all rights reserved.  Since MyMemories is open-source, freely
available software, you are free to use, modify, copy, and
distribute--perpetually and irrevocably--the source code and object code
produced from the source, as well as copy and distribute modified
versions of this software. You must, however, include this copyright
statement along with any code built using MyMemories that you release. No
copyright statement needs to be provided if you just ship binary
executables of your software products.

You can use MyMemories software in commercial and/or binary software
releases and are under no obligation to redistribute any of your source
code that is built using the software. Note, however, that you may not
misappropriate the MyMemories code, such as copyrighting it yourself or
claiming authorship of the MyMemories software code, in a way that will
prevent the software from being distributed freely using an open-source
development model. You needn't inform anyone that you're using MyMemories
software in your software, though we encourage you to let us know so we
can promote your project in our success stories.

MyMemories is provided as is with no warranties of any kind, including
the warranties of design, merchantability, and fitness for a particular
purpose, noninfringement, or arising from a course of dealing, usage or
trade practice.  Vanderbilt University and University of Maryland, their
employees, and students shall have no liability with respect to the
infringement of copyrights, trade secrets or any patents by DOC software
or any part thereof.  Moreover, in no event will Vanderbilt University,
University of Maryland, their employees, or students be liable for any
lost revenue or profits or other special, indirect and consequential
damages.

MyMemories is provided with no support and without any obligation on the
part of Vanderbilt University and University of Maryland, their
employees, or students to assist in its use, correction, modification,
or enhancement.

The names Vanderbilt University and University of Maryland may not be
used to endorse or promote products or services derived from this source
without express written permission from Vanderbilt University or
University of Maryland. This license grants no permission to call
products or services derived from the MyMemories source, nor does it
grant permission for the name Vanderbilt University or
University of Maryland to appear in their names.
 */

package com.ariesmcrae.eskwela.mymemories.ui.story;

import java.text.ParseException;
import java.util.Date;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.ariesmcrae.eskwela.mymemories.R;
import com.ariesmcrae.eskwela.mymemories.orm.EskwelaResolver;
import com.ariesmcrae.eskwela.mymemories.orm.StoryData;

/**
 * @author aries@ariesmcrae.com
 */
public class EditStoryFragment extends Fragment {

	final static public String LOG_TAG = EditStoryFragment.class.getCanonicalName();
	// variable for passing around row index
	final static public String rowIdentifyerTAG = "index";
	EditText titleET;
	EditText bodyET;
	TextView audioLinkET;
	TextView videoLinkET;
	EditText imageNameET;
	TextView imageMetaDataET;
	EditText tagsET;
	TextView storyTimeET;
	Date date;
	EditText latitudeET;
	EditText longitudeET;

	// Button(s) used
	Button saveButton;
	Button resetButton;
	Button cancelButton;

	// parent Activity
	OnOpenWindowInterface mOpener;
	// custom ContentResolver wrapper.
	EskwelaResolver eskwelaResolver;

	// listener to button presses.
	// TODO determine/label pattern.
	OnClickListener myOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.story_edit_button_save:
				doSaveButtonClick();
				break;
			case R.id.story_edit_button_reset:
				doResetButtonClick();
				break;
			case R.id.story_edit_button_cancel:
				doCancelButtonClick();
				break;
			default:
				break;
			}
		}
	};

	public static EditStoryFragment newInstance(long index) {
		EditStoryFragment f = new EditStoryFragment();
		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putLong(rowIdentifyerTAG, index);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mOpener = (OnOpenWindowInterface) activity;
			eskwelaResolver = new EskwelaResolver(activity);
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnOpenWindowListener");
		}
	}

	@Override
	public void onDetach() {
		mOpener = null;
		eskwelaResolver = null;
		super.onDetach();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Get the Buttons
		saveButton = (Button) getView().findViewById(R.id.story_edit_button_save);
		resetButton = (Button) getView().findViewById(R.id.story_edit_button_reset);
		cancelButton = (Button) getView().findViewById(R.id.story_edit_button_cancel);

		// Get the EditTexts
		titleET = (EditText) getView().findViewById(R.id.story_edit_title);
		bodyET = (EditText) getView().findViewById(R.id.story_edit_body);
		audioLinkET = (TextView) getView().findViewById(R.id.story_edit_audio_link);
		videoLinkET = (TextView) getView().findViewById(R.id.story_edit_video_link);
		imageNameET = (EditText) getView().findViewById(R.id.story_edit_image_name);
		imageMetaDataET = (TextView) getView().findViewById(R.id.story_edit_image_meta_data);
		tagsET = (EditText) getView().findViewById(R.id.story_edit_tags);
		storyTimeET = (TextView) getView().findViewById(R.id.story_edit_story_time);
		latitudeET = (EditText) getView().findViewById(R.id.story_edit_latitude);
		longitudeET = (EditText) getView().findViewById(R.id.story_edit_longitude);

		// setup the onClick callback methods
		saveButton.setOnClickListener(myOnClickListener);
		resetButton.setOnClickListener(myOnClickListener);
		cancelButton.setOnClickListener(myOnClickListener);
		// set the EditTexts to this Story's Values
		setValuesToDefault();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.story_edit_fragment, container, false);
		container.setBackgroundColor(Color.GRAY);
		return view;
	}

	public void doResetButtonClick() {
		setValuesToDefault();
	}

	public void doSaveButtonClick() {
		Toast.makeText(getActivity(), "Updated.", Toast.LENGTH_SHORT).show();
		StoryData location = makeStoryDataFromUI();
		if (location != null) {
			try {
				eskwelaResolver.updateStoryWithID(location);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		} else {
			return;
		}
		if (getResources().getBoolean(R.bool.isTablet) == true) {
			mOpener.openViewStoryFragment(getUniqueKey());
		} else {
			getActivity().finish(); // same as hitting 'back' button
		}
	}

	public StoryData makeStoryDataFromUI() {

		Editable titleEditable = titleET.getText();
		Editable bodyEditable = bodyET.getText();
		String audioLinkEditable = (String) audioLinkET.getText();
		String videoLinkEditable = (String) videoLinkET.getText();
		Editable imageNameEditable = imageNameET.getText();
		String imageMetaDataEditable = (String) imageMetaDataET.getText();
		Editable tagsEditable = tagsET.getText();
		String storyTimeEditable = (String) storyTimeET.getText();
		Editable latitudeEditable = latitudeET.getText();
		Editable longitudeEditable = longitudeET.getText();

		// Try to parse the date into long format
		try {
			date = StoryData.FORMAT.parse(storyTimeEditable.toString());
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			Log.e("CreateStoryFragment", "Date was not parsable, reverting to current time");
			date = new Date();
		}

		long loginId = 0;
		long storyId = 0;
		String title = "";
		String body = "";
		String audioLink = "";
		String videoLink = "";
		String imageName = "";
		String imageMetaData = "";
		String tags = "";
		long creationTime = 0;
		long storyTime = 0;
		double latitude = 0;
		double longitude = 0;

		// pull values from Editables
		title = String.valueOf(titleEditable.toString());
		body = String.valueOf(bodyEditable.toString());
		audioLink = String.valueOf(audioLinkEditable.toString());
		videoLink = String.valueOf(videoLinkEditable.toString());
		imageName = String.valueOf(imageNameEditable.toString());
		imageMetaData = String.valueOf(imageMetaDataEditable.toString());
		tags = String.valueOf(tagsEditable.toString());
		storyTime = date.getTime();
		latitude = Double.valueOf(latitudeEditable.toString());
		longitude = Double.valueOf(longitudeEditable.toString());

		// return new StoryData object with
		return new StoryData(getUniqueKey(), loginId, storyId, title, body, audioLink, videoLink, imageName, imageMetaData, tags,
				creationTime, storyTime, latitude, longitude);

	}

	public void doCancelButtonClick() {
		if (getResources().getBoolean(R.bool.isTablet) == true) {
			// put
			mOpener.openViewStoryFragment(getUniqueKey());
		} else {
			getActivity().finish(); // same as hitting 'back' button
		}

	}

	public boolean setValuesToDefault() {

		StoryData storyData;
		try {
			storyData = eskwelaResolver.getStoryDataViaRowID(getUniqueKey());
		} catch (RemoteException e) {
			Log.d(LOG_TAG, "" + e.getMessage());
			e.printStackTrace();
			return false;
		}

		if (storyData != null) {
			Log.d(LOG_TAG, "setValuesToDefualt :" + storyData.toString());
			// set the EditTexts to the current values
			titleET.setText(String.valueOf(storyData.title).toString());
			bodyET.setText(String.valueOf(storyData.body).toString());
			audioLinkET.setText("file:///" + String.valueOf(storyData.audioLink).toString());
			videoLinkET.setText(String.valueOf(storyData.videoLink).toString());
			imageNameET.setText(String.valueOf(storyData.imageName).toString());
			imageMetaDataET.setText(String.valueOf(storyData.imageLink).toString());
			tagsET.setText(String.valueOf(storyData.tags).toString());
			storyTimeET.setText(StoryData.FORMAT.format(storyData.storyTime));
			latitudeET.setText(Double.valueOf(storyData.latitude).toString());
			longitudeET.setText(Double.valueOf(storyData.longitude).toString());
			return true;
		}
		return false;
	}

	public long getUniqueKey() {
		return getArguments().getLong(rowIdentifyerTAG, 0);
	}

}
