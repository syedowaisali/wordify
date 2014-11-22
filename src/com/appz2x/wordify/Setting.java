package com.appz2x.wordify;

import java.util.ArrayList;

import com.play2x.wordify.adapter.MyDictionaryViewAdapter;
import com.play2x.wordify.adapter.OnlineDictionaryViewAdapter;

public class Setting {
	
	// status code
	public static final int ERROR = 0;
	public static final int SUCCESS = 1;
	public static final int INFO = 2;
	public static final int EMPTY = 0;
	public static final String CHECK_OUT = "check_out";
	public static final String CHECK_IN = "check_in";
	public static final String PHONE_NUMBER = "03122672245";
	
	public static OnlineDictionaryViewAdapter onlineAdapter;
	public static MyDictionaryViewAdapter myAdapter;
	public static ArrayList<DictionaryItem> myDicItems;
	
	// dictionary item per request
	public static final int ITEM_PER_REQUEST = 10;
	
	// set 1 second to splash screen time out
	public static final int SPLASH_TIMEOUT = 3000;
	
	// web service url
	public static final String WEB_URL = "http://www.mypromoanimation.com/dic/web_service";
	
	// bearer access token 
	public static final String TOKEN = "63d9cc58df7e024621b4a7c3507736c2d1ffe9a94ca4dab0093c48773a715351d7af0275f44c1cb015681392326b62b4";
	
	
	
}
