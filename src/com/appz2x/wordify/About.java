package com.appz2x.wordify;

import android.os.Bundle;
import android.support.v7.app.ActionBar;


public class About extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		// run activity slide
		runSplashSlide(R.id.sc_about, R.id.activity_splash, new SlideListener(){
			
			@Override
			public void onSlideComplete(){
				
			}
		
		});
				
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		changeFont(R.id.heading);
		changeFont(R.id.about_content);
	}
	
	// back to main menu
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		overridePendingTransition (R.anim.open_main, R.anim.close_next);
	}
}
