package com.appz2x.wordify;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;


public class ContactUs extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_us);
		
		// run activity slide
		runSplashSlide(R.id.sc_contact, R.id.activity_splash, new SlideListener(){
			
			@Override
			public void onSlideComplete(){
				
			}
		
		});
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		changeFont(R.id.heading);
		

	}
	
	// back to main menu
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		overridePendingTransition (R.anim.open_main, R.anim.close_next);
	}
	
	public void callNow(View v){
		Intent dialIntent = new Intent("android.intent.action.DIAL");
		dialIntent.setData(Uri.parse("tel:" + Setting.PHONE_NUMBER));
		startActivity(dialIntent);
	}
}
