package com.doghouse.physicssimulator.util;

import java.util.ArrayList;

import com.doghouse.physicssimluator.LevelInfo;
import com.doghouse.physicssimluator.model.LevelResult;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ScoresDatabase extends SQLiteOpenHelper {
	
   public static final String LEVEL = "level",
			UNLOCKED = "unlocked",
			STARS = "stars";
   
   private static final String DB_NAME = "user_database";
   
    private static final String TABLE_CREATE = 
    		"CREATE TABLE " + DB_NAME +"(" +
            " _ID INTEGER PRIMARY KEY, " +
    		UNLOCKED + " INTEGER, "+
            LEVEL + " INTEGER, " +
            STARS + " INTEGER)";

	private static final String TAG = "ScoresDatabase";
    


	public ScoresDatabase(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
		// TODO create initial level results which are no stars
					// number of levels
		db.insert(DB_NAME, null, getDefaultContentValues(0,1));
		for(int i = 1; i < LevelInfo.NUM_LEVELS; i++){
			Log.d("DBonCreate",i+"");
			db.insert(DB_NAME, null, getDefaultContentValues(i,0));
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
	
	private ContentValues getDefaultContentValues(int levelId,int unlocked){
		ContentValues values = new ContentValues();
		values.put(LEVEL, levelId);
		values.put(UNLOCKED, unlocked);
		values.put(STARS, 0);
		
		return values;
	}
	
	
	public static ArrayList<LevelResult> getAllLevelResults(Context context){
		ScoresDatabase db = new ScoresDatabase(context);
		return getAllLevelResults(context, db);
		
	}
	
	public static ArrayList<LevelResult> getAllLevelResults(Context context, ScoresDatabase db){
		Cursor cursor = db.getReadableDatabase().query(DB_NAME, null, null, null, null, null, null);
		ArrayList<LevelResult> levelResults = new ArrayList<LevelResult>();
		if(cursor != null && cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				levelResults.add(new LevelResult(cursor));
			}while(cursor.moveToNext());
		}
		Log.d("GetAllLevelResults",levelResults.size()+"");
		return levelResults;
	}
	
	
	public static void addLevelScore(Context context, int level, int stars){
		if(stars > 0){
			// unlock ++level
			ScoresDatabase db = new ScoresDatabase(context);
			if(level  + 1 != LevelInfo.NUM_LEVELS){
				ContentValues values = new ContentValues();
				values.put(UNLOCKED, 1);
				db.getWritableDatabase().update(DB_NAME, values, LEVEL + " = ", new String[]{""+(level+1)});
			}
			
			// if stars > current stars in DB replace
			
			Cursor cursor = db.getReadableDatabase().query(DB_NAME, new String[]{STARS}, LEVEL + " = ", new String[]{""+level}, null, null, null);
			if(cursor == null || cursor.getCount() < 1){
				Log.d(TAG,"queried for level info wrong");
			}else{
				cursor.moveToFirst();
				int currentStars = cursor.getInt(0);
				if(currentStars < stars){
					ContentValues values = new ContentValues();
					values.put(STARS, stars);
					db.getWritableDatabase().update(DB_NAME, values, LEVEL + " = ", new String[]{""+level});
				}
			}
		}
	}
	
}
