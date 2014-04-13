package com.pseudosurface.physics;



import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Collision 
{
	public final Lock lock = new ReentrantLock();
	public double[][] collisionPoints;
	public double[][] collisionNormals;
	
	public double[] BSApoint;
	public double[] BSAnormal;
	public double BSAR;
	
	public TangibleObject objectA;
	public TangibleObject objectB;
	
	public Collision()
	{
	}
}
