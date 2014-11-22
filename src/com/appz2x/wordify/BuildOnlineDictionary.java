package com.appz2x.wordify;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.play2x.wordify.adapter.OnlineDictionaryViewAdapter;

public class BuildOnlineDictionary {
	
	private Activity activity;
	ListView lv;
	public static OnlineDictionaryViewAdapter adapter;
	public static ArrayList<DictionaryItem> dicItems;
	ProgressDialog pDialog;
	
	// flag for current page
	private int current_page = 1;
	
	// json Object
	JSONObject jsonObj;
	
	// json data object
	JSONArray dataArr;
	
	// set return status
	int return_status;
	
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
	
	// dictionary word meaning
	DictionaryItem dic_item;
	
	// set return message
	String return_message;
	
	Button btnLoadMore;
	
	public BuildOnlineDictionary(Activity a){
		activity = a;
		dic_item = new DictionaryItem();
		Setting.onlineAdapter = adapter;
	}
	
	public void build(ListView listView){
		
		lv = listView;
		
		dicItems = new ArrayList<DictionaryItem>();
		
		// load more button
		btnLoadMore = new Button(activity);
		btnLoadMore.setText("Load More");
		btnLoadMore.setTextColor(Color.WHITE);
		btnLoadMore.setBackgroundResource(R.drawable.loadmore_word_btn);
		
		/**
         * Listening to Load More button click event
         * */
		btnLoadMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				current_page += 1;
				new loadMoreOnlnlineWords().execute();
			}
		});
		
		// load dictionary
		new loadMoreOnlnlineWords().execute();
		
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
            dicItems.add(item);
            
        }
        
        if( ! is_last_page && current_page == 1){
        	lv.addFooterView(btnLoadMore);
        }
        
        if(is_last_page){
        	lv.removeFooterView(btnLoadMore);
        }
        
        if(current_page == 1){
        	adapter = new OnlineDictionaryViewAdapter(activity, dicItems);
            lv.setAdapter(adapter);
        }
        
        if(current_page > 1){
        	
        	// notify adapter
        	adapter.notifyDataSetChanged();
        	
        	// get listview current position - used to maintain scroll position
        	int currentPosition = lv.getFirstVisiblePosition();
        	
        	// Setting new scroll position
            lv.setSelectionFromTop(currentPosition + 1, 0);
        }
	}
	
	public void noItem(){
		// log message
		Log.d("BMD", "no item online found");
		
		// display message in text view
		View v = PageOptionStorage.getInstance().getOnlineView();
		TextView online_dic_msg = (TextView) v.findViewById(R.id.online_dic_msg);
		online_dic_msg.setText("No Item Found.");
		online_dic_msg.setVisibility(View.VISIBLE);
	}
	
	public static void notifyAdapter(){
		adapter.notifyDataSetChanged();
	}
	
	/**
     * Async Task that send a request to url
     * Gets new list view data
     * Appends to list view
     * */
    private class loadMoreOnlnlineWords extends AsyncTask<Void, Void, Void> {
 
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
            
            String url = Setting.WEB_URL + "?func=_get_dictionary_list&page=" + current_page + "&token=" + Setting.TOKEN;
            Log.d("url", url);
            
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            
            
            Log.d("Response: ", "> " + jsonStr);
 
            if (jsonStr != null) {
                try {
                    jsonObj = new JSONObject(jsonStr);
                    return_status = jsonObj.getInt("result");
                    return_message = jsonObj.getString("message");
                    total = jsonObj.getInt("total");
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
            		
            		if(total > 0){
            			start_item = jsonObj.getInt("start_item");
                		limit_item = jsonObj.getInt("limit_item");
                		page_total = jsonObj.getInt("page_total");
                		is_last_page = jsonObj.getBoolean("is_last_page");
                		
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
