package com.appz2x.wordify;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ToggleButton;


public class AppSetting extends BaseActivity {

	AlertDialog loginDialog;
	
	Editable email;
	Editable pass;
	CharSequence[] animList = {"Accelerate Decelerate", "Accelerate", "Anticipate", "Anticipate Overshoot", "Bounce", "Decelerate", "Random"};
	int selectedAnim = 4;
	TextView curAnim;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		// run activity slide
		runSplashSlide(R.id.sc_setting, R.id.activity_splash, new SlideListener(){
			
			@Override
			public void onSlideComplete(){
				
			}
		
		});
				
		// change setting row fonts
		changeFont(R.id.heading);
		changeFont(R.id.set_admin_enable_txt);
		changeFont(R.id.set_notification_txt);
		changeFont(R.id.set_menu_anim_txt);
		changeFont(R.id.toggle_admin_enable_btn);
		changeFont(R.id.toggle_test_switch_btn);
		changeFont(R.id.toggle_notification_btn);
		changeFont(R.id.set_menu_anim_btn);
		
		// check token is exists in shared preference
		if(sp.contain(AppHelper.ADMIN_AUTH_TOKEN)){
			
			// set admin access toggle button to on
			// get toggle button
			ToggleButton btn = (ToggleButton) findViewById(R.id.toggle_admin_enable_btn);
			btn.setChecked(true);
		}
		
		// check is online test
		if(sp.contain(AppHelper.TEST_OPTION)){
			// set test to online
			ToggleButton btn = (ToggleButton) findViewById(R.id.toggle_test_switch_btn);
			btn.setChecked(true);
		}
		
		// check if tile anim is exist in shared preference
		if(sp.contain(AppHelper.TILE_ANIM)){
			selectedAnim = sp.getInt(AppHelper.TILE_ANIM);
		}
		
		// set current animation name
		curAnim = (TextView) findViewById(R.id.set_menu_anim_txt);
		curAnim.setText(animList[selectedAnim]);
	}
	
	// back to main menu
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		overridePendingTransition (R.anim.open_main, R.anim.close_next);
	}
	
	// toggle admin access
	public void toggleAdminAccess(View v){
		
		// is the toggle button on
		final ToggleButton btn = (ToggleButton) v;
		
		if(btn.isChecked()){
			
			View view = getLayoutInflater().inflate(R.layout.admin_login, null);
			
			final EditText emailET = (EditText) view.findViewById(R.id.admin_login_email_et);
			final EditText passET  = (EditText) view.findViewById(R.id.admin_login_pass_et);
			
			email = emailET.getText();
			pass  = passET.getText();
			
			Builder builder = new AlertDialog.Builder(this);
			
			// set title
			builder.setTitle("Login")
			
			// set view
			.setView(view)
			
			//set positive button
			.setPositiveButton("Login", null)
			
			//set negative button
			.setNegativeButton("Cancel", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					btn.setChecked(false);
				}
			});
			
			loginDialog = builder.create();
			loginDialog.show();
			
			loginDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					// check internet connection
					if(cd.isConnectingToInternet()){
						if( ! AppHelper.isValidEmail(email.toString())){
							toast("Email not valid format.");
						}
						else if(pass.toString().equals("")){
							toast("Password field can't be blank.");
						}
						else{
							
							// hide soft keyboard
							InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							mgr.hideSoftInputFromWindow(passET.getWindowToken(), 0);
							
							String url = api_url + "func=_get_auth_token&email=" + email.toString() + "&pass=" + pass.toString(); 
							runUrlTask(url, "Authenticating...", new ResultCallback(){
								
								@Override
								public void apiResultCallback(JSONObject jsonObj){
									
									try {
									
										if(jsonObj.getInt("result") == Setting.SUCCESS){
											try {
												setToken(jsonObj.getString("token"), jsonObj.getString("message"));
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
										else if(jsonObj.getInt("result") == Setting.INFO){
											authFailed(jsonObj.getString("message"));
										}
									} catch (JSONException e) {
										e.printStackTrace();
									}
								};
							});
						}
					}
					else{
						toast("Internet not connected.");
					}
				}
			});
			
			passET.setOnEditorActionListener( new OnEditorActionListener() {
				
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					// TODO Auto-generated method stub
					
					if(actionId == EditorInfo.IME_ACTION_DONE){
						loginDialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
					}
					
					return false;
				}
			});
		}
		else{
			
			// check token is exists in shared preference
			if(SharedPrefs.getInstance().contain(AppHelper.ADMIN_AUTH_TOKEN)){
				
				// remove token in shared preference
				SharedPrefs.getInstance().remove(AppHelper.ADMIN_AUTH_TOKEN);
				
				// show toast message
				toast("Authentication removed.");
			}
		}
	}
	
	// toggle test option
	public void toggleTest(View v ){
		boolean on = ((ToggleButton) v).isChecked();
		
		if(on){
			sp.add(AppHelper.TEST_OPTION, "on");
			//toast("Online test on");
		}
		else{
			if(sp.contain(AppHelper.TEST_OPTION)){
				sp.remove(AppHelper.TEST_OPTION);
				//toast("Offline test on");
			}
		}
	}
	
	// toggle notification
	public void toggleNotification(View v){
		boolean on = ((ToggleButton) v).isChecked();
		
		if(on){
			sp.add(AppHelper.NOTIFICATION_OPTION, "on");
			toast("Notification on");
		}
		else{
			if(sp.contain(AppHelper.NOTIFICATION_OPTION)){
				sp.remove(AppHelper.NOTIFICATION_OPTION);
				toast("Notification off");
			}
		}
	}
	
	// set menu animation
	public void setMenuAnim(View v){
		
		new AlertDialog.Builder(this)
		
		// set title
		.setTitle("Selection Animation")
		
		// set items
		.setSingleChoiceItems(animList, selectedAnim, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				selectedAnim = which;
				sp.add(AppHelper.TILE_ANIM, selectedAnim);
				curAnim.setText(animList[selectedAnim]);
				dialog.dismiss();
			}
		})
				
		// show
		.show();
	}
	
	// set token to preference
	private void setToken(String token, String message){
		
		// save token to shared preferences
		sp.add(AppHelper.ADMIN_AUTH_TOKEN, token);
		
		// hide login dialog
		loginDialog.dismiss();
		
		// show toast message
		toast(message);
	}
	
	// authentication failed
	private void authFailed(String message){
		toast(message);
	}
}
