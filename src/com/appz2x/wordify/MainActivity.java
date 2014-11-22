package com.appz2x.wordify;

import java.util.Random;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;


public class MainActivity extends BaseActivity {
	
	public static int ANIMATION_DELAY = 3000;
	final int[] fwd_anim_id = {R.anim.tile1, R.anim.tile2, R.anim.tile3, R.anim.tile4, R.anim.tile5, R.anim.tile6};
	final int[] bck_anim_id = {R.anim.tile1_back, R.anim.tile2_back, R.anim.tile3_back, R.anim.tile4_back, R.anim.tile5_back, R.anim.tile6_back};
	public int[] btns		= {R.drawable.add_btn, R.drawable.test_btn, R.drawable.learning_btn, R.drawable.setting_btn, R.drawable.contact_btn, R.drawable.about_btn, R.drawable.translate_btn};
	private int[][] anim_btns  = {
			{R.drawable.add_btn, R.drawable.add_btn},
			{R.drawable.test_btn, R.drawable.test_btn},
			{R.drawable.learning_btn, R.drawable.learning_btn},
			{R.drawable.setting_btn, R.drawable.setting_btn},
			{R.drawable.contact_btn, R.drawable.contact_btn},
			{R.drawable.about_btn, R.drawable.about_btn}
	};
	final LinearLayout.LayoutParams[] anim_params = new LinearLayout.LayoutParams[6];
	final LinearLayout[] anim_layout = new LinearLayout[6];
	final int[] anim_box_width = new int[6];
	final Animation[] fwd_anim = new Animation[6];
	final Animation[] bck_anim = new Animation[6];
	private boolean[] is_run_first = {true, true, true, true, true ,true};
	final int[] delay = {2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000, 11000};
	ImageButton a1b1, a1b2, a2b1, a2b2, a3b1, a3b2, a4b1, a4b2, a5b1, a5b2, a6b1, a6b2;
	private Random rand = new Random();
	public boolean is_fwd_anim = true;
	private boolean is_first_time = true;
	public int nextBtn, currentBtn;
	private int selection;
	private boolean run = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// start tile animation
		setupAnimation();
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		run = false;
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		run = true;
	}

	// add new word activity
	public void gotoAddNewWord(View v){
		gotoActivity(AddNewWord.class);
	}
	
	// goto learning activity
	public void gotoLearning(View v){
		gotoActivity(Dictionary.class);
	}
	
	// goto test activity
	public void gotoTest(View v){
		gotoActivity(Test.class);
	}
	
	// goto setting activity
	public void gotoSetting(View v){
		gotoActivity(AppSetting.class);
	}
	
	// goto stats activity
	public void gotoStats(View v){
		gotoActivity(Stats.class);
	}
	
	// goto contact us activity
	public void gotoContactUs(View v){
		gotoActivity(ContactUs.class);
	}
	
	// goto about activity
	public void gotoAbout(View v){
		gotoActivity(About.class);
	}
	
	//set listener
	private void setListener(ImageButton btn, int res){
		
		btn.setOnClickListener(null);
		
		switch(res){
		case R.drawable.add_btn:
			btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					gotoAddNewWord(v);
				}
			});
			break;
			
		case R.drawable.test_btn:
			btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					gotoTest(v);
				}
			});
			break;
			
		case R.drawable.learning_btn:
			btn.setOnClickListener(new OnClickListener() {
							
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					gotoLearning(v);
				}
			});
			break;
			
		case R.drawable.setting_btn:
			btn.setOnClickListener(new OnClickListener() {
							
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					gotoSetting(v);
				}
			});
			break;
			
		case R.drawable.contact_btn:
			btn.setOnClickListener(new OnClickListener() {
							
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					gotoContactUs(v);
				}
			});
			break;
			
		case R.drawable.about_btn:
			btn.setOnClickListener(new OnClickListener() {
							
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					gotoAbout(v);
				}
			});
			break;
			
		case R.drawable.translate_btn:
			btn.setOnClickListener(new OnClickListener() {
							
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					gotoActivity(Translate.class);
				}
			});
			break;
		}
	}
	
	// set animation interpolator
	private void setInterpolator(Animation anim){
		
		int anim_id = (sp.contain(AppHelper.TILE_ANIM)) ? sp.getInt(AppHelper.TILE_ANIM) : 4;
		
		if(anim_id == 6){
			anim_id = rand.nextInt(5);
			runInterpolator(anim, anim_id);
		}
		else{
			runInterpolator(anim, anim_id);
		}
	}
	
	// set animation interpolator
	private void runInterpolator(Animation anim, int anim_id){
		
		switch(anim_id){
		case 0:
			anim.setInterpolator(new AccelerateDecelerateInterpolator());
			break;
			
		case 1:
			anim.setInterpolator(new AccelerateInterpolator());
			break;
			
		case 2:
			anim.setInterpolator(new AnticipateInterpolator());
			break;
			
		case 3:
			anim.setInterpolator(new AnticipateOvershootInterpolator());
			break;
		
		case 4:
			anim.setInterpolator(new BounceInterpolator());
			break;
			
		case 5:
			anim.setInterpolator(new DecelerateInterpolator());
			break;
		}
	}
	
	// tile animation
	private void startTileAnimation(){
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				runAnimation();
			}
		}, ANIMATION_DELAY);
	}
	
	// run animation
	private void runAnimation() {
		
		if(run){

			if(is_first_time){
				is_first_time = false;
				
				a1b1 = (ImageButton) findViewById(R.id.anim_1_btn_1);
				a1b2 = (ImageButton) findViewById(R.id.anim_1_btn_2);
				a2b1 = (ImageButton) findViewById(R.id.anim_2_btn_1);
				a2b2 = (ImageButton) findViewById(R.id.anim_2_btn_2);
				a3b1 = (ImageButton) findViewById(R.id.anim_3_btn_1);
				a3b2 = (ImageButton) findViewById(R.id.anim_3_btn_2);
				a4b1 = (ImageButton) findViewById(R.id.anim_4_btn_1);
				a4b2 = (ImageButton) findViewById(R.id.anim_4_btn_2);
				a5b1 = (ImageButton) findViewById(R.id.anim_5_btn_1);
				a5b2 = (ImageButton) findViewById(R.id.anim_5_btn_2);
				a6b1 = (ImageButton) findViewById(R.id.anim_6_btn_1);
				a6b2 = (ImageButton) findViewById(R.id.anim_6_btn_2);
				
				// set next btn drawable
				nextBtn = R.drawable.translate_btn;
				
				// layout menu box 1
				anim_layout[0] = (LinearLayout) findViewById(R.id.anim_menu_box_1);
				anim_box_width[0] = anim_layout[0].getMeasuredWidth();
				anim_layout[0].setLayoutParams(new LinearLayout.LayoutParams(anim_box_width[0] * 2, 500));
				a1b1.setLayoutParams(new LinearLayout.LayoutParams(anim_box_width[0], LayoutParams.MATCH_PARENT));
				a1b2.setLayoutParams(new LinearLayout.LayoutParams(anim_box_width[0], LayoutParams.MATCH_PARENT));
				
				// layout menu box 2
				anim_layout[1] = (LinearLayout) findViewById(R.id.anim_menu_box_2);
				anim_box_width[1] = anim_layout[1].getMeasuredWidth();
				
				// layout menu box 3
				anim_layout[2] = (LinearLayout) findViewById(R.id.anim_menu_box_3);
				anim_box_width[2] = anim_layout[2].getMeasuredWidth();
				
				// layout menu box 4
				anim_layout[3] = (LinearLayout) findViewById(R.id.anim_menu_box_4);
				anim_box_width[3] = anim_layout[3].getMeasuredWidth();
				
				// layout menu box 5
				anim_layout[4] = (LinearLayout) findViewById(R.id.anim_menu_box_5);
				anim_box_width[4] = anim_layout[4].getMeasuredWidth();
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(anim_box_width[4] * 2, 260);
				param.setMargins(-anim_box_width[4], 0, 0, 0);
				anim_layout[4].setLayoutParams(param);
				a5b1.setLayoutParams(new LinearLayout.LayoutParams(anim_box_width[4], LayoutParams.MATCH_PARENT));
				a5b2.setLayoutParams(new LinearLayout.LayoutParams(anim_box_width[4], LayoutParams.MATCH_PARENT));
				
				// layout menu box 6
				anim_layout[5] = (LinearLayout) findViewById(R.id.anim_menu_box_6);
				anim_box_width[5] = anim_layout[5].getMeasuredWidth();
				LinearLayout.LayoutParams param6 = new LinearLayout.LayoutParams(anim_box_width[5] * 2, 260);
				param6.setMargins(0, 0, 0, 0);
				anim_layout[5].setLayoutParams(param6);
				a6b1.setLayoutParams(new LinearLayout.LayoutParams(anim_box_width[5], LayoutParams.MATCH_PARENT));
				a6b2.setLayoutParams(new LinearLayout.LayoutParams(anim_box_width[5], LayoutParams.MATCH_PARENT));
			}
			
			// random tile selection
			selection = rand.nextInt(fwd_anim.length);
			
			switch(selection){
			
			case 0:
				
				if(is_run_first[0]){
					is_run_first[0] = false;
	
					a1b2.setBackgroundResource(nextBtn);
					anim_btns[0][1] = nextBtn;
					setListener(a1b2, nextBtn);
					nextBtn = anim_btns[0][0];
					setInterpolator(fwd_anim[0]);
					anim_layout[0].startAnimation(fwd_anim[0]);
				}
				else{
					is_run_first[0] = true;
	
					a1b1.setBackgroundResource(nextBtn);
					anim_btns[0][0] = nextBtn;
					setListener(a1b1, nextBtn);
					nextBtn = anim_btns[0][1];
					setInterpolator(bck_anim[0]);
					anim_layout[0].startAnimation(bck_anim[0]);
				}
				
				break;
				
			case 1:
				
				if(is_run_first[1]){
					is_run_first[1] = false;
					
					a2b1.setBackgroundResource(nextBtn);
					anim_btns[1][0] = nextBtn;
					setListener(a2b1, nextBtn);
					nextBtn = anim_btns[1][1];
					setInterpolator(fwd_anim[1]);
					anim_layout[1].startAnimation(fwd_anim[1]);
				}
				else{
					is_run_first[1] = true;
					
					a2b2.setBackgroundResource(nextBtn);
					anim_btns[1][1] = nextBtn;
					setListener(a2b2, nextBtn);
					nextBtn = anim_btns[1][0];
					setInterpolator(bck_anim[1]);
					anim_layout[1].startAnimation(bck_anim[1]);
				}
				
				break;
				
			case 2:
						
				if(is_run_first[2]){
					is_run_first[2] = false;
					
					a3b2.setBackgroundResource(nextBtn);
					anim_btns[2][1] = nextBtn;
					setListener(a3b2, nextBtn);
					nextBtn = anim_btns[2][0];
					setInterpolator(fwd_anim[2]);
					anim_layout[2].startAnimation(fwd_anim[2]);
				}
				else{
					is_run_first[2] = true;
					
					a3b1.setBackgroundResource(nextBtn);
					anim_btns[2][0] = nextBtn;
					setListener(a3b1, nextBtn);
					nextBtn = anim_btns[2][1];
					setInterpolator(bck_anim[2]);
					anim_layout[2].startAnimation(bck_anim[2]);
				}
				
				break;
						
			case 3:
				
				if(is_run_first[3]){
					is_run_first[3] = false;
					
					a4b1.setBackgroundResource(nextBtn);
					anim_btns[3][0] = nextBtn;
					setListener(a4b1, nextBtn);
					nextBtn = anim_btns[3][1];
					setInterpolator(fwd_anim[3]);
					anim_layout[3].startAnimation(fwd_anim[3]);
				}
				else{
					is_run_first[3] = true;
					
					a4b2.setBackgroundResource(nextBtn);
					anim_btns[3][1] = nextBtn;
					setListener(a4b2, nextBtn);
					nextBtn = anim_btns[3][0];
					setInterpolator(bck_anim[3]);
					anim_layout[3].startAnimation(bck_anim[3]);
				}
				
				break;
				
			case 4:
				
				if(is_run_first[4]){
					is_run_first[4] = false;
					
					a5b1.setBackgroundResource(nextBtn);
					anim_btns[4][0] = nextBtn;
					setListener(a5b1, nextBtn);
					nextBtn = anim_btns[4][1];
					setInterpolator(fwd_anim[4]);
					anim_layout[4].startAnimation(fwd_anim[4]);
				}
				else{
					is_run_first[4] = true;
					
					a5b2.setBackgroundResource(nextBtn);
					anim_btns[4][1] = nextBtn;
					setListener(a5b2, nextBtn);
					nextBtn = anim_btns[4][0];
					setInterpolator(bck_anim[4]);
					anim_layout[4].startAnimation(bck_anim[4]);
				}
				
				break;
				
			case 5:
				
				if(is_run_first[5]){
					is_run_first[5] = false;
					
					a6b2.setBackgroundResource(nextBtn);
					anim_btns[5][1] = nextBtn;
					setListener(a6b2, nextBtn);
					nextBtn = anim_btns[5][0];
					setInterpolator(fwd_anim[5]);
					anim_layout[5].startAnimation(fwd_anim[5]);
				}
				else{
					is_run_first[5] = true;
					
					a6b1.setBackgroundResource(nextBtn);
					anim_btns[5][0] = nextBtn;
					setListener(a6b1, nextBtn);
					nextBtn = anim_btns[5][1];
					setInterpolator(bck_anim[5]);
					anim_layout[5].startAnimation(bck_anim[5]);
				}
				
				break;
			
			}
		}
		ANIMATION_DELAY = delay[rand.nextInt(delay.length)];
		startTileAnimation();
		
	}
	
	// setup animation
	private void setupAnimation(){
		
		// forward
		for(int m = 0; m < fwd_anim.length; m++){
			fwd_anim[m] = AnimationUtils.loadAnimation(this, fwd_anim_id[m]);
		}
		
		// set forward listener 1
		fwd_anim[0].setAnimationListener(new AnimationListener() {
			
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
                anim_layout[0].startAnimation(anim);
                
                LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) anim_layout[0].getLayoutParams();
                param.setMargins(-anim_box_width[0], 0, 0, 0);
                anim_layout[0].setLayoutParams(param);
			}
		});
		
		// set forward listener 2
		fwd_anim[1].setAnimationListener(new AnimationListener() {
			
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
                anim_layout[1].startAnimation(anim);
                
                LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) anim_layout[1].getLayoutParams();
                param.setMargins(0, 0, 0, 0);
                anim_layout[1].setLayoutParams(param);
			}
		});
		
		// set forward listener 3
		fwd_anim[2].setAnimationListener(new AnimationListener() {
			
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
                anim_layout[2].startAnimation(anim);
                
                LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) anim_layout[2].getLayoutParams();
                param.setMargins(0, -300, 0, 0);
                anim_layout[2].setLayoutParams(param);
			}
		});
		
		// set forward listener 4
		fwd_anim[3].setAnimationListener(new AnimationListener() {
			
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
                anim_layout[3].startAnimation(anim);
                
                LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) anim_layout[3].getLayoutParams();
                param.setMargins(0, 0, 0, 0);
                anim_layout[3].setLayoutParams(param);
			}
		});
		
		// set forward listener 5
		fwd_anim[4].setAnimationListener(new AnimationListener() {
			
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
                anim_layout[4].startAnimation(anim);
                
                LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) anim_layout[4].getLayoutParams();
                param.setMargins(0, 0, 0, 0);
                anim_layout[4].setLayoutParams(param);
			}
		});
		
		// set forward listener 6
		fwd_anim[5].setAnimationListener(new AnimationListener() {
			
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
                anim_layout[5].startAnimation(anim);
                
                LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) anim_layout[5].getLayoutParams();
                param.setMargins(-anim_box_width[5], 0, 0, 0);
                anim_layout[5].setLayoutParams(param);
			}
		});
		
		// reverse ----------------------------------------------------------
		
		for(int n = 0; n < bck_anim.length; n++){
			bck_anim[n] = AnimationUtils.loadAnimation(this, bck_anim_id[n]);
		}
		
		// set backword listener 1
		bck_anim[0].setAnimationListener(new AnimationListener() {
			
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
                anim_layout[0].startAnimation(anim);
                
                LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) anim_layout[0].getLayoutParams();
                param.setMargins(0, 0, 0, 0);
                anim_layout[0].setLayoutParams(param);
			}
		});
		
		// set backword listener 2
		bck_anim[1].setAnimationListener(new AnimationListener() {
			
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
                anim_layout[1].startAnimation(anim);
                
                LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) anim_layout[1].getLayoutParams();
                param.setMargins(-(anim_box_width[1] / 2), 0, 0, 0);
                anim_layout[1].setLayoutParams(param);
			}
		});
		
		// set backword listener 3
		bck_anim[2].setAnimationListener(new AnimationListener() {
			
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
                anim_layout[2].startAnimation(anim);
                
                LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) anim_layout[2].getLayoutParams();
                param.setMargins(0, 0, 0, 0);
                anim_layout[2].setLayoutParams(param);
			}
		});
		
		// set backword listener 4
		bck_anim[3].setAnimationListener(new AnimationListener() {
			
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
                anim_layout[3].startAnimation(anim);
                
                LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) anim_layout[3].getLayoutParams();
                param.setMargins(0, -300, 0, 0);
                anim_layout[3].setLayoutParams(param);
			}
		});
		
		// set backword listener 5
		bck_anim[4].setAnimationListener(new AnimationListener() {
			
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
                anim_layout[4].startAnimation(anim);
                
                LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) anim_layout[4].getLayoutParams();
                param.setMargins(-anim_box_width[4], 0, 0, 0);
                anim_layout[4].setLayoutParams(param);
			}
		});
		
		// set backword listener 6
		bck_anim[5].setAnimationListener(new AnimationListener() {
			
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
                anim_layout[5].startAnimation(anim);
                
                LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) anim_layout[5].getLayoutParams();
                param.setMargins(0, 0, 0, 0);
                anim_layout[5].setLayoutParams(param);
			}
		});
		
		// start animation
		startTileAnimation();
	}
}
