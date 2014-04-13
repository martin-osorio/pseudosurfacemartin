package com.doghouse.physicssimluator.views;

import com.doghouse.physicssimluator.LevelSelectionActivity;
import com.doghouse.physicssimluator.R;
import com.doghouse.physicssimulator.util.ScoresDatabase;
import com.pseudosurface.levels.template.SimulatorActivity;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LevelCompletionFragment extends DialogFragment implements View.OnClickListener{

	private int id;
	private int stars;
	private long time;
	
	private TextView levelTitle;
	private TextView timeText;
	
	private Button retry;
	private Button nextLevel;
	
	private ImageView star1, star2, star3;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.level_completion, container);
		levelTitle = (TextView) v.findViewById(R.id.level_completion_name);
		timeText = (TextView) v.findViewById(R.id.completion_time);
		
		retry = (Button) v.findViewById(R.id.level_completion_retry);
		nextLevel = (Button) v.findViewById(R.id.level_completion_next);
		
		star1 = (ImageView) v.findViewById(R.id.star_1);
		star2 = (ImageView) v.findViewById(R.id.star_2);
		star3 = (ImageView) v.findViewById(R.id.star_3);
		
		return v;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		id = getArguments().getInt("level_id");
		stars = getArguments().getInt("stars");
		time = getArguments().getLong("time");
		
		levelTitle.setText("Level "+ id);
		timeText.setText(StarViewsHelper.formatMilisecondTime(time));
		
		StarViewsHelper.colorStars(getActivity(), star1, star2, star3, stars,false);
		
		if(stars == 0){
			nextLevel.setBackgroundResource(R.drawable.grey_outline_square);
			nextLevel.setTextColor(getActivity().getResources().getColor(R.color.grey));
			nextLevel.setClickable(false);
		}
		
		retry.setOnClickListener(this);
		nextLevel.setOnClickListener(this);
		
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.level_completion_retry:
			SimulatorActivity simulatorActivity = (SimulatorActivity)getActivity();
			simulatorActivity.world.resetSimulation();
			
			dismiss();
			break;
		case R.id.level_completion_next:
			int level = getActivity().getIntent().getIntExtra("level", 1) +1;
			dismiss();
			getActivity().finish();
			LevelSelectionActivity.startLevel(getActivity(), level);
		}
		
	}
	
	public static LevelCompletionFragment getInstance(int id,int stars, long time){
		LevelCompletionFragment fragment = new LevelCompletionFragment();
		
        Bundle args = new Bundle();
        args.putInt("level_id", id);
        args.putInt("stars", stars);
        args.putLong("time", time);
        fragment.setArguments(args);
        
        return fragment;
		
	}
	
	public static void showLevelCompletionFragment(int level_id, int stars, long time, FragmentActivity activity){
		LevelCompletionFragment fragment = getInstance(level_id, stars, time);
		
	    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
	    ft.addToBackStack(null);

	    fragment.show(ft, "level_completion_frag");
	    
	    //ScoresDatabase.addLevelScore(activity, level_id, stars);
	}
	

}
