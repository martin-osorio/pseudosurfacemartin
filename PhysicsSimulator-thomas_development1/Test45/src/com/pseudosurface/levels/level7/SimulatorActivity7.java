package com.pseudosurface.levels.level7;

import android.util.Log;
import android.view.GestureDetector;

import com.pseudosurface.levels.template.Renderer;
import com.pseudosurface.levels.template.SimulatorActivity;
import com.pseudosurface.levels.template.World;
import com.pseudosurface.physics.Math3D;
import com.pseudosurface.physics.TangibleObject;


public class SimulatorActivity7 extends SimulatorActivity{
    
    public SimulatorActivity7()
    {
    	super();
    }
	public Renderer getNewRenderer() {
		return new Renderer7(this);
	}

	public World getNewWorld() {
		return new World7(renderer);
	}

	public Shader7 getShader() {
		return new Shader7();
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
    	
		return; //No special checks for this level.
	}

	public void collision(int i, int j) {
	}
}
