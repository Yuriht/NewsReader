package com.kuzmych.newsreader.DB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.kuzmych.newsreader.Api.RSSItem;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

	//DB
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "NewsDB";

	//Tables
	private static final String TABLE_KORR = "Korrespondent";

	//Columns
	private static final String KEY_ID = "id";
	private static final String Korr_Title = "Title";
	private static final String Korr_Link = "Link";
	private static final String Korr_Description = "Description";
	private static final String Korr_Fulltext = "Fulltext";
	private static final String Korr_Image = "Image";
	private static final String Korr_PubDate = "PubDate";
	private static final String Korr_Guid = "Guid";


	public SQLiteDatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE = "CREATE TABLE " + TABLE_KORR + "("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ Korr_Title + " TEXT,"
				+ Korr_Link + " TEXT,"
				+ Korr_Description + " TEXT,"
				+ Korr_Fulltext + " TEXT,"
				+ Korr_Image + " TEXT,"
				+ Korr_PubDate + " LONG,"
				+ Korr_Guid + " TEXT"
				+ ")";
		db.execSQL(CREATE_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_KORR);
		// Create tables again
		onCreate(db);
	}

	/**
	 *  CRUD(Create, Read, Update, Delete) Operations
	 */

	public void addItem(RSSItem item) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(Korr_Title, item.getTitle());
		values.put(Korr_Link, item.getLink());
		values.put(Korr_Description, item.getDescription());
		values.put(Korr_Fulltext, item.getFulltext());
		values.put(Korr_Image, item.getImage());
		values.put(Korr_PubDate, item.getPubDate());
		values.put(Korr_Guid, item.getGuid());

		// Inserting Row
		db.insert(TABLE_KORR, null, values);
		db.close(); // Closing database connection
	}

	// Getting single item
	public RSSItem getItem(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_KORR, new String[]
						{KEY_ID,
								Korr_Title,
								Korr_Link,
								Korr_Description,
								Korr_Fulltext,
								Korr_Image,
								Korr_PubDate,
								Korr_Guid
						}, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		RSSItem item = new RSSItem(
				cursor.getInt(0),
				cursor.getString(1),
				cursor.getString(2),
				cursor.getString(3),
				cursor.getString(4),
				cursor.getString(5),
				cursor.getString(6),
				cursor.getString(7));
		return item;
	}

	// Check guid exist
	public Boolean GuidExist(String guid) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_KORR, new String[]
						{KEY_ID,
								Korr_Guid
						}, Korr_Guid + "=?",
				new String[] { String.valueOf(guid) }, null, null, null, null);

		return cursor.getCount() > 0;
	}

	// Getting All items
	public List getAllItems() {
		List itemList = new ArrayList();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_KORR + " ORDER BY " + Korr_PubDate + " DESC";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				RSSItem item = new RSSItem(
						cursor.getInt(0),
						cursor.getString(1),
						cursor.getString(2),
						cursor.getString(3),
						cursor.getString(4),
						cursor.getString(5),
						cursor.getString(6),
						cursor.getString(7)
				);
				// Adding vehicle to list
				itemList.add(item);
			} while (cursor.moveToNext());
		}

		return itemList;
	}

	// Updating single item
	public int updateItem(RSSItem item) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(Korr_Title, item.getTitle());
		values.put(Korr_Link, item.getLink());
		values.put(Korr_Description, item.getDescription());
		values.put(Korr_Fulltext, item.getFulltext());
		values.put(Korr_Image, item.getImage());
		values.put(Korr_PubDate, item.getPubDate());
		values.put(Korr_Guid, item.getGuid());

		// updating row
		return db.update(TABLE_KORR, values, KEY_ID + " = ?",
				new String[] { String.valueOf(item.getId()) });
	}

	// Deleting single item
	public void deleteItem(RSSItem item) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_KORR, KEY_ID + " = ?",
				new String[] { String.valueOf(item.getId()) });
		db.close();
	}

	// Deleting all items
	public void deleteAllItems() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_KORR,null,null);
		db.close();
	}

	// Getting items Count
	public int getItemsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_KORR;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		return cursor.getCount();
	}

}