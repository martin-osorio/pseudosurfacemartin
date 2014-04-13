package com.pseudosurface.levels.template;

import java.util.Random;

import android.opengl.GLES20;

import com.pseudosurface.physics.ConvexSurface;
import com.pseudosurface.physics.Face;
import com.pseudosurface.physics.Math3D;
import com.pseudosurface.physics.TangibleFace;
import com.pseudosurface.physics.TangibleObject;
import com.pseudosurface.physics.TangibleSphere;
import com.pseudosurface.physics.XZEquation;

public abstract class World 
{
	public Renderer renderer;
	public int playerIndex;
	
	public double gravity = -4.33;
	public double[] playerGravity = new double[] { 0 , -15, 0};
	
	public double maxDrawDistance = 60;
	
	public double fallStartHeight = 0;
	
	public TangibleObject[] objects = new TangibleObject[0];
	public TangibleObject[] defaultData = new TangibleObject[0];

	public static boolean frozen;
	
	Random random = new Random(System.nanoTime());
	
	double ups_ClockLast, ups_ClockThis;
	int ups_Frames;
	String ups = "0";
	
	public World(Renderer renderer)
	{
		this.renderer = renderer; 
		frozen = false;
	}
	
	void clear()
	{
		objects = new TangibleObject[0];
		defaultData = new TangibleObject[0];
	}

	public abstract void loadSimulation();
	public abstract int getPlayerIndex();

	public void resetSimulation()
	{
		for(int i = 0; i < objects.length; i++) objects[i].loadDefaults(defaultData[i]);
		
		//A component of an old optimization methodology involving VBO's
//		if(numStaticTriangles == 0)
//		{
//			loadNumStaticTriangles();
//			loadNumDynamicTriangles();
//			loadNumUniqueEdges();
//		}
	}
	
	int numStaticTriangles;
	void loadNumStaticTriangles()
	{
		numStaticTriangles = 0;
		for(int i = 0; i < objects.length; i++)
			if(objects[i].infiniteMass)
				for(int j = 0; j < objects[i].surfaces.length; j++)
					for(int k = 0; k < objects[i].surfaces[j].faces.length; k++)
						numStaticTriangles += objects[i].surfaces[j].faces[k].faceVerticesTransformed.length;
	}

	int numDynamicTriangles;
	
	void loadNumDynamicTriangles()
	{
		numDynamicTriangles = 0;
		for(int i = 0; i < objects.length; i++)
			if(!objects[i].infiniteMass)
				for(int j = 0; j < objects[i].surfaces.length; j++)
					for(int k = 0; k < objects[i].surfaces[j].faces.length; k++)
						numDynamicTriangles += objects[i].surfaces[j].faces[k].faceVerticesTransformed.length;
	}
	
	int numUniqueEdges;
	
	void loadNumUniqueEdges()
	{
		numUniqueEdges = 0;
		for(int i = 0; i < objects.length; i++)
			for(int j = 0; j < objects[i].surfaces.length; j++)
				numUniqueEdges += objects[i].surfaces[j].edgeIndexing.length;
	}

	public void draw(float[] modelMatrix, float[] viewMatrix, float[] projectionMatrix)
	{	
		for(int i = 0; i < objects.length; i++)
		{
			if(Math3D.distance(objects[i].displacement, objects[getPlayerIndex()].displacement) < maxDrawDistance+objects[i].surfaces[0].maxRadius)
			{
				if(objects[i].cullFaces)
				{
					objects[i].draw(modelMatrix, viewMatrix, projectionMatrix);
				}
				else
				{
			        GLES20.glDisable(GLES20.GL_CULL_FACE);
					objects[i].draw(modelMatrix, viewMatrix, projectionMatrix);
			        GLES20.glEnable(GLES20.GL_CULL_FACE);
				}
			}
		}
	}
}
