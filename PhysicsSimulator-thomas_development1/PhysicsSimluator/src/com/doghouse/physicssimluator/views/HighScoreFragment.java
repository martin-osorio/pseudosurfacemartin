package com.doghouse.physicssimluator.views;

import com.doghouse.physicssimluator.R;
import com.doghouse.physicssimluator.model.Score;
import com.doghouse.physicssimulator.util.ScoreManager;
import com.doghouse.physicssimulator.util.ScoresDatabase;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class HighScoreFragment extends DialogFragment implements View.OnClickListener{
	
	private EditText name;
	private Button submitButton;
	
	public static HighScoreFragment getInstance(int id,int stars, long time){
		HighScoreFragment fragment = new HighScoreFragment();
		
        Bundle args = new Bundle();
        args.putInt("level_id", id);
        args.putInt("stars", stars);
        args.putLong("time", time);
        fragment.setArguments(args);
        
        return fragment;
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.high_score, container);
		name = (EditText) v.findViewById(R.id.high_scores_name_edit_text);
		submitButton = (Button) v.findViewById(R.id.high_score_submit);
		submitButton.setOnClickListener(this);
		
		name.setText(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("username", ""));
		
		return v;
	}

	@Override
	public void onClick(View v) {
		String username = name.getText().toString();
		Editor ed = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
		ed.putString("username", username);
		ed.commit();
		
		this.dismiss();
		Bundle args = this.getArguments();
		int level = args.getInt("level_id");
		int stars = args.getInt("stars");
		long time = args.getLong("time");
		LevelCompletionFragment.showLevelCompletionFragment(level, stars, time, getActivity());
		
		Score score = new Score(username, time, stars);
		score.setLevel(level);
		new ScoreManager().submitHighScoreName(score);
	}
	
	public static void showHighScoreFragment(int level_id, int stars, long time, FragmentActivity activity){
		HighScoreFragment fragment = getInstance(level_id, stars, time);
		
	    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
	    ft.addToBackStack(null);

	    fragment.show(ft, "level_completion_frag");
	    
	    ScoresDatabase.addLevelScore(activity, level_id, stars);
	}
}
