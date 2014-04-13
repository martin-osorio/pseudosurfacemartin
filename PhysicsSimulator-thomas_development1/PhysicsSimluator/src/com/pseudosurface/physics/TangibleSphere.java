package com.pseudosurface.physics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;


import com.pseudosurface.levels.template.*;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class TangibleSphere extends TangibleObject
{
    private final float[] mRotationMatrix = new float[16];
	public float radius;
    
	public TangibleSphere(int slices, int stacks, float radius)
	{
		super();

		ConvexSurface surface;
		surface = Shapes.loadSphere(slices, stacks, radius);
		this.radius = radius;
		
		color_lit_highligt[0] = 0;
		color_lit_highligt[1] = 0;
		color_lit_highligt[2] = 0;
		color_lit_highligt[3] = 1.0f;

		color_shadow_highligt[0] = 0;
		color_shadow_highligt[1] = 0;
		color_shadow_highligt[2] = 0;
		color_shadow_highligt[3] = 1.0f;

//		color_lit_solid[0] = 0.5f;
//		color_lit_solid[1] = 0.5f;
//		color_lit_solid[2] = 0.5f;
//		color_lit_solid[3] = 1.0f;


		color_lit_solid[0] = 0.0f;
		color_lit_solid[1] = 0.0f;
		color_lit_solid[2] = 0.55f;
		color_lit_solid[3] = 1.0f;
		
		color_shadow_solid[0] = 0.0f;
		color_shadow_solid[1] = 0.0f;
		color_shadow_solid[2] = 0.25f;
		color_shadow_solid[3] = 1.0f;
		
		surface.relativeRotation.loadAxisAngle(0, 1, 0, 0);
		surface.relativePosition[0] = 0;
		surface.relativePosition[1] = 0;
		surface.relativePosition[2] = 0;

		surfaces = Math3D.push_back(surface, surfaces);
		this.loadPhysicalProperties();
		reloadVerts();
	}

	public void clean()
	{
		triangleVertexBuffer = null;
		edgeVertexBuffer = null;
		normalVertexBuffer = null;
	}
	
    // number of coordinates per vertex in this array
    private final int COORDS_PER_VERTEX = 3;

    private FloatBuffer triangleVertexBuffer;
    private float[] triangleVertices;
    private int triangleVertexCount;
    private int triangleVertexStride; // 4 bytes per vertex

    private FloatBuffer edgeVertexBuffer;
    private float[] edgeVertices;
    private int edgeVertexCount;
    private int edgeVertexStride; // 4 bytes per vertex
    
    private FloatBuffer normalVertexBuffer;
    private float[] normalVertices;
    private int normalVertexCount;
    private int normalVertexStride; // 4 bytes per vertex

    // Set color with red, green, blue and alpha (opacity) values
    public float color[] = { 0, 1, 0, 1};
    
    void reloadVerts()
    {	
    	ArrayList<Float> coords = new ArrayList<Float>(0);

    	for(int i = 0; i < surfaces.length; i++)
    		for(int j = 0; j < surfaces[i].faces.length; j++)
    			for(int k = 0; k < surfaces[i].faces[j].faceVertices.length; k++)
    			{
    				coords.add((float) surfaces[i].faces[j].faceVertices[k][0]);
    				coords.add((float) surfaces[i].faces[j].faceVertices[k][1]);
    				coords.add((float) surfaces[i].faces[j].faceVertices[k][2]);

    				coords.add((float) surfaces[i].faces[j].faceVertices[(k+1)%surfaces[i].faces[j].faceVertices.length][0]);
    				coords.add((float) surfaces[i].faces[j].faceVertices[(k+1)%surfaces[i].faces[j].faceVertices.length][1]);
    				coords.add((float) surfaces[i].faces[j].faceVertices[(k+1)%surfaces[i].faces[j].faceVertices.length][2]);
    			}
    	
		edgeVertices = new float[coords.size()];
		for(int i = 0; i < coords.size(); i++)
			edgeVertices[i] = coords.get(i);
		edgeVertexCount = edgeVertices.length / COORDS_PER_VERTEX;
	    edgeVertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    	
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
        		edgeVertices.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        edgeVertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        edgeVertexBuffer.put(edgeVertices);
        // set the buffer to read the first coordinate
        edgeVertexBuffer.position(0);
    	
    	coords = new ArrayList<Float>(0);

    	ArrayList<Float> normals = new ArrayList<Float>(0);

    	for(int i = 0; i < surfaces.length; i++)
    		for(int j = 0; j < surfaces[i].faces.length; j++)
    			for(int k = 0; k < surfaces[i].faces[j].faceVertices.length-1; k++)
    			{
					//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					coords.add((float) surfaces[i].faces[j].faceVertices[0][0]);
					coords.add((float) surfaces[i].faces[j].faceVertices[0][1]);
					coords.add((float) surfaces[i].faces[j].faceVertices[0][2]);
					
					//normals.add((float) surfaces[i].faces[j].faceVertices[0][0]);
					//normals.add((float) surfaces[i].faces[j].faceVertices[0][1]);
					//normals.add((float) surfaces[i].faces[j].faceVertices[0][2]);
					normals.add((float) surfaces[i].faces[j].normalTransformed[0]+(float) surfaces[i].faces[j].faceVertices[0][0]);
					normals.add((float) surfaces[i].faces[j].normalTransformed[1]+(float) surfaces[i].faces[j].faceVertices[0][1]);
					normals.add((float) surfaces[i].faces[j].normalTransformed[2]+(float) surfaces[i].faces[j].faceVertices[0][2]);
					
					//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					
					coords.add((float) surfaces[i].faces[j].faceVertices[(k+1)%surfaces[i].faces[j].faceVertices.length][0]);
					coords.add((float) surfaces[i].faces[j].faceVertices[(k+1)%surfaces[i].faces[j].faceVertices.length][1]);
					coords.add((float) surfaces[i].faces[j].faceVertices[(k+1)%surfaces[i].faces[j].faceVertices.length][2]);
					
					//normals.add((float) surfaces[i].faces[j].faceVertices[(k+1)%surfaces[i].faces[j].faceVertices.length][0]);
					//normals.add((float) surfaces[i].faces[j].faceVertices[(k+1)%surfaces[i].faces[j].faceVertices.length][1]);
					//normals.add((float) surfaces[i].faces[j].faceVertices[(k+1)%surfaces[i].faces[j].faceVertices.length][2]);
					normals.add((float) surfaces[i].faces[j].normalTransformed[0]+(float) surfaces[i].faces[j].faceVertices[(k+1)%surfaces[i].faces[j].faceVertices.length][0]);
					normals.add((float) surfaces[i].faces[j].normalTransformed[1]+(float) surfaces[i].faces[j].faceVertices[(k+1)%surfaces[i].faces[j].faceVertices.length][1]);
					normals.add((float) surfaces[i].faces[j].normalTransformed[2]+(float) surfaces[i].faces[j].faceVertices[(k+1)%surfaces[i].faces[j].faceVertices.length][2]);
					
					//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					
					coords.add((float) surfaces[i].faces[j].faceVertices[(k+2)%surfaces[i].faces[j].faceVertices.length][0]);
					coords.add((float) surfaces[i].faces[j].faceVertices[(k+2)%surfaces[i].faces[j].faceVertices.length][1]);
					coords.add((float) surfaces[i].faces[j].faceVertices[(k+2)%surfaces[i].faces[j].faceVertices.length][2]);
					
					//normals.add((float) surfaces[i].faces[j].faceVertices[(k+2)%surfaces[i].faces[j].faceVertices.length][0]);
					//normals.add((float) surfaces[i].faces[j].faceVertices[(k+2)%surfaces[i].faces[j].faceVertices.length][1]);
					//normals.add((float) surfaces[i].faces[j].faceVertices[(k+2)%surfaces[i].faces[j].faceVertices.length][2]);
					normals.add((float) surfaces[i].faces[j].normalTransformed[0]+(float) surfaces[i].faces[j].faceVertices[(k+2)%surfaces[i].faces[j].faceVertices.length][0]);
					normals.add((float) surfaces[i].faces[j].normalTransformed[1]+(float) surfaces[i].faces[j].faceVertices[(k+2)%surfaces[i].faces[j].faceVertices.length][1]);
					normals.add((float) surfaces[i].faces[j].normalTransformed[2]+(float) surfaces[i].faces[j].faceVertices[(k+2)%surfaces[i].faces[j].faceVertices.length][2]);
					
					//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    			}
    	
		triangleVertices = new float[coords.size()];
		for(int i = 0; i < coords.size(); i++)
			triangleVertices[i] = coords.get(i);
		triangleVertexCount = triangleVertices.length / COORDS_PER_VERTEX;
	    triangleVertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    	
        // initialize vertex byte buffer for shape coordinates
        bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                triangleVertices.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        triangleVertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        triangleVertexBuffer.put(triangleVertices);
        // set the buffer to read the first coordinate
        triangleVertexBuffer.position(0);
  
    	
		normalVertices = new float[normals.size()];
		for(int i = 0; i < normals.size(); i++)
			normalVertices[i] = normals.get(i);
		normalVertexCount = normalVertices.length / COORDS_PER_VERTEX;
	    normalVertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    	
        // initialize vertex byte buffer for shape coordinates
        bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                normalVertices.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        normalVertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        normalVertexBuffer.put(normalVertices);
        // set the buffer to read the first coordinate
        normalVertexBuffer.position(0);
    }


    public double[] lastPosition = new double[3];
    public Quaternion lastRotation = new Quaternion();
    double[] axis = new double[3];
	public void draw(float[] modelMatrix, float[] viewMatrix, float[] projectionMatrix) {

        lastPosition[0] += (this.translation[0]-lastPosition[0])/3.0f;
        lastPosition[1] += (this.translation[1]-lastPosition[1])/3.0f;
        lastPosition[2] += (this.translation[2]-lastPosition[2])/3.0f;

        lastRotation.W += (this.rotationQuaternion.W-lastRotation.W)/3.0f;
        lastRotation.X += (this.rotationQuaternion.X-lastRotation.X)/3.0f;
        lastRotation.Y += (this.rotationQuaternion.Y-lastRotation.Y)/3.0f;
        lastRotation.Z += (this.rotationQuaternion.Z-lastRotation.Z)/3.0f;
        
        // Add program to OpenGL environment
        GLES20.glUseProgram(Renderer.shader.mProgram);
        GLES20.glLineWidth(2.5f);
        
    	//reloadVerts();
    	
        //vertexBuffer.put(GridCoords);
        //vertexBuffer.position(0);
        
        // get handle to vertex shader's vPosition member

        // Enable a handle to the Grid vertices
        GLES20.glEnableVertexAttribArray(Renderer.shader.mPositionHandle);
       	Renderer.shader.loadMaterial(color_lit_solid, color_shadow_solid);

        // set the buffer to read the first coordinate
        triangleVertexBuffer.position(0);
        // Prepare the Grid coordinate data
        GLES20.glVertexAttribPointer(Renderer.shader.mPositionHandle, COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT, false,
                                     triangleVertexStride, triangleVertexBuffer);        	



        // Enable a handle to the Grid vertices
        GLES20.glEnableVertexAttribArray(Renderer.shader.mNormalHandle);

        // set the buffer to read the first coordinate
        normalVertexBuffer.position(0);
        // Prepare the Grid coordinate data
        GLES20.glVertexAttribPointer(Renderer.shader.mNormalHandle, COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT, false,
                                     normalVertexStride, normalVertexBuffer);


        double angle;
        
        angle = lastRotation.getAxisAngle(axis);

        float[] mRotationMatrix = modelMatrix.clone();
        
		if(axis[0]*axis[0]+axis[1]*axis[1]+axis[2]*axis[2] < .01 || angle < .0001)
			Matrix.setRotateM(mRotationMatrix, 0,0, 0,1,0);
		else
			Matrix.setRotateM(mRotationMatrix, 0,(float) Math.toDegrees(angle), (float) axis[0], (float) axis[1], (float) axis[2]);
		
        float[] finalModelMatrix = new float[16];
        
		Matrix.translateM(finalModelMatrix, 0, modelMatrix, 0, (float) this.lastPosition[0], (float) this.lastPosition[1], (float) this.lastPosition[2]);
		 
		Matrix.multiplyMM(finalModelMatrix, 0, finalModelMatrix.clone(), 0, mRotationMatrix, 0);
        
        
        
        // get handle to shape's transformation matrix
        
        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(Renderer.shader.modelMatrixHandle, 1, false, finalModelMatrix, 0);
        Renderer.checkGlError("glUniformMatrix4fv");
        GLES20.glUniformMatrix4fv(Renderer.shader.viewMatrixHandle, 1, false, viewMatrix, 0);
        Renderer.checkGlError("glUniformMatrix4fv");
        GLES20.glUniformMatrix4fv(Renderer.shader.projectionMatrixHandle, 1, false, projectionMatrix, 0);
        Renderer.checkGlError("glUniformMatrix4fv");
        
        GLES20.glEnable(GLES20.GL_POLYGON_OFFSET_FILL);
        GLES20.glPolygonOffset(1, .1f);
        
       	GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, triangleVertexCount);
        
        // set the buffer to read the first coordinate
        edgeVertexBuffer.position(0);
        // Prepare the Grid coordinate data
        GLES20.glVertexAttribPointer(Renderer.shader.mPositionHandle, COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT, false,
                                     edgeVertexStride, edgeVertexBuffer);

        Renderer.shader.loadMaterial(color_lit_highligt, color_shadow_highligt);
        
        GLES20.glDisable(GLES20.GL_POLYGON_OFFSET_FILL);
        GLES20.glUniform1i(Renderer.shader.lightEnabledHandle, 0); 
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, edgeVertexCount);
        GLES20.glUniform1i(Renderer.shader.lightEnabledHandle, 1);

        GLES20.glLineWidth(1.0f);
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(Renderer.shader.mPositionHandle);
    }
	
	public void loadDefaults(TangibleObject defaults)
	{
		super.loadDefaults(defaults);
		lastPosition = defaults.displacement.clone();
	}
}
