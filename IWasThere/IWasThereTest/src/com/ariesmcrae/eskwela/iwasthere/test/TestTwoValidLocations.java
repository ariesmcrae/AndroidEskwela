package com.ariesmcrae.eskwela.iwasthere.test;

import android.test.ActivityInstrumentationTestCase2;

import com.ariesmcrae.eskwela.iwasthere.PlaceViewActivity;
import com.robotium.solo.Solo;


public class TestTwoValidLocations extends
		ActivityInstrumentationTestCase2<PlaceViewActivity> {
	private Solo solo;

	public TestTwoValidLocations() {
		super(PlaceViewActivity.class);
	}

	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation());
		getActivity();
	}

	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}

	public void testRun() {
		// Wait for activity: 'com.ariesmcrae.eskwela.iwasthere.PlaceViewActivity'
		solo.waitForActivity(com.ariesmcrae.eskwela.iwasthere.PlaceViewActivity.class,
				4000);

		solo.sleep(2000);

		// Click on action bar item
		solo.clickOnActionBarItem(com.ariesmcrae.eskwela.iwasthere.R.id.delete_badges);

		solo.sleep(2000);

		// Click on action bar item
		solo.clickOnActionBarItem(com.ariesmcrae.eskwela.iwasthere.R.id.place_one);

		solo.sleep(2000);

		// Click on Get New Place
		solo.clickOnView(solo.getView(com.ariesmcrae.eskwela.iwasthere.R.id.footer));

		solo.sleep(2000);

		// Click on action bar item
		solo.clickOnActionBarItem(com.ariesmcrae.eskwela.iwasthere.R.id.place_two);

		solo.sleep(2000);

		// Click on Get New Place
		solo.clickOnView(solo.getView(com.ariesmcrae.eskwela.iwasthere.R.id.footer));

		solo.sleep(5000);

		// Click on action bar item
		solo.clickOnActionBarItem(com.ariesmcrae.eskwela.iwasthere.R.id.print_badges);

	}
}
