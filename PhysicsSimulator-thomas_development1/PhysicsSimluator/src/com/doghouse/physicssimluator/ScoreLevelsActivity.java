package com.doghouse.physicssimluator;

import java.util.ArrayList;

import com.doghouse.physicssimluator.adapters.LevelSelectorAdapter;
import com.doghouse.physicssimluator.model.LevelScores;
import com.doghouse.physicssimluator.model.Score;
import com.doghouse.physicssimluator.views.AnalogProgressView;
import com.doghouse.physicssimulator.util.ScoreManager;
import com.doghouse.physicssimulator.util.ScoresRetriever;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ScoreLevelsActivity extends Activity implements ScoresRetriever.ScoresListener{

	private ListView scores_list;
	private static final String TAG = "ScoreLevelsActivity";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.scores);
	    
	    scores_list = (ListView) findViewById(R.id.scores_list);
	
	    ScoresRetriever retriever = new ScoresRetriever(this); 
	    retriever.startFetchingScores();
	    
//	    testScoreSubmitting();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		
	}

	@Override
	public void onScoresRetrieved(final ArrayList<LevelScores> scores_list) {
		for(LevelScores levelScore: scores_list){
			Log.i(TAG, "LEVEL "+levelScore.getLevel_id());
				for(int i = 0; i < levelScore.scoresCount(); i++){
					Log.i(TAG, ""+levelScore.getScoreAt(i).getTime());
					Log.i(TAG, levelScore.getScoreAt(i).getName());
				}
		}
		
		((AnalogProgressView)findViewById(R.id.scores_progress)).setVisibility(View.INVISIBLE);
		this.scores_list.setAdapter(new LevelSelectorAdapter(this,false));
		this.scores_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				ScoresActivity.setLevelScores(scores_list.get(arg2));
				ScoreLevelsActivity.this.startActivity(new Intent(ScoreLevelsActivity.this,ScoresActivity.class));
				
			}
		});
		
	}
	
	public void testScoreSubmitting(){
		Score score = new Score("Lord Helix", 20345, 3, 7);
		new ScoreManager().submitHighScoreName(score);
	}
}
