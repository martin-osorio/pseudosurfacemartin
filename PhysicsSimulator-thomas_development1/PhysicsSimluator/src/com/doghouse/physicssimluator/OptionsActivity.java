package com.doghouse.physicssimluator;

import com.doghouse.physicssimulator.util.OptionsManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class OptionsActivity extends Activity implements OnClickListener {

	private Button submitScoreToggle;
	
	private TextView sfxVolume;
	private TextView musicVolume;
	
	private OptionsManager manager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);
		
		submitScoreToggle = (Button) findViewById(R.id.options_sumbit_score_toggle);
		sfxVolume = (TextView) findViewById(R.id.options_sfx_volume);
		musicVolume = (TextView) findViewById(R.id.options_music_volume);
		
		manager = new OptionsManager(this);
		
		submitScoreToggle.setOnClickListener(this);
		
		findViewById(R.id.options_music_minus).setOnClickListener(this);
		findViewById(R.id.options_music_plus).setOnClickListener(this);
		findViewById(R.id.options_sfx_minus).setOnClickListener(this);
		findViewById(R.id.options_sfx_plus).setOnClickListener(this);
		
		sfxVolume.setText(manager.getSfxVolume()+"");
		musicVolume.setText(manager.getMusicVolume()+"");
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.options_sumbit_score_toggle:
			manager.toggleSubmitHighScore(submitScoreToggle);
			break;
		case R.id.options_music_minus:
			manager.onMusicVolumeChange(musicVolume, -1);
			break;
		case R.id.options_music_plus:
			manager.onMusicVolumeChange(musicVolume, 1);
			break;
		case R.id.options_sfx_minus:
			manager.onSfxVolumeChange(sfxVolume, -1);
			break;
		case R.id.options_sfx_plus:
			manager.onSfxVolumeChange(sfxVolume, 1);
			break;
		
		
		}
		
	}


}
