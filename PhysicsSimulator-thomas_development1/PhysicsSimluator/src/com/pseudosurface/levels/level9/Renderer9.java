package com.pseudosurface.levels.level9;

import android.opengl.GLES20;

import com.pseudosurface.levels.template.Renderer;
import com.pseudosurface.levels.template.SimulatorActivity;
import com.pseudosurface.levels.template.Stars;
import com.pseudosurface.levels.template.TextureQuad;
import com.pseudosurface.physics.Math3D;
import com.pseudosurface.physics.TangibleFace;
import com.pseudosurface.physics.TangibleSphere;

public class Renderer9 extends Renderer{

	TextureQuad galaxy;
	
	public Renderer9(SimulatorActivity simulation) {
		super(simulation);
		starbox = new Stars(1000, true);
		galaxy = new TextureQuad(150, 150, this);
	}
	
	public void additionalInitialization()
	{
	    simulation.zoom = 30f;
    	GLES20.glClearColor(.45f/3f, .45f/3f, .75f/3f, 0.0f);
	}
	
	public void preliminaryRendering(float[] modelMatrix, float[] viewMatrix, float[] projectionMatrix) 
	{
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
        		simulation.victoryCallback.victoryCallback(/*(int)(Math.random()*4)*/0, (long)((currentTime-simulation.startTime)/1E6));
        	simulation.startTime = currentTime;
        	
        	simulation.world.resetSimulation();
        }
        for(int i = 0; i < simulation.world.objects.length; i++)
        {

            if(Math.abs(simulation.world.objects[i].displacement[1]) > 50 &&
            		Math3D.distance(simulation.world.defaultData[i].displacement, ((TangibleSphere)simulation.world.objects[simulation.world.getPlayerIndex()]).lastPosition)> 70)
            {
            	simulation.world.objects[i].loadDefaults(simulation.world.defaultData[i]);
            }
        }

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glUniform1i(((Shader9)shader).isGalaxyHandle, 1); 
        galaxy.translation[0] = lastEyePos[0]; 
        galaxy.translation[1] = lastEyePos[1]; 
        galaxy.translation[2] = lastEyePos[2]+190;
        galaxy.draw(modelMatrix, viewMatrix, projectionMatrix);
        GLES20.glUniform1i(((Shader9)shader).isGalaxyHandle, 0);
        GLES20.glDisable(GLES20.GL_BLEND);
	}

	public void moveCamera()
	{
        float distLR = (float) (simulation.zoom);
        
//        simulation.angleLR = -(float) (Math.PI/2f);
        
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
