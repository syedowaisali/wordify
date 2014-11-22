package com.appz2x.wordify;

import java.util.HashSet;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs{
	
	///////////////////////////////////////////////
	// CONSTANTS
	///////////////////////////////////////////////
	
	public static final SharedPrefs INSTANCE = new SharedPrefs();
	
	///////////////////////////////////////////////
	// VARIABLES
	///////////////////////////////////////////////
	
	private SharedPreferences sharedPrefs;
	private SharedPreferences.Editor editor;
	
	///////////////////////////////////////////////
	// CONSTRUCTOR
	///////////////////////////////////////////////
	
	public SharedPrefs(){}
	
	///////////////////////////////////////////////
	// INSTANCE
	///////////////////////////////////////////////
	
	public static SharedPrefs getInstance(){
		return INSTANCE;
	}
	
	///////////////////////////////////////////////
	// SETTER'S
	///////////////////////////////////////////////
	
	public void add(String key, String value){
		editor.putString(key, value);
		commit();
	}

	@SuppressLint("NewApi")
	public void add(String key, Set<String> values){
		editor.putStringSet(key, values);
		commit();
	}
	
	public void add(String key, int value){
		editor.putInt(key, value);
		commit();
	}
	
	public void add(String key, float value){
		editor.putFloat(key, value);
		commit();
	}
	
	public void add(String key, boolean value){
		editor.putBoolean(key, value);
		commit();
	}
	
	public void add(String key, long value){
		editor.putLong(key, value);
		commit();
	}
	
	
	///////////////////////////////////////////////
	// GETTER'S
	///////////////////////////////////////////////
	
	public String getString(String key){
		return sharedPrefs.getString(key, "");
	}
	
	@SuppressLint("NewApi")
	public Set<String> getStringSet(String key){
		return sharedPrefs.getStringSet(key, new HashSet<String>());
	}
	
	public int getInt(String key){
		return sharedPrefs.getInt(key, 0);
	}
	
	public float getFloat(String key){
		return sharedPrefs.getFloat(key, 0f);
	}
	
	public boolean getBoolean(String key){
		return sharedPrefs.getBoolean(key, false);
	}
	
	public long getLong(String key){
		return sharedPrefs.getLong(key, 0);
	}
	
	///////////////////////////////////////////////
	// DESTROY
	///////////////////////////////////////////////
	
	public void remove(String key){
		editor.remove(key);
		commit();
	}
	
	///////////////////////////////////////////////
	// CHECKING
	///////////////////////////////////////////////
	
	public boolean contain(String key){
		return sharedPrefs.contains(key);
	}
	
	///////////////////////////////////////////////
	// INITIALIZE
	///////////////////////////////////////////////
	
	public void init(Context context){
		sharedPrefs = context.getSharedPreferences(AppHelper.MY_PREFERENCE, Context.MODE_PRIVATE);
		editor = sharedPrefs.edit();
	}
	
	///////////////////////////////////////////////
	// 	HELPERS
	///////////////////////////////////////////////
	
	private void commit(){
		editor.commit();
	}
}
