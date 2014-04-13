package com.doghouse.physicssimluator.model;

import com.doghouse.physicssimulator.util.ScoresDatabase;

import android.content.ContentValues;
import android.database.Cursor;

public class LevelResult {
	
	private int levelId;
	protected int stars = 0; // max 3
	
	private String levelName;
	
	private boolean unlocked = false;
	
	public LevelResult(int levelId,long time){
		
	}
	
	public LevelResult(Cursor cursor) {
		unlocked = cursor.getInt(1) == 1;
		levelId = cursor.getInt(2);
		stars = cursor.getInt(3);
		levelName = "Level "+(levelId +1);
	}

	public ContentValues getAsRow(){
		ContentValues values = new ContentValues();
		values.put(ScoresDatabase.LEVEL, levelId);
		values.put(ScoresDatabase.STARS, stars);
		int unlocked = 1;
		if(!this.unlocked){
			unlocked = 0;
		}
		values.put(ScoresDatabase.UNLOCKED, unlocked);
		
		return values;
	}
	
	public int getLevelId() {
		return levelId;
	}
	public String getLevelName() {
		return levelName;
	}
	public int getStars() {
		return stars;
	}
	public boolean isUnlocked() {
		return unlocked;
	}
	

}
