package com.appz2x.wordify;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;


public class Translate extends BaseActivity implements OnNavigationListener {
	
	ProgressBar loader;
	TextView translatedText;
	EditText word;
	String lang = "ur";
	private String[] lang_code = {"ur", "ar", "bn", "zh-TW", "cs", "nl", "eo", "fr", "de", "el", "hi", "ga", "it", "ja", "ko", "mr", "ne", "fa", "ru", "es", "ta", "tr"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.translate);
		
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		// run activity slide
		runSplashSlide(R.id.sc_translate, R.id.activity_splash, new SlideListener(){
			
			@Override
			public void onSlideComplete(){
				setLangSpinner(actionBar);
			}
		
		});
		
		changeFont(R.id.heading);
		changeFont(R.id.btn_translate);
		
		loader = (ProgressBar) findViewById(R.id.translate_loader);
		translatedText = (TextView) findViewById(R.id.translated_txt);
		word = (EditText) findViewById(R.id.translate_word);
		
		word.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if(actionId == EditorInfo.IME_ACTION_DONE){
					translateWord(v);
				}
				return false;
			}
		});
		
	}
	
	private void setLangSpinner(ActionBar actionBar){
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		
		ArrayList<String> itemList = new ArrayList<String>();
		itemList.add("Urdu");
		itemList.add("Arabic");
		itemList.add("Bengali");
		itemList.add("Chinese");
		itemList.add("Czech");
		itemList.add("Dutch");
		itemList.add("Esperanto");
		itemList.add("French");
		itemList.add("German");
		itemList.add("Greek");
		itemList.add("Hindi");
		itemList.add("Irish");
		itemList.add("Italian");
		itemList.add("Japanese");
		itemList.add("Korean");
		itemList.add("Marathi");
		itemList.add("Nepali");
		itemList.add("Persian");
		itemList.add("Russian");
		itemList.add("Spanish");
		itemList.add("Tamil");
		itemList.add("Turkish");
		
		ArrayAdapter<String> aAdpt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, itemList);
		
		actionBar.setListNavigationCallbacks(aAdpt, this);
		
	}
	
	// translate word
	public void translateWord(View v){
		if(isNetConnect()){
			 new runTranslation().execute();
		}
		else{
			toast("Internet not connect.");
		}
	}
	
	// back to main menu
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		overridePendingTransition (R.anim.open_main, R.anim.close_next);
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// TODO Auto-generated method stub
		lang = lang_code[position];
		return false;
	}
	
	private class runTranslation extends AsyncTask<Void, Void, Void>{
		
		@Override
		public void onPreExecute(){
			super.onPreExecute();
			translatedText.setVisibility(View.GONE);
			loader.setVisibility(View.VISIBLE);
		}
		
		@Override
		public Void doInBackground(Void... params){
			
			String url;
			try {
				url = api_url + "func=_get_translate&word=" + URLEncoder.encode(word.getText().toString(), "UTF-8") + "&lang=" + lang;
			
				ServiceHandler sh = new ServiceHandler();
				String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
				
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
			
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			return null;
		}
		
		@Override
		public void onPostExecute(Void result){
			super.onPostExecute(result);
			loader.setVisibility(View.GONE);
			translatedText.setVisibility(View.INVISIBLE);
			
			try{
				if(jsonObject.getInt("result") == Setting.SUCCESS){
					translatedText.setText(jsonObject.getString("match"));
					translatedText.setVisibility(View.VISIBLE);
				}
				else if(jsonObject.getInt("result") == Setting.INFO){
					toast(jsonObject.getString("message"));
				}
			}catch(JSONException e){
				e.printStackTrace();
			}

		}
	}
}
