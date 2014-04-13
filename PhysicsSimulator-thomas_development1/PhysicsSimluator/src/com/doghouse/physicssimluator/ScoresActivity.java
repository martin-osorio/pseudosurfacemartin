package com.doghouse.physicssimluator;

import com.doghouse.physicssimluator.adapters.ScoresAdapter;
import com.doghouse.physicssimluator.model.LevelScores;

import android.app.ListActivity;
import android.os.Bundle;

public class ScoresActivity extends ListActivity {

	private static LevelScores scores = null;
	
	public static void setLevelScores(LevelScores scores){
		ScoresActivity.scores = scores;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setListAdapter(new ScoresAdapter(scores,this));
	}
}
