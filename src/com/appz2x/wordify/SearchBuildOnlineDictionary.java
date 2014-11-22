package com.appz2x.wordify;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.play2x.wordify.adapter.SearchOnlineDictionaryViewAdapter;

public class SearchBuildOnlineDictionary {
	
	private Activity activity;
	ListView lv;
	private SearchOnlineDictionaryViewAdapter adapter;
	private ArrayList<DictionaryItem> searchOnlineDicItems;
	ProgressDialog pDialog;
	
	// json Object
	JSONObject jsonObj;
	
	// json data object
	JSONArray dataArr;
	
	// set return status
	int return_status;
	
	// dictionary word meaning
	DictionaryItem dic_item;
	
	// set return message
	String return_message;
	
	// check word is found
	public boolean is_found = false;
	
	// search keyword
	public String search_key;
	
	public SearchBuildOnlineDictionary(Activity a, String key){
		activity = a;
		dic_item = new DictionaryItem();
		search_key = key;
	}
	
	public void build(ListView listView){
		
		lv = listView;
		
		searchOnlineDicItems = new ArrayList<DictionaryItem>();
		
		// load dictionary
		new searchLoadOnlineWords().execute();
		
	}
	
	public void bindItems(JSONArray data) throws JSONException{
      
        // looping through all item nodes <item>
        for (int i = 0; i < data.length(); i++) {
        	
        	JSONObject item_block = data.getJSONObject(i);
        	
            // creating new Object
            DictionaryItem item = new DictionaryItem();

            // adding each child node to HashMap key => value
            item.setId(item_block.getInt("id"));
            item.setDatetime(item_block.getString("date_time"));
            item.setWord(item_block.getString("word"));
            item.setMeanings(item_block.getJSONArray("meanings"));
            item.setRowMeaning(item_block.getString("row_meaning"));
            item.setCheckState(false);

            // adding HashList to ArrayList
            searchOnlineDicItems.add(item);
            
        }
        
    	adapter = new SearchOnlineDictionaryViewAdapter(activity, searchOnlineDicItems);
        lv.setAdapter(adapter);
        
	}
	
	public void noItem(){
		// log message
		Log.d("BMD", "no item online found");

	}
	
	public void notifyAdapter(){
		adapter.notifyDataSetChanged();
	}
	
	/**
     * Async Task that send a request to url
     * Gets new list view data
     * Appends to list view
     * */
    private class searchLoadOnlineWords extends AsyncTask<Void, Void, Void> {
 
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
        	 // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            
            String url = Setting.WEB_URL + "?func=_get_search_words&key=" + search_key + "&token=" + Setting.TOKEN;
            url = url.replace(" ", "%20").replace("@", "%40");
            Log.d("url", url);
            
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            
            
            Log.d("Response: ", "> " + jsonStr);
 
            if (jsonStr != null) {
                try {
                    jsonObj = new JSONObject(jsonStr);
                    return_status = jsonObj.getInt("result");
                    return_message = jsonObj.getString("message");
                    is_found = jsonObj.getBoolean("is_found");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
 
            return null;
        }
 
        protected void onPostExecute(Void result) {
            // closing progress dialog

            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            
            if(return_status == Setting.SUCCESS){
            	try {
            		
            		if(is_found){
    					dataArr = new JSONArray(jsonObj.getString("data"));
    					bindItems(dataArr);
            		}
            		else{
            			noItem();
            		}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }else if(return_status == Setting.INFO){
            	//infoItem();
            }
        }
    }
}
