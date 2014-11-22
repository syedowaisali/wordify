package com.appz2x.wordify;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AddNewWord extends BaseActivity {
	
	EditText word;
	EditText meaning;
	TextView statusTv;
	private boolean is_online_add = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_word);
	
		// get action bar
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		// run activity slide
		runSplashSlide(R.id.sc_add_new_word, R.id.activity_splash, new SlideListener(){
			
			@Override
			public void onSlideComplete(){
				
			}
		
		});
		
		// change fonts
		changeFont(R.id.heading);
		changeFont(R.id.btn_add_new_word);
		changeFont(R.id.dic_add_status);
		
		word = (EditText) findViewById(R.id.txt_add_new_word);
		meaning = (EditText) findViewById(R.id.txt_add_new_meaning);
		statusTv = (TextView) findViewById(R.id.dic_add_status);
		
		if(sp.contain(AppHelper.ADMIN_AUTH_TOKEN)){
			statusTv.setText("Online Dictionary");
			is_online_add = true;
		}
		else{
			statusTv.setText("My Dictionary");
			is_online_add = false;
		}
	}

	// add new word
	public void addNewWord(View v){
		
		// check blank word field
		if( ! word.getText().toString().equals("")){
			
			// check blank meaning field
			if( ! meaning.getText().toString().equals("")){
				
				// check is online add
				if(is_online_add){
					
					// check is internet connect
					if(isNetConnect()){
						
						String auth_key = sp.getString(AppHelper.ADMIN_AUTH_TOKEN);
						String url = api_url + "func=_add_new_wm&dic_word=" + word.getText().toString() + "&dic_meaning=" + meaning.getText().toString() + "&auth_key=" + auth_key;
						
						runUrlTask(url, "Please Wait...", new ResultCallback() {
							
							@Override
							public void apiResultCallback(JSONObject jsonObj) {
								// TODO Auto-generated method stub
								try{
									toast(jsonObj.getString("message"));
									
									// reset word and meaning text;
									word.setText("");
									meaning.setText("");
									
								}catch(JSONException e){
									e.printStackTrace();
								}
							}
						});
					}
					
					// net connection failed
					else{
						toast("Internet not connected.");
					}
				}
				
				// or my dictionary
				else{
					if( ! dd.isExistsWord(word.getText().toString())){
						DictionaryItem item = new DictionaryItem();
						item.setWord(word.getText().toString());
						item.setRowMeaning(meaning.getText().toString());
						
						// add word and meaning to my dictionary
						dd.addNewItem(item);
						
						toast("Add new word & meaning successfully");
						
						// reset word and meaning text;
						word.setText("");
						meaning.setText("");
					}
					else{
						toast("Add another word this word already exists. ");
					}
				}
			}
			else{
				toast("Meaning field can't be blank.");
			}
		}
		else{
			toast("Word field can't be blank.");
		}
		
	}

	// back to main menu
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		overridePendingTransition (R.anim.open_main, R.anim.close_next);
	}
}
