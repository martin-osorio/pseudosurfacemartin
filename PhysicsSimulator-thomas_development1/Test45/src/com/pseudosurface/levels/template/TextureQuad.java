package com.pseudosurface.levels.template;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import com.pseudosurface.physics.Math3D;
import com.pseudosurface.physics.TangibleObject;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class TextureQuad extends TangibleObject
{
    private final float[] mRotationMatrix = new float[16];
	
    float width = 30, height = 30;
    
    private Renderer renderer;
    
	public TextureQuad(float width, float height, Renderer renderer)
	{
		super();
		
		this.renderer = renderer;
		
		this.width = width;
		this.height = height;
		
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

		reloadVerts();
	}

    // number of coordinates per vertex in this array
    private final int COORDS_PER_VERTEX = 3;

    private FloatBuffer quadVertexBuffer;
    private float[] quadVertices;
    private int quadVertexCount;
    private int quadVertexStride; // 4 bytes per vertex

    private final int COORDS_PER_TEX_COORD = 2;
    
    private FloatBuffer texCoordsBuffer;
    private float[] texCoords;
    private int texCoordCount;
    private int texCoordStride; // 4 bytes per vertex

    // Set color with red, green, blue and alpha (opacity) values
    public float color[] = { 0, 0, 0, 1};
    
    void reloadVerts()
    {	
    	ArrayList<Float> coords = new ArrayList<Float>(0);
    	
    	coords = new ArrayList<Float>(0);
    	float[] point;
		
		point = new float[]{0-width/2, 0-height/2, 0};
		
		coords.add(point[0]);
		coords.add(point[1]);
		coords.add(point[2]);
		
		point = new float[] {width-width/2, 0-height/2, 0};
		
		coords.add(point[0]);
		coords.add(point[1]);
		coords.add(point[2]);
		
		point = new float[] {width-width/2, height-height/2, 0};
		
		coords.add(point[0]);
		coords.add(point[1]);
		coords.add(point[2]);

		point = new float[]{0-width/2, 0-height/2, 0};
		
		coords.add(point[0]);
		coords.add(point[1]);
		coords.add(point[2]);
		
		point = new float[] {width-width/2, height-height/2, 0};
		
		coords.add(point[0]);
		coords.add(point[1]);
		coords.add(point[2]);
		
		point = new float[] {0-width/2, height-height/2, 0};
		
		coords.add(point[0]);
		coords.add(point[1]);
		coords.add(point[2]);
		
		///////////////////////////////////////////////////////////////

    	ArrayList<Float> texCoordsArrayList = new ArrayList<Float>(0);

		point = new float[] {0, 0};
		
		texCoordsArrayList.add(point[0]);
		texCoordsArrayList.add(point[1]);


		point = new float[] {1, 0};
		
		texCoordsArrayList.add(point[0]);
		texCoordsArrayList.add(point[1]);


		point = new float[] {1, 1};
		
		texCoordsArrayList.add(point[0]);
		texCoordsArrayList.add(point[1]);


		point = new float[] {0, 0};
		
		texCoordsArrayList.add(point[0]);
		texCoordsArrayList.add(point[1]);

		point = new float[] {1, 1};
		
		texCoordsArrayList.add(point[0]);
		texCoordsArrayList.add(point[1]);

		point = new float[] {0, 1};
		
		texCoordsArrayList.add(point[0]);
		texCoordsArrayList.add(point[1]);

		///////////////////////////////////////////////////////////////
		
		quadVertices = new float[coords.size()];
		for(int i = 0; i < coords.size(); i++)
			quadVertices[i] = coords.get(i);
		quadVertexCount = quadVertices.length / COORDS_PER_VERTEX;
	    quadVertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    	
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                quadVertices.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        quadVertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        quadVertexBuffer.put(quadVertices);
        // set the buffer to read the first coordinate
        quadVertexBuffer.position(0);

		///////////////////////////////////////////////////////////////
		
		texCoords = new float[texCoordsArrayList.size()];
		for(int i = 0; i < texCoordsArrayList.size(); i++)
			texCoords[i] = texCoordsArrayList.get(i);
		texCoordCount = texCoords.length / COORDS_PER_TEX_COORD;
		texCoordStride = COORDS_PER_TEX_COORD * 4; // 4 bytes per vertex
    	
        // initialize vertex byte buffer for shape coordinates
        bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
        		texCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        texCoordsBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        texCoordsBuffer.put(texCoords);
        // set the buffer to read the first coordinate
        texCoordsBuffer.position(0);
    }


	@Override
	public
	void draw(float[] modelMatrix, float[] viewMatrix, float[] projectionMatrix) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(renderer.shader.mProgram);

        renderer.shader.loadMaterial(color_lit_solid, color_shadow_solid);

        texCoordsBuffer.position(0);
        GLES20.glVertexAttribPointer(renderer.shader.mTexCoordHandle, 2, GLES20.GL_FLOAT, false, 0, texCoordsBuffer);
        GLES20.glEnableVertexAttribArray(renderer.shader.mTexCoordHandle);
        
        quadVertexBuffer.position(0);
        GLES20.glVertexAttribPointer(renderer.shader.mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, quadVertexStride, quadVertexBuffer);
        GLES20.glEnableVertexAttribArray(renderer.shader.mPositionHandle);

        // Set color for drawing the Grid

        GLES20.glUniform4fv(renderer.shader.mColorHandle, 1, color_lit_solid, 0);
        
        double[] axis = new double[3];
        double angle;
        
        angle = rotationQuaternion.getAxisAngle(axis);

        float[] mRotationMatrix = modelMatrix.clone();
        
		if(axis[0]*axis[0]+axis[1]*axis[1]+axis[2]*axis[2] < .01 || angle < .0001)
			Matrix.setRotateM(mRotationMatrix, 0,0, 0,1,0);
		else
			Matrix.setRotateM(mRotationMatrix, 0,(float) Math.toDegrees(angle), (float) axis[0], (float) axis[1], (float) axis[2]);
		
        float[] finalModelMatrix = new float[16];
        
		Matrix.translateM(finalModelMatrix, 0, modelMatrix, 0, (float) this.translation[0], (float) this.translation[1], (float) this.translation[2]);
		 
		// Combine the rotation matrix with the projection and camera view
		Matrix.multiplyMM(finalModelMatrix, 0, finalModelMatrix.clone(), 0, mRotationMatrix, 0);
        
        // get handle to shape's transformation matrix
        
        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(renderer.shader.modelMatrixHandle, 1, false, finalModelMatrix, 0);
        Renderer.checkGlError("glUniformMatrix4fv");
        GLES20.glUniformMatrix4fv(renderer.shader.viewMatrixHandle, 1, false, viewMatrix, 0);
        Renderer.checkGlError("glUniformMatrix4fv");
        GLES20.glUniformMatrix4fv(renderer.shader.projectionMatrixHandle, 1, false, projectionMatrix, 0);
        Renderer.checkGlError("glUniformMatrix4fv");

        // Draw the Grid
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, quadVertexCount);
        
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(renderer.shader.mPositionHandle);
        GLES20.glDisableVertexAttribArray(renderer.shader.mTexCoordHandle);
    }
}

