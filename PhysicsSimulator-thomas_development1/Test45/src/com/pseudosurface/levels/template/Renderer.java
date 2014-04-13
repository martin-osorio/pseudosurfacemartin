/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pseudosurface.levels.template;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.pseudosurface.physics.Math3D;
import com.pseudosurface.physics.TangibleCube;
import com.pseudosurface.physics.TangibleFace;
import com.pseudosurface.physics.TangibleSphere;

import android.annotation.TargetApi;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Build;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public abstract class Renderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";

    private final float[] modelMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];

    // Declare as volatile because we are updating it from another thread
    public volatile float mAngle;
    
    public static Shader shader;
    
    public SimulatorActivity simulation;
    
    public Renderer(SimulatorActivity simulation)
    {
    	this.simulation = simulation;
    }
    
    public void clean()
    {
    	starbox.clean();
    	shader.clean();
    }
    
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        simulation.world.loadSimulation();
        simulation.startWord();
        
		shader = simulation.getShader();

        GLES20.glUseProgram(Renderer.shader.mProgram);
        
        additionalInitialization();
    }
    
    public float[] lastLightPos = new float[3];
    public static float[] lastEyePos = new float[]{0, 0, 20};
    public float[] lastEyeLook = new float[]{0, 0, 0};

	protected float newRelX = 0;
	protected float newRelZ = 0;

	TangibleCube skyBox = new TangibleCube(800, 800, 800);

	public Stars starbox = new Stars(50, true);
	LightSource lightSource = new LightSource();
	
    public void onDrawFrame(GL10 unused) {

        GLES20.glUniform1f(shader.mVShaderTimeHandle, (float) System.nanoTime()/(float)1000000000);
        Renderer.checkGlError("glUniform1f");
        
        // Draw background color
//        GLES20.glClearColor((float) Math.cos(MainActivity.time*5.0+0.0),
//			        		(float) Math.cos(MainActivity.time*5.0+4.0*Math.PI/3.0),
//			        		(float) Math.cos(MainActivity.time*5.0+2.0*Math.PI/3.0), 1.0f);
        
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);



        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.setIdentityM(viewMatrix, 0);
        Matrix.setIdentityM(projectionMatrix, 0);

//        float distLR = (float) (MainActivity.zoom*Math.cos(MainActivity.angleUD));
//        float distUD = (float) (MainActivity.zoom*Math.sin(MainActivity.angleUD));
        
        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, 
				//(float)Math.cos(MainActivity.time)*10, 5, (float)Math.sin(MainActivity.time)*10,
//				(float) (0+distLR*Math.cos(MainActivity.angleLR)),
//				(float) (0+distUD),
//				(float) (0+distLR*Math.sin(MainActivity.angleLR)),
//				0, 0, 0,
        		lastEyePos[0],
        		lastEyePos[1],
        		lastEyePos[2],
        		lastEyeLook[0],
        		lastEyeLook[1],
        		lastEyeLook[2],
        		
				0f, 1.0f, 0.0f);
        

        moveCamera();
    
        Matrix.perspectiveM(projectionMatrix, 0, 45, aspectRatio, 1,1000);
        
        GLES20.glUniformMatrix4fv(Renderer.shader.lightPerspectiveHandle, 1, false, viewMatrix.clone(), 0);
        Renderer.checkGlError("glUniformMatrix4fv");

//        lastLightPos[0] += ((float) ((TangibleSphere)simulation.world.objects[simulation.world.getPlayerIndex()]).lastPosition[0]-1.0f*simulation.world.objects[simulation.world.getPlayerIndex()].linearVelocity[0]-lastLightPos[0])/4.0f;
//        lastLightPos[1] += (float) (((TangibleSphere)simulation.world.objects[simulation.world.getPlayerIndex()]).lastPosition[1]+10f-lastLightPos[1])/4.0;
//        lastLightPos[2] += ((float)((TangibleSphere)simulation.world.objects[simulation.world.getPlayerIndex()]).lastPosition[2]-1.0f*simulation.world.objects[simulation.world.getPlayerIndex()].linearVelocity[2]-lastLightPos[2])/4.0f;
        
        lastLightPos[0] += ((float) ((TangibleSphere)simulation.world.objects[simulation.world.getPlayerIndex()]).lastPosition[0]-lastLightPos[0])/4.0f;
        lastLightPos[1] += (float) (((TangibleSphere)simulation.world.objects[simulation.world.getPlayerIndex()]).lastPosition[1]+10f-lastLightPos[1])/4.0;
        lastLightPos[2] += ((float)((TangibleSphere)simulation.world.objects[simulation.world.getPlayerIndex()]).lastPosition[2]-lastLightPos[2])/4.0f;
        
    	shader.reloadLights(viewMatrix,lastLightPos,
    				new float[]{
    					(float) ((TangibleSphere)simulation.world.objects[simulation.world.getPlayerIndex()]).lastPosition[0],
    					(float) ((TangibleSphere)simulation.world.objects[simulation.world.getPlayerIndex()]).lastPosition[1],//-MainActivity.linear_acceleration[2],
						(float) ((TangibleSphere)simulation.world.objects[simulation.world.getPlayerIndex()]).lastPosition[2]
					}); 
        shader.loadSphereShadow(((TangibleSphere)(simulation.world.objects[simulation.world.getPlayerIndex()])).lastPosition, ((TangibleSphere) simulation.world.objects[simulation.world.getPlayerIndex()]).radius, viewMatrix);
        
        double maxSpeed = 25;
        double mag = Math3D.magnitude(simulation.world.objects[simulation.world.getPlayerIndex()].linearVelocity);
        if(mag > maxSpeed)
        {
        	Math3D.scale(simulation.world.objects[simulation.world.getPlayerIndex()].linearVelocity, .95);
        }

        
        double[] r = {0, -(simulation.world.objects[simulation.world.getPlayerIndex()].surfaces[0].radius), 0.0};
        double[] rCrossOmega = new double[3];
        Math3D.cross(r, simulation.world.objects[simulation.world.getPlayerIndex()].omega, rCrossOmega);
        mag = Math3D.magnitude(rCrossOmega);
        if(mag > maxSpeed)
        {
        	Math3D.scale(simulation.world.objects[simulation.world.getPlayerIndex()].omega, .95);
        }
        
        double maxYRot = .5;
        if(Math.abs(simulation.world.objects[simulation.world.getPlayerIndex()].omega[1]) > maxYRot)
        {
        	simulation.world.objects[simulation.world.getPlayerIndex()].omega[1]*= .95;
        }
        
        
        
        // Calculate the projection and view transformation
        
        // Create a rotation for the triangle
//        long time = SystemClock.uptimeMillis() % 4000L;
//        float angle = 0.090f * ((int) time);
//        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1.0f);
//
//        // Combine the rotation matrix with the projection and camera view
//        Matrix.multiplyMM(mMVPMatrix, 0, mRotationMatrix, 0, mMVPMatrix, 0);

        // Draw triangle

        /////////////////////////

        
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glUniform1i(Renderer.shader.isStarboxHandle, 1);
        starbox.translation[0] = lastEyePos[0]; 
        starbox.translation[1] = lastEyePos[1]; 
        starbox.translation[2] = lastEyePos[2];
        starbox.draw(modelMatrix, viewMatrix, projectionMatrix);
        GLES20.glUniform1i(Renderer.shader.isStarboxHandle, 0);
        GLES20.glDisable(GLES20.GL_BLEND);
        GLES20.glUniform1i(Renderer.shader.isStarboxHandle, 0);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glUniform1i(Renderer.shader.isSkyboxHandle, 1); 
        skyBox.translation[0] = lastEyePos[0]; 
        skyBox.translation[1] = lastEyePos[1]; 
        skyBox.translation[2] = lastEyePos[2];
//        skyBox.draw(modelMatrix, viewMatrix, projectionMatrix);
        GLES20.glUniform1i(Renderer.shader.isSkyboxHandle, 0);
        GLES20.glDisable(GLES20.GL_BLEND);

        preliminaryRendering(modelMatrix, viewMatrix, projectionMatrix);
        
//        lightSource.translation[0] = lastLightPos[0]; 
//        lightSource.translation[1] = lastLightPos[1]; 
//        lightSource.translation[2] = lastLightPos[2];
        
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);

        GLES20.glEnable(GLES20.GL_CULL_FACE);
        simulation.world.draw(modelMatrix, viewMatrix, projectionMatrix);//Lighting disabled
        GLES20.glDisable(GLES20.GL_CULL_FACE);

        GLES20.glDisable(GLES20.GL_BLEND);

        GLES20.glDepthMask(false);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glUniform1i(Renderer.shader.isLightHandle, 1); 
        lightSource.translation[0] = lastLightPos[0]; 
        lightSource.translation[1] = lastLightPos[1]; 
        lightSource.translation[2] = lastLightPos[2];
        lightSource.draw(modelMatrix, viewMatrix, projectionMatrix);
        GLES20.glUniform1i(Renderer.shader.isLightHandle, 0);
        GLES20.glDisable(GLES20.GL_BLEND);
        GLES20.glDepthMask(true);
        
        additionalRendering(modelMatrix, viewMatrix, projectionMatrix);
    }

    public abstract void moveCamera();
	public abstract void additionalInitialization();
    public abstract void preliminaryRendering(float[] modelMatrix, float[] viewMatrix, float[] projectionMatrix);
	public abstract void additionalRendering(float[] modelMatrix, float[] viewMatrix, float[] projectionMatrix);
    
    float aspectRatio = 1;
    
    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        aspectRatio = (float) width / height;

		// Set the OpenGL viewport to the same size as the surface.
		GLES20.glViewport(0, 0, width, height);

		// Create a new perspective projection matrix. The height will stay the same
		// while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 10.0f;
     
        Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far);

    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        Log.e("Shader info log: ", "["+GLES20.glGetShaderInfoLog(shader)+"]");

        return shader;
    }

    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     *
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * Renderer.checkGlError("glGetUniformLocation");</pre>
     *
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
}