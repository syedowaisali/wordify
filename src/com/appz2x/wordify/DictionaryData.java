package com.appz2x.wordify;

import java.util.ArrayList;
import java.util.List;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DictionaryData extends SQLiteAssetHelper{
	
	// database version
	private static final int DATABASE_VERSION = 2;
	
	// database name
	private static final String DATABASE_NAME = "wordify.db";
	
	// dictionary table name
	private static final String TABLE_NAME = "dictionary";
	private static final String SCORE_TABLE = "score";
	
	// dictionary table column name
	private static final String ID	        = "id";
	private static final String DIC_ID      = "dic_id";
	private static final String DIC_WORD    = "dic_word";
	private static final String DIC_MEANING = "dic_meaning";
	private static final String DIC_FAV	    = "dic_fav";
	
	// score table column name
	private static final String SC_ID    = "id";
	private static final String SC_SCORE = "score";
	
	// constructor
	public DictionaryData(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	// create
	/*@Override
	public void onCreate(SQLiteDatabase db){
		
		// create dictionary table tables
		String createFormTable = "CREATE TABLE " + TABLE_NAME + "(" +
				ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
				DIC_ID + " CHAR(255), " +
				DIC_WORD + " CHAR(255), " +
				DIC_MEANING + " CHAR(255), " +
				DIC_FAV + " CHAR(255)  )";
		
		db.execSQL(createFormTable);
		
		// create score table
		String createScoreTable = "CREATE TABLE " + SCORE_TABLE + "(" +
				SC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				SC_SCORE + "INT(11) )";
		
		db.execSQL(createScoreTable);
	}*/
	
	// upgrade
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		
		// drop older table if exist
		if(oldVersion != newVersion){
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			/*db.execSQL("DROP TABLE IF EXISTS " + SCORE_TABLE);*/
		}
		
		// create table again
		onCreate(db);
	}
	
	// adding new item
	public void addNewItem(DictionaryItem item){
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(DIC_ID, item.getID());
		values.put(DIC_WORD, item.getWord());
		values.put(DIC_MEANING, item.getRowMeaning());
		values.put(DIC_FAV, "no");
		
		// inserting row
		db.insert(TABLE_NAME, null, values);
		
		// close database  connection
		db.close();
	}
	
	// getting single item
	public DictionaryItem getItem(int item_id){
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		String[] columns = new String[]{DIC_ID, DIC_WORD, DIC_MEANING, DIC_FAV};
		String[] whereArgs = new String[]{String.valueOf(item_id)};
		Cursor cursor = db.query(TABLE_NAME, columns, DIC_ID + " = ?", whereArgs, null, null, null, null);
		
		if(cursor != null){
			cursor.moveToFirst();
		}
		
		DictionaryItem item = new DictionaryItem();
		item.setId(cursor.getInt(cursor.getColumnIndex(ID)));
		item.setWord(cursor.getString(cursor.getColumnIndex(DIC_WORD)));
		item.setRowMeaning(cursor.getString(cursor.getColumnIndex(DIC_MEANING)));
		item.setFav(cursor.getString(cursor.getColumnIndex(DIC_FAV)));
		
		// return item
		return item;
	}
	
	// getting random item
	public DictionaryItem getRandItem(){
	
		String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY RANDOM() LIMIT 1";
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if(cursor != null){
			cursor.moveToFirst();
		}
		
		DictionaryItem item = new DictionaryItem();
		item.setId(cursor.getInt(cursor.getColumnIndex(ID)));
		item.setWord(cursor.getString(cursor.getColumnIndex(DIC_WORD)));
		item.setRowMeaning(cursor.getString(cursor.getColumnIndex(DIC_MEANING)));
		item.setFav(cursor.getString(cursor.getColumnIndex(DIC_FAV)));
		
		// return item
		return item;
	}
	
	// getting all items
	public List<DictionaryItem> getAllItems(){
		
		List<DictionaryItem> items = new ArrayList<DictionaryItem>();
		
		// select all query
		String selectQuery = "SELECT * FROM " + TABLE_NAME;
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		// looping through all rows and adding to forms list
		if(cursor.moveToFirst()){
			do{
				DictionaryItem item = new DictionaryItem();
				item.setId(cursor.getInt(cursor.getColumnIndex(DIC_ID)));
				item.setWord(cursor.getString(cursor.getColumnIndex(DIC_WORD)));
				item.setRowMeaning(cursor.getString(cursor.getColumnIndex(DIC_MEANING)));
				item.setFav(cursor.getString(cursor.getColumnIndex(DIC_FAV)));
				
				// adding form to forms list
				items.add(item);
			}while(cursor.moveToNext());
		}
		
		// close connection
		cursor.close();
		
		// return item list
		return items;
	}
	
	// updating single item
	public int updateItem(DictionaryItem item){
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(DIC_WORD, item.getWord());
		values.put(DIC_MEANING, item.getRowMeaning());
		values.put(DIC_FAV, item.getFav());
		
		String[] whereArgs = new String[]{String.valueOf(item.getID())};
		
		// updating row
		return db.update(TABLE_NAME, values, DIC_ID + " = ?", whereArgs);
	}
	
	// delete single item
	public void deleteItem(int item_id){
		
		SQLiteDatabase db = this.getWritableDatabase();
		String[] whereArgs = new String[]{String.valueOf(item_id)};
		db.delete(TABLE_NAME, DIC_ID + " = ?", whereArgs);
		db.close();
	}
	
	// getting items count
	public int getItemsCount(){
		
		String countQuery = "SELECT * FROM " + TABLE_NAME;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		
		// return count
		int total_items = cursor.getCount();
		cursor.close();
		
		return total_items;
	}
	
	// getting score count
	public int getScoreCount(){
		
		String countQuery = "SELECT * FROM " + SCORE_TABLE;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		
		// return count
		int total_items = cursor.getCount();
		cursor.close();
		
		return total_items;
	}
	
	// check word exists
	public boolean isExistsWord(String word){
		
		String countQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + DIC_WORD + " = '" + word + "'";
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		
		// return count
		int item_found = cursor.getCount();
		cursor.close();
		
		if(item_found > 0){
			return true;
		}
		else{
			return false;
		}
	}

	// getting pagination items
	public List<DictionaryItem> getPagingItem(int page){
		
		List<DictionaryItem> items = new ArrayList<DictionaryItem>();
		
		// select all query
		String selectQuery = "SELECT * FROM " + TABLE_NAME;
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		int total_item = cursor.getCount();
		int item_per_request = Setting.ITEM_PER_REQUEST;
		int start;
		int start_item;
		int limit_item;
		int total_pages = 0;
		boolean is_last_page;
		
		
		if(total_item > 0){
			start = (page == 1) ? 0 : ((page - 1) * item_per_request);
			
			if(total_item > item_per_request){
				int rem = total_item % item_per_request;
				int round_item = total_item - rem;
				total_pages = (rem > 0) ? (round_item / item_per_request) + 1 : (round_item / item_per_request);
				
				if(page == total_pages){
					limit_item = total_item;
				}
				else{
					limit_item = (item_per_request * page);
				}
				
				start_item = ((item_per_request * page) - item_per_request) + 1;
			}
			else{
				total_pages = 1;
				limit_item = total_item;
				start_item = 1;
			}
			
			if(page <= total_pages){
				is_last_page = (page == total_pages) ? true : false;
				
				String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + DIC_ID + " DESC LIMIT " + start + ", " + item_per_request;
				
				SQLiteDatabase rsl = this.getReadableDatabase();
				Cursor row = rsl.rawQuery(sql, null);
				

				// looping through all rows and adding to forms list
				if(row.moveToFirst()){
					do{
						DictionaryItem item = new DictionaryItem();
						item.setId(row.getInt(row.getColumnIndex(DIC_ID)));
						item.setWord(row.getString(row.getColumnIndex(DIC_WORD)));
						item.setRowMeaning(row.getString(row.getColumnIndex(DIC_MEANING)));
						item.setFav(row.getString(row.getColumnIndex(DIC_FAV)));
						
						// adding form to forms list
						items.add(item);
					}while(row.moveToNext());
				}
				
				// close connection
				row.close();
				
				PageOptionStorage.getInstance().setStartItem(start_item);
				PageOptionStorage.getInstance().setLimitItem(limit_item);
				PageOptionStorage.getInstance().setPageTotal(item_per_request);
				PageOptionStorage.getInstance().setTotal(total_item);
				PageOptionStorage.getInstance().setLastPage(is_last_page);
			}
		}
		
		// return items
		return items;
	}
	
	// get search items
	public List<DictionaryItem> getSearchItems(String key){
		
		List<DictionaryItem> items = new ArrayList<DictionaryItem>();
		
		// select all query
		String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + DIC_WORD + " LIKE('%" + key + "%') OR " + DIC_MEANING + " LIKE('%" + key + "%')";
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		// looping through all rows and adding to forms list
		if(cursor.moveToFirst()){
			do{
				DictionaryItem item = new DictionaryItem();
				item.setId(cursor.getInt(cursor.getColumnIndex(DIC_ID)));
				item.setWord(cursor.getString(cursor.getColumnIndex(DIC_WORD)));
				item.setRowMeaning(cursor.getString(cursor.getColumnIndex(DIC_MEANING)));
				item.setFav(cursor.getString(cursor.getColumnIndex(DIC_FAV)));
				
				// adding form to forms list
				items.add(item);
			}while(cursor.moveToNext());
		}
		
		// close curosr
		cursor.close();
		
		// return all search items
		return items;
	}
	
	// add new score
	public void addNewScore(Score score){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(SC_SCORE, score.getScore());
		
		// inserting row
		db.insert(SCORE_TABLE, null, values);
		
		// close database  connection
		db.close();
	}
	
	// get score scores
	public List<Score> getScores(){
		
		List<Score> scores = new ArrayList<Score>();
		
		// select all query
		String selectQuery = "SELECT * FROM " + SCORE_TABLE;
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		// looping through all rows and adding to scores list
		if(cursor.moveToFirst()){
			do{
				Score score = new Score();
				score.setId(cursor.getInt(cursor.getColumnIndex(SC_ID)));
				score.setScore(cursor.getInt(cursor.getColumnIndex(SC_SCORE)));
				
				// adding form to forms list
				scores.add(score);
			}while(cursor.moveToNext());
		}
		
		// close curosr
		cursor.close();
		
		// return all search items
		return scores;
	}
}
