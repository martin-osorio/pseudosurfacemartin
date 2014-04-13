package com.doghouse.physicssimluator.views;

import com.doghouse.physicssimluator.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public abstract class StarViewsHelper {
	
	
	
	public static void colorStars(Context context,ImageView star1, ImageView star2, ImageView star3, int starCount,boolean noStarsIsGrey){
		if(starCount == 1){
			star1.setImageDrawable(context.getResources().getDrawable(R.drawable.green_square));
			
			star2.setImageDrawable(context.getResources().getDrawable(R.drawable.green_outline_square));
			star3.setImageDrawable(context.getResources().getDrawable(R.drawable.green_outline_square));
		}
		else if(starCount == 2){
			star1.setImageDrawable(context.getResources().getDrawable(R.drawable.green_square));
			star2.setImageDrawable(context.getResources().getDrawable(R.drawable.green_square));
			
			star3.setImageDrawable(context.getResources().getDrawable(R.drawable.green_outline_square));
		}
		else if(starCount == 3){
			star1.setImageDrawable(context.getResources().getDrawable(R.drawable.green_square));
			star2.setImageDrawable(context.getResources().getDrawable(R.drawable.green_square));
			star3.setImageDrawable(context.getResources().getDrawable(R.drawable.green_square));
		}
		else{
			Drawable colorFrame = null;
			if(noStarsIsGrey){
				colorFrame = context.getResources().getDrawable(R.drawable.grey_outline_square);
			}else{
				colorFrame = context.getResources().getDrawable(R.drawable.green_outline_square);
			}
			star1.setImageDrawable(colorFrame);
			star2.setImageDrawable(colorFrame);
			star3.setImageDrawable(colorFrame);
		}
		
		
	}
	
	public static String formatMilisecondTime(long time){
		int seconds = (int) (time / 1000);
		int minutes = seconds / 60 ;
		int leftoverMilis = (int) (time - (seconds * 1000));
		seconds = seconds % 60;
		
		
		
		return minutes + ":"+seconds+":"+leftoverMilis;
	}

}
