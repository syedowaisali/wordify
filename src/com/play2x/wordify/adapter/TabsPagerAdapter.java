package com.play2x.wordify.adapter;

import com.appz2x.wordify.MyDictionary;
import com.appz2x.wordify.OnlineDictionary;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	Activity activity;
	
	public TabsPagerAdapter(FragmentManager fm, Activity a) {
		super(fm);
		activity = a;
	}

	@Override
	public Fragment getItem(int index) {
		// TODO Auto-generated method stub
		switch (index) {
        case 0:
            // Top Rated fragment activity
            return new MyDictionary(activity);
        case 1:
            // Games fragment activity
            return new OnlineDictionary(activity);
        }
		
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}

}
