package com.pseudosurface.physics;


import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;



public abstract class TangibleObject 
{	
	public String name = "";
	
	public ConvexSurface[] surfaces;
	
	public Quaternion rotationQuaternion;
	Quaternion omegaQuaternion;
	Quaternion tempRotation;

	public double[] constantAcceleration = new double[3];
	public double[] linearAcceleration = new double[3];
	public double[] linearVelocity = new double[3];
	public double[] displacement = new double[3];
	public double[] alpha = new double[3];
	public double[] omega = new double[3];

	public double restitution;
	public boolean infiniteMass;
	public boolean notFalling = true;
	public boolean isDecoration = false;
	public boolean infiniteInertia;

	private double dSx, dSy, dSz;
	public double[] AABB = new double[6];
	public double[] maxPositiveAxis = new double[3];
	public double[] maxNegativeAxis = new double[3];
	
	public double density;
	public double mass;
	
	private double[] inertiaTensorBodySpace = new double[9];
	private double[] inertiaTensorBodySpaceInverse = new double[9];
	
	public double[] InertiaTensor = new double[9];
	public double[] InertiaTensorInverse = new double[9];
	
	double[] original = new double[3];
	double[] transformed = new double[3];
	public double[] translation = new double[3];

	double[] rotationMatrixTranspose = new double[9];
	double[] temp3x3InertiaTensor = new double[9];

	public double mewImpulse;
	public double mewContact;

	double energy;
	public double[] playerForce = new double[3];
	public boolean editing;
	
	double maxPlayerForce = 7;

	public boolean cullFaces = true;
	
	public static int controlledSurface = 0;

	public float[] color_lit_solid = new float[4];
	public float[] color_lit_highligt = new float[4];
	public float[] color_shadow_solid = new float[4];
	public float[] color_shadow_highligt = new float[4];
	
	public TangibleObject()
	{
		surfaces = new ConvexSurface[0];
		
		rotationQuaternion = new Quaternion();
		rotationQuaternion.loadAxisAngle(0, 1, 0, 0);
		omegaQuaternion = new Quaternion();
		omegaQuaternion.loadAxisAngle(0, 1, 0, 0);
		tempRotation = new Quaternion();
		tempRotation.loadAxisAngle(0, 1, 0, 0);
		
		constantAcceleration[0] = 0;
		constantAcceleration[1] = 0;
		constantAcceleration[2] = 0;

		linearAcceleration[0] = 0;
		linearAcceleration[1] = 0;
		linearAcceleration[2] = 0;

		linearVelocity[0] = 0;
		linearVelocity[1] = 0;
		linearVelocity[2] = 0;
		
		displacement[0] = 0;
		displacement[1] = 0;
		displacement[2] = 0;
		
		alpha[0] = 0;
		alpha[1] = 0;
		alpha[2] = 0;
		
		omega[0] = 0;
		omega[1] = 0;
		omega[2] = 0;
		
		density = 10;
		restitution = .05;
		infiniteMass = false;
		
		mewImpulse = .0;
		mewContact = .0;
		
		playerForce[0] = 0;
		playerForce[1] = 0;
		playerForce[2] = 0;

		color_lit_solid[0] = 0.0f;
		color_lit_solid[1] = 0.9f;
		color_lit_solid[2] = 0.0f;
		color_lit_solid[3] = 1.0f;
		
		color_shadow_highligt[0] = 0.0f;
		color_shadow_highligt[1] = 1.0f;
		color_shadow_highligt[2] = 0.0f;
		color_shadow_highligt[3] = 1.0f;
	
		color_shadow_solid[0] = 0.0f;
		color_shadow_solid[1] = 0.0f;
		color_shadow_solid[2] = 0.0f;
		color_shadow_solid[3] = 1.0f;
		
		color_lit_highligt[0] = 1.0f;
		color_lit_highligt[1] = 1.0f;
		color_lit_highligt[2] = 1.0f;
		color_lit_highligt[3] = 1.0f;
	}

	public double energy(double[] velocity, double[] omega)
	{
		double energy = 0;
		energy += .5*mass*Math3D.magnitude(velocity)*Math3D.magnitude(velocity);
		double[] temp = new double[3];
		Math3D.mult1x3times3x3(omega, InertiaTensor, temp);
	    energy += Math3D.mult1x3times3x1(temp, omega);
		return energy;
	}
	
	public TangibleObject(String objectName)
	{
		this();
		name = objectName;
	}
	
	public void transform(double timeStep)
	{	
		if(!editing)
		{
			
			if(Math3D.magnitude(linearVelocity) < maxPlayerForce)
			{
				linearAcceleration[0] = constantAcceleration[0] + playerForce[0];
				linearAcceleration[1] = constantAcceleration[1] + playerForce[1];
				linearAcceleration[2] = constantAcceleration[2] + playerForce[2];
			}
			else
			{
				linearAcceleration[0] = constantAcceleration[0];
				linearAcceleration[1] = constantAcceleration[1];
				linearAcceleration[2] = constantAcceleration[2];
			}
				
			linearVelocity[0] += (linearAcceleration[0])*timeStep;
			linearVelocity[1] += (linearAcceleration[1])*timeStep;
			linearVelocity[2] += (linearAcceleration[2])*timeStep;
			
			omega[0] += (alpha[0])*timeStep;
			omega[1] += (alpha[1])*timeStep;
			omega[2] += (alpha[2])*timeStep;
			
			
			displacement[0] += linearVelocity[0]*timeStep;
			displacement[1] += linearVelocity[1]*timeStep;
			displacement[2] += linearVelocity[2]*timeStep;
			
			if(omega[0]*omega[0]+omega[1]*omega[1]+omega[2]*omega[2] < .01 || timeStep < .0001)
				omegaQuaternion.loadAxisAngle(0, 1, 0, 0);
			else
				omegaQuaternion.loadOmega(omega[0]*timeStep, omega[1]*timeStep, omega[2]*timeStep);
			
			omegaQuaternion.times(rotationQuaternion, rotationQuaternion);
			rotationQuaternion.loadRotationMatrix();
			
			for(int i = 0; i < surfaces.length; i++)
			{	
				original[0] = surfaces[i].relativePosition[0];
				original[1] = surfaces[i].relativePosition[1];
				original[2] = surfaces[i].relativePosition[2];

				rotationQuaternion.transform(original, transformed);

				translation[0] = transformed[0] + displacement[0];
				translation[1] = transformed[1] + displacement[1];
				translation[2] = transformed[2] + displacement[2];

				rotationQuaternion.times(surfaces[i].relativeRotation, tempRotation);
				tempRotation.loadRotationMatrix();

				for(int j = 0; j < surfaces[i].faces.length; j++)
				{
					surfaces[i].faces[j].rotate(tempRotation, translation);
				}
			}
		}
		else
		{
			rotationQuaternion.loadRotationMatrix();
			
			for(int i = 0; i < surfaces.length; i++)
			{	
				original[0] = surfaces[i].relativePosition[0];
				original[1] = surfaces[i].relativePosition[1];
				original[2] = surfaces[i].relativePosition[2];

				rotationQuaternion.transform(original, transformed);

				translation[0] = transformed[0] + displacement[0];
				translation[1] = transformed[1] + displacement[1];
				translation[2] = transformed[2] + displacement[2];

				rotationQuaternion.times(surfaces[i].relativeRotation, tempRotation);
				tempRotation.loadRotationMatrix();

				for(int j = 0; j < surfaces[i].faces.length; j++)
				{
					surfaces[i].faces[j].rotate(tempRotation, translation);
				}
			}
		}
		energy = energy(linearVelocity, omega);
	}


	public void loadPhysicalProperties()
	{
		loadAABB();
		loadMassCenter();
		loadAABB();
		loadMass();
		loadInertiaTensor();
		

		transform(0);
		for(int i = 0; i < surfaces.length; i++)
			surfaces[i].loadMaxRadius();
	}

	void loadAABB()
	{
		transform(0);

		double[] point = new double[3];

		double minX, minY, minZ,
			   	maxX, maxY, maxZ;

		minX = Float.MAX_VALUE;
		minY = Float.MAX_VALUE;
		minZ = Float.MAX_VALUE;
			
		maxX = -Float.MAX_VALUE;
		maxY = -Float.MAX_VALUE;
		maxZ = -Float.MAX_VALUE;
			

		for(int i = 0; i < surfaces.length; i++)
			for(int j = 0; j < surfaces[i].faces.length; j++)
				for(int k = 0; k < surfaces[i].faces[j].faceVerticesTransformed.length; k++)
				{
					point[0] = surfaces[i].faces[j].faceVerticesTransformed[k][0];
					point[1] = surfaces[i].faces[j].faceVerticesTransformed[k][1];
					point[2] = surfaces[i].faces[j].faceVerticesTransformed[k][2];
					
					if(minX > point[0])
						minX = point[0];
					if(minY > point[1])
						minY = point[1];
					if(minZ > point[2])
						minZ = point[2];
					
					if(maxX < point[0])
						maxX = point[0]; 
					if(maxY < point[1])
						maxY = point[1]; 
					if(maxZ < point[2])
						maxZ = point[2];
				}

		AABB[0] = minX;
		AABB[1] = minY;
		AABB[2] = minZ;
		AABB[3] = maxX;
		AABB[4] = maxY;
		AABB[5] = maxZ;

		dSx = (AABB[3] - AABB[0])/10.0;
		dSy = (AABB[4] - AABB[1])/10.0;
		dSz = (AABB[5] - AABB[2])/10.0;
	}

	public void loadTempAABB()
	{
		maxPositiveAxis[0] = -Double.MAX_VALUE;
		maxPositiveAxis[1] = -Double.MAX_VALUE;
		maxPositiveAxis[2] = -Double.MAX_VALUE;

		maxNegativeAxis[0] = Double.MAX_VALUE;
		maxNegativeAxis[1] = Double.MAX_VALUE;
		maxNegativeAxis[2] = Double.MAX_VALUE;

		for(int i = 0; i < surfaces.length; i++)
			for(int j = 0; j < surfaces[i].faces.length; j++)
				for(int k = 0; k < surfaces[i].faces[j].faceVerticesTransformed.length; k++)
				{
					if(surfaces[i].faces[j].faceVerticesTransformed[k][0]-displacement[0] > maxPositiveAxis[0])
						maxPositiveAxis[0] = surfaces[i].faces[j].faceVerticesTransformed[k][0]-displacement[0];
					if(surfaces[i].faces[j].faceVerticesTransformed[k][1]-displacement[1] > maxPositiveAxis[1])
						maxPositiveAxis[1] = surfaces[i].faces[j].faceVerticesTransformed[k][1]-displacement[1];
					if(surfaces[i].faces[j].faceVerticesTransformed[k][2]-displacement[2] > maxPositiveAxis[2])
						maxPositiveAxis[2] = surfaces[i].faces[j].faceVerticesTransformed[k][2]-displacement[2];

					if(surfaces[i].faces[j].faceVerticesTransformed[k][0]-displacement[0] < maxNegativeAxis[0])
						maxNegativeAxis[0] = surfaces[i].faces[j].faceVerticesTransformed[k][0]-displacement[0];
					if(surfaces[i].faces[j].faceVerticesTransformed[k][1]-displacement[1] < maxNegativeAxis[1])
						maxNegativeAxis[1] = surfaces[i].faces[j].faceVerticesTransformed[k][1]-displacement[1];
					if(surfaces[i].faces[j].faceVerticesTransformed[k][2]-displacement[2] < maxNegativeAxis[2])
						maxNegativeAxis[2] = surfaces[i].faces[j].faceVerticesTransformed[k][2]-displacement[2];
				}
	}

	double[] pointVelocity = new double[3];
	double[] getPointVelocity(double[] rA) 
	{
		Math3D.cross(omega, rA, pointVelocity);
		Math3D.add3x1plus3x1(pointVelocity, linearVelocity, pointVelocity);
		return pointVelocity;
	}

	double r;
    double[] temp = new double[3];
	double[] normal = new double[3];
	double[] center = new double[3];
	
	boolean pointIsInsideTransformed(double[] point)
	{
	    	
		for(int i = 0; i < surfaces.length; i++)
		{
			double maxR = -Double.MAX_VALUE;
			for(int j = 0; j < surfaces[i].faces.length; j ++)
			{
				normal[0] = surfaces[i].faces[j].normalTransformed[0];
				normal[1] = surfaces[i].faces[j].normalTransformed[1];
				normal[2] = surfaces[i].faces[j].normalTransformed[2];
		    	
				center[0] = surfaces[i].faces[j].faceCenterTransformed[0];
				center[1] = surfaces[i].faces[j].faceCenterTransformed[1];
				center[2] = surfaces[i].faces[j].faceCenterTransformed[2];

				Math3D.sub3x1minus3x1(point, center, temp);
		    	
				r = Math3D.dot( normal, temp); 
		    	
				if(r > maxR)
		    		maxR = r;
			}
			if(maxR < 0)
				return true;
		}   
		return false;
	}

	void loadMassCenter()
	{
		double[] sum = new double[3];
		double[] total = new double[3];
		double[] thisPoint = new double[3];

		sum[0] = 0;
		sum[1] = 0;
		sum[2] = 0;
		
		total[0] = 0;
		total[1] = 0;
		total[2] = 0;


		for(double x = AABB[0]; x < AABB[3]; x+= dSx)
		for(double y = AABB[1]; y < AABB[4]; y+= dSy)
		for(double z = AABB[2]; z < AABB[5]; z+= dSz)
		{
			thisPoint[0] = x;
			thisPoint[1] = y;
			thisPoint[2] = z;

			if(pointIsInsideTransformed(thisPoint))
			{
				sum[0] += x;
				sum[1] += y;
				sum[2] += z;

				total [0] += 1;
				total [1] += 1;
				total [2] += 1;
			}
		}

		sum[0] /= total[0];
		sum[1] /= total[1];
		sum[2] /= total[2];

		for(int i = 0; i < surfaces.length; i++)
		{
			surfaces[i].relativePosition[0] -= sum[0];
			surfaces[i].relativePosition[1] -= sum[1];
			surfaces[i].relativePosition[2] -= sum[2];
		}
	}

	void loadMass()
	{	
		mass = 0;
			
		double[] thisPoint = new double[3];
		
		

		for(double x = AABB[0]; x < AABB[3]; x+= dSx)
		for(double y = AABB[1]; y < AABB[4]; y+= dSy)
		for(double z = AABB[2]; z < AABB[5]; z+= dSz)
		{
			thisPoint[0] = x;
			thisPoint[1] = y;
			thisPoint[2] = z;
				
			if(pointIsInsideTransformed(thisPoint))
				mass += dSx*dSy*dSz * density;
		}
	}

	void loadInertiaTensor()
	{
		for(int i = 0; i < 9; i++)
			inertiaTensorBodySpace[i] = 0;

		double[] thisPoint = new double[3];
			
		

		for(double x = AABB[0]; x < AABB[3]; x+= dSx)
			for(double y = AABB[1]; y < AABB[4]; y+= dSy)
				for(double z = AABB[2]; z < AABB[5]; z+= dSz)
				{
					thisPoint[0] = x;
					thisPoint[1] = y;
					thisPoint[2] = z;
						
					if(pointIsInsideTransformed(thisPoint))
					{
						inertiaTensorBodySpace[0] += (y*y + z*z);
						inertiaTensorBodySpace[1] += -(x*y);
						inertiaTensorBodySpace[2] += -(x*z);
							
						inertiaTensorBodySpace[3] += -(y*x);
						inertiaTensorBodySpace[4] += (x*x + z*z);
						inertiaTensorBodySpace[5] += -(y*z);

						inertiaTensorBodySpace[6] += -(z*x);
						inertiaTensorBodySpace[7] += -(z*y);
						inertiaTensorBodySpace[8] += (x*x + y*y);
					}
				}

		double pointMass = dSx*dSy*dSz * density; //Volume times density.		
		for(int i = 0; i < 9; i++)
			inertiaTensorBodySpace[i] *= pointMass;
		
		Math3D.matrixInverse3x3(inertiaTensorBodySpace, inertiaTensorBodySpaceInverse);
	}

	public void loadAuxilaryValues()
	{
		rotationQuaternion.loadRotationMatrix();
			
		Math3D.transpose3x3(rotationQuaternion.rotoationMatrix,
							rotationMatrixTranspose);

		///////////////////////////////////////
			
		Math3D.mult3x3times3x3(rotationQuaternion.rotoationMatrix,
							inertiaTensorBodySpaceInverse,
							temp3x3InertiaTensor);
			
		Math3D.mult3x3times3x3(temp3x3InertiaTensor,
							rotationMatrixTranspose,
							InertiaTensorInverse);

		///////////////////////////////////////
			
		Math3D.mult3x3times3x3(rotationQuaternion.rotoationMatrix,
							inertiaTensorBodySpace,
							temp3x3InertiaTensor);
			
		Math3D.mult3x3times3x3(temp3x3InertiaTensor,
							rotationMatrixTranspose,
							InertiaTensor);
			
		//Matrix.display3x3(InertiaTensorInverse); System.out.println("//////////////////////////////////");
	}

	public void loadDefaults(TangibleObject defaultData)
	{	
		rotationQuaternion.W = defaultData.rotationQuaternion.W;
		rotationQuaternion.X = defaultData.rotationQuaternion.X;
		rotationQuaternion.Y = defaultData.rotationQuaternion.Y;
		rotationQuaternion.Z = defaultData.rotationQuaternion.Z;
		rotationQuaternion.loadRotationMatrix();
		
		omegaQuaternion.W = defaultData.omegaQuaternion.W;
		omegaQuaternion.X = defaultData.omegaQuaternion.X;
		omegaQuaternion.Y = defaultData.omegaQuaternion.Y;
		omegaQuaternion.Z = defaultData.omegaQuaternion.Z;
		omegaQuaternion.loadRotationMatrix();
		
		tempRotation.W = defaultData.tempRotation.W;
		tempRotation.X = defaultData.tempRotation.X;
		tempRotation.Y = defaultData.tempRotation.Y;
		tempRotation.Z = defaultData.tempRotation.Z;
		tempRotation.loadRotationMatrix();
		
		constantAcceleration[0] = defaultData.constantAcceleration[0];
		constantAcceleration[1] = defaultData.constantAcceleration[1];
		constantAcceleration[2] = defaultData.constantAcceleration[2];

		linearAcceleration[0] = defaultData.linearAcceleration[0];
		linearAcceleration[1] = defaultData.linearAcceleration[1];
		linearAcceleration[2] = defaultData.linearAcceleration[2];

		linearVelocity[0] = defaultData.linearVelocity[0];
		linearVelocity[1] = defaultData.linearVelocity[1];
		linearVelocity[2] = defaultData.linearVelocity[2];
		
		displacement[0] = defaultData.displacement[0];
		displacement[1] = defaultData.displacement[1];
		displacement[2] = defaultData.displacement[2];
		
		alpha[0] = defaultData.alpha[0];
		alpha[1] = defaultData.alpha[1];
		alpha[2] = defaultData.alpha[2];
		
		omega[0] = defaultData.omega[0];
		omega[1] = defaultData.omega[1];
		omega[2] = defaultData.omega[2];

		density = defaultData.density;
		restitution = defaultData.restitution;
		infiniteMass = defaultData.infiniteMass;
		notFalling = defaultData.notFalling;
		isDecoration = defaultData.isDecoration;
		infiniteInertia = defaultData.infiniteInertia;
		mewImpulse = defaultData.mewImpulse;
		mewContact = defaultData.mewContact;
		
		playerForce[0] = defaultData.playerForce[0];
		playerForce[1] = defaultData.playerForce[1];
		playerForce[2] = defaultData.playerForce[2];
		
		editing = defaultData.editing;
		name = defaultData.name;

		color_lit_solid = defaultData.color_lit_solid.clone();
		color_lit_highligt = defaultData.color_lit_highligt.clone();
		color_shadow_solid = defaultData.color_shadow_solid.clone();
		color_shadow_highligt = defaultData.color_shadow_highligt.clone();
		
		cullFaces = defaultData.cullFaces;
		
		transform(0);
	}
	
	public abstract void draw(float[] modelMatrix, float[] viewMatrix, float[] projectionMatrix);
}
