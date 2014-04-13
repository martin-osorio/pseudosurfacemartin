package com.doghouse.physicssimluator;

import com.doghouse.physicssimluator.views.ShareDialogFragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;

public class Main extends Activity implements AnimationListener, View.OnClickListener {
	
	public static final String SHOULD_FADE_IN = "shouldFadeIn";
//	private static final int STATUS_SHOWING = 0;
//	private static final int STATUS_FADING = 1;
//	private static final int STATUS_HIDDEN = 2;
//
//	private int splashStatus = 0;
	
	private FrameLayout splashFrame;
	
//	private ListView list;
	
	Animation fadeOut = new AlphaAnimation(1, 0);	
	
	MediaPlayer song;
	private BroadcastReceiver mReceiver;
	boolean LevelSelectionActivityPaused;
	boolean LevelSelectionActivityStarted;
	boolean isSongPlaying;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		findViewById(R.id.main_play).setOnClickListener(this);
		findViewById(R.id.main_share).setOnClickListener(this);
		findViewById(R.id.main_options).setOnClickListener(this);
		findViewById(R.id.main_scores).setOnClickListener(this);
		
		/**/
		song = MediaPlayer.create(Main.this, R.raw.i);
		song.start();
		isSongPlaying = true;
		LevelSelectionActivityStarted = false;
		
		IntentFilter intentFilter = new IntentFilter("android.intent.action.MAIN");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String msg = intent.getStringExtra("msg");
                if(msg.compareTo("stop") == 0){
                	song.release();
                	isSongPlaying = false;
                }
                else if(msg.compareTo("paused") == 0){
                	LevelSelectionActivityPaused = true;
                }
                else if(msg.compareTo("start") == 0){
                	if(!isSongPlaying){
	                	song = MediaPlayer.create(Main.this, R.raw.i);
	            		song.start();
	            		isSongPlaying = true;
                	}
                }
            }
        };
        this.registerReceiver(mReceiver, intentFilter);
		/**/
		
		
//		splashFrame = (FrameLayout) findViewById(R.id.main_splash_frame);
//		list = ((ListView)findViewById(R.id.main_list));
//		list.setAdapter(new MainAdapter(this));
//		if(shouldFadeIn(getIntent())){
//			splashStatus = 1;
//			list.setEnabled(false);
//			
//			fadeOut.setInterpolator(new AccelerateInterpolator());
//			fadeOut.setStartOffset(1000);
//			fadeOut.setDuration(1000);
//			fadeOut.setAnimationListener(this);
//			fadeOut.setFillBefore(true);
//			new Thread(new Runnable(){
//	
//				@Override
//				public void run() {
//					try {
//						Thread.sleep(2000); // sleep for 2 seconds
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					runOnUiThread(new Runnable(){
//	
//						@Override
//						public void run() {
//							list.setEnabled(true);
//							splashFrame.startAnimation(fadeOut);
//							
//						}});
//					
//					
//				}}).start();
//		}else{
//			splashFrame.setVisibility(View.GONE);
//		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		splashFrame.setVisibility(View.GONE);
		
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		
	}
	
//	private boolean shouldFadeIn(Intent intent){
//		return intent.getBooleanExtra(SHOULD_FADE_IN, true) && splashStatus == 0;
//	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch(v.getId()){
		case R.id.main_play:
			intent = new Intent(this,LevelSelectionActivity.class);
			LevelSelectionActivityStarted = true;
			startActivity(intent);
			break;
		case R.id.main_scores:
			startActivity(new Intent(this,ScoreLevelsActivity.class));
			break;
		case R.id.main_options:
			startActivity(new Intent(this,OptionsActivity.class));
			break;
		case R.id.main_share:
			showShareDialog();
			break;
		
		}
	}
	
	private void showShareDialog(){
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
	    
	    ShareDialogFragment shareFragment = ShareDialogFragment.getInstance();
	    shareFragment.show(ft, "dialog");
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		
		if(LevelSelectionActivityPaused){
			song.release();
		}
		else if(!LevelSelectionActivityStarted){
			song.release();
		}
	}
	
	@Override
	public void onResume(){
		super.onResume();
	}
}
