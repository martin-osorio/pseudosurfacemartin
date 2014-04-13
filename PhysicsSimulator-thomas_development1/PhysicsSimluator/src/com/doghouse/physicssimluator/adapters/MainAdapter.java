package com.doghouse.physicssimluator.adapters;

import java.util.ArrayList;

import com.doghouse.physicssimluator.LevelSelectionActivity;
import com.doghouse.physicssimluator.R;
import com.doghouse.physicssimluator.ScoreLevelsActivity;
import com.doghouse.physicssimluator.views.ShareDialogFragment;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;

public class MainAdapter extends BaseAdapter implements OnClickListener{
	
	ArrayList<String> buttonTitles;

	private Activity context;
	
	LayoutInflater inflater;
	public MainAdapter(Activity context){
		inflater = LayoutInflater.from(context);
		buttonTitles = new ArrayList<String>();
		buttonTitles.add("Play");
		buttonTitles.add("Options");
		buttonTitles.add("Share");
		this.context = context;
	}
	@Override
	public int getCount() {
		return 3;
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
			row = inflater.inflate(R.layout.main_row, null);
			holder = new ViewHolder(row);
			row.setTag(holder);
		}else{
			holder = (ViewHolder) row.getTag();
		}
		holder.button.setText(buttonTitles.get(position));
		holder.button.setOnClickListener(this);
		
		return row;
	}
	
	public class ViewHolder{
		Button button;
		
		public ViewHolder(View row){
			button = (Button) row.findViewById(R.id.main_row_button);
		}
		
	}

	@Override
	public void onClick(View v) {
		Button b = (Button) v;
		String title = b.getText().toString();
		Intent intent = null;
		if(title.equals("Play")){
			intent = new Intent(context,LevelSelectionActivity.class);
			context.startActivity(intent);
		}else if(title.equals("Scores")){
			context.startActivity(new Intent(context,ScoreLevelsActivity.class));
			
		}else if(title.equals("Options")){
//			intent = new Intent(context,)
		}else if(title.equals("Share")){
			showShareDialog(context);
		}
		
	}
	
	private void showShareDialog(Activity context){
	    FragmentTransaction ft = context.getFragmentManager().beginTransaction();
	    
	    ShareDialogFragment shareFragment = ShareDialogFragment.getInstance();
	    shareFragment.show(ft, "dialog");
	}

}
