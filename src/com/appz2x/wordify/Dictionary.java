package com.appz2x.wordify;


import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.play2x.wordify.adapter.TabsPagerAdapter;


@SuppressLint("NewApi")
public class Dictionary extends BaseActivity {
	
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
    public ProgressDialog pDialog;
	public Button btnLoadMore;
	Menu searchMenu;
	
	private static final int MY_DICTIONARY = 0;
	private static final int ONLINE_DICTIONARY = 1;
	
    // table title
	private String[] tabs = {"My Dictionary", "Online Dictionary" };
	
	// online dictionary
	BuildOnlineDictionary bod;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.dictionary);
		
		// get action bar
		android.support.v7.app.ActionBar bar = getSupportActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		
		// run activity slide
		runSplashSlide(R.id.sc_dictionary, R.id.activity_splash, new SlideListener(){
			
			@Override
			public void onSlideComplete(){
				setupTabs();
			}
		});
	}
	
	private void setupTabs(){
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.dictionary_menu, searchMenu);
		
		// load more button
		btnLoadMore = new Button(this);
		btnLoadMore.setText("Load More");
		PageOptionStorage.getInstance().setMyDicLMB(btnLoadMore);
		
		viewPager = (ViewPager) findViewById(R.id.pager);
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), Dictionary.this);
		viewPager.setAdapter(mAdapter);
		
		final ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			
			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				// when the tab is selected, switch to the
				// corresponding page in the ViewPager.
				viewPager.setCurrentItem(tab.getPosition());
			}
			
			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
			}
		};
		
		viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
			
			@Override
			public void onPageSelected(int position){
				// when swiping between pages, select the
				// corresponding tab.
				getActionBar().setSelectedNavigationItem(position);
			}
		});
		
		// adding tabs
		for(String tab_name : tabs){
			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(tabListener));
		}
	}

	public void myDelSelWord(View v){
		new AlertDialog.Builder(this)
		
		// set title
		.setTitle("Alert")
		
		// set message
		.setMessage("Are you you want to delete " + PageOptionStorage.getInstance().getMyDicItems().size() + " words.")
		
		// set positive listener
		.setPositiveButton("Yes", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				new deleteSelectedWords().execute();
			}
		})
		
		// set negative listener
		.setNegativeButton("No", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		})
		
		// show dialog
		.show();
		
	}
	
	public void onlineDownloadSelWord(View v){
		new downloadSelectedWords().execute();
	}
	
	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		//MenuInflater inflater = getMenuInflater();
		//inflater.inflate(R.menu.dictionary_menu, menu);
		searchMenu = menu;
		
        return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch(item.getItemId()){
			case R.id.action_search:
				
				CharSequence[] items = {"My Dictionary", "Online Dictionary"};
				new AlertDialog.Builder(this)
				
				// set title
				.setTitle("Select Dictionary")
				
				// set options.set
				.setItems(items, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch(which){
						case MY_DICTIONARY:
							searchInDictionary(MY_DICTIONARY);
							break;
							
						case ONLINE_DICTIONARY:
							searchInDictionary(ONLINE_DICTIONARY);
							break;
						}
					}
				})
				
				// show
				.show();
				
				break;
		}
		
		return false;
	}
	
	// back to main menu
	@Override
	public void onBackPressed(){
		super.onBackPressed();
		overridePendingTransition (R.anim.open_main, R.anim.close_next);
	}
	
    // search words and meanings in my and online dicitonary
    private void searchInDictionary(final int type){
    	
    	String title = null;
    	
    	switch(type){
    	case MY_DICTIONARY:
    		title = "My Dictionary";
    		break;
    		
    	case ONLINE_DICTIONARY:
    		title = "Online Dictionary";
    		break;
    	}
    	
    	View searchView = getLayoutInflater().inflate(R.layout.search_dictionary, null);
    	
    	final ListView lv = (ListView) searchView.findViewById(R.id.search_dictionary_list);
    	lv.setEmptyView(searchView.findViewById(R.id.search_empty_view));
    	
    	final EditText search_key = (EditText) searchView.findViewById(R.id.search_dic_et);
    	
    	Builder builder = new AlertDialog.Builder(this);
    	
    	// set title
    	builder.setTitle(title)
    	
    	//set view
    	.setView(searchView)
    	
    	// set negative listener
    	.setNegativeButton("Cancel", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		})
		
		// set search as positive listener
		.setPositiveButton("Search", null);
    	
    	final AlertDialog dialog = builder.create();
    	dialog.show();

    	// set positive listener
    	dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if( ! search_key.getText().toString().equals("")){
					
					InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mgr.hideSoftInputFromWindow(search_key.getWindowToken(), 0);
					
					switch(type){
					case MY_DICTIONARY:
						SearchBuildMyDictionary sd = new SearchBuildMyDictionary(Dictionary.this, search_key.getText().toString());
						sd.build(lv);
						break;
						
					case ONLINE_DICTIONARY:
						
						if(new ConnectionDetector(Dictionary.this).isConnectingToInternet()){
							SearchBuildOnlineDictionary od = new SearchBuildOnlineDictionary(Dictionary.this, search_key.getText().toString());
							od.build(lv);
						}
						else{
							Toast.makeText(Dictionary.this, "Internet not connected.", Toast.LENGTH_LONG).show();
						}
						break;
					}
					
				}
				else{
					Toast.makeText(Dictionary.this, "Search field can't blank.", Toast.LENGTH_LONG).show();
				}
			}
		});
    	
    	search_key.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if(actionId == EditorInfo.IME_ACTION_DONE){
					dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
				}
				return false;
			}
		});
    }
    
    /*
     * AsyncTask
     */
    private class deleteSelectedWords extends AsyncTask<Void, Integer, Void>{
    	
    	int counter = 0;
    	int length = PageOptionStorage.getInstance().getMyDicItems().size();
    	ArrayList<DictionaryItem> items = PageOptionStorage.getInstance().getMyDicItems();
    	
    	@Override
    	protected void onPreExecute(){
    		super.onPreExecute();
    		pDialog = new ProgressDialog(Dictionary.this);
    		pDialog.setTitle("Deleting");
    		pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    		pDialog.setMax(length);
    		pDialog.setProgress(counter);
    		pDialog.setIndeterminate(false);
    		pDialog.setCancelable(false);
    		pDialog.show();
    	}
    	
    	@Override
    	protected Void doInBackground(Void... params){
    		
    		for(DictionaryItem item : items){
    			
    			// update progress
    			publishProgress(counter);
    			
    			DictionaryData dd = new DictionaryData(Dictionary.this);
				dd.deleteItem(item.getID());
				
    			/*try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
    			
    			++counter;
    		}
    		
    		BuildMyDictionary.myDicItems.clear();
    		
    		return null;
    	}
    	
    	@Override
    	protected void onProgressUpdate(Integer... progress){
    		super.onProgressUpdate(progress);
    		pDialog.setProgress(progress[0]);
    	}
    	
    	@Override
    	protected void onPostExecute(Void result){
    		super.onPostExecute(result);
    		if(pDialog.isShowing())
    			pDialog.dismiss();
    		
    		
    		BuildMyDictionary.notifyInvalidate();
			if(new ConnectionDetector(Dictionary.this).isConnectingToInternet()){
				BuildOnlineDictionary.notifyAdapter();
			}
			
			// clear all items
			PageOptionStorage.getInstance().clearMyDicItems();
			PageOptionStorage.getInstance().hideMyDelButton();
			
			PageOptionStorage.getInstance().getMyDicLMB().setOnClickListener(null);
			
			// build my dictionary
	    	BuildMyDictionary bmd = new BuildMyDictionary(Dictionary.this);
	    	ListView lv = (ListView) PageOptionStorage.getInstance().getMyView().findViewById(R.id.my_dictionary_list);
	    	lv.setAdapter(null);
	    	lv.removeFooterView(PageOptionStorage.getInstance().getMyDicLMB());
			bmd.build(lv);
			
    	}
    }
    
    /*
     * AsyncTask
     */
    private class downloadSelectedWords extends AsyncTask<Void, Integer, Void>{
    	
    	int counter = 0;
    	int length = PageOptionStorage.getInstance().getOnlineDicItems().size();
    	ArrayList<DictionaryItem> items = PageOptionStorage.getInstance().getOnlineDicItems();
    	
    	@Override
    	protected void onPreExecute(){
    		super.onPreExecute();
    		pDialog = new ProgressDialog(Dictionary.this);
    		pDialog.setTitle("Downloading");
    		pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    		pDialog.setMax(length);
    		pDialog.setProgress(counter);
    		pDialog.setIndeterminate(false);
    		pDialog.setCancelable(false);
    		pDialog.show();
    	}
    	
    	@Override
    	protected Void doInBackground(Void... params){
    		
    		for(DictionaryItem item : items){
    			
    			// update progress
    			publishProgress(counter);
    			
    			DictionaryData dd = new DictionaryData(Dictionary.this);
				if( ! dd.isExistsWord(item.getWord())){
					dd.addNewItem(item);
					item.setCheckState(false);
					BuildMyDictionary.myDicItems.add(item);
				}
    			
    			/*try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
    			
    			++counter;
    		}

    		return null;
    	}
    	
    	@Override
    	protected void onProgressUpdate(Integer... progress){
    		super.onProgressUpdate(progress);
    		pDialog.setProgress(progress[0]);
    	}
    	
    	@Override
    	protected void onPostExecute(Void result){
    		super.onPostExecute(result);
    		if(pDialog.isShowing())
    			pDialog.dismiss();
    		
    		
    		BuildMyDictionary.notifyAdapter();
			if(new ConnectionDetector(Dictionary.this).isConnectingToInternet()){
				BuildOnlineDictionary.notifyAdapter();
			}
			
			// clear all items
			PageOptionStorage.getInstance().clearOnlineDicItems();
			PageOptionStorage.getInstance().hideOnlineDownloadButton();
    	}
    }
}
