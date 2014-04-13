package com.pseudosurface.levels.template;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import com.pseudosurface.physics.Math3D;
import com.pseudosurface.physics.TangibleObject;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class Stars extends TangibleObject
{
    private final float[] mRotationMatrix = new float[16];
	
    int numStars = 150;
    boolean reflected;
    
	public Stars(int num, boolean reflected)
	{
		super();
		
		color_lit_highligt[0] = 1;
		color_lit_highligt[1] = 1;
		color_lit_highligt[2] = 1;

		color_shadow_highligt[0] = 1;
		color_shadow_highligt[1] = 1;
		color_shadow_highligt[2] = 1;

		color_lit_solid[0] = 1;
		color_lit_solid[1] = 1;
		color_lit_solid[2] = 1;
		
		color_shadow_solid[0] = 1;
		color_shadow_solid[1] = 1;
		color_shadow_solid[2] = 1;
		
		this.reflected = reflected;
		numStars = num;
		reloadVerts();
	}

	public void clean()
	{
		starVertexBuffer = null;
	}
	
    // number of coordinates per vertex in this array
    private final int COORDS_PER_VERTEX = 3;

    private FloatBuffer starVertexBuffer;
    private float[] starVertices;
    private int starVertexCount;
    private int starVertexStride; // 4 bytes per vertex

    // Set color with red, green, blue and alpha (opacity) values
    public float color[] = { 0, 0, 0, 1};
    
    void reloadVerts()
    {	

    	ArrayList<Float> coords = new ArrayList<Float>(0);
    	
    	coords = new ArrayList<Float>(0);

    	for(int i = 0; i < (reflected? numStars/2: numStars); i++)
    	{
			float[] point = {(float) Math.random()*10.0f-5.0f,
							(float) Math.random()*10.0f-5.0f,
							(float) Math.random()*10.0f-5.0f};
			
			Math3D.normalize(point);
			Math3D.scale(point, 1000.0);
			
			coords.add(point[0]);
			coords.add(point[1]);
			coords.add(point[2]);
			
			if(reflected)
			{
				coords.add(point[0]);
				coords.add(-point[1]);
				coords.add(point[2]);
			}
    	}
    	
		starVertices = new float[coords.size()];
		for(int i = 0; i < coords.size(); i++)
			starVertices[i] = coords.get(i);
		starVertexCount = starVertices.length / COORDS_PER_VERTEX;
	    starVertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    	
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                starVertices.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        starVertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        starVertexBuffer.put(starVertices);
        // set the buffer to read the first coordinate
        starVertexBuffer.position(0);
    }


	@Override
	public
	void draw(float[] modelMatrix, float[] viewMatrix, float[] projectionMatrix) {
		
        // Add program to OpenGL environment
        GLES20.glUseProgram(Renderer.shader.mProgram);
        
        // Enable a handle to the Grid vertices
        GLES20.glEnableVertexAttribArray(Renderer.shader.mPositionHandle);
        Renderer.shader.loadMaterial(color_lit_solid, color_shadow_solid);
        // set the buffer to read the first coordinate
        starVertexBuffer.position(0);
        // Prepare the Grid coordinate data
        GLES20.glVertexAttribPointer(Renderer.shader.mPositionHandle, COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT, false,
                                     starVertexStride, starVertexBuffer);

        // Set color for drawing the Grid

        GLES20.glUniform4fv(Renderer.shader.mColorHandle, 1, color_lit_solid, 0);
        
        double[] axis = new double[3];
        double angle;
        
        angle = rotationQuaternion.getAxisAngle(axis);

        float[] mRotationMatrix = modelMatrix.clone();
        
		if(axis[0]*axis[0]+axis[1]*axis[1]+axis[2]*axis[2] < .01 || angle < .0001)
			Matrix.setRotateM(mRotationMatrix, 0,0, 0,1,0);
		else
			Matrix.setRotateM(mRotationMatrix, 0,(float) Math.toDegrees(angle), (float) axis[0], (float) axis[1], (float) axis[2]);
		
        float[] finalModelMatrix = new float[16];


        this.translation[0] = Renderer.lastEyePos[0]; 
        this.translation[1] = Renderer.lastEyePos[1]; 
        this.translation[2] = Renderer.lastEyePos[2];
        
		Matrix.translateM(finalModelMatrix, 0, modelMatrix, 0, (float) this.translation[0], (float) this.translation[1], (float) this.translation[2]);
		 
		// Combine the rotation matrix with the projection and camera view
		Matrix.multiplyMM(finalModelMatrix, 0, finalModelMatrix.clone(), 0, mRotationMatrix, 0);
        
        // get handle to shape's transformation matrix
        
        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(Renderer.shader.modelMatrixHandle, 1, false, finalModelMatrix, 0);
        Renderer.checkGlError("glUniformMatrix4fv");
        GLES20.glUniformMatrix4fv(Renderer.shader.viewMatrixHandle, 1, false, viewMatrix, 0);
        Renderer.checkGlError("glUniformMatrix4fv");
        GLES20.glUniformMatrix4fv(Renderer.shader.projectionMatrixHandle, 1, false, projectionMatrix, 0);
        Renderer.checkGlError("glUniformMatrix4fv");

        // Draw the Grid
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, starVertexCount);
        
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(Renderer.shader.mPositionHandle);
    }
}

