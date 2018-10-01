package com.example.favorite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "AddtoFav";
	private static final String TABLE_NAME = "Favorite";
	private static final String KEY_ID = "id";
	private static final String KEY_HID = "hid";
 	private static final String KEY_HNAME = "hname";
	private static final String KEY_HIMAGE = "himage";
	private static final String KEY_HVIDEO = "hvideo";
	private static final String KEY_HDESC = "hdesc";
	private static final String KEY_HLATI = "hlati";
	private static final String KEY_HLONGI = "hlongi";
	private static final String KEY_HADDRE = "haddr";
	private static final String KEY_HWEB = "hweb";
	private static final String KEY_HPHONE = "hphone";
	private static final String KEY_HMAIL = "hmail";
	private static final String KEY_HRATE = "hrate";



	public DatabaseHandler(Context context) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," 
				+ KEY_HID + " TEXT,"
 				+ KEY_HNAME + " TEXT,"
				+ KEY_HIMAGE + " TEXT,"
				+ KEY_HVIDEO + " TEXT,"
				+ KEY_HDESC + " TEXT,"
				+ KEY_HLATI + " TEXT,"
				+ KEY_HLONGI + " TEXT,"
				+ KEY_HADDRE + " TEXT,"
				+ KEY_HWEB + " TEXT,"
				+ KEY_HPHONE + " TEXT,"
				+ KEY_HMAIL + " TEXT,"
				+ KEY_HRATE + " TEXT"
				+ ")";
		db.execSQL(CREATE_CONTACTS_TABLE);		

	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

		// Create tables again
		onCreate(db);
	}

	//Adding Record in Database

	public	void AddtoFavorite(Pojo pj)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_HID, pj.H_id());
 		values.put(KEY_HNAME, pj.getHotel_name());
		values.put(KEY_HIMAGE, pj.getHotel_image());
		values.put(KEY_HVIDEO, pj.getHotel_video());
		values.put(KEY_HDESC, pj.getHotel_description());
		values.put(KEY_HLATI, pj.getHotel_map_latitude());
		values.put(KEY_HLONGI, pj.getHotel_map_longitude());
		values.put(KEY_HADDRE, pj.getHotel_address());
		values.put(KEY_HMAIL, pj.getHotel_eamil());
		values.put(KEY_HPHONE, pj.getHotel_phone());
		values.put(KEY_HWEB, pj.getHotel_webb());
		values.put(KEY_HRATE,pj.getHotel_rate());

		// Inserting Row
		db.insert(TABLE_NAME, null, values);
		db.close(); // Closing database connection

	}

	// Getting All Data
	public List<Pojo> getAllData() 
	{
		List<Pojo> dataList = new ArrayList<Pojo>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_NAME;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) 
		{
			do {
				Pojo contact = new Pojo();
				contact.setId(Integer.parseInt(cursor.getString(0)));
				contact.setH_id(cursor.getString(1));
 				contact.setHotel_name(cursor.getString(2));
				contact.setHotel_image(cursor.getString(3));
				contact.setHotel_video(cursor.getString(4));
				contact.setHotel_description(cursor.getString(5));
				contact.setHotel_map_latitude(cursor.getString(6));
				contact.setHotel_map_longitude(cursor.getString(7));
				contact.setHotel_address(cursor.getString(8));
				contact.setHotel_eamil(cursor.getString(9));
				contact.setHotel_phone(cursor.getString(10));
				contact.setHotel_webb(cursor.getString(11));
				contact.setHotel_rate(cursor.getString(12));

				// Adding contact to list
				dataList.add(contact);
			} while (cursor.moveToNext());
		}

		// return contact list
		return dataList;
	}

	//getting single row

	public List<Pojo> getFavRow(String id) 
	{
		List<Pojo> dataList = new ArrayList<Pojo>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_NAME +" WHERE hid="+id;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) 
		{
			do {
				Pojo contact = new Pojo();
				contact.setId(Integer.parseInt(cursor.getString(0)));
				contact.setH_id(cursor.getString(1));
 				contact.setHotel_name(cursor.getString(2));
				contact.setHotel_image(cursor.getString(3));
				contact.setHotel_video(cursor.getString(4));
				contact.setHotel_description(cursor.getString(5));
				contact.setHotel_map_latitude(cursor.getString(6));
				contact.setHotel_map_longitude(cursor.getString(7));
				contact.setHotel_address(cursor.getString(8));
				contact.setHotel_eamil(cursor.getString(9));
				contact.setHotel_phone(cursor.getString(10));
				contact.setHotel_webb(cursor.getString(11));
				contact.setHotel_rate(cursor.getString(12));

				// Adding contact to list
				dataList.add(contact);
			} while (cursor.moveToNext());
		}

		// return contact list
		return dataList;
	}

	//for remove favorite

	public void RemoveFav(Pojo contact)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME, KEY_HID + " = ?",
				new String[] { String.valueOf(contact.H_id()) });
		db.close();
	}

	public enum DatabaseManager {
		INSTANCE;
		private SQLiteDatabase db;
		private boolean isDbClosed =true;
		DatabaseHandler dbHelper;
		public void init(Context context) {
			dbHelper = new DatabaseHandler(context);
			if(isDbClosed){
				isDbClosed =false;
				this.db = dbHelper.getWritableDatabase();
			}

		}


		public boolean isDatabaseClosed(){
			return isDbClosed;
		}

		public void closeDatabase(){
			if(!isDbClosed && db != null){
				isDbClosed =true;
				db.close();
				dbHelper.close();
			}
		}
	}
}
