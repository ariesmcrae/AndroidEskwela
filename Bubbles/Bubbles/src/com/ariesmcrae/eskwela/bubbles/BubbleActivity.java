/**
The MIT License (MIT)

Copyright (c) 2014 Aries McRae

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
**/
package com.ariesmcrae.eskwela.bubbles;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
//import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import com.ariesmcrae.eskwela.bubbles.R;

/** @author aries@ariesmcrae.com */
public class BubbleActivity extends Activity {

	// These variables are for testing purposes, do not modify
	private final static int RANDOM = 0;
	private final static int SINGLE = 1;
	private final static int STILL = 2;
	private static int speedMode = RANDOM;

	private static final int MENU_STILL = Menu.FIRST;
	private static final int MENU_SINGLE_SPEED = Menu.FIRST + 1;
	private static final int MENU_RANDOM_SPEED = Menu.FIRST + 2;

//	private static final String TAG = "AndroidEskwela-Bubbles";

	// Main view
	private RelativeLayout mFrame;

	// Bubble image
	private Bitmap mBitmap;

	// Display dimensions
	private int mDisplayWidth, mDisplayHeight;

	/********* Sound variables *************************************/

	// AudioManager
	private AudioManager mAudioManager;

	// SoundPool
	private SoundPool mSoundPool;
	
	// ID for the bubble popping sound
	private int mSoundID;
	
	// Audio volume
	private float mStreamVolume;

	/***********End sound ********************************************/
	 
	// Gesture Detector
	private GestureDetector mGestureDetector;

	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		// Set up user interface
		mFrame = (RelativeLayout) findViewById(R.id.frame);

		// Load basic bubble Bitmap
		mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.b64);
	}

	
	

	/** State where the UI becomes visible and the user can start interacting.*/
	@Override
	protected void onResume() {
		super.onResume();

		// Manage bubble popping sound. Use AudioManager.STREAM_MUSIC as stream type
		mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

		mStreamVolume = (float) mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC); //divide by is the forward.

		mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0); //0 is currently not implemented by Google.

		mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				if (status == 0) {
					setupGestureDetector();
				}
			}
		});
		
		mSoundID = mSoundPool.load(this, R.raw.bubble_pop, 1); //load the sound from res/raw/bubble_pop.wav		
	}

	
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	
		if (hasFocus) {
			// Get the size of the display so this view knows where borders are
			mDisplayWidth = mFrame.getWidth();
			mDisplayHeight = mFrame.getHeight();
		}
	}

	
	
	// Set up GestureDetector
	private void setupGestureDetector() {
		mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
			@Override // If a fling gesture starts on a BubbleView then change the BubbleView's velocity
			public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
				for (int i = 0; i < mFrame.getChildCount(); i++) {
					BubbleView currentBubbleView = (BubbleView) mFrame.getChildAt(i);	
					
					//getX = co-ordinates relative to the View
					//getRawX = co-ordinates relative to the screen device.
					boolean intersects = currentBubbleView.intersects(event1.getX(), event1.getY());		
					
					if (intersects) {
						currentBubbleView.deflect(velocityX, velocityY);
						break;
					}
				}
				
				return false;
			}

			
			// If a single tap intersects a BubbleView, then pop the BubbleView
			// Otherwise, create a new BubbleView at the tap's location and add it to mFrame.
			@Override
			public boolean onSingleTapConfirmed(MotionEvent event) {
				boolean bubbleWasTouched = false;
				
				for (int i = 0; i < mFrame.getChildCount(); i++) {
					BubbleView currentBubbleView = (BubbleView) mFrame.getChildAt(i);
		
					bubbleWasTouched = currentBubbleView.intersects(event.getX(), event.getY());
					
					if (bubbleWasTouched) {
						currentBubbleView.stop(true);
						break;
					}
				} //for
				
				if (!bubbleWasTouched) {
					BubbleView newBubbleView = new BubbleView(mFrame.getContext(), event.getX(), event.getY());
					newBubbleView.start();						
					mFrame.addView(newBubbleView);
				}
				
				return bubbleWasTouched;
			}
		});
	}

	
	
	/** Triggered when view is touched. */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//True if motion even has been consumed.
		return mGestureDetector.onTouchEvent(event);
	}

	
	
	@Override
	protected void onPause() {
		if (mSoundPool != null) {
			mSoundPool.unload(mSoundID);
			mSoundPool.release();
			mSoundPool = null;
		}
	
		super.onPause();
	}

	
	
	// BubbleView is a View that displays a bubble.
	// This class handles animating, drawing, popping amongst other actions.
	// A new BubbleView is created for each bubble on the display
	private class BubbleView extends View {
		private static final int BITMAP_SIZE = 64;
		private static final int REFRESH_RATE = 40;
		private final Paint mPainter = new Paint();
		private ScheduledFuture<?> mMoverFuture;
		private int mScaledBitmapWidth; //width of bubble
		private Bitmap mScaledBitmap; // bubble image

		// location, speed and direction of the bubble
		private float mXPos, mYPos, mDx, mDy;
		private long mRotate, mDRotate;

		public BubbleView(Context context, float x, float y) {
			super(context);
//			log("Creating Bubble at: x:" + x + " y:" + y);

			// Create a new random number generator to randomize size, rotation, speed and direction
			Random r = new Random();

			// Creates the bubble bitmap for this BubbleView
			createScaledBitmap(r);

			// Adjust position to center the bubble under user's finger
			mXPos = x - (mScaledBitmapWidth / 2);
			mYPos = y - (mScaledBitmapWidth / 2);

			// Set the BubbleView's speed and direction
			setSpeedAndDirection(r);

			// Set the BubbleView's rotation
			setRotation(r);

			mPainter.setAntiAlias(true);
		}

		
		
		private void setRotation(Random r) {
			if (speedMode == RANDOM) {
				// Generates a random number between 1 to 3. Plus one because nextInt(3) will generate a random number between 0 and 3. We only want between 1 to 3.
				mDRotate = r.nextInt(3) + 1; 
			} else {
				mDRotate = 0;
			}
		}

		
		// Used by test cases		
		private void setSpeedAndDirection(Random r) {
			switch (speedMode) {
				case SINGLE:
					// Fixed speed
					mDx = 10;
					mDy = 10;
					break;
				case STILL:
					// No speed
					mDx = 0;
					mDy = 0;
					break;
				default:
					// Limit movement speed in the x and y direction to [-3..3].
					int LOW = -3;
					int HIGH = 4;
					int randomNoBetweenMinusThreeAndThreeXAxis = r.nextInt(HIGH - LOW) + LOW;
					int randomNoBetweenMinusThreeAndThreeYAxis = r.nextInt(HIGH - LOW) + LOW;
					mDx = randomNoBetweenMinusThreeAndThreeXAxis;
					mDy = randomNoBetweenMinusThreeAndThreeYAxis;
			}
		}

		
		
		private void createScaledBitmap(Random r) {
			if (speedMode != RANDOM) {
				mScaledBitmapWidth = BITMAP_SIZE * 3;
			} else {
				// Set scaled bitmap size in range [1..3] * BITMAP_SIZE
				int randomNumberBetweenOneAndThree = r.nextInt(3) +1; //Plus one because nextInt(3) will generate a random number between 0 and 3. We want between 1 to 3.
				mScaledBitmapWidth = BITMAP_SIZE * randomNumberBetweenOneAndThree;
			}

			mScaledBitmap = Bitmap.createScaledBitmap(mBitmap, mScaledBitmapWidth, mScaledBitmapWidth, false); //WTF? true or false?
		}

		
		
		/** Start moving the BubbleView & updating the display **/
		private void start() {
			// Creates a WorkerThread
			ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

			// Execute the run() in Worker Thread every REFRESH_RATE // milliseconds
			// Save reference to this job in mMoverFuture
			mMoverFuture = executor.scheduleWithFixedDelay(new Runnable() {
				@Override
				public void run() {
					// Each time this method is run the BubbleView should move one step. 
					// If the BubbleView exits the display, stop the BubbleView's Worker Thread. Otherwise, request that the BubbleView be redrawn.
					if (moveWhileOnScreen()) { //move returns true if bubble is still visible on the screen. If visible, then postInvalidate will call Bubble.onDraw
						stop(false);						
					} else {
						postInvalidate(); // postInvalidate will call Bubble.onDraw
					}
				}//run
			}, 0, REFRESH_RATE, TimeUnit.MILLISECONDS);
		}

		
		
		private synchronized boolean intersects(float x, float y) {
			// Apply pythegorean theorem.
			int radiusOfBubble = mScaledBitmapWidth / 2;
			double xOffSet = (mXPos + radiusOfBubble) - x;
			double yOffset = (mYPos + radiusOfBubble) - y;
			double distanceOfTouch = Math.sqrt((xOffSet * xOffSet) + (yOffset * yOffset));
			
			return distanceOfTouch <= radiusOfBubble; //true if the BubbleView intersects position (x,y)
		}


		
		// Cancel the Bubble's movement
		// Remove Bubble from mFrame
		// Play pop sound if the BubbleView was popped
		private void stop(final boolean popped) {
			if (mMoverFuture != null  && mMoverFuture.cancel(true)) {
				// This work will be performed on the UI Thread
				mFrame.post(new Runnable() {
					@Override
					public void run() {
						mFrame.removeView(BubbleView.this);						

						if (popped) {
//							log("Pop!");
							mSoundPool.play(mSoundID, mStreamVolume, mStreamVolume, 0, 0, 1);		
						}

//						log("Bubble removed from view!");
					}
				});
			}
		}


		
		// Change the Bubble's speed and direction
		private synchronized void deflect(float velocityX, float velocityY) {
//			log("velocity X:" + velocityX + " velocity Y:" + velocityY);

			mDx = velocityX / REFRESH_RATE;
			mDy = velocityY / REFRESH_RATE;			
		}

		
		
		
		@Override // Draw the Bubble at its current location
		protected synchronized void onDraw(Canvas canvas) {
			canvas.save();

			// Increase the rotation of the original image by mDRotate
			mRotate += mDRotate;

			// Rotate the canvas by current rotation
			canvas.rotate(mRotate, mXPos + (mScaledBitmapWidth / 2), mYPos + (mScaledBitmapWidth / 2));

			// Draw the bitmap at it's new location
			canvas.drawBitmap(mScaledBitmap, mXPos, mYPos, mPainter);

			canvas.restore();
		}

		
		
		private synchronized boolean moveWhileOnScreen() {
			// Move the BubbleView 
			mXPos = mXPos + mDx;
			mYPos = mYPos + mDy;

			return isOutOfView();
		}

		
		
		private boolean isOutOfView() {
			// Return true if the BubbleView has exited the screen
			boolean isOutOfView = false;
			
			if (mXPos < (0 - mScaledBitmapWidth)
					|| mXPos > (mDisplayHeight + mScaledBitmapWidth)
					|| mYPos < (0 - mScaledBitmapWidth)
					|| mYPos > (mDisplayWidth + mScaledBitmapWidth)) {
				isOutOfView = true;
			}
			
			return isOutOfView;
		}//end isOutOfView
		
		
		
	} //End BubbleView Class

	
	
	// Do not modify below here
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(Menu.NONE, MENU_STILL, Menu.NONE, "Still Mode");
		menu.add(Menu.NONE, MENU_SINGLE_SPEED, Menu.NONE, "Single Speed Mode");
		menu.add(Menu.NONE, MENU_RANDOM_SPEED, Menu.NONE, "Random Speed Mode");

		return true;
	}

	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_STILL:
			speedMode = STILL;
			return true;
		case MENU_SINGLE_SPEED:
			speedMode = SINGLE;
			return true;
		case MENU_RANDOM_SPEED:
			speedMode = RANDOM;
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	
	
//	private static void log(String message) {
//		try {
//			Thread.sleep(500);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		Log.i(TAG, message);
//	}
}