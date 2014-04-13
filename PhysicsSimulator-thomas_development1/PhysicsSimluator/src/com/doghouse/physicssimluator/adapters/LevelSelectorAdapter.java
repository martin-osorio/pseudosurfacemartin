package com.doghouse.physicssimluator.adapters;

import com.doghouse.physicssimluator.LevelInfo;
import com.doghouse.physicssimluator.R;
import com.doghouse.physicssimluator.model.LevelResult;
import com.doghouse.physicssimluator.model.LevelResultsManager;
import com.doghouse.physicssimluator.views.StarViewsHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class LevelSelectorAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	
	private LevelResultsManager manager = null;
	
	private Context context;
	
	private boolean showStars = false;
	
	public LevelSelectorAdapter(Context context, boolean showStars){
		this.context = context;
		
		manager = new LevelResultsManager(context);
		this.showStars = showStars;
		
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return LevelInfo.NUM_LEVELS;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View row, ViewGroup parent) {
		ViewHolder holder = null;
		if(row == null){
			row = inflater.inflate(R.layout.level_selection_row, null);
			holder = new ViewHolder(row);
			row.setTag(holder);
		}else{
			holder = (ViewHolder) row.getTag();
		}
		LevelResult levelResult = manager.getLevelResult(position);
		holder.title.setText(levelResult.getLevelName());
		if(showStars){
			if(levelResult.isUnlocked() && levelResult.getStars() <= 0){
				holder.star1.setImageDrawable(context.getResources().getDrawable(R.drawable.green_outline_square));
				holder.star2.setImageDrawable(context.getResources().getDrawable(R.drawable.green_outline_square));
				holder.star3.setImageDrawable(context.getResources().getDrawable(R.drawable.green_outline_square));
				holder.title.setTextColor(context.getResources().getColor(R.color.green));
			}else{
				holder.title.setTextColor(context.getResources().getColor(R.color.grey));
				StarViewsHelper.colorStars(context, holder.star1, holder.star2, holder.star3, levelResult.getStars(), true);
			}
		}else{
			holder.starsFrame.setVisibility(View.INVISIBLE);
			holder.title.setTextColor(context.getResources().getColor(R.color.green));
		}
		
		return row;
	}
	
	public class ViewHolder{
		TextView title;
		ImageView star1,star2,star3;
		FrameLayout starsFrame;
		
		public ViewHolder(View row){
			title = (TextView) row.findViewById(R.id.level_selection_row);
			star1 = (ImageView) row.findViewById(R.id.star_1);
			star2 = (ImageView) row.findViewById(R.id.star_2);
			star3 = (ImageView) row.findViewById(R.id.star_3);
			starsFrame = (FrameLayout) row.findViewById(R.id.stars_frame);
		}
		
	}
	

}
