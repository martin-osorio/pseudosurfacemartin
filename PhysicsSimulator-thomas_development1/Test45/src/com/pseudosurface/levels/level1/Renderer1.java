package com.pseudosurface.levels.level1;

import android.opengl.GLES20;

import com.pseudosurface.levels.template.Demon;
import com.pseudosurface.levels.template.Renderer;
import com.pseudosurface.levels.template.SimulatorActivity;
import com.pseudosurface.levels.template.Stars;
import com.pseudosurface.levels.template.TextureQuad;
import com.pseudosurface.physics.Math3D;
import com.pseudosurface.physics.TangibleFace;
import com.pseudosurface.physics.TangibleSphere;

public class Renderer1 extends Renderer{

	public Renderer1(SimulatorActivity simulation) {
		super(simulation);
		starbox = new Stars(1000, true);
	}
	
	public void additionalInitialization()
	{
	    simulation.zoom = 30f;
	}
	
	public void preliminaryRendering(float[] modelMatrix, float[] viewMatrix, float[] projectionMatrix) 
	{
		float scale = (float) ((System.nanoTime()-((World1)(simulation.world)).startTime)/22.5/1E9);
//		float maxScale = .935f;
		float maxScale = .0f;
		if(scale > maxScale) scale = maxScale;
    	GLES20.glClearColor(.45f-.5f*scale,
    						.45f-.5f*scale, 
    						.8f-.8f*scale, 
    						0.0f);
    	for(int i = 0; i < simulation.world.objects.length; i++)
    	{
    		Math3D.scaleColor(simulation.world.defaultData[i].color_shadow_solid, 1.0f-scale/2.0f, simulation.world.objects[i].color_shadow_solid);
    	}
    	
        if(Math.abs(((TangibleSphere)simulation.world.objects[simulation.world.getPlayerIndex()]).lastPosition[1]) > 50)
        {
        	((TangibleSphere)simulation.world.objects[simulation.world.getPlayerIndex()]).lastPosition[0] = 0;
        	((TangibleSphere)simulation.world.objects[simulation.world.getPlayerIndex()]).lastPosition[1] = 0;
        	((TangibleSphere)simulation.world.objects[simulation.world.getPlayerIndex()]).lastPosition[2] = 0;

        	simulation.world.objects[simulation.world.getPlayerIndex()].omega[0] = 0;
        	simulation.world.objects[simulation.world.getPlayerIndex()].omega[1] = 0;
        	simulation.world.objects[simulation.world.getPlayerIndex()].omega[2] = 0;

        	simulation.world.objects[simulation.world.getPlayerIndex()].linearVelocity[0] = 0;
        	simulation.world.objects[simulation.world.getPlayerIndex()].linearVelocity[1] = 0;
        	simulation.world.objects[simulation.world.getPlayerIndex()].linearVelocity[2] = 0;

        	double currentTime = System.nanoTime();
        	if(simulation.victoryCallback!=null)
        		simulation.victoryCallback.victoryCallback((int)(Math.random()*4), (long)((currentTime-simulation.startTime)/1E6));
        	simulation.startTime = currentTime;
        	
        	simulation.world.resetSimulation();
        }

        doDemonAI();
        
        ////////////////////////////////////////////
        ////////////////////////////////////////////
        ////////////////////////////////////////////
	}

	private void doDemonAI() 
	{
		
	}

	public void moveCamera()
	{
        float distLR = (float) (simulation.zoom);
        
    	lastEyeLook[0] += ((float) ((TangibleSphere)simulation.world.objects[simulation.world.getPlayerIndex()]).lastPosition[0]-lastEyeLook[0])/2.0f;
    	lastEyeLook[1] += ((float) ((TangibleSphere)simulation.world.objects[simulation.world.getPlayerIndex()]).lastPosition[1]+4-lastEyeLook[1])/2.0f;
    	lastEyeLook[2] += ((float) ((TangibleSphere)simulation.world.objects[simulation.world.getPlayerIndex()]).lastPosition[2]-lastEyeLook[2])/2.0f;

        lastEyePos[0] = (float) (((TangibleSphere)simulation.world.objects[simulation.world.getPlayerIndex()]).lastPosition[0]+distLR*Math.cos(simulation.angleLR));
        lastEyePos[2] = (float) (((TangibleSphere)simulation.world.objects[simulation.world.getPlayerIndex()]).lastPosition[2]+distLR*Math.sin(simulation.angleLR));
        lastEyePos[1] = (float) ((TangibleSphere)simulation.world.objects[simulation.world.getPlayerIndex()]).lastPosition[1]+10;
        if(lastEyePos[1] < 0)
        	lastEyePos[1] = 0;

        float XZVelocityMag = (float) Math.sqrt(simulation.world.objects[simulation.world.getPlayerIndex()].linearVelocity[0]*simulation.world.objects[simulation.world.getPlayerIndex()].linearVelocity[0]+
        						simulation.world.objects[simulation.world.getPlayerIndex()].linearVelocity[2]*simulation.world.objects[simulation.world.getPlayerIndex()].linearVelocity[2]);
        {
        	if(XZVelocityMag > .1)
        	{
        		newRelX += ((float) (-10.0*simulation.world.objects[simulation.world.getPlayerIndex()].linearVelocity[0]/XZVelocityMag)-newRelX)/10;
            	newRelZ += ((float) (-10.0*simulation.world.objects[simulation.world.getPlayerIndex()].linearVelocity[2]/XZVelocityMag)-newRelZ)/10;
          		simulation.angleLR = (float) (Math.atan2(-newRelZ, -newRelX)-Math.PI);

    	        lastEyePos[0] += (float) (((TangibleSphere)simulation.world.objects[simulation.world.getPlayerIndex()]).lastPosition[0]+newRelX-lastEyePos[0])/10.0f;
    	        lastEyePos[2] += (float) (((TangibleSphere)simulation.world.objects[simulation.world.getPlayerIndex()]).lastPosition[2]+newRelZ-lastEyePos[2])/10.0f;
    	        lastEyePos[1] += (float) (((TangibleFace)(simulation.world.objects[0])).equation.getHeight(((TangibleSphere)simulation.world.objects[simulation.world.getPlayerIndex()]).lastPosition[0]+newRelX-lastEyePos[0], ((TangibleSphere)simulation.world.objects[simulation.world.getPlayerIndex()]).lastPosition[2]+newRelZ-lastEyePos[2], ((TangibleFace)(simulation.world.objects[0])).equation.xou(lastEyePos[0], lastEyePos[1]), ((TangibleFace)(simulation.world.objects[0])).equation.zov(lastEyePos[0], lastEyePos[2]))+((TangibleSphere)simulation.world.objects[simulation.world.getPlayerIndex()]).lastPosition[1]+10-lastEyePos[1])/10;
        	}
//        	else
        	{
        	}
        }
	}
	public void additionalRendering(float[] modelMatrix, float[] viewMatrix, float[] projectionMatrix) 
	{
	}


}
