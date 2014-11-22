package com.play2x.wordify.adapter;

import java.util.ArrayList;

import com.appz2x.wordify.BuildMyDictionary;
import com.appz2x.wordify.BuildOnlineDictionary;
import com.appz2x.wordify.DictionaryData;
import com.appz2x.wordify.DictionaryItem;
import com.appz2x.wordify.PageOptionStorage;
import com.appz2x.wordify.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class OnlineDictionaryViewAdapter extends BaseAdapter{
	
	private Activity activity;
	private ArrayList<DictionaryItem> data;
	private static LayoutInflater inflater = null;
	public static final int DOWNLOAD = 0;
	public static final int DELETE = 1;
	Typeface font;
	
	public OnlineDictionaryViewAdapter(Activity a, ArrayList<DictionaryItem> d){
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;
		final ViewHolder holder;
		
		final DictionaryItem item = data.get(position);
		
		if(convertView == null){
			vi = inflater.inflate(R.layout.dictionary_item, parent, false);
			holder = new ViewHolder();
			
			holder.word_label = (TextView) vi.findViewById(R.id.dic_word_label);
			holder.meaning_label = (TextView) vi.findViewById(R.id.dic_meaning_label);
			holder.word = (TextView) vi.findViewById(R.id.dic_word);
			holder.meaning = (TextView) vi.findViewById(R.id.dic_meaning);
			holder.check_image = (ImageView) vi.findViewById(R.id.item_check);
			holder.notify_image = (ImageView) vi.findViewById(R.id.item_notify);
			vi.setTag(holder);
		}
		else{
			holder = (ViewHolder) vi.getTag();
		}
		
		// setting all values in listview

		holder.word_label.setTypeface(font);
		
		holder.meaning_label.setTypeface(font);
		
		holder.word.setText(item.getWord());
		holder.word.setTypeface(font);
		
		holder.meaning.setText(item.getRowMeaning());
		holder.meaning.setTypeface(font);
		
		holder.notify_image.setImageResource(R.drawable.notify_off);
		
		holder.check_image.setImageResource(R.drawable.ic_checkout);
		holder.check_image.setVisibility(View.VISIBLE);
		
		
		// checking duplication
		DictionaryData dd = new DictionaryData(activity);
		if( dd.isExistsWord(item.getWord())){
			holder.notify_image.setImageResource(R.drawable.notify_on);
		}
		
		vi.setOnClickListener(null);
		vi.setOnLongClickListener(null);
		
		if( ! dd.isExistsWord(item.getWord())){
			
			if(item.getCheckState()){
				holder.check_image.setImageResource(R.drawable.ic_checkin);
				item.setCheckState(true);
			}
			
			/**
	         * Listening to listview single row selected
	         * **/
			vi.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if( ! item.getCheckState()){
						holder.check_image.setImageResource(R.drawable.ic_checkin);
						item.setCheckState(true);
						PageOptionStorage.getInstance().pushOnlineDicItem(item);
					}
					else{
						holder.check_image.setImageResource(R.drawable.ic_checkout);
						item.setCheckState(false);
						PageOptionStorage.getInstance().popOnlineDicItem(item);
					}
				}
			});
		}
		else{
			holder.check_image.setVisibility(View.GONE);
		}
		
		if( ! dd.isExistsWord(item.getWord())){
			/**
	         * Listening to listview single row selected
	         * **/
			vi.setOnLongClickListener(new OnLongClickListener(){
				
				@Override
				public boolean onLongClick(View view){
					
					AlertDialog.Builder adb = new AlertDialog.Builder(activity);
					CharSequence opts[] = new CharSequence[]{"Download"};
					adb.setItems(opts, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
							switch(which){
							case DOWNLOAD:
								
								// add new item to dictionary database
								DictionaryData dd = new DictionaryData(activity);
								
								if( ! dd.isExistsWord(item.getWord())){
									dd.addNewItem(item);
									BuildMyDictionary.myDicItems.add(item);
									BuildMyDictionary.notifyAdapter();
									BuildOnlineDictionary.notifyAdapter();
									Toast.makeText(activity, "Successfully Donwload " + item.getWord(), Toast.LENGTH_LONG).show();
									// if row is selected
									if(item.getCheckState()){
										item.setCheckState(false);
										PageOptionStorage.getInstance().updateOnlineDownloadButton(item);
									}
								}
								else{
									Toast.makeText(activity, "Download Failed " + item.getWord() + " Already Exists ", Toast.LENGTH_LONG).show();
								}
								
								break;
							}
						}

					});
					//adb.setNegativeButton("Cancel", null);
					adb.setTitle("Select Option");
					AlertDialog alert = adb.create();
					alert.show();
					
					return true;
				}
			});
		}
		
		return vi;
	}
	
	static class ViewHolder{
		TextView word_label;
		TextView meaning_label;
		TextView word;
		TextView meaning;
		ImageView check_image;
		ImageView notify_image;
	}
}

