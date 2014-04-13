package com.doghouse.physicssimluator.views;

import com.doghouse.physicssimluator.R;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.widget.TextView;

public class TimeView extends TextView implements Runnable{

	private long miliseconds = 0;
	private long lastNanoTime = 0;
	private boolean running = false;
	
	private Handler uiThread;
	
	public TimeView(Context context) {
		super(context);
		init();
	}
	public TimeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public TimeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init(){
		setTypeface(Typeface.MONOSPACE);
		setTextColor(getResources().getColor(R.color.green));
		uiThread = new Handler(Looper.getMainLooper());
	}
	
	public void startTime(){
		lastNanoTime = System.nanoTime();
		running = true;
		new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(running){
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {e.printStackTrace();}
					long newNanoTime = System.nanoTime();
					miliseconds += (newNanoTime - lastNanoTime)/10e9;
					lastNanoTime = newNanoTime;
					uiThread.post(this);
				}
				
			}}).start();
		
	}
	
	public void pauseTime(){
		running = false;
	}
	
	public void resetTime(){
		running = false;
		miliseconds = 0;
	}
	public long getTime(){
		return miliseconds;
	}
	@Override
	public void run() {
		setText(StarViewsHelper.formatMilisecondTime(miliseconds));
		
	}
	
}
