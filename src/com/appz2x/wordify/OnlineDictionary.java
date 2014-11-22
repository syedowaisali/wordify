package com.appz2x.wordify;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class OnlineDictionary extends Fragment {
	 
	private Activity activity;
	
	public OnlineDictionary(Activity a){
		activity = a;
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.online_dictionary, container, false);
        ListView lv = (ListView)rootView.findViewById(R.id.online_dictionary_list);
        
        PageOptionStorage.getInstance().setOnlineView(rootView);
        
        if(new ConnectionDetector(activity).isConnectingToInternet()){
			
        	// build online dictionary
        	BuildOnlineDictionary bod = new BuildOnlineDictionary(activity);
			bod.build(lv);
		}
		else{
			lv.setVisibility(View.GONE);
			TextView dic_online_msg = (TextView) rootView.findViewById(R.id.online_dic_msg);
			dic_online_msg.setText("Internet not connected.");
			dic_online_msg.setVisibility(View.VISIBLE);
		}
        
        return rootView;
    }
}
