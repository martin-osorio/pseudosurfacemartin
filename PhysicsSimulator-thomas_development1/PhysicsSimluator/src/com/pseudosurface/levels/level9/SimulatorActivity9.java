package com.pseudosurface.levels.level9;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import com.doghouse.physicssimluator.R;
import com.pseudosurface.levels.level1.SimulatorActivity1;
import com.pseudosurface.levels.template.Renderer;
import com.pseudosurface.levels.template.SimulatorActivity;
import com.pseudosurface.levels.template.World;
import com.pseudosurface.physics.Math3D;
import com.pseudosurface.physics.TangibleObject;


public class SimulatorActivity9 extends SimulatorActivity{
	
	MediaPlayer song;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		song = MediaPlayer.create(SimulatorActivity9.this, R.raw.i);
		song.setLooping(true);
		song.start();
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		
		song.release();
	}
	
	public Renderer getNewRenderer() {
		return new Renderer9(this);
	}

	public World getNewWorld() {
		return new World9(renderer);
	}

	public Shader9 getShader() {
		return new Shader9();
	}

	double x, y, z;
	double r = 15;
	double angularVelocity = .5;
	double theta;
	double[] p = new double[3];
	double[] p1 = new double[3];
	
	public void move(double timeStep) {

		double time = System.nanoTime()/1E9;
		
		for(int i = 0; i < world.objects.length; i++)
			if(!this.world.frozen && !this.world.objects[i].isDecoration)
			{	
				this.world.objects[i].transform(timeStep);
				this.world.objects[i].loadAuxilaryValues();
			}
		
		if(!this.world.frozen)
		{
			if(world.objects[((World9)world).movingPlatformIndex1].displacement[2] < (float) (2*Math.PI*3/((World9)world).function_length)+60-.75+60)
			{
				world.objects[((World9)world).movingPlatformIndex1].displacement[2] = (float) (2*Math.PI*3/((World9)world).function_length)+60-.75+60;
				world.objects[((World9)world).movingPlatformIndex1].linearVelocity[2] = ((World9)world).platformSpeed;
			}
			else if(world.objects[((World9)world).movingPlatformIndex1].displacement[2] > (float) (2*Math.PI*3/((World9)world).function_length)+60-.75+60+55)
			{
				world.objects[((World9)world).movingPlatformIndex1].displacement[2] = (float) (2*Math.PI*3/((World9)world).function_length)+60-.75+60+55;
				world.objects[((World9)world).movingPlatformIndex1].linearVelocity[2] = -((World9)world).platformSpeed;
			}
		}
    	
		return; //No special checks for this level.
	}

	public void collision(int i, int j) {
	}
}
