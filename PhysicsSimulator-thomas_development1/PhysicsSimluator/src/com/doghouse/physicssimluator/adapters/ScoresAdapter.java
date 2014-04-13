package com.doghouse.physicssimluator.adapters;

import com.doghouse.physicssimluator.R;
import com.doghouse.physicssimluator.model.LevelScores;
import com.doghouse.physicssimluator.model.Score;
import com.doghouse.physicssimluator.views.StarViewsHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ScoresAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private LevelScores levelScores;
	private Context context;
	public ScoresAdapter(LevelScores levelScores, Context context){
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.levelScores = levelScores;
	}
	@Override
	public int getCount() {
		return levelScores.scoresCount();
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
			row = inflater.inflate(R.layout.scores_row, null);
			holder = new ViewHolder(row);
			row.setTag(holder);
		}else{
			holder = (ViewHolder) row.getTag();
		}
		Score score = levelScores.getScoreAt(position);
		int stars = score.getStars();
		StarViewsHelper.colorStars(context, holder.star1, holder.star2, holder.star3, stars,false);
		
		holder.levelName.setText(score.getName());
		holder.time.setText(score.getTimeString());
		holder.rank.setText(""+(position+1));
		
		
		return row;
	}
	
	public class ViewHolder{
		
		ImageView star1,star2,star3;
		
		TextView levelName;
		TextView time;
		TextView rank;
		
		public ViewHolder(View row){
			star1 = (ImageView) row.findViewById(R.id.star_1);
			star2 = (ImageView) row.findViewById(R.id.star_2);
			star3 = (ImageView) row.findViewById(R.id.star_3);
			
			levelName = (TextView) row.findViewById(R.id.score_row_level);
			time = (TextView) row.findViewById(R.id.scores_row_time);
			rank = (TextView) row.findViewById(R.id.scores_row_rank);
		}
	}

}
