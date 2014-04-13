package com.doghouse.physicssimluator;

import com.doghouse.physicssimluator.adapters.LevelSelectorAdapter;
import com.pseudosurface.levels.level1.SimulatorActivity1;
import com.pseudosurface.levels.level2.SimulatorActivity2;
import com.pseudosurface.levels.level3.SimulatorActivity3;
import com.pseudosurface.levels.level4.SimulatorActivity4;
import com.pseudosurface.levels.level5.SimulatorActivity5;
import com.pseudosurface.levels.level6.SimulatorActivity6;
import com.pseudosurface.levels.level7.SimulatorActivity7;
import com.pseudosurface.levels.level8.SimulatorActivity8;
import com.pseudosurface.levels.level9.SimulatorActivity9;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class LevelSelectionActivity extends Activity implements OnItemClickListener{
	
	MediaPlayer song;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.level_selection);
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		
		Intent i = new Intent("android.intent.action.MAIN").putExtra("msg", "paused");
        this.sendBroadcast(i);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		ListView list = (ListView) findViewById(R.id.level_selection_list);
		list.setAdapter(new LevelSelectorAdapter(this, true));
		list.setOnItemClickListener(this);
	}
	
	@Override
	protected void onRestart(){
		super.onRestart();
		
		Intent i = new Intent("android.intent.action.MAIN").putExtra("msg", "start");
        this.sendBroadcast(i);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//		TextView t = (TextView) arg1.findViewById(R.id.level_selection_row);
//		String text = t.getText().toString();
		
		Intent i = new Intent("android.intent.action.MAIN").putExtra("msg", "stop");
        this.sendBroadcast(i);
		
		startLevel(this, arg2+1);		
	}
	
	public static void startLevel(Context context, int level){
		if(level == 1)
			context.startActivity(new Intent(context,SimulatorActivity1.class).putExtra("level", level));

		if(level == 2)
			context.startActivity(new Intent(context,SimulatorActivity2.class).putExtra("level", level));

		if(level == 3)
			context.startActivity(new Intent(context,SimulatorActivity3.class).putExtra("level", level));

		if(level == 4)
			context.startActivity(new Intent(context,SimulatorActivity4.class).putExtra("level", level));

		if(level == 5)
			context.startActivity(new Intent(context,SimulatorActivity5.class).putExtra("level", level));

		if(level == 6)
		{
			Intent intent = new Intent(context,SimulatorActivity6.class).putExtra("level", level);
			intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			context.startActivity(intent);
		}

		if(level == 7)
		{
			Intent intent = new Intent(context,SimulatorActivity7.class).putExtra("level", level);
			intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			context.startActivity(intent);
		}
		if(level == 8)
		{
			Intent intent = new Intent(context,SimulatorActivity8.class).putExtra("level", level);
			intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			context.startActivity(intent);
		}
		if(level == 9)
		{
			Intent intent = new Intent(context,SimulatorActivity9.class).putExtra("level", level);
			intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			context.startActivity(intent);
		}
	}

}
