package com.pseudosurface.levels.level5;

import android.opengl.GLES20;

import com.pseudosurface.levels.template.LightSource;
import com.pseudosurface.levels.template.Renderer;
import com.pseudosurface.levels.template.SimulatorActivity;
import com.pseudosurface.levels.template.Stars;
import com.pseudosurface.levels.template.TextureQuad;
import com.pseudosurface.physics.Math3D;
import com.pseudosurface.physics.TangibleFace;

public class Renderer5 extends Renderer{

//	public TextureQuad demonFace;

	TextureQuad galaxy;
	public Renderer5(SimulatorActivity simulation) {
		super(simulation);
		starbox = new Stars(1000, false);

		galaxy = new TextureQuad(150, 150, this);
	}
	
	public void additionalInitialization()
	{
    	GLES20.glClearColor(.45f/4f, .45f/4f, .75f/4f, 0.0f);
//    	demonFace = new TextureQuad(200, 200);
//    	demonFace.translation[1] = 50000;
	}

	
//	float[] lastDemonPosition = new float[3];
//    float[] newDemonPosition = new float[3];
//    float[] direction = new float[3];
//    float boxRadius = (float) Math.sqrt(400*400+400*400);
	public void preliminaryRendering(float[] modelMatrix, float[] viewMatrix, float[] projectionMatrix) 
	{
		//lastEyePos[1] += simulation.world.objects[simulation.world.getPlayerIndex()].translation[1]+5;

        if(Math.abs(simulation.world.objects[simulation.world.getPlayerIndex()].displacement[1]) > 50)
        {
        	simulation.world.objects[simulation.world.getPlayerIndex()].displacement[0] = 0;
        	simulation.world.objects[simulation.world.getPlayerIndex()].displacement[1] = 0;
        	simulation.world.objects[simulation.world.getPlayerIndex()].displacement[2] = 0;

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

	    GLES20.glEnable(GLES20.GL_BLEND);
	    GLES20.glUniform1i(((Shader5)shader).isGalaxyHandle, 1); 
	    galaxy.translation[0] = lastEyePos[0]+190; 
	    galaxy.translation[1] = lastEyePos[1]; 
	    galaxy.translation[2] = lastEyePos[2];
	    galaxy.rotationQuaternion.loadAxisAngle(0, 1, 0, Math.PI/2);
	    galaxy.draw(modelMatrix, viewMatrix, projectionMatrix);
	    GLES20.glUniform1i(((Shader5)shader).isGalaxyHandle, 0);
	    GLES20.glDisable(GLES20.GL_BLEND);
	}

	
	public void moveCamera()
	{
        float distLR = (float) (simulation.zoom);
        
    	lastEyeLook[0] += ((float) simulation.world.objects[simulation.world.getPlayerIndex()].displacement[0]-lastEyeLook[0])/2.0f;
    	lastEyeLook[1] += ((float) simulation.world.objects[simulation.world.getPlayerIndex()].displacement[1]+4-lastEyeLook[1])/2.0f;
    	lastEyeLook[2] += ((float) simulation.world.objects[simulation.world.getPlayerIndex()].displacement[2]-lastEyeLook[2])/2.0f;

        lastEyePos[0] = (float) (simulation.world.objects[simulation.world.getPlayerIndex()].displacement[0]+distLR*Math.cos(simulation.angleLR));
        lastEyePos[2] = (float) (simulation.world.objects[simulation.world.getPlayerIndex()].displacement[2]+distLR*Math.sin(simulation.angleLR));
        lastEyePos[1] = (float) (((TangibleFace)(simulation.world.objects[0])).equation.getHeight(simulation.world.objects[simulation.world.getPlayerIndex()].displacement[0]+distLR*Math.cos(simulation.angleLR), simulation.world.objects[simulation.world.getPlayerIndex()].displacement[2]+distLR*Math.sin(simulation.angleLR), ((TangibleFace)(simulation.world.objects[0])).equation.xou(lastEyePos[0], lastEyePos[2]), ((TangibleFace)(simulation.world.objects[0])).equation.zov(lastEyePos[0], lastEyePos[2]))+10);

        float XZVelocityMag = (float) Math.sqrt(simulation.world.objects[simulation.world.getPlayerIndex()].linearVelocity[0]*simulation.world.objects[simulation.world.getPlayerIndex()].linearVelocity[0]+
        						simulation.world.objects[simulation.world.getPlayerIndex()].linearVelocity[2]*simulation.world.objects[simulation.world.getPlayerIndex()].linearVelocity[2]);
        {
        	if(XZVelocityMag > .1)
        	{
        		newRelX += ((float) (-10.0*simulation.world.objects[simulation.world.getPlayerIndex()].linearVelocity[0]/XZVelocityMag)-newRelX)/10;
            	newRelZ += ((float) (-10.0*simulation.world.objects[simulation.world.getPlayerIndex()].linearVelocity[2]/XZVelocityMag)-newRelZ)/10;
          		simulation.angleLR = (float) (Math.atan2(-newRelZ, -newRelX)-Math.PI);

    	        lastEyePos[0] += (float) (simulation.world.objects[simulation.world.getPlayerIndex()].displacement[0]+newRelX-lastEyePos[0])/10.0f;
    	        lastEyePos[2] += (float) (simulation.world.objects[simulation.world.getPlayerIndex()].displacement[2]+newRelZ-lastEyePos[2])/10.0f;
    	        lastEyePos[1] += (float) (((TangibleFace)(simulation.world.objects[0])).equation.getHeight(simulation.world.objects[simulation.world.getPlayerIndex()].displacement[0]+newRelX-lastEyePos[0], simulation.world.objects[simulation.world.getPlayerIndex()].displacement[2]+newRelZ-lastEyePos[2], ((TangibleFace)(simulation.world.objects[0])).equation.xou(lastEyePos[0], lastEyePos[1]), ((TangibleFace)(simulation.world.objects[0])).equation.zov(lastEyePos[0], lastEyePos[2]))+simulation.world.objects[simulation.world.getPlayerIndex()].displacement[1]+10-lastEyePos[1])/10;
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
