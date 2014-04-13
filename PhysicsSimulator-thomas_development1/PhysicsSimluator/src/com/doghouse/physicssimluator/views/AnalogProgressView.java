package com.doghouse.physicssimluator.views;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class AnalogProgressView extends TextView implements Runnable {

	public AnalogProgressView(Context context) {
		super(context);
	}
	public AnalogProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public AnalogProgressView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private Handler handler;
	private int state = 0;
	
	public void increment(){
		if(state == 3){
			state = 1;
		}else{
			state++;
		}
	}
	
	public void start(){
		handler = new Handler(Looper.getMainLooper());
		new Thread(new Runnable(){@Override
		public void run() {
			while(isShown()){
				handler.post(this);
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {e.printStackTrace();}
			}
		}}).start();
		
		
	}
		@Override
		public void setVisibility(int visibility) {
			super.setVisibility(visibility);
			if(visibility == View.VISIBLE){
				start();
			}
		}
		@Override
		public void run() {
			String text = "";
			switch (state){
				case 1: 
					text = ".";
					break;
				case 2:
					text = ". .";
					break;
				case 3:
					text = ". . .";
					break;
			}
			setText(text);
			
		}
}
