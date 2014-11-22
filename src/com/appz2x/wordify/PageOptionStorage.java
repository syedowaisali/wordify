package com.appz2x.wordify;

import java.util.ArrayList;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class PageOptionStorage {
	
	public static PageOptionStorage instance;
	
	private String admin_token = "";
	private int start_item = 0;
	private int limit_item = 0;
	private int page_total = 0;
	private int total = 0;
	private boolean last_page = false;
	private View my_view;
	private View online_view;
	private ArrayList<DictionaryItem> myDicItemsList;
	private ArrayList<DictionaryItem> onlineDicItemsList;
	private Button btnMyDicLoadMore;
	
	public PageOptionStorage() {
		// TODO Auto-generated constructor stub
		this.myDicItemsList = new ArrayList<DictionaryItem>();
		this.onlineDicItemsList = new ArrayList<DictionaryItem>();
	}
	
	public static PageOptionStorage getInstance(){
		if(instance == null){
			instance = new PageOptionStorage();
		}
		
		return instance;
	}
	
	/** Setter ********************************************************************************************************/
	
	// set adminstrator token
	public void setAdminToken(String token){
		admin_token = token;
	}
	
	// set start item
	public void setStartItem(int _start_item){
		start_item = _start_item;
	}
	
	// set limit item
	public void setLimitItem(int _limit_item){
		limit_item = _limit_item;
	}
	
	// set page total
	public void setPageTotal(int _page_total){
		page_total = _page_total;
	}
	
	// set total
	public void setTotal(int _total){
		total = _total;
	}
	
	// set last page
	public void setLastPage(boolean _last_page){
		last_page = _last_page;
	}
	
	// set my view
	public void setMyView(View _my_view){
		my_view = _my_view;
	}
	
	// set online view
	public void setOnlineView(View _online_view){
		online_view = _online_view;
	}
	
	// push my dictionary item
	public void pushMyDicItem(DictionaryItem item){
		this.myDicItemsList.add(item);
		
		Button b = (Button) my_view.findViewById(R.id.my_del_sel_word);
		
		if(this.myDicItemsList.size() == 1){
			ListView lv = (ListView) my_view.findViewById(R.id.my_dictionary_list);
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lv.getLayoutParams();
			params.weight = 0.1f;
			lv.setLayoutParams(params);
			
			LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) b.getLayoutParams();
			p.weight = 0.9f;
			b.setLayoutParams(p);
			b.setVisibility(View.VISIBLE);
		}
		
		String counter = String.valueOf(this.myDicItemsList.size());
		b.setText("Delete " + counter);
		
	}
	
	// push online dictionary item
	public void pushOnlineDicItem(DictionaryItem item){
		this.onlineDicItemsList.add(item);
		
		Button b = (Button) online_view.findViewById(R.id.online_download_sel_word);
		
		if(this.onlineDicItemsList.size() == 1){
			ListView lv = (ListView) online_view.findViewById(R.id.online_dictionary_list);
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lv.getLayoutParams();
			params.weight = 0.1f;
			lv.setLayoutParams(params);
			
			LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) b.getLayoutParams();
			p.weight = 0.9f;
			b.setLayoutParams(p);
			b.setVisibility(View.VISIBLE);
		}
		
		String counter = String.valueOf(this.onlineDicItemsList.size());
		b.setText("Download " + counter);
	}
	
	// clear my dictionary item
	public void clearMyDicItems(){
		this.myDicItemsList.clear();
	}
	
	// clear online dictionary item
	public void clearOnlineDicItems(){
		this.onlineDicItemsList.clear();
	}
	
	// pop my dictionary item
	public void popMyDicItem(DictionaryItem item){
		
		int did = item.getID();
		int pos = 0;
		
		for(DictionaryItem single_item : this.myDicItemsList){
			if(did == single_item.getID()){
				myDicItemsList.remove(pos);
				break;
			}
			else{
				++pos;
			}
		}
		
		Button b = (Button) my_view.findViewById(R.id.my_del_sel_word);
		
		if(this.myDicItemsList.size() == 0){
			ListView lv = (ListView) my_view.findViewById(R.id.my_dictionary_list);
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lv.getLayoutParams();
			params.weight = 0f;
			lv.setLayoutParams(params);
			
			LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) b.getLayoutParams();
			p.weight = 1f;
			b.setLayoutParams(p);
			b.setVisibility(View.GONE);
		}
		
		String counter = String.valueOf(this.myDicItemsList.size());
		b.setText("Delete " + counter);
	}
	
	// pop online dictionary item
	public void popOnlineDicItem(DictionaryItem item){
		
		int did = item.getID();
		int pos = 0;
		
		for(DictionaryItem single_item : this.onlineDicItemsList){
			if(did == single_item.getID()){
				onlineDicItemsList.remove(pos);
				break;
			}
			else{
				++pos;
			}
		}
		
		Button b = (Button) online_view.findViewById(R.id.online_download_sel_word);
		
		if(this.onlineDicItemsList.size() == 0){
			ListView lv = (ListView) online_view.findViewById(R.id.online_dictionary_list);
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lv.getLayoutParams();
			params.weight = 0f;
			lv.setLayoutParams(params);
			
			LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) b.getLayoutParams();
			p.weight = 1f;
			b.setLayoutParams(p);
			b.setVisibility(View.GONE);
		}
		
		String counter = String.valueOf(this.onlineDicItemsList.size());
		b.setText("Download " + counter);
	}
	
	// update my dictionary delete button
	public void updateMyDelButton(DictionaryItem item){
		
		int did = item.getID();
		int pos = 0;
		
		for(DictionaryItem single_item : this.myDicItemsList){
			if(did == single_item.getID()){
				myDicItemsList.remove(pos);
				break;
			}
			else{
				++pos;
			}
		}
		
		Button delBtn = (Button) my_view.findViewById(R.id.my_del_sel_word);
		
		int len = myDicItemsList.size();
		if(len == 0){
			ListView lv = (ListView) my_view.findViewById(R.id.my_dictionary_list);
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lv.getLayoutParams();
			params.weight = 0f;
			lv.setLayoutParams(params);
			
			LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) delBtn.getLayoutParams();
			p.weight = 1f;
			delBtn.setLayoutParams(p);
			delBtn.setVisibility(View.GONE);
		}
		
		delBtn.setText(String.valueOf("Delete " + len));
	}
	
	// update online dictionary delete button
	public void updateOnlineDownloadButton(DictionaryItem item){
		
		int did = item.getID();
		int pos = 0;
		
		for(DictionaryItem single_item : this.onlineDicItemsList){
			if(did == single_item.getID()){
				onlineDicItemsList.remove(pos);
				break;
			}
			else{
				++pos;
			}
		}
		
		Button b = (Button) online_view.findViewById(R.id.online_download_sel_word);
		
		int len = onlineDicItemsList.size();
		if(len == 0){
			ListView lv = (ListView) online_view.findViewById(R.id.online_dictionary_list);
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lv.getLayoutParams();
			params.weight = 0f;
			lv.setLayoutParams(params);
			
			LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) b.getLayoutParams();
			p.weight = 1f;
			b.setLayoutParams(p);
			b.setVisibility(View.GONE);
		}
		
		b.setText(String.valueOf("Download " + len));
	}
	
	// hide my delete button
	public void hideMyDelButton(){
		
		ListView lv = (ListView) my_view.findViewById(R.id.my_dictionary_list);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lv.getLayoutParams();
		params.weight = 0f;
		lv.setLayoutParams(params);
		
		Button delBtn = (Button) my_view.findViewById(R.id.my_del_sel_word);
		LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) delBtn.getLayoutParams();
		p.weight = 1f;
		delBtn.setLayoutParams(p);
		delBtn.setVisibility(View.GONE);
		delBtn.setText(String.valueOf("Delete 0"));
	}
	
	// hide online download button
	public void hideOnlineDownloadButton(){
		
		ListView lv = (ListView) online_view.findViewById(R.id.online_dictionary_list);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lv.getLayoutParams();
		params.weight = 0f;
		lv.setLayoutParams(params);
		
		Button b = (Button) online_view.findViewById(R.id.online_download_sel_word);
		LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) b.getLayoutParams();
		p.weight = 1f;
		b.setLayoutParams(p);
		b.setVisibility(View.GONE);
		b.setText(String.valueOf("Download 0"));
	}
	
	// set my dictionary load more button
	public void setMyDicLMB(Button btn){
		btnMyDicLoadMore = btn;
		btnMyDicLoadMore.setTextColor(Color.WHITE);
		btnMyDicLoadMore.setBackgroundResource(R.drawable.loadmore_word_btn);
	}
	
	/** Getter ********************************************************************************************************/
	
	public String getAdminToken(){
		return admin_token;
	}
	
	// get start number
	public int getStartItem(){
		return start_item;
	}
	
	// get limit number
	public int getLImitItem(){
		return limit_item;
	}
	
	// get page total
	public int getPageTotal(){
		return page_total;
	}
	
	// get total
	public int getTotal(){
		return total;
	}
	
	// get last page
	public boolean getLastPage(){
		return last_page;
	}
	
	// get my view
	public View getMyView(){
		return my_view;
	}
	
	// get online view
	public View getOnlineView(){
		return online_view;
	}
	
	// get my dictionary item list
	public ArrayList<DictionaryItem> getMyDicItems(){
		return this.myDicItemsList;
	}
	
	// get online dictionary list
	public ArrayList<DictionaryItem> getOnlineDicItems(){
		return this.onlineDicItemsList;
	}
	
	// get my dictionary load more button
	public Button getMyDicLMB(){
		return this.btnMyDicLoadMore;
	}
}
