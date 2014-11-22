package com.appz2x.wordify;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

interface ResultCallback{
	void apiResultCallback(JSONObject jsonObj);
}

interface SlideListener{
	void onSlideComplete();
}

public abstract class BaseActivity extends ActionBarActivity{
	
	SharedPrefs sp = SharedPrefs.getInstance();
	ConnectionDetector cd;
	DictionaryData dd;
	ProgressDialog pDialog;
	ServiceHandler sh;
	public String progressMessage;
	JSONObject jsonObject;
	String api_url = Setting.WEB_URL + "?token=" + Setting.TOKEN + "&";
	ResultCallback callback;
	public final int ACTIVITY_SPLASH_DELAY = 2000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		sp.init(this);
		cd = new ConnectionDetector(this);
		dd = new DictionaryData(this);
		// creating service handler class instance
		sh = new ServiceHandler();
	}
	
	public void changeFont(int res){
		TextView tv = (TextView)findViewById(res);
		Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/oswald_regular.otf");
		tv.setTypeface(typeFace);
	}
	
	
	public void toast(String message){
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
	
	public void toast(int message){
		toast(String.valueOf(message));
	}
	
	public void toast(float message){
		toast(String.valueOf(message));
	}
	
	public void toast(String message, int length){
		Toast.makeText(this, message, length).show();
	}
	
	public void toast(int message, int length){
		toast(String.valueOf(message), length);
	}
	
	public void toast(float message, int length){
		toast(String.valueOf(message), length);
	}
	
	public void runUrlTask(String url, String message, ResultCallback callback){
		progressMessage = message;
		this.callback = callback;
		new runUrlAsyncTask().execute(url, url);
		
	}
	
	public void gotoActivity(Class<?> act){
		Intent intent = new Intent(this, act);
		startActivity(intent);
		overridePendingTransition (R.anim.open_next, R.anim.close_main);
	}
	
	// check internet connection
	public boolean isNetConnect(){
		return cd.isConnectingToInternet();
	}
	
	// run activity splash slide
	public void runSplashSlide(final int res, final int splash_res, final SlideListener callback){

		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				final LinearLayout layout = (LinearLayout) BaseActivity.this.findViewById(res);
				final int layout_width = layout.getMeasuredWidth();
				layout.setLayoutParams(new LinearLayout.LayoutParams(layout_width * 2, LayoutParams.MATCH_PARENT));
				
				final ImageView splash = (ImageView) BaseActivity.this.findViewById(splash_res);
				splash.setLayoutParams(new LinearLayout.LayoutParams(layout_width, LayoutParams.MATCH_PARENT));
				
				Animation anim = AnimationUtils.loadAnimation(BaseActivity.this, R.anim.activity_splash_slide);
				anim.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						TranslateAnimation anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
		                anim.setDuration(1);
		                layout.startAnimation(anim);
		                splash.setVisibility(View.GONE);
		                
		                layout.setLayoutParams(new LinearLayout.LayoutParams(layout_width, LayoutParams.MATCH_PARENT));
		                LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) layout.getLayoutParams();
		                param.setMargins(0, 0, 0, 0);
		                layout.setLayoutParams(param);
		                
		                callback.onSlideComplete();
					}
				});
				
				// start animation
				layout.startAnimation(anim);
			}
		}, ACTIVITY_SPLASH_DELAY);
	}

	// AsyncTask
	private class runUrlAsyncTask extends AsyncTask<String, Void, Void>{
		
		@Override
		protected void onPreExecute(){
			pDialog = new ProgressDialog(BaseActivity.this);
			pDialog.setMessage(progressMessage);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String url = params[0];
			// set url
			//String url = Setting.WEB_URL + "?func=_get_auth_token&email=" + email.toString() + "&pass=" + pass.toString() + "&token=" + Setting.TOKEN;
			url = url.replace("@", "%40").replace(" ", "%20");
			
			Log.d("url", url);

			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
			
			Log.d("Response: > ", jsonStr);
			
			if(jsonStr != null){
				try{
					
					jsonObject = new JSONObject(jsonStr);
					
				}catch(JSONException e){
					e.printStackTrace();
				}
			}
			else{
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result){
			super.onPostExecute(result);
			
			if(pDialog.isShowing())
				pDialog.dismiss();
			
			callback.apiResultCallback(jsonObject);
			
			/*if(return_status == Setting.SUCCESS){
				try {
					setToken(jsonObject.getString("token"), return_message);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(return_status == Setting.INFO){
				authFailed(return_message);
			}*/
		}
	}
}
