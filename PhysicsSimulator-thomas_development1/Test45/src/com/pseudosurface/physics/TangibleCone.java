package com.pseudosurface.physics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;


import com.pseudosurface.levels.template.*;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class TangibleCone extends TangibleObject
{
    private final float[] mRotationMatrix = new float[16];
    
	public float coneRadius;
	public float coneHeight;

	public TangibleCone(int slices, double coneRadius, double coneHeight)
	{
		super();

		this.coneRadius = (float) coneRadius;
		this.coneHeight = (float) coneHeight;
		


		color_lit_highligt[0] = 0f;;
		color_lit_highligt[1] = .55f;
		color_lit_highligt[2] = 0;
		color_lit_highligt[3] = 1;
		
		color_shadow_highligt[0] = 0;
		color_shadow_highligt[1] = 1;
		color_shadow_highligt[2] = 0;
		color_shadow_highligt[3] = 1;

		color_lit_solid[0] = .0f;
		color_lit_solid[1] = .55f;
		color_lit_solid[2] = 0f;
		color_lit_solid[3] = 1f;
				
		color_shadow_solid[0] = 0.0f;
		color_shadow_solid[1] = .55f/2;
		color_shadow_solid[2] = 0.0f;
		color_shadow_solid[3] = 1.0f;
		
		ConvexSurface surface;

		surface = Shapes.loadCone(slices, coneHeight, coneRadius);

		surface.relativeRotation.loadAxisAngle(0, 1, 0, 0);//((double)i)*.333);
		surface.relativePosition[0] = 0;
		surface.relativePosition[1] = 0;
		surface.relativePosition[2] = 0;
		
		this.loadPhysicalProperties();
		
		surfaces = Math3D.push_back(surface, surfaces);
		
		reloadVerts();
	}

	public void clean()
	{
		cone_triangleVertexBuffer = null;	
		cone_edgeVertexBuffer = null;
		cone_normalVertexBuffer = null;
	}
	
    // number of coordinates per vertex in this array
    private final int COORDS_PER_VERTEX = 3;

    private FloatBuffer cone_triangleVertexBuffer;
    private float[] cone_triangleVertices;
    private int cone_triangleVertexCount;
    private int cone_triangleVertexStride; // 4 bytes per vertex

    private FloatBuffer cone_edgeVertexBuffer;
    private float[] cone_edgeVertices;
    private int cone_edgeVertexCount;
    private int cone_edgeVertexStride; // 4 bytes per vertex
    
    private FloatBuffer cone_normalVertexBuffer;
    private float[] cone_normalVertices;
    private int cone_normalVertexCount;
    private int cone_normalVertexStride; // 4 bytes per vertex

    // Set color with red, green, blue and alpha (opacity) values
    public float color[] = { 0, 1, 0, 1};
    
    void reloadVerts()
    {
    	ArrayList<Float> coords = new ArrayList<Float>(0);

    	int i = 0; // cone
			for(int j = 0; j < surfaces[i].faces.length; j++)
				for(int k = 0; k < surfaces[i].faces[j].faceVertices.length; k++)
				{
					coords.add((float) surfaces[i].faces[j].faceVertices[k][0]+(float)surfaces[i].relativePosition[0]);
					coords.add((float) surfaces[i].faces[j].faceVertices[k][1]+(float)surfaces[i].relativePosition[1]);
					coords.add((float) surfaces[i].faces[j].faceVertices[k][2]+(float)surfaces[i].relativePosition[2]);
	
					coords.add((float) surfaces[i].faces[j].faceVertices[(k+1)%surfaces[i].faces[j].faceVertices.length][0]+(float)surfaces[i].relativePosition[0]);
					coords.add((float) surfaces[i].faces[j].faceVertices[(k+1)%surfaces[i].faces[j].faceVertices.length][1]+(float)surfaces[i].relativePosition[1]);
					coords.add((float) surfaces[i].faces[j].faceVertices[(k+1)%surfaces[i].faces[j].faceVertices.length][2]+(float)surfaces[i].relativePosition[2]);
				}
    	
		cone_edgeVertices = new float[coords.size()];
		for(i = 0; i < coords.size(); i++)
			cone_edgeVertices[i] = coords.get(i);
		cone_edgeVertexCount = cone_edgeVertices.length / COORDS_PER_VERTEX;
		cone_edgeVertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    	
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
        		cone_edgeVertices.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        cone_edgeVertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        cone_edgeVertexBuffer.put(cone_edgeVertices);
        // set the buffer to read the first coordinate
        cone_edgeVertexBuffer.position(0);
    	
    	coords = new ArrayList<Float>(0);

    	ArrayList<Float> normals = new ArrayList<Float>(0);

    	i = 0; // cone
		for(int j = 0; j < surfaces[i].faces.length; j++)
			for(int k = 0; k < surfaces[i].faces[j].faceVertices.length-1; k++)
			{
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				coords.add((float) surfaces[i].faces[j].faceVertices[0][0]+(float)surfaces[i].relativePosition[0]);
				coords.add((float) surfaces[i].faces[j].faceVertices[0][1]+(float)surfaces[i].relativePosition[1]);
				coords.add((float) surfaces[i].faces[j].faceVertices[0][2]+(float)surfaces[i].relativePosition[2]);
				
				//normals.add((float) surfaces[i].faces[j].faceVertices[0][0]);
				//normals.add((float) surfaces[i].faces[j].faceVertices[0][1]);
				//normals.add((float) surfaces[i].faces[j].faceVertices[0][2]);
				normals.add((float) surfaces[i].faces[j].normalTransformed[0]+(float) surfaces[i].faces[j].faceVertices[0][0]);
				normals.add((float) surfaces[i].faces[j].normalTransformed[1]+(float) surfaces[i].faces[j].faceVertices[0][1]);
				normals.add((float) surfaces[i].faces[j].normalTransformed[2]+(float) surfaces[i].faces[j].faceVertices[0][2]);
				
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				coords.add((float) surfaces[i].faces[j].faceVertices[(k+1)%surfaces[i].faces[j].faceVertices.length][0]+(float)surfaces[i].relativePosition[0]);
				coords.add((float) surfaces[i].faces[j].faceVertices[(k+1)%surfaces[i].faces[j].faceVertices.length][1]+(float)surfaces[i].relativePosition[1]);
				coords.add((float) surfaces[i].faces[j].faceVertices[(k+1)%surfaces[i].faces[j].faceVertices.length][2]+(float)surfaces[i].relativePosition[2]);
				
				//normals.add((float) surfaces[i].faces[j].faceVertices[(k+1)%surfaces[i].faces[j].faceVertices.length][0]);
				//normals.add((float) surfaces[i].faces[j].faceVertices[(k+1)%surfaces[i].faces[j].faceVertices.length][1]);
				//normals.add((float) surfaces[i].faces[j].faceVertices[(k+1)%surfaces[i].faces[j].faceVertices.length][2]);
				normals.add((float) surfaces[i].faces[j].normalTransformed[0]+(float) surfaces[i].faces[j].faceVertices[(k+1)%surfaces[i].faces[j].faceVertices.length][0]);
				normals.add((float) surfaces[i].faces[j].normalTransformed[1]+(float) surfaces[i].faces[j].faceVertices[(k+1)%surfaces[i].faces[j].faceVertices.length][1]);
				normals.add((float) surfaces[i].faces[j].normalTransformed[2]+(float) surfaces[i].faces[j].faceVertices[(k+1)%surfaces[i].faces[j].faceVertices.length][2]);
				
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				coords.add((float) surfaces[i].faces[j].faceVertices[(k+2)%surfaces[i].faces[j].faceVertices.length][0]+(float)surfaces[i].relativePosition[0]);
				coords.add((float) surfaces[i].faces[j].faceVertices[(k+2)%surfaces[i].faces[j].faceVertices.length][1]+(float)surfaces[i].relativePosition[1]);
				coords.add((float) surfaces[i].faces[j].faceVertices[(k+2)%surfaces[i].faces[j].faceVertices.length][2]+(float)surfaces[i].relativePosition[2]);
				
				//normals.add((float) surfaces[i].faces[j].faceVertices[(k+2)%surfaces[i].faces[j].faceVertices.length][0]);
				//normals.add((float) surfaces[i].faces[j].faceVertices[(k+2)%surfaces[i].faces[j].faceVertices.length][1]);
				//normals.add((float) surfaces[i].faces[j].faceVertices[(k+2)%surfaces[i].faces[j].faceVertices.length][2]);
				normals.add((float) surfaces[i].faces[j].normalTransformed[0]+(float) surfaces[i].faces[j].faceVertices[(k+2)%surfaces[i].faces[j].faceVertices.length][0]);
				normals.add((float) surfaces[i].faces[j].normalTransformed[1]+(float) surfaces[i].faces[j].faceVertices[(k+2)%surfaces[i].faces[j].faceVertices.length][1]);
				normals.add((float) surfaces[i].faces[j].normalTransformed[2]+(float) surfaces[i].faces[j].faceVertices[(k+2)%surfaces[i].faces[j].faceVertices.length][2]);
				
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			}
    	
    	cone_triangleVertices = new float[coords.size()];
		for(i = 0; i < coords.size(); i++)
			cone_triangleVertices[i] = coords.get(i);
		cone_triangleVertexCount = cone_triangleVertices.length / COORDS_PER_VERTEX;
		cone_triangleVertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    	
        // initialize vertex byte buffer for shape coordinates
        bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
        		cone_triangleVertices.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        cone_triangleVertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        cone_triangleVertexBuffer.put(cone_triangleVertices);
        // set the buffer to read the first coordinate
        cone_triangleVertexBuffer.position(0);
  
    	
        cone_normalVertices = new float[normals.size()];
		for(i = 0; i < normals.size(); i++)
			cone_normalVertices[i] = normals.get(i);
		cone_normalVertexCount = cone_normalVertices.length / COORDS_PER_VERTEX;
		cone_normalVertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    	
        // initialize vertex byte buffer for shape coordinates
        bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
        		cone_normalVertices.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        cone_normalVertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        cone_normalVertexBuffer.put(cone_normalVertices);
        // set the buffer to read the first coordinate
        cone_normalVertexBuffer.position(0);
    }


    double[] axis = new double[3];
	public void draw(float[] modelMatrix, float[] viewMatrix, float[] projectionMatrix) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(Renderer.shader.mProgram);
        GLES20.glLineWidth(2.5f);

    	//reloadVerts();
    	
        //vertexBuffer.put(GridCoords);
        //vertexBuffer.position(0);
        
        // get handle to vertex shader's vPosition member

        // Enable a handle to the Grid vertices


        double angle;
        
        angle = rotationQuaternion.getAxisAngle(axis);

        float[] mRotationMatrix = modelMatrix.clone();
        
		if(axis[0]*axis[0]+axis[1]*axis[1]+axis[2]*axis[2] < .01 || angle < .0001)
			Matrix.setRotateM(mRotationMatrix, 0,0, 0,1,0);
		else
			Matrix.setRotateM(mRotationMatrix, 0,(float) Math.toDegrees(angle), (float) axis[0], (float) axis[1], (float) axis[2]);
		
        float[] finalModelMatrix = new float[16];

        
		Matrix.translateM(finalModelMatrix, 0, modelMatrix, 0, (float) this.translation[0], (float) this.translation[1], (float) this.translation[2]);
		 
		Matrix.multiplyMM(finalModelMatrix, 0, finalModelMatrix.clone(), 0, mRotationMatrix, 0);
        
        
        
        // get handle to shape's transformation matrix
        
        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(Renderer.shader.modelMatrixHandle, 1, false, finalModelMatrix, 0);
        Renderer.checkGlError("glUniformMatrix4fv");
        GLES20.glUniformMatrix4fv(Renderer.shader.viewMatrixHandle, 1, false, viewMatrix, 0);
        Renderer.checkGlError("glUniformMatrix4fv");
        GLES20.glUniformMatrix4fv(Renderer.shader.projectionMatrixHandle, 1, false, projectionMatrix, 0);
        Renderer.checkGlError("glUniformMatrix4fv");

//////////// Draw cone:
        GLES20.glEnableVertexAttribArray(Renderer.shader.mPositionHandle);
        GLES20.glEnable(GLES20.GL_POLYGON_OFFSET_FILL);
        GLES20.glPolygonOffset(1, .1f);
        
       	Renderer.shader.loadMaterial(color_lit_solid, color_shadow_solid);
       	
        // set the buffer to read the first coordinate
        cone_triangleVertexBuffer.position(0);
        // Prepare the Grid coordinate data
        GLES20.glVertexAttribPointer(Renderer.shader.mPositionHandle, COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT, false,
                                     cone_triangleVertexStride, cone_triangleVertexBuffer);        	



        // Enable a handle to the Grid vertices
        GLES20.glEnableVertexAttribArray(Renderer.shader.mNormalHandle);

        // set the buffer to read the first coordinate
        cone_normalVertexBuffer.position(0);
        // Prepare the Grid coordinate data
        GLES20.glVertexAttribPointer(Renderer.shader.mNormalHandle, COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT, false,
                                     cone_normalVertexStride, cone_normalVertexBuffer);
        
        
       	GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, cone_triangleVertexCount);
        
        // set the buffer to read the first coordinate
       	cone_edgeVertexBuffer.position(0);
        // Prepare the Grid coordinate data
        GLES20.glVertexAttribPointer(Renderer.shader.mPositionHandle, COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT, false,
                                     cone_edgeVertexStride, cone_edgeVertexBuffer);

        Renderer.shader.loadMaterial(color_lit_highligt, color_shadow_highligt);
        
        GLES20.glDisable(GLES20.GL_POLYGON_OFFSET_FILL);

        GLES20.glUniform1i(Renderer.shader.lightEnabledHandle, 0); 
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, cone_edgeVertexCount);
        GLES20.glUniform1i(Renderer.shader.lightEnabledHandle, 1);
        GLES20.glDisableVertexAttribArray(Renderer.shader.mPositionHandle);
    }
}
