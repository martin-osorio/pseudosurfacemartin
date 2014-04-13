package com.doghouse.physicssimulator.util;

import com.doghouse.physicssimluator.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;

public class OptionsManager {
	
	public static final String HIGH_SCORE_SUBMIT = "submit_high_score";
	public static final String SFX_VOLUME = "sfx_vlume";
	public static final String MUSIC_VOLUME = "music_volume";
	
	private SharedPreferences prefs;
	private Context context;
	public OptionsManager(Context context) {
		this.context = context;
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	
	private void onVolumeChange(TextView volumeText, int change, String key){
		int volume = prefs.getInt(key, 5);
		volume = volume + change;
		if(volume <=5 && volume >= 0){
			Editor ed = prefs.edit();
			ed.putInt(key, volume);
			ed.commit();
			volumeText.setText(volume+"");
		}
	}
	
	public int getMusicVolume(){
		return prefs.getInt(MUSIC_VOLUME, 5);
	}
	public int getSfxVolume(){
		return prefs.getInt(SFX_VOLUME, 5);
	}
	
	public void onSfxVolumeChange(TextView volumeText, int change){
		onVolumeChange(volumeText, change, SFX_VOLUME);
	}
	public void onMusicVolumeChange(TextView volumeText, int change){
		onVolumeChange(volumeText, change, MUSIC_VOLUME);
	}
	
	public void toggleSubmitHighScore(Button button){
		Editor ed = prefs.edit();
		boolean submitHighScore = !prefs.getBoolean(HIGH_SCORE_SUBMIT, true); // flipped value
		ed.putBoolean(HIGH_SCORE_SUBMIT, submitHighScore);
		ed.commit();
		
		int backgroundRes = 0;
		int textColor = 0;
		String text = null;
		if(submitHighScore){
			text = "Yes";
			backgroundRes = R.drawable.button;
			textColor = context.getResources().getColor(R.color.green);
		}else{
			text = "No";
			backgroundRes = R.drawable.button_pressed;
			textColor = context.getResources().getColor(R.color.red);
		}
		button.setBackgroundResource(backgroundRes);
		button.setTextColor(textColor);
		button.setText(text);
	}
	
	public static boolean shouldSubmitHighScores(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(HIGH_SCORE_SUBMIT, true);
	}
	
	public static int getSfxVolume(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getInt(SFX_VOLUME, 5);
	}
	public static int getMusicVolume(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context).getInt(MUSIC_VOLUME, 5);
	}

}
