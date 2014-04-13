package com.doghouse.physicssimluator.model;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LevelScores {
	
	public static final String LEVEL_ID = "level";
	public static final String LEVEL_SCORES = "scores";
	public static final String NAME = "name";
	public static final String TIME = "time";
	public static final String STARS = "stars";
	public static final String ID = "id";
	
	private int level_id = -1;
	private ArrayList<Score> scores;
	
	public LevelScores(JSONObject obj){
		scores = new ArrayList<Score>();
		try {
			level_id = obj.getInt(LEVEL_ID);
			JSONArray scoresArray = obj.getJSONArray(LEVEL_SCORES);
			for(int i = 0; i < scoresArray.length(); i++){
				String name = scoresArray.getJSONObject(i).getString(NAME);
				long time = scoresArray.getJSONObject(i).getLong(TIME);
				int stars = scoresArray.getJSONObject(i).getInt(STARS);
				scores.add(new Score(name,time,stars));
			}
		} catch (JSONException e) {e.printStackTrace();}
		
		Collections.sort(scores);
	}
	
	public int getLevel_id() {
		return level_id;
	}
	
	public int scoresCount(){
		return scores.size();
	}
	
	public Score getScoreAt(int i){
		return scores.get(i);
	}

	public boolean isHighScore(Score score) {
		for (Score score1 : scores){
			if(score.getStars() >= score1.getStars() && score.getTime() < score1.getTime()){
				return true;
			}
		}
		return false;
	}

}
