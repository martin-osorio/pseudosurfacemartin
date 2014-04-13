package com.pseudosurface.levels.level2;

import android.util.Log;

import com.pseudosurface.levels.template.Renderer;
import com.pseudosurface.levels.template.SimulatorActivity;
import com.pseudosurface.levels.template.World;
import com.pseudosurface.physics.Math3D;
import com.pseudosurface.physics.TangibleObject;


public class SimulatorActivity2 extends SimulatorActivity{
	
	public Renderer getNewRenderer() {
		return new Renderer2(this);
	}

	public World getNewWorld() {
		return new World2(renderer);
	}

	public Shader2 getShader() {
		return new Shader2();
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
				if(i == World2.movingPlatformsStartIndex)
				{
					theta = angularVelocity*time;
					
					r = 15;
					
					x = 5+3*10;
					y = -15; 
					z = 155+r+r*Math.cos(theta);
					
					p[0] = x;
					p[1] = y;
					p[2] = z;
	
					p1[0] = x;
					p1[1] = y;
					p1[2] = 155+r+r*Math.cos(theta+angularVelocity*timeStep);
					
					Math3D.sub3x1minus3x1(p1, p, p1);
					Math3D.scale(p1, 1.0/timeStep);
					
					this.world.objects[i].displacement[0] = x;
					this.world.objects[i].displacement[1] = y;
					this.world.objects[i].displacement[2] = z;
				
					Math3D.scale(this.world.objects[i].linearVelocity, 0);
					
					this.world.objects[i].transform(timeStep);
					this.world.objects[i].loadAuxilaryValues();

					this.world.objects[i].linearVelocity[0] = p1[0];
					this.world.objects[i].linearVelocity[1] = p1[1];
					this.world.objects[i].linearVelocity[2] = p1[2];
				}
				else if(i == World2.movingPlatformsStartIndex+1)
				{
					theta = angularVelocity*time+Math.PI;

					r = 20;

					x = 5+3*10-r-r*Math.cos(theta);
					y = -15;  
					z = 195;
					
					p[0] = x;
					p[1] = y;
					p[2] = z;

					p1[0] = 5+3*10-r-r*Math.cos(theta+angularVelocity*timeStep);
					p1[1] = y;
					p1[2] = z;
					
					
					Math3D.sub3x1minus3x1(p1, p, p1);
					Math3D.scale(p1, 1.0/timeStep);
					
					this.world.objects[i].displacement[0] = x;
					this.world.objects[i].displacement[1] = y;
					this.world.objects[i].displacement[2] = z;
				
					Math3D.scale(this.world.objects[i].linearVelocity, 0);
				
					this.world.objects[i].transform(timeStep);
					this.world.objects[i].loadAuxilaryValues();

					this.world.objects[i].linearVelocity[0] = p1[0];
					this.world.objects[i].linearVelocity[1] = p1[1];
					this.world.objects[i].linearVelocity[2] = p1[2];
				}
				else
				{
					this.world.objects[i].transform(timeStep);
					this.world.objects[i].loadAuxilaryValues();
				}
			}
    	
		return; //No special checks for this level.
	}

	public void collision(int i, int j) {
		final int player, nonPlayer;
		if(i == world.getPlayerIndex())
		{
			player = i;
			nonPlayer = j;
		}
		else if(j == world.getPlayerIndex())
		{
			player = j;
			nonPlayer = i;
			
		}
		else
			return;
		
		if(nonPlayer >= World2.fallAwayPlatformsStartIndex &&
		   nonPlayer < World2.fallAwayPlatformsStartIndex+World2.numFallawayPlatforms &&
		   world.objects[nonPlayer].notFalling)
		{
			world.objects[nonPlayer].notFalling = false;
			new Thread(new Runnable(){
				public void run() {
					try {
//						float tempG = world.objects[nonPlayer].color_lit_highligt[1];
//						world.objects[nonPlayer].color_lit_highligt[0] = tempG;
//						world.objects[nonPlayer].color_lit_highligt[1] = 0;
//						world.objects[nonPlayer].color_lit_highligt[2] = 0;
//
//						tempG = world.objects[nonPlayer].color_shadow_highligt[1];
//						world.objects[nonPlayer].color_shadow_highligt[0] = tempG;
//						world.objects[nonPlayer].color_shadow_highligt[1] = 0;
//						world.objects[nonPlayer].color_shadow_highligt[2] = 0;
//						
//						tempG = world.objects[nonPlayer].color_lit_solid[1];
//						world.objects[nonPlayer].color_lit_solid[0] = tempG;
//						world.objects[nonPlayer].color_lit_solid[1] = 0;
//						world.objects[nonPlayer].color_lit_solid[2] = 0;
//;
//						tempG = world.objects[nonPlayer].color_shadow_solid[1];
//						world.objects[nonPlayer].color_shadow_solid[0] = tempG;
//						world.objects[nonPlayer].color_shadow_solid[1] = 0;
//						world.objects[nonPlayer].color_shadow_solid[2] = 0;
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					world.objects[nonPlayer].constantAcceleration[1] = world.gravity/2.0;
				}
				
			}).start();
		}
	}
}
