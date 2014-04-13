package com.doghouse.physicssimluator.model;

import java.util.ArrayList;

import android.content.Context;

import com.doghouse.physicssimulator.util.ScoresDatabase;

public class LevelResultsManager {

	private ArrayList<LevelResult> levelResults;
	
	public LevelResultsManager(Context context){
		levelResults = ScoresDatabase.getAllLevelResults(context);
	}
	
	
	public LevelResult getLevelResult(int levelId){
		return levelResults.get(levelId);
	}
	
}
