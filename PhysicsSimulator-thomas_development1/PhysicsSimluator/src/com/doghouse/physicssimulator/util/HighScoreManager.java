package com.doghouse.physicssimulator.util;

import java.util.ArrayList;

import com.doghouse.physicssimluator.model.LevelScores;
import com.doghouse.physicssimluator.model.Score;

public class HighScoreManager {
	
	private static ArrayList<LevelScores> levelScores;
	private static ScoresRetriever scoresRetriever;
	
	public static void refreshData(){
		if(scoresRetriever == null){
			scoresRetriever = new ScoresRetriever(new ScoresRetriever.ScoresListener() {
				
				@Override
				public void onScoresRetrieved(ArrayList<LevelScores> scores_list) {
					levelScores = scores_list;
					
				}
			});
		}
	}
	
	public static ArrayList<LevelScores> getLevelScores() {
		return levelScores;
	}
	
	public static boolean isPossibleHighScore(Score score,int level){
		if(HighScoreManager.levelScores != null){
			LevelScores levelScores = HighScoreManager.levelScores.get(level);
			return levelScores.isHighScore(score);
		}
		return false;
		
	}

}
