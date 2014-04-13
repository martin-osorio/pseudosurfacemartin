package com.pseudosurface.physics;


public class CollisionMatrix 
{
	public static Collision[][] matrix;
	
	public static void setObjects(TangibleObject[] objects)
	{
		matrix = new Collision[objects.length][objects.length];
		
		for(int i = 0; i < matrix.length; i++)
			for(int j = 0; j < matrix.length; j++)
			{
				matrix[i][j] = new Collision();
				matrix[i][j].objectA = objects[i];
				matrix[i][j].objectB = objects[j];
			}
	}
}
