package com.appz2x.wordify;

import java.util.List;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Stats extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats);
		
		// run activity slide
		runSplashSlide(R.id.sc_stats, R.id.activity_splash, new SlideListener(){
			
			@Override
			public void onSlideComplete(){
				
			}
		
		});
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		changeFont(R.id.heading);
		
		// build graph
		buildGraphView();

	}
	
	// back to main menu
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		overridePendingTransition (R.anim.open_main, R.anim.close_next);
	}
	
	// build graph view
	private void buildGraphView(){
		
		if(dd.getScoreCount() != Setting.EMPTY){
			
			List<Score> scores = dd.getScores();
			GraphViewData[] data = new GraphViewData[dd.getScoreCount()];
			int index = 0;
			for(Score score : scores){
				data[index] = new GraphViewData((index + 1), score.getScore());
				++index;
			}
			
			GraphView graphView = new LineGraphView(this, "Chart");
			graphView.addSeries(new GraphViewSeries(data)); // data
			
			// set scrollable
			graphView.setScrollable(true);
			
			// optional - activate scaling / zooming
			graphView.setScalable(true);
			
			LinearLayout layout = (LinearLayout) findViewById(R.id.stats_box);
			layout.addView(graphView);
		}
		else{
			TextView tv = (TextView) findViewById(R.id.score_chart_view);
			tv.setVisibility(View.VISIBLE);
		}
	}
}
