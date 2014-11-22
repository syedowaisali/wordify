package com.appz2x.wordify;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.play2x.wordify.adapter.SearchMyDictionaryViewAdapter;

public class SearchBuildMyDictionary {
	
	private Activity activity;
	ListView lv;
	private SearchMyDictionaryViewAdapter adapter;
	private ArrayList<DictionaryItem> searchMyDicItems;
	ProgressDialog pDialog;
	private List<DictionaryItem> items;
	
	private String search_key;
	
	
	// check avaiable data
	private boolean is_data;
	
	// dictionary word meaning
	//DictionaryItem dic_item;
	
	// set return message
	String return_message;

	public SearchBuildMyDictionary(Activity a, String key){
		activity = a;
		//dic_item = new DictionaryItem();
		searchMyDicItems = new ArrayList<DictionaryItem>();
		search_key= key;

	}
	
	public void build(ListView listView){
		
		lv = listView;
		
		// load dictionary
		new loadMySearchWords().execute();
	}
	
	public void fetchItems(){
      
        // looping through all item nodes <item>
        for (DictionaryItem item : items) {
            // adding HashList to ArrayList
        	searchMyDicItems.add(item);
        }

        adapter = new SearchMyDictionaryViewAdapter(activity, searchMyDicItems);
        lv.setAdapter(adapter);

	}
	 
	public void noItem(){
		
		// log message
		Log.d("BMD", "no item found my dictionary");
		adapter = new SearchMyDictionaryViewAdapter(activity, searchMyDicItems);
        lv.setAdapter(adapter);
	}
	
	public void notifyAdapter(){
		adapter.notifyDataSetChanged();
	}
	
	public void notifyInvalidate(){
		adapter.notifyDataSetInvalidated();
		adapter = null;
	}
	
	/**
     * Async Task that send a request to url
     * Gets new list view data
     * Appends to list view
     * */
    private class loadMySearchWords extends AsyncTask<Void, Void, Void> {
 
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
        		items = dd.getSearchItems(search_key);
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
            	fetchItems();
            }
            else{
            	//noItem();
            }
        }
    }
}
