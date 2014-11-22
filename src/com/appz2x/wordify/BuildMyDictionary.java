package com.appz2x.wordify;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;


import com.play2x.wordify.adapter.MyDictionaryViewAdapter;

public class BuildMyDictionary {
	
	private Activity activity;
	ListView lv;
	public static MyDictionaryViewAdapter adapter;
	public static ArrayList<DictionaryItem> myDicItems;
	ProgressDialog pDialog;
	List<DictionaryItem> items;
	
	// flag for current page
	private int current_page = 1;

	// start item
	int start_item;
	
	// int limit item
	int limit_item;
	
	// page total
	int page_total;
	
	// total
	int total;
	
	// is last page
	boolean is_last_page;
	
	// set return status
	int return_status;
	
	// check avaiable data
	boolean is_data;
	
	// dictionary word meaning
	DictionaryItem dic_item;
	
	// set return message
	String return_message;

	public BuildMyDictionary(Activity a){
		activity = a;
		dic_item = new DictionaryItem();
		myDicItems = new ArrayList<DictionaryItem>();

	}
	
	public void build(ListView listView){
		
		lv = listView;
		
		/**
         * Listening to Load More button click event
         * */
		PageOptionStorage.getInstance().getMyDicLMB().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				current_page += 1;
				new loadMoreMyWords().execute();
			}
		});

		// load dictionary
		new loadMoreMyWords().execute();
	}
	
	public void fetchItems(){
      
        // looping through all item nodes <item>
        for (DictionaryItem item : items) {
            // adding HashList to ArrayList
        	myDicItems.add(item);
        }
        
        
        
        if( ! is_last_page && current_page == 1){
        	lv.addFooterView(PageOptionStorage.getInstance().getMyDicLMB());
        }
        
        if(is_last_page){
        	lv.removeFooterView(PageOptionStorage.getInstance().getMyDicLMB());
        }
        
        // Appending new data to menuItems ArrayList
        if(current_page == 1){
        	adapter = new MyDictionaryViewAdapter(activity, myDicItems);
            lv.setAdapter(adapter);
        }
        
        if(current_page > 1){
        	
        	// get listview current position - used to maintain scroll position
        	int currentPosition = lv.getFirstVisiblePosition();
        	
        	// Setting new scroll position
            lv.setSelectionFromTop(currentPosition + 1, 0);
        }

	}
	 
	public void noItem(){
		
		// log message
		Log.d("BMD", "no item found my dictionary");
		adapter = new MyDictionaryViewAdapter(activity, myDicItems);
        lv.setAdapter(adapter);
	}
	
	public static void removeByItem(DictionaryItem item){
		int did = item.getID();
		int pos = 0;
		
		for(DictionaryItem single_item : myDicItems){
			if(did == single_item.getID()){
				myDicItems.remove(pos);
				break;
			}
			else{
				++pos;
			}
		}
	}
	
	public static void notifyAdapter(){
		adapter.notifyDataSetChanged();
	}
	
	public static void notifyInvalidate(){
		adapter.notifyDataSetInvalidated();
		adapter = null;
		myDicItems = null;
	}
	
	/**
     * Async Task that send a request to url
     * Gets new list view data
     * Appends to list view
     * */
    private class loadMoreMyWords extends AsyncTask<Void, Void, Void> {
 
        @Override
        protected void onPreExecute() {
            // Showing progress dialog before sending http request
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage("Please wait..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        protected Void doInBackground(Void... arg0) {
        	
        	DictionaryData dd = new DictionaryData(activity);
        	
        	if(dd.getItemsCount() > 0){
        		is_data = true;
        		items = dd.getPagingItem(current_page);
        	}
 
            return null;
            
        }
 
        protected void onPostExecute(Void result) {
            // closing progress dialog

            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            
            if(is_data){
            	start_item = PageOptionStorage.getInstance().getStartItem();
            	limit_item = PageOptionStorage.getInstance().getLImitItem();
            	page_total = PageOptionStorage.getInstance().getPageTotal();
            	total = PageOptionStorage.getInstance().getTotal();
            	is_last_page = PageOptionStorage.getInstance().getLastPage();
            	
            	fetchItems();
            }
            else{
            	noItem();
            }
        }
    }
}
