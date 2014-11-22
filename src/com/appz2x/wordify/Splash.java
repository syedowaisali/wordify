package com.appz2x.wordify;

import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class Splash extends ActionBarActivity {
	
	final Splash sSplashScreen = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		handler.postDelayed(runnable, Setting.SPLASH_TIMEOUT);	
	}

	final Handler handler = new Handler();
	final Runnable runnable = new Runnable()
	{
		public void run()
		{	
			startMainActivity();			
		}
	};	
	
	/**
     * Processes splash screen touch events
     */
	@Override
	public boolean onTouchEvent(MotionEvent evt)
	{
		if(evt.getAction() == MotionEvent.ACTION_DOWN)
		{
			handler.removeCallbacks(runnable);
			startMainActivity();			
		}
		return true;
	}
	
	public void startMainActivity()
	{
		finish();
		
		// Run next activity
		Intent intent = new Intent();
		intent.setClass(sSplashScreen, MainActivity.class);
		startActivity(intent);
	}

}
