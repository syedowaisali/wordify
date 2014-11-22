package com.appz2x.wordify;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


@SuppressLint("ValidFragment")
public class MyDictionary extends Fragment {
	
private Activity activity;
	
	public MyDictionary(Activity a){
		activity = a;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View rootView = inflater.inflate(R.layout.my_dictionary, container, false);
		ListView lv = (ListView)rootView.findViewById(R.id.my_dictionary_list);
		lv.setEmptyView(rootView.findViewById(R.id.my_empty_view));
		
		PageOptionStorage.getInstance().setMyView(rootView);
        
		// build my dictionary
    	BuildMyDictionary bmd = new BuildMyDictionary(activity);
		bmd.build(lv);
        
		return rootView;
	}
	
}
