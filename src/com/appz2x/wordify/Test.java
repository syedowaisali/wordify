package com.appz2x.wordify;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class Test extends BaseActivity implements OnTouchListener {
	
	private static final char OFFLINE = 0;
	private static final char ONLINE  = 1;
	private static final int INTERVAL = 1000;
	private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	GestureDetector gestureDetector;
	private TextView testStatus, testTime, testWord, ansWrong, ansRight;
	private EditText testMeaning;
	private ProgressBar wordLoader;
	private Button btnStartStop;
	private LinearLayout qbLayout;
	private boolean is_test_start = false;
	private boolean is_process = false;
	private boolean match = false;
	private int time = 0;
	private int time_limit = 3600;		// 1 hour
	private int minute = time_limit / 60;
	private int second = 59;
	private int tick = 1;
	private int qbl_width;
	private int ans_right = 0;
	private int ans_wrong = 0;
	private int limit_skip_q = 10;
	private int skip_q = 0;
	private int count_q = 0;
	private JSONObject jObj;
	private String return_message;
	private int return_status;
	private char test_cat;
	private String url = api_url + "func=_get_rand_word";
	private ArrayList<DictionaryItem> used_questions;
	private DictionaryItem nextQ;
	private Animation q_skip_anim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		// initialize
		init();
	}
	
	// init
	private void init(){
		gestureDetector = new GestureDetector(this, new MyGestureDetector());
		q_skip_anim = AnimationUtils.loadAnimation(this, R.anim.q_skip);
		q_skip_anim.setInterpolator(new DecelerateInterpolator());
		q_skip_anim.setAnimationListener(new AnimationListener() {
			
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
		        qbLayout.startAnimation(anim);
		        
		        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) qbLayout.getLayoutParams();
		        params.setMargins(-qbl_width, 0, 0, 0);
		        qbLayout.setLayoutParams(params);
		        
		        new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						getQ();
					}
				}, INTERVAL / 2);
			}
		});
		
		// run activity slide
		runSplashSlide(R.id.sc_test, R.id.activity_splash, new SlideListener(){
			
			@Override
			public void onSlideComplete(){
				qbl_width = qbLayout.getMeasuredWidth();
				
				qbLayout.setLayoutParams(new LinearLayout.LayoutParams(qbl_width * 2, LayoutParams.MATCH_PARENT));
				testWord.setLayoutParams(new LinearLayout.LayoutParams(qbl_width, LayoutParams.MATCH_PARENT));
				wordLoader.setLayoutParams(new LinearLayout.LayoutParams(qbl_width, 60));
			}
		
		});
		
		// change fonts
		changeFont(R.id.heading);
		changeFont(R.id.test_word);
		changeFont(R.id.btn_test_check);
		changeFont(R.id.dic_test_time);
		changeFont(R.id.dic_test_status);
		changeFont(R.id.test_start_stop_btn);
		changeFont(R.id.correct_answer);
		changeFont(R.id.wrong_answer);
		
		ansRight = (TextView) findViewById(R.id.correct_answer);
		ansWrong = (TextView) findViewById(R.id.wrong_answer);
		testStatus = (TextView) findViewById(R.id.dic_test_status);
		btnStartStop = (Button) findViewById(R.id.test_start_stop_btn);
		
		if(sp.contain(AppHelper.TEST_OPTION)){
			testStatus.setText("Online Dictionary");
			test_cat = ONLINE;
		}
		else{
			testStatus.setText("My Dictionary");
			test_cat = OFFLINE;
		}
		
		// set test meaning
		testMeaning = (EditText) findViewById(R.id.test_meaning);
		
		// set time
		testTime = (TextView) findViewById(R.id.dic_test_time);
		testTime.setText("Time: " + formator(minute) + ":00");
		
		// set initail text button start & stop
		btnStartStop.setText("START");
		
		// setup question box
		testWord = (TextView) findViewById(R.id.test_word);
		testWord.setOnTouchListener(this);
		wordLoader = (ProgressBar) findViewById(R.id.nw_loader);
		qbLayout = (LinearLayout) findViewById(R.id.q_box);
		
		// create used question storage
		used_questions = new ArrayList<DictionaryItem>();
	}
	
	
	public void checkTest(View v){
		
		if(is_test_start){
			if(! is_process){
				if(! testMeaning.getText().toString().equals("")){
					String row_meanings = nextQ.getRowMeaning();
					String[] words = row_meanings.split(",");

					for(int i = 0; i < words.length; i++){
						if(words[i].equals(testMeaning.getText().toString())){
							match = true;
							break;
						}
					}
					
					
					if(match){
						++ans_right;
						ansRight.setText(String.valueOf(ans_right));
						toast("(: !!! Correct !!! :)", Toast.LENGTH_SHORT);
					}
					else{
						++ans_wrong;
						ansWrong.setText(String.valueOf(ans_wrong));
						toast("): Wrong :( \n" + nextQ.getRowMeaning(), Toast.LENGTH_SHORT);
						
					}
					
					match = false;
					testMeaning.setText("");
					used_questions.add(nextQ);
					is_process = true;
					++count_q;
					qbLayout.startAnimation(q_skip_anim);
				}
				else{
					toast("Answer field can't be blank.");
				}
			}
			else{
				toast("Wait...");
			}
		}
		else{
			toast("Test is not start", Toast.LENGTH_SHORT);
		}
	}
	// test start stop button
	public void testStartStop(View v){
		
		// if test is not start
		if( ! is_test_start){
			
			// start test
			// set test start to true
			is_test_start = true;
			
			// change start button text to stop
			btnStartStop.setText("STOP");
			
			// set time
			testTime.setText("Time: " + formator(--minute) + ":" + formator(second));
			
			// starting test
			startTest();
			
		}
		else{	
			// change stop button text to start
			btnStartStop.setText("START");
			
			// stoping test
			stopTest();
		}
	}
	
	// back to main menu
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		overridePendingTransition (R.anim.open_main, R.anim.close_next);
	}
	
	// starting test
	private void startTest(){
		
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) qbLayout.getLayoutParams();
		params.setMargins(-qbl_width, 0, 0, 0);
		qbLayout.setLayoutParams(params);
		
		getQ();
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(time < time_limit){
					runTest();
				}
				else{
					stopTest();
				}
			}
		}, INTERVAL);
		
	}
	
	// running test
	private void runTest(){
		
		// check if test is start
		if(is_test_start){
			
			time += tick;
			second -= tick;
			
			if(second < 0){
				if(minute < 1){
					second = 0;
				}
				else{
					second = 59;
					--minute;
				}
			}
			
			testTime.setText("Time: " + formator(minute) + ":" + formator(second));
			
			// set handler
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(time < time_limit){
						runTest();
					}
					else{
						stopTest();
					}
				}
			}, INTERVAL);
		}
	}
	
	// stoping test
	private void stopTest(){
		
		/*// check minimum 20 should be correct
		if(ans_right >= 20){
			
			// save score
			int total_score = (ans_right * 100) / time;
			
			// create score object
			Score score = new Score();
			score.setScore(total_score);
			
			// add score to db
			dd.addNewScore(score);
			
			// toast
			toast("Score Saved.");
		}
		else{
			toast("Score not saved.\nCorrect answer is less than 20.");
		}*/
		
		toast("Test STOP");
		
		resetVariables();
	}
	private void resetVariables(){
		is_test_start = false;
		is_process = false;
		match = false;
		count_q = 0;
		skip_q = 0;
		ans_wrong = 0;
		ans_right = 0;
		time = 0;
		minute = time_limit / 60;
		second = 59;
		used_questions.clear();
		nextQ = null;
		testTime.setText("Time: " + formator(minute) + ":00");
		ansRight.setText(String.valueOf(ans_right));
		ansWrong.setText(String.valueOf(ans_wrong));
		testWord.setText("");
	}
	
	// formator
	private String formator(int value){
		if(value < 10){
			return "0" + value;
		}
		else{
			return String.valueOf(value);
		}
	}
	
	// get next question
	private void getQ(){
		is_process = true;
		if(test_cat == OFFLINE){
			new getOfflineQ().execute();
		}
		else if(test_cat == ONLINE){
			new getOnlineQ().execute();
		}
	}
	
	// set qusetion
	private void setQ(DictionaryItem q){
		testWord.setText(q.getWord());
		
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) qbLayout.getLayoutParams();
		params.setMargins(0, 0, 0, 0);
		qbLayout.setLayoutParams(params);
		is_process = false;
	}
	
	// swipe left
	private void swipeLeft(){
		if(is_test_start){
			if(! is_process){
				if(skip_q < limit_skip_q){
					is_process = true;
					used_questions.add(nextQ);
					++skip_q;
					++count_q;
					qbLayout.startAnimation(q_skip_anim);
				}
				else{
					toast("Skip Limit is Over.", Toast.LENGTH_SHORT);
				}
			}
		}
	}
	
	// swipe right
	private void swipeRight(){
		
	}
	
	// touch listener
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return gestureDetector.onTouchEvent(event);
	}
	
	// get question in my dictionary
	private class getOfflineQ extends AsyncTask<Void, Void, Void>{
		
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
		}
		
		@Override
		protected Void doInBackground(Void... params){
			
			// set random question
			nextQ = dd.getRandItem();
			
			// return null value
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result){
			super.onPostExecute(result);
			setQ(nextQ);
		}
	}

	// get question in online dictionary
	private class getOnlineQ extends AsyncTask<Void, Void, Void>{
		
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
		}
		
		@Override
		protected Void doInBackground(Void... params){
			
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
			
			if(jsonStr != null){
				try{
					jObj = new JSONObject(jsonStr);
					
					return_status = jObj.getInt("result");
					return_message = jObj.getString("message");
					
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
			
			if(return_status == Setting.SUCCESS){
				try{
					JSONObject data = new JSONObject(jObj.getString("list"));
					DictionaryItem item = new DictionaryItem();
					item.setWord(data.getString("word"));
					item.setRowMeaning(data.getString("row_meaning"));
					item.setId(data.getInt("id"));
					item.setDatetime(data.getString("date_time"));
					
					nextQ = item;
					
					// set question
					setQ(nextQ);
				}catch(JSONException e){
					e.printStackTrace();
				}
			}
			else{
				toast(return_message);
			}
		}
	}
	
	// gesture detector listener
	private class MyGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    swipeLeft();
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    swipeRight();
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

            @Override
        public boolean onDown(MotionEvent e) {
              return true;
        }
    }

}
