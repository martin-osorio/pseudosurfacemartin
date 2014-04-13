package com.pseudosurface.physics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;


import com.pseudosurface.levels.template.*;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class TangibleTree extends TangibleObject
{
    private final float[] mRotationMatrix = new float[16];
    
	public float trunkRadius;
	public float canopyRadius;
	public float trunkHeight;
	public float canopyHeight;

	public float[] trunk_color_lit_highligt = new float[4];
	public float[] trunk_color_shadow_highligt = new float[4];
	public float[] trunk_color_lit_solid = new float[4];
	public float[] trunk_color_shadow_solid = new float[4];

	public float[] canopy_color_lit_highligt = new float[4];
	public float[] canopy_color_shadow_highligt = new float[4];
	public float[] canopy_color_lit_solid = new float[4];
	public float[] canopy_color_shadow_solid = new float[4];

	public TangibleTree(int slices, double trunkRadius, double canopyRadius, double trunkHeight, double canopyHeight)
	{
		super();

		this.trunkRadius = (float) trunkRadius;
		this.canopyRadius = (float) canopyRadius;
		this.trunkHeight = (float) trunkHeight;
		this.canopyHeight = (float) canopyHeight;

		trunk_color_lit_highligt[0] = .33f;;
		trunk_color_lit_highligt[1] = .16f;
		trunk_color_lit_highligt[2] = 0;
		trunk_color_lit_highligt[3] = 1;
		
		trunk_color_shadow_highligt[0] = .66f;
		trunk_color_shadow_highligt[1] = .32f;
		trunk_color_shadow_highligt[2] = 0;
		trunk_color_shadow_highligt[3] = 1;

		trunk_color_lit_solid[0] = .33f;
		trunk_color_lit_solid[1] = .16f;
		trunk_color_lit_solid[2] = 0f;
		trunk_color_lit_solid[3] = 1f;
				
		trunk_color_shadow_solid[0] = 0.0f;
		trunk_color_shadow_solid[1] = 0.0f;
		trunk_color_shadow_solid[2] = 0.0f;
		trunk_color_shadow_solid[3] = 1.0f;
		


		canopy_color_lit_highligt[0] = 0f;;
		canopy_color_lit_highligt[1] = .55f;
		canopy_color_lit_highligt[2] = 0;
		canopy_color_lit_highligt[3] = 1;
		
		canopy_color_shadow_highligt[0] = 0;
		canopy_color_shadow_highligt[1] = 1;
		canopy_color_shadow_highligt[2] = 0;
		canopy_color_shadow_highligt[3] = 1;

		canopy_color_lit_solid[0] = .0f;
		canopy_color_lit_solid[1] = .66f;
		canopy_color_lit_solid[2] = 0f;
		canopy_color_lit_solid[3] = 1f;
				
		canopy_color_shadow_solid[0] = 0.0f;
		canopy_color_shadow_solid[1] = 0.0f;
		canopy_color_shadow_solid[2] = 0.0f;
		canopy_color_shadow_solid[3] = 1.0f;
		
		ConvexSurface surface;

		surface = Shapes.loadCylinder(slices, trunkHeight, trunkRadius);

		surface.relativeRotation.loadAxisAngle(0, 1, 0, 0);//((double)i)*.333);
		surface.relativePosition[0] = 0;
		surface.relativePosition[1] = 0;
		surface.relativePosition[2] = 0;

		surfaces = Math3D.push_back(surface, surfaces);

		surface = Shapes.loadCone(slices, canopyHeight, canopyRadius);

		surface.relativeRotation.loadAxisAngle(0, 1, 0, 0);//((double)i)*.333);
		surface.relativePosition[0] = 0;
		surface.relativePosition[1] = trunkHeight/2.0 + canopyHeight/2.0;
		surface.relativePosition[2] = 0;
		
		surfaces = Math3D.push_back(surface, surfaces);
		
		this.loadPhysicalProperties();
		reloadVerts();
	}

	public void clean()
	{
		trunk_triangleVertexBuffer = null;	
		trunk_edgeVertexBuffer = null;
		trunk_normalVertexBuffer = null;

		canopy_triangleVertexBuffer = null;	
		canopy_edgeVertexBuffer = null;
		canopy_normalVertexBuffer = null;
	}
	
    // number of coordinates per vertex in this array
    private final int COORDS_PER_VERTEX = 3;

    private FloatBuffer trunk_triangleVertexBuffer;
    private float[] trunk_triangleVertices;
    private int trunk_triangleVertexCount;
    private int trunk_triangleVertexStride; // 4 bytes per vertex

    private FloatBuffer trunk_edgeVertexBuffer;
    private float[] trunk_edgeVertices;
    private int trunk_edgeVertexCount;
    private int trunk_edgeVertexStride; // 4 bytes per vertex
    
    private FloatBuffer trunk_normalVertexBuffer;
    private float[] trunk_normalVertices;
    private int trunk_normalVertexCount;
    private int trunk_normalVertexStride; // 4 bytes per vertex

    
    private FloatBuffer canopy_triangleVertexBuffer;
    private float[] canopy_triangleVertices;
    private int canopy_triangleVertexCount;
    private int canopy_triangleVertexStride; // 4 bytes per vertex

    private FloatBuffer canopy_edgeVertexBuffer;
    private float[] canopy_edgeVertices;
    private int canopy_edgeVertexCount;
    private int canopy_edgeVertexStride; // 4 bytes per vertex
    
    private FloatBuffer canopy_normalVertexBuffer;
    private float[] canopy_normalVertices;
    private int canopy_normalVertexCount;
    private int canopy_normalVertexStride; // 4 bytes per vertex

    // Set color with red, green, blue and alpha (opacity) values
    public float color[] = { 0, 1, 0, 1};
    
    void reloadVerts()
    {
    	reloadTrunkVerts();
    	reloadCanopyVerts();
    }
    
    void reloadTrunkVerts()
    {	
    	ArrayList<Float> coords = new ArrayList<Float>(0);

    	int i = 0; // trunk
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
    	
		trunk_edgeVertices = new float[coords.size()];
		for(i = 0; i < coords.size(); i++)
			trunk_edgeVertices[i] = coords.get(i);
		trunk_edgeVertexCount = trunk_edgeVertices.length / COORDS_PER_VERTEX;
		trunk_edgeVertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    	
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
        		trunk_edgeVertices.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        trunk_edgeVertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        trunk_edgeVertexBuffer.put(trunk_edgeVertices);
        // set the buffer to read the first coordinate
        trunk_edgeVertexBuffer.position(0);
    	
    	coords = new ArrayList<Float>(0);

    	ArrayList<Float> normals = new ArrayList<Float>(0);

    	i = 0; // trunk
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
    	
    	trunk_triangleVertices = new float[coords.size()];
		for(i = 0; i < coords.size(); i++)
			trunk_triangleVertices[i] = coords.get(i);
		trunk_triangleVertexCount = trunk_triangleVertices.length / COORDS_PER_VERTEX;
		trunk_triangleVertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    	
        // initialize vertex byte buffer for shape coordinates
        bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
        		trunk_triangleVertices.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        trunk_triangleVertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        trunk_triangleVertexBuffer.put(trunk_triangleVertices);
        // set the buffer to read the first coordinate
        trunk_triangleVertexBuffer.position(0);
  
    	
        trunk_normalVertices = new float[normals.size()];
		for(i = 0; i < normals.size(); i++)
			trunk_normalVertices[i] = normals.get(i);
		trunk_normalVertexCount = trunk_normalVertices.length / COORDS_PER_VERTEX;
		trunk_normalVertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    	
        // initialize vertex byte buffer for shape coordinates
        bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
        		trunk_normalVertices.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        trunk_normalVertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        trunk_normalVertexBuffer.put(trunk_normalVertices);
        // set the buffer to read the first coordinate
        trunk_normalVertexBuffer.position(0);
    }


    void reloadCanopyVerts()
    {	
    	ArrayList<Float> coords = new ArrayList<Float>(0);

    	int i = 1; // canopy
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
    	
		canopy_edgeVertices = new float[coords.size()];
		for(i = 0; i < coords.size(); i++)
			canopy_edgeVertices[i] = coords.get(i);
		canopy_edgeVertexCount = canopy_edgeVertices.length / COORDS_PER_VERTEX;
		canopy_edgeVertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    	
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
        		canopy_edgeVertices.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        canopy_edgeVertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        canopy_edgeVertexBuffer.put(canopy_edgeVertices);
        // set the buffer to read the first coordinate
        canopy_edgeVertexBuffer.position(0);
    	
    	coords = new ArrayList<Float>(0);

    	ArrayList<Float> normals = new ArrayList<Float>(0);

    	i = 1; // canopy
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
    	
    	canopy_triangleVertices = new float[coords.size()];
		for(i = 0; i < coords.size(); i++)
			canopy_triangleVertices[i] = coords.get(i);
		canopy_triangleVertexCount = canopy_triangleVertices.length / COORDS_PER_VERTEX;
		canopy_triangleVertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    	
        // initialize vertex byte buffer for shape coordinates
        bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
        		canopy_triangleVertices.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        canopy_triangleVertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        canopy_triangleVertexBuffer.put(canopy_triangleVertices);
        // set the buffer to read the first coordinate
        canopy_triangleVertexBuffer.position(0);
  
    	
        canopy_normalVertices = new float[normals.size()];
		for(i = 0; i < normals.size(); i++)
			canopy_normalVertices[i] = normals.get(i);
		canopy_normalVertexCount = canopy_normalVertices.length / COORDS_PER_VERTEX;
		canopy_normalVertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    	
        // initialize vertex byte buffer for shape coordinates
        bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
        		canopy_normalVertices.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        canopy_normalVertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        canopy_normalVertexBuffer.put(canopy_normalVertices);
        // set the buffer to read the first coordinate
        canopy_normalVertexBuffer.position(0);
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

////////////Draw trunk:
        GLES20.glEnableVertexAttribArray(Renderer.shader.mPositionHandle);
        GLES20.glEnable(GLES20.GL_POLYGON_OFFSET_FILL);
        GLES20.glPolygonOffset(1, .1f);
        
       	Renderer.shader.loadMaterial(trunk_color_lit_solid, trunk_color_shadow_solid);
        // set the buffer to read the first coordinate
        trunk_triangleVertexBuffer.position(0);
        // Prepare the Grid coordinate data
        GLES20.glVertexAttribPointer(Renderer.shader.mPositionHandle, COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT, false,
                                     trunk_triangleVertexStride, trunk_triangleVertexBuffer);        	



        // Enable a handle to the Grid vertices
        GLES20.glEnableVertexAttribArray(Renderer.shader.mNormalHandle);

        // set the buffer to read the first coordinate
        trunk_normalVertexBuffer.position(0);
        // Prepare the Grid coordinate data
        GLES20.glVertexAttribPointer(Renderer.shader.mNormalHandle, COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT, false,
                                     trunk_normalVertexStride, trunk_normalVertexBuffer);
        
        
       	GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, trunk_triangleVertexCount);
        
        // set the buffer to read the first coordinate
       	trunk_edgeVertexBuffer.position(0);
        // Prepare the Grid coordinate data
        GLES20.glVertexAttribPointer(Renderer.shader.mPositionHandle, COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT, false,
                                     trunk_edgeVertexStride, trunk_edgeVertexBuffer);

        Renderer.shader.loadMaterial(trunk_color_lit_highligt, trunk_color_shadow_highligt);
        
        GLES20.glDisable(GLES20.GL_POLYGON_OFFSET_FILL);
        GLES20.glUniform1i(Renderer.shader.lightEnabledHandle, 0); 
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, trunk_edgeVertexCount);
        GLES20.glUniform1i(Renderer.shader.lightEnabledHandle, 1);

        GLES20.glLineWidth(1.0f);
        GLES20.glDisableVertexAttribArray(Renderer.shader.mPositionHandle);
//////////// End of drawing trunk.
//////////// Draw canopy:
        GLES20.glEnableVertexAttribArray(Renderer.shader.mPositionHandle);
        GLES20.glEnable(GLES20.GL_POLYGON_OFFSET_FILL);
        GLES20.glPolygonOffset(1, .1f);
        
       	Renderer.shader.loadMaterial(canopy_color_lit_solid, canopy_color_shadow_solid);
       	
        // set the buffer to read the first coordinate
        canopy_triangleVertexBuffer.position(0);
        // Prepare the Grid coordinate data
        GLES20.glVertexAttribPointer(Renderer.shader.mPositionHandle, COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT, false,
                                     canopy_triangleVertexStride, canopy_triangleVertexBuffer);        	



        // Enable a handle to the Grid vertices
        GLES20.glEnableVertexAttribArray(Renderer.shader.mNormalHandle);

        // set the buffer to read the first coordinate
        canopy_normalVertexBuffer.position(0);
        // Prepare the Grid coordinate data
        GLES20.glVertexAttribPointer(Renderer.shader.mNormalHandle, COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT, false,
                                     canopy_normalVertexStride, canopy_normalVertexBuffer);
        
        
       	GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, canopy_triangleVertexCount);
        
        // set the buffer to read the first coordinate
       	canopy_edgeVertexBuffer.position(0);
        // Prepare the Grid coordinate data
        GLES20.glVertexAttribPointer(Renderer.shader.mPositionHandle, COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT, false,
                                     canopy_edgeVertexStride, canopy_edgeVertexBuffer);

        Renderer.shader.loadMaterial(canopy_color_lit_highligt, canopy_color_shadow_highligt);
        
        GLES20.glDisable(GLES20.GL_POLYGON_OFFSET_FILL);
        GLES20.glUniform1i(Renderer.shader.lightEnabledHandle, 0); 
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, canopy_edgeVertexCount);
        GLES20.glUniform1i(Renderer.shader.lightEnabledHandle, 1);

        GLES20.glLineWidth(1.0f);
        GLES20.glDisableVertexAttribArray(Renderer.shader.mPositionHandle);
////////////End of drawing canopy.

        
        
        
        
        
        
        
        
        
        
        
        // Disable vertex array
    }
}
