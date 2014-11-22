package com.play2x.wordify.adapter;

import java.util.ArrayList;

import com.appz2x.wordify.BuildMyDictionary;
import com.appz2x.wordify.BuildOnlineDictionary;
import com.appz2x.wordify.ConnectionDetector;
import com.appz2x.wordify.DictionaryData;
import com.appz2x.wordify.DictionaryItem;
import com.appz2x.wordify.PageOptionStorage;
import com.appz2x.wordify.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyDictionaryViewAdapter extends BaseAdapter{
	
	public static final int DELETE = 0;
	private static LayoutInflater inflater = null;
	private ArrayList<DictionaryItem> data;
	private Activity activity;
	Typeface font;
	
	
	public MyDictionaryViewAdapter(Activity a, ArrayList<DictionaryItem> d){
		activity = a;
		data = d;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		font = Typeface.createFromAsset(activity.getAssets(), "fonts/oswald_regular.otf");
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;
		final ViewHolder holder;
		
		final DictionaryItem item = data.get(position);
		item.setPosition(position);
		
		if(convertView == null){
			vi = inflater.inflate(R.layout.dictionary_item, null);
			holder = new ViewHolder();
			
			holder.word_label = (TextView) vi.findViewById(R.id.dic_word_label);
			holder.meaning_label = (TextView) vi.findViewById(R.id.dic_meaning_label);
			holder.word = (TextView) vi.findViewById(R.id.dic_word);
			holder.meaning = (TextView) vi.findViewById(R.id.dic_meaning);
			holder.check_image = (ImageView) vi.findViewById(R.id.item_check);
			holder.notify_image = (ImageView) vi.findViewById(R.id.item_notify);
			holder.topBar = (LinearLayout) vi.findViewById(R.id.blue_bar);
			
			vi.setTag(holder);
		}
		else{
			holder = (ViewHolder) vi.getTag();
		}
		
		// setting all values in listview

		holder.topBar.setBackgroundColor(Color.RED);
		
		holder.word_label.setTypeface(font);
		
		holder.meaning_label.setTypeface(font);
		
		holder.word.setText(item.getWord());
		holder.word.setTypeface(font);
		
		holder.meaning.setText(item.getRowMeaning());
		holder.meaning.setTypeface(font);

		holder.notify_image.setVisibility(View.GONE);
		
		holder.check_image.setImageResource(R.drawable.ic_checkout);
		
		if(item.getCheckState()){
			holder.check_image.setImageResource(R.drawable.ic_checkin);
			item.setCheckState(true);
		}
		
		/**
         * Listening to listview single row selected
         * **/
		vi.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if( ! item.getCheckState()){
					holder.check_image.setImageResource(R.drawable.ic_checkin);
					item.setCheckState(true);
					PageOptionStorage.getInstance().pushMyDicItem(item);
				}
				else{
					holder.check_image.setImageResource(R.drawable.ic_checkout);
					item.setCheckState(false);
					PageOptionStorage.getInstance().popMyDicItem(item);
				}
			}
		});
		
		/**
         * Listening to listview single row selected
         * **/
		vi.setOnLongClickListener(new OnLongClickListener(){
			
			@Override
			public boolean onLongClick(View view){
				
				AlertDialog.Builder adb = new AlertDialog.Builder(activity);
				CharSequence opts[] = new CharSequence[]{"Delete"};
				adb.setItems(opts, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
						switch(which){
						
						case DELETE:
							
							new AlertDialog.Builder(activity)
							.setTitle("Confirmation")
							.setMessage("Are you sure you want to delete this word.")
							.setPositiveButton("Yes", new OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									DictionaryData dd = new DictionaryData(activity);
									dd.deleteItem(item.getID());
									BuildMyDictionary.myDicItems.remove(position);
									BuildMyDictionary.notifyAdapter();
									if(new ConnectionDetector(activity).isConnectingToInternet()){
										BuildOnlineDictionary.notifyAdapter();
									}
									
									// if row is selected
									if(item.getCheckState()){
										PageOptionStorage.getInstance().updateMyDelButton(item);
									}
								}
							})
							.setNegativeButton("No", new OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									
								}
							})
							.show();
							
							break;
						}
					}

				});
				//adb.setNegativeButton("Cancel", null);
				adb.setTitle("Select Options");
				AlertDialog alert = adb.create();
				alert.show();
				
				return true;
			}
		});
		
		return vi;
	}
	
	static class ViewHolder{
		TextView word_label;
		TextView meaning_label;
		TextView word;
		TextView meaning;
		ImageView check_image;
		ImageView notify_image;
		LinearLayout topBar;
	}
}
