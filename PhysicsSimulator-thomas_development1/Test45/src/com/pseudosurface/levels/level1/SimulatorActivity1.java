package com.pseudosurface.levels.level1;

import android.util.Log;

import com.pseudosurface.levels.template.Renderer;
import com.pseudosurface.levels.template.Shader;
import com.pseudosurface.levels.template.SimulatorActivity;
import com.pseudosurface.levels.template.World;
import com.pseudosurface.physics.TangibleObject;

public class SimulatorActivity1 extends SimulatorActivity{
	
	public Renderer getNewRenderer() {
		return new Renderer1(this);
	}

	public World getNewWorld() {
		return new World1(renderer);
	}

	public Shader getShader() {
		return new Shader1();
	}

	public void move(double timeStep) {
		
//		int i = 1;
		for(int i = 0; i < world.objects.length; i++)
			if(!this.world.frozen && !this.world.objects[i].isDecoration)
			{	
				this.world.objects[i].transform(timeStep);
				this.world.objects[i].loadAuxilaryValues();
			}
    	
		return; //No special checks for this level.
	}

	public void collision(int i, int j) 
	{
	}

}
