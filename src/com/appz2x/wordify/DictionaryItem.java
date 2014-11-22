package com.appz2x.wordify;

import org.json.JSONArray;

public class DictionaryItem {
	
	int id = 0;						// item id
	int position;					// store item position
	String date_time = null;		// item date time
	String word = null;				// item word
	JSONArray meanings;				// item meanings
	String row_meaning;				// item row meaning
	String fav;						// item favourite
	boolean check_state;			// item check state
	
	/* Setting Functions */
	
	// set id
	public void setId(int _id){
		this.id = _id;
	}
	
	// set position
	public void setPosition(int _pos){
		this.position = _pos;
	}
	
	// set date time
	public void setDatetime(String _dt){
		this.date_time = _dt;
	}
	
	//set word
	public void setWord(String _word){
		this.word = _word;
	}
	
	// set meanings
	public void setMeanings(JSONArray _meanings){
		this.meanings = _meanings;
	}
	
	// set row meaning
	public void setRowMeaning(String _row_meaning){
		this.row_meaning = _row_meaning;
	}
	
	// set favourite
	public void setFav(String _fav){
		this.fav = _fav;
	}
	
	// set check states
	public void setCheckState(boolean state){
		this.check_state = state;
	}
	
	/* Getter Functions */
	
	// get id
	public int getID(){
		return this.id;
	}
	
	// get date time
	public String getDatetime(){
		return this.date_time;
	}
	
	// get word
	public String getWord(){
		return this.word;
	}
	
	// get meanings
	public JSONArray getMeanings(){
		return this.meanings;
	}
	
	// get row meaning
	public String getRowMeaning(){
		return this.row_meaning;
	}
	
	// get favourite
	public String getFav(){
		return this.fav;
	}
	
	// get check state
	public boolean getCheckState(){
		return this.check_state;
	}
	
	// get item position
	public int getPosition(){
		return this.position;
	}
}


