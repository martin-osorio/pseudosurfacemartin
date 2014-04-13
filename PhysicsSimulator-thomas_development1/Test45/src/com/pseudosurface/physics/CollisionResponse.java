package com.pseudosurface.physics;


import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.linear.LinearConstraint;
import org.apache.commons.math.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math.optimization.linear.Relationship;
import org.apache.commons.math.optimization.linear.SimplexSolver;


public class CollisionResponse
{
	Collision collisionParameters;

	double[] impulses;
	boolean found;
	int iterations;
	int iterationsMax;
	
	
	double[] rA = new double[3];
	double[] rB = new double[3];
	double[] temp3x1 = new double[3];
	double[] vpa = new double[3];
	double[] vpb = new double[3];
	double[] relV = new double[3];

	
	//These variables store the collision point in body space for each of the objects, and also their tildes. 
	double[] rAt = new double[9];
	double[] rBt = new double[9];

	//These variables are recycled every iteration in the calculation of the numerator of J.
	double[] A = new double[3]; 
	double[] B = new double[3]; 
	double[] temp = new double[3];
	
	//These variables are recycled every iteration in the calculation of the denominator of J.
	double[] temp3x3A = new double[9];
	double[] temp3x3B = new double[9];
	double[] temp3x1A = new double[3];
	double denominator;
	
	//Used to store a reference to the collision normal every iteration:
	double[] n = new double[3];
	
	//Accumulates the magnitude of the impulse. Scale the collision normal by this to get the actual impulse vector.
	double Jmag;

	double[] tempA = new double[3];
	double[] tempB = new double[3];
	double[] J = new double[3];
	
	double[][] tangentialNormals = new double[0][3];
	double[][] restingContactTangents = new double[0][3];
	
	TangibleObject Ai; 
	TangibleObject Bi;

	public double[] forces;

	double[] nCopy = new double[3];
	
	public CollisionResponse()
	{
		iterationsMax = 10;
	}

	public void resolveCollisions(Collision parameters) 
	{
		collisionParameters = parameters;
		
		impulses = new double[collisionParameters.collisionPoints.length];
		iterations = 0;

		loadTangentialNormals();
		do{
			found = false;
			for(int i = 0; i < collisionParameters.collisionPoints.length && !found; i++)
			{
				if(getRelativeVelocity(i) < .0000)
				{
					LoadImpulse(i);
					ApplyImpulse(i);
					ApplyImpulseFriction(i);
					found = true;
				}
			}
			//System.out.println(Game.getName(contactingObjects[0][0]));//Displays the name of the ConvexPolyhedron currently colliding.
			iterations++;
		}
		while(found && iterations < iterationsMax);
		
		for(int i = 0; i < collisionParameters.collisionPoints.length; i++) ;
	}

	double getRelativeVelocity(int impulseIndex) 
	{
		Math3D.sub3x1minus3x1(collisionParameters.collisionPoints[impulseIndex], collisionParameters.objectA.displacement, rA);
		Math3D.sub3x1minus3x1(collisionParameters.collisionPoints[impulseIndex], collisionParameters.objectB.displacement, rB);
			
		Math3D.cross(collisionParameters.objectA.omega, rA, temp3x1);
		Math3D.add3x1plus3x1(temp3x1, collisionParameters.objectA.linearVelocity, vpa);

		Math3D.cross(collisionParameters.objectB.omega, rB, temp3x1);
		Math3D.add3x1plus3x1(temp3x1, collisionParameters.objectB.linearVelocity, vpb);
			
		Math3D.sub3x1minus3x1(vpa, vpb, relV);
		return Math3D.dot(relV, collisionParameters.collisionNormals[impulseIndex]);
	}

	double getRelativeVelocity(int impulseIndex, CollisionDetector parameters) 
	{
		Math3D.sub3x1minus3x1(parameters.collisionPoints[impulseIndex], parameters.collisionObjects[impulseIndex][0].displacement, rA);
		Math3D.sub3x1minus3x1(parameters.collisionPoints[impulseIndex], parameters.collisionObjects[impulseIndex][1].displacement, rB);
			
		Math3D.cross(parameters.collisionObjects[impulseIndex][0].omega, rA, temp3x1);
		Math3D.add3x1plus3x1(temp3x1, parameters.collisionObjects[impulseIndex][0].linearVelocity, vpa);

		Math3D.cross(parameters.collisionObjects[impulseIndex][1].omega, rB, temp3x1);
		Math3D.add3x1plus3x1(temp3x1, parameters.collisionObjects[impulseIndex][1].linearVelocity, vpb);
			
		Math3D.sub3x1minus3x1(vpa, vpb, relV);
		return Math3D.dot(relV, parameters.collisionNormals[impulseIndex]);
	}
	
	double getRelativeContactVelocity(int impulseIndex) 
	{
		Math3D.sub3x1minus3x1(collisionParameters.collisionPoints[impulseIndex], collisionParameters.objectA.displacement, rA);
		Math3D.sub3x1minus3x1(collisionParameters.collisionPoints[impulseIndex], collisionParameters.objectB.displacement, rB);
			
		Math3D.cross(collisionParameters.objectA.omega, rA, temp3x1);
		Math3D.add3x1plus3x1(temp3x1, collisionParameters.objectA.linearVelocity, vpa);

		Math3D.cross(collisionParameters.objectB.omega, rB, temp3x1);
		Math3D.add3x1plus3x1(temp3x1, collisionParameters.objectB.linearVelocity, vpb);
			
		Math3D.sub3x1minus3x1(vpa, vpb, relV);
		return Math3D.dot(relV, collisionParameters.collisionNormals[impulseIndex]);
	}

	void LoadImpulse(int impulseIndex) 
	{
			///////////////////////////////////////////
			/////////////////NUMERATOR/////////////////
			///////////////////////////////////////////
				Math3D.sub3x1minus3x1(collisionParameters.collisionPoints[impulseIndex], collisionParameters.objectA.displacement, rA);
				Math3D.sub3x1minus3x1(collisionParameters.collisionPoints[impulseIndex], collisionParameters.objectB.displacement, rB);

				//The normal of the collision always faces away from object B ("ConvexPolyhedron contactingObjects[0][1]")
				n[0] = collisionParameters.collisionNormals[impulseIndex][0]; 
				n[1] = collisionParameters.collisionNormals[impulseIndex][1]; 
				n[2] = collisionParameters.collisionNormals[impulseIndex][2]; 
				
				Jmag = - ((collisionParameters.objectA.restitution + collisionParameters.objectB.restitution)/2.0 + 1.0);

				Math3D.loadTilde(rA, rAt);
				Math3D.loadTilde(rB, rBt);
				
				Math3D.mult3x3times3x1(rAt, collisionParameters.objectA.omega, A);
				Math3D.mult3x3times3x1(rBt, collisionParameters.objectB.omega, B);
				
				Math3D.sub3x1minus3x1(collisionParameters.objectA.linearVelocity, collisionParameters.objectB.linearVelocity, temp);
				
				Math3D.sub3x1minus3x1(temp, A, temp); //Minus (-)
				Math3D.add3x1plus3x1(temp, B, temp);  //Plus (+)
				
				Jmag *= Math3D.dot(temp, n);
			///////////////////////////////////////////
			////////////////DENOMINATOR////////////////
			///////////////////////////////////////////
				denominator = 0;
				if(!collisionParameters.objectA.infiniteMass)
				{
					denominator += 1.0/collisionParameters.objectA.mass;

					Math3D.mult3x3times3x3(rAt, collisionParameters.objectA.InertiaTensorInverse, temp3x3A);
					Math3D.mult3x3times3x3(temp3x3A, rAt, temp3x3B);
					Math3D.mult3x3times3x1(temp3x3B, n, temp3x1A);
					
					denominator -= Math3D.dot(n, temp3x1A);
				}
				
				if(!collisionParameters.objectB.infiniteMass)
				{
					denominator += 1.0/collisionParameters.objectB.mass;

					Math3D.mult3x3times3x3(rBt, collisionParameters.objectB.InertiaTensorInverse, temp3x3A);
					Math3D.mult3x3times3x3(temp3x3A, rBt, temp3x3B);
					Math3D.mult3x3times3x1(temp3x3B, n, temp3x1A);
					
					denominator -= Math3D.dot(n, temp3x1A);
				}

				Jmag /= denominator;
				impulses[impulseIndex] = Jmag;
	}


	void ApplyImpulse(int impulseIndex)
	{			
		
		if(impulses[impulseIndex] > 0)
		{
			J[0] = collisionParameters.collisionNormals[impulseIndex][0];
			J[1] = collisionParameters.collisionNormals[impulseIndex][1];
			J[2] = collisionParameters.collisionNormals[impulseIndex][2];

			Math3D.scale(J, impulses[impulseIndex]);
			
			if(!collisionParameters.objectA.infiniteMass) //Objects with infinite mass are not affected by impulses.
			{
				collisionParameters.objectA.linearVelocity[0] += J[0]/collisionParameters.objectA.mass;
				collisionParameters.objectA.linearVelocity[1] += J[1]/collisionParameters.objectA.mass;
				collisionParameters.objectA.linearVelocity[2] += J[2]/collisionParameters.objectA.mass;

				Math3D.sub3x1minus3x1(collisionParameters.collisionPoints[impulseIndex], collisionParameters.objectA.displacement, rA);
				Math3D.cross(rA, J, tempA);
				Math3D.mult3x3times3x1(collisionParameters.objectA.InertiaTensorInverse, tempA, tempB);
			
				if(!collisionParameters.objectA.infiniteInertia)
				{
					collisionParameters.objectA.omega[0] += tempB[0];
					collisionParameters.objectA.omega[1] += tempB[1];
					collisionParameters.objectA.omega[2] += tempB[2];
				}
			}
				
			Math3D.scale(J, -1);
				
			if(!collisionParameters.objectB.infiniteMass) //Objects with infinite mass are not affected by impulses.
			{
				collisionParameters.objectB.linearVelocity[0] += J[0]/collisionParameters.objectB.mass;
				collisionParameters.objectB.linearVelocity[1] += J[1]/collisionParameters.objectB.mass;
				collisionParameters.objectB.linearVelocity[2] += J[2]/collisionParameters.objectB.mass;

				Math3D.sub3x1minus3x1(collisionParameters.collisionPoints[impulseIndex], collisionParameters.objectB.displacement, rB);
				Math3D.cross(rB, J, tempA);
				Math3D.mult3x3times3x1(collisionParameters.objectB.InertiaTensorInverse, tempA, tempB);
				
				if(!collisionParameters.objectB.infiniteInertia)
				{
					collisionParameters.objectB.omega[0] += tempB[0];
					collisionParameters.objectB.omega[1] += tempB[1];
					collisionParameters.objectB.omega[2] += tempB[2];
				}
			}
		}
	}


	public void clear()
	{
		if(collisionParameters == null)
			return;
		collisionParameters.collisionPoints = new double[0][0];
		collisionParameters.collisionNormals = new double[0][0];
		restingContactTangents = new double[0][3];
	}

	Collection<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
	
	public void LoadContactForces()
	{	
		forces =  new double[collisionParameters.collisionPoints.length];
		double[] b = new double[collisionParameters.collisionPoints.length];
		double[][] m = new double[collisionParameters.collisionPoints.length][];
		for(int i = 0; i < collisionParameters.collisionPoints.length; i++)
			m[i] = new double[collisionParameters.collisionPoints.length];
			
		for(int i = 0; i < collisionParameters.collisionPoints.length; i++)
		{
			Ai = collisionParameters.objectA;
			Bi = collisionParameters.objectB;
				
			for(int j = 0; j < collisionParameters.collisionPoints.length; j++)
			{
				m[i][j] = loadContactTermM(i,j);
			}
			b[i] = loadContactTermB(i);
		}
		
		for(int i = 0; i < collisionParameters.collisionPoints.length; i++)
			b[i] *= -1.0;

		constraints.clear();
		for(int i = 0; i < collisionParameters.collisionPoints.length; i++)
			constraints.add(new LinearConstraint(m[i], Relationship.GEQ, b[i]));

		double[] forceCoefficiants = new double[collisionParameters.collisionPoints.length];
		for(int i = 0; i < collisionParameters.collisionPoints.length; i++)
			forceCoefficiants[i] = 1;
		
		LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(forceCoefficiants, 0);
		
		RealPointValuePair solution = null;
		try
		{
			solution = new SimplexSolver().optimize(objectiveFunction, constraints, GoalType.MINIMIZE, true);
		}
		catch(Exception e)
		{
			for(int i = 0; i < collisionParameters.collisionPoints.length; i++)
				forces[i] = 0;
			return;
		}
		
		for(int i = 0; i < collisionParameters.collisionPoints.length; i++)
			forces[i] = solution.getPoint()[i];
			
		/*
		if(forces.length > 0)
		{
			double[] Xi = findVanishingContacts(m, forces, b);
			double vanishingThreshold = 1;  // if Xi > vanishingThreshold, it is vanishing
			int[] combination = new int[Xi.length]; // 0 = Vanishing; 1 = Non-Vanishing
			for(int i = 0; i < combination.length; i++)
				if(Xi[i] < vanishingThreshold)
					combination[i] = 1;
			
			for(int i = 0; i < combination.length; i++)
			{
				if(combination[i] == 0)
					for(int j = 0; j < m.length; j++)
						m[j][i] = 0;
			}


			forceCoefficiants = new double[collisionParameters.collisionPoints.length];
			constraints = new ArrayList<LinearConstraint>();
			for(int i = 0; i < collisionParameters.collisionPoints.length; i++)
				if(combination[i] == 1)
					constraints.add(new LinearConstraint(m[i], Relationship.EQ, b[i]));
					for(int j = 0; j < m.length; j++)
						m[j][i] = 0;
				}
				else
				{
					forceCoefficiants[i] = 1;
					constraints.add(new LinearConstraint(m[i], Relationship.GEQ, b[i]));

			forceCoefficiants = new double[collisionParameters.collisionPoints.length];
			for(int i = 0; i < collisionParameters.collisionPoints.length; i++)
				forceCoefficiants[i] = (double) combination[i];
			
			objectiveFunction = new LinearObjectiveFunction(forceCoefficiants, 0);
			
			solution = null;
			try
			{
				objectiveFunction = new LinearObjectiveFunction(forceCoefficiants, 0);
				solution = new SimplexSolver().optimize(objectiveFunction, constraints, GoalType.MINIMIZE, true);
			}
			catch(Exception e)
			{
				return;
			}
			
			forces = solution.getPoint().clone();
		}
		*/
	}

	private double[] findVanishingContacts(double[][] m, double[] f, double[] negB)
	{	
		double[] Xi = new double[f.length];
		
		for(int i = 0; i < f.length; i++)
		{
			for(int j = 0; j < f.length; j++)
			{
				Xi[i] +=  f[j]*m[i][j];
			}
			Xi[i] -= negB[i];
		}

		/*
		for(int i = 0; i < Xi.length; i++)
		{
			System.out.printf("'%5.5f'", Xi[i]);
			System.out.println();
		}
		System.out.println("-------------------------------");
		*/
		
		return Xi;
	}
	
	/* Legacy Code..
	private double satisfiesConstraintC(double[][] m, double[] f, double[] negB)
	{	
		double[][] F = new double[f.length][1];
		double[][] Ft = new double[1][f.length];
		double[][] B = new double[f.length][1];
		
		for(int i = 0; i < f.length; i++)
		{
			F[i][0] = f[i];
			Ft[0][i] = f[i];
			B[i][0] = -negB[i];
		}
		
		double[][] left;
		left = Matrix.multKxMtimesMxN(Ft, m);
		left = Matrix.multKxMtimesMxN(left, F);

		double[][] right;
		right = Matrix.multKxMtimesMxN(Ft, B);
		right[0][0] = -right[0][0];
		
		return Math.abs(left[0][0]-right[0][0]);
	}
	*/

	double loadContactTermM(int i, int j) 
	{
		int phiA = phi(j, Ai);
		int phiB = phi(j, Bi);

		double sumA[] = {0, 0, 0};
		double sumB[] = {0, 0, 0};
			
		if(phiA != 0)
		{
			nCopy[0] = collisionParameters.collisionNormals[i][0];
			nCopy[1] = collisionParameters.collisionNormals[i][1];
			nCopy[2] = collisionParameters.collisionNormals[i][2];

			Math3D.scale(nCopy, 1.0/Ai.mass);

			Math3D.sub3x1minus3x1(collisionParameters.collisionPoints[i], Ai.displacement, rA);
			Math3D.sub3x1minus3x1(collisionParameters.collisionPoints[i], Ai.displacement, rB);

			Math3D.mult3x3times3x1(Ai.InertiaTensorInverse, rB, sumA);
			Math3D.cross(rA, sumA, rB);
			Math3D.cross(rB, collisionParameters.collisionNormals[i], sumA);
				
			Math3D.sub3x1minus3x1(nCopy, sumA, sumA);
			Math3D.scale(sumA, (double)(phiA));
		}
		if(phiB != 0)
		{
			nCopy[0] = collisionParameters.collisionNormals[i][0];
			nCopy[1] = collisionParameters.collisionNormals[i][1];
			nCopy[2] = collisionParameters.collisionNormals[i][2];

			Math3D.scale(nCopy, 1.0/Bi.mass);

			Math3D.sub3x1minus3x1(collisionParameters.collisionPoints[i], Bi.displacement, rA);
			Math3D.sub3x1minus3x1(collisionParameters.collisionPoints[i], Bi.displacement, rB);

			Math3D.mult3x3times3x1(Bi.InertiaTensorInverse, rB, sumB);
			Math3D.cross(rA, sumB, rB);
			Math3D.cross(rB, collisionParameters.collisionNormals[i], sumB);
				
			Math3D.sub3x1minus3x1(nCopy, sumB, sumB);
			Math3D.scale(sumB, (double)(phiB));
		}
			
		Math3D.sub3x1minus3x1(sumA, sumB, sumA);
		return Math3D.dot(sumA, collisionParameters.collisionNormals[i]);
	}

	double accel[] = {0, 0, 0};
	double alpha[] = new double[3];
	double[] tempA3x3 = new double[9];
	double[] tempB3x3 = new double[9];
	double[] omegaTilde = new double[9];
	double[] tempA3x1 = new double[3];
	double[] tempB3x1 = new double[3];
	double[] runningSum = new double[3];
	
	public void ApplyContactForces(double timeStep, TangibleObject object)
	{	
		Math3D.scale(accel, 0);
		for(int i = 0; i < collisionParameters.collisionNormals.length; i ++)
		{
			temp[0] = collisionParameters.collisionNormals[i][0];
			temp[1] = collisionParameters.collisionNormals[i][1];
			temp[2] = collisionParameters.collisionNormals[i][2];

			Math3D.scale(temp, forces[i]/object.mass*phi(i, object));
			Math3D.add3x1plus3x1(accel, temp, accel);
		}
		
		Math3D.scale(accel, timeStep);
		Math3D.add3x1plus3x1(object.linearVelocity, accel, object.linearVelocity);

		////////////////////////////////////////////////////
		////////////////////////////////////////////////////
		////////////////////////////////////////////////////
		
		Math3D.loadTilde(object.omega, omegaTilde);
		
		Math3D.mult3x3times3x3(object.InertiaTensorInverse, omegaTilde, tempA3x3);
		Math3D.mult3x3times3x3(tempA3x3, object.InertiaTensor, tempB3x3);
		Math3D.mult3x3times3x1(tempB3x3, object.omega, alpha);
		Math3D.scale(alpha, -1d);
		
		Math3D.scale(runningSum, 0);
		
		for(int i = 0; i < collisionParameters.collisionPoints.length; i ++)
		{
			double phi = phi(i, object);
			Math3D.sub3x1minus3x1(collisionParameters.collisionPoints[i], object.displacement, tempA3x1);
			Math3D.cross(tempA3x1, collisionParameters.collisionNormals[i], tempB3x1);
			Math3D.mult3x3times3x1(object.InertiaTensorInverse, tempB3x1, tempA3x1);
			Math3D.scale(tempA3x1, forces[i]*phi);
			Math3D.add3x1plus3x1(runningSum, tempA3x1, runningSum);
		}
		Math3D.add3x1plus3x1(runningSum, alpha, alpha);

		Math3D.scale(alpha, timeStep);
		
		if(Math3D.magnitude(runningSum) != 0 && !object.infiniteInertia) //Without this line Omega gets changed even when all forces on the object are 0.
			Math3D.add3x1plus3x1(object.omega, alpha, object.omega);

	}

	int phi(int i, TangibleObject obj)
	{
		if(collisionParameters.objectA == obj && !collisionParameters.objectA.infiniteMass)
			return 1;
		else if(collisionParameters.objectB == obj && !collisionParameters.objectB.infiniteMass)
			return -1;
		else 
			return 0;
	}

	double sum[] = {0, 0, 0};
	double sumA[] = {0, 0, 0};
	double sumB[] = {0, 0, 0};
	
	double loadContactTermB(int i) 
	{
		Math3D.scale(sum, 0);	
		
		Math3D.sub3x1minus3x1(Ai.linearAcceleration, Bi.linearAcceleration, sum);
			
		Math3D.sub3x1minus3x1(collisionParameters.collisionPoints[i], Ai.displacement, rA);
		Math3D.cross(Ai.omega, rA, rB);
		Math3D.cross(Ai.omega, rB, rA);
			
		Math3D.add3x1plus3x1(sum, rA, sum);

		Math3D.sub3x1minus3x1(collisionParameters.collisionPoints[i], Bi.displacement, rA);
		Math3D.cross(Bi.omega, rA, rB);
		Math3D.cross(Bi.omega, rB, rA);
			
		Math3D.sub3x1minus3x1(sum, rA, sum);
			
		////////////////////////////////////////////////////////////////

		Math3D.scale(sumA, 0);
		Math3D.scale(sumB, 0);
		
		////////////////////////////////////////////////////////////////
		if(!Ai.infiniteMass)
		{
			Math3D.loadTilde(Ai.omega, rAt);
			Math3D.mult3x3times3x3(Ai.InertiaTensorInverse, rAt, rBt);
			Math3D.mult3x3times3x3(rBt, Ai.InertiaTensor, rAt);
			Math3D.mult3x3times3x1(rAt, Ai.omega, rA);
				
			Math3D.sub3x1minus3x1(Ai.alpha, rA, rB);
			Math3D.sub3x1minus3x1(collisionParameters.collisionPoints[i], Ai.displacement, rA);
				
			Math3D.cross(rA, rB, sumA);
		}
		////////////////////////////////////////////////////////////////
		if(!Bi.infiniteMass)
		{
			Math3D.loadTilde(Bi.omega, rAt);
			Math3D.mult3x3times3x3(Bi.InertiaTensorInverse, rAt, rBt);
			Math3D.mult3x3times3x3(rBt, Bi.InertiaTensor, rAt);
			Math3D.mult3x3times3x1(rAt, Bi.omega, rA);
				
			Math3D.sub3x1minus3x1(Bi.alpha, rA, rB);
			Math3D.sub3x1minus3x1(collisionParameters.collisionPoints[i], Bi.displacement, rA);
				
			Math3D.cross(rA, rB, sumB);
		}
		////////////////////////////////////////////////////////////////
		Math3D.sub3x1minus3x1(sum, sumA, sum);
		Math3D.add3x1plus3x1(sum, sumB, sum);

		////////////////////////////////////////////////////////////////
		double answer = Math3D.dot(sum, collisionParameters.collisionNormals[i]);

		Math3D.sub3x1minus3x1(Ai.linearVelocity, Bi.linearVelocity, sum);
			
		Math3D.sub3x1minus3x1(collisionParameters.collisionPoints[i], Ai.displacement, rA);
		Math3D.cross(Ai.omega, rA, sumA);
		Math3D.sub3x1minus3x1(collisionParameters.collisionPoints[i], Bi.displacement, rA);
		Math3D.cross(Bi.omega, rA, sumB);
			
		Math3D.add3x1plus3x1(sum, sumA, sum);
		Math3D.sub3x1minus3x1(sum, sumB, sum);
			
		Math3D.cross(Bi.omega, collisionParameters.collisionNormals[i], rA);
			
		answer += 2*Math3D.dot(sum, rA);

		return answer;
	}


double mewImpulse = .550;
double mewContact = 5.00;
	
	void ApplyImpulseFriction(int impulseIndex)
	{

		Math3D.sub3x1minus3x1(collisionParameters.collisionPoints[impulseIndex], collisionParameters.objectA.displacement, rA);
		Math3D.sub3x1minus3x1(collisionParameters.collisionPoints[impulseIndex], collisionParameters.objectB.displacement, rB);

		Math3D.cross(collisionParameters.objectA.omega, rA, temp3x1);
		Math3D.add3x1plus3x1(temp3x1, collisionParameters.objectA.linearVelocity, vpa);

		Math3D.cross(collisionParameters.objectB.omega, rB, temp3x1);
		Math3D.add3x1plus3x1(temp3x1, collisionParameters.objectB.linearVelocity, vpb);

		Math3D.sub3x1minus3x1(vpa, vpb, relV);
			
		J[0] = tangentialNormals[impulseIndex][0];
		J[1] = tangentialNormals[impulseIndex][1];
		J[2] = tangentialNormals[impulseIndex][2];
	
		Math3D.scale(J, -mewImpulse);

		if(Math3D.magnitude(relV) > 3)
		{
			if(!collisionParameters.objectA.infiniteMass) //Objects with infinite mass are not affected by impulses.
			{
				collisionParameters.objectA.linearVelocity[0] += J[0]/collisionParameters.objectA.mass;
				collisionParameters.objectA.linearVelocity[1] += J[1]/collisionParameters.objectA.mass;
				collisionParameters.objectA.linearVelocity[2] += J[2]/collisionParameters.objectA.mass;
	
				Math3D.sub3x1minus3x1(collisionParameters.collisionPoints[impulseIndex], collisionParameters.objectA.displacement, rA);
				Math3D.cross(rA, J, tempA);
				Math3D.mult3x3times3x1(collisionParameters.objectA.InertiaTensorInverse, tempA, tempB);
	
				if(!collisionParameters.objectA.infiniteInertia)
				{
					collisionParameters.objectA.omega[0] += tempB[0];
					collisionParameters.objectA.omega[1] += tempB[1];
					collisionParameters.objectA.omega[2] += tempB[2];
				}
			}
	
			Math3D.scale(J, -1);
	
			if(!collisionParameters.objectB.infiniteMass) //Objects with infinite mass are not affected by impulses.
			{
				collisionParameters.objectB.linearVelocity[0] += J[0]/collisionParameters.objectB.mass;
				collisionParameters.objectB.linearVelocity[1] += J[1]/collisionParameters.objectB.mass;
				collisionParameters.objectB.linearVelocity[2] += J[2]/collisionParameters.objectB.mass;
	
				Math3D.sub3x1minus3x1(collisionParameters.collisionPoints[impulseIndex], collisionParameters.objectB.displacement, rB);
				Math3D.cross(rB, J, tempA);
				Math3D.mult3x3times3x1(collisionParameters.objectB.InertiaTensorInverse, tempA, tempB);
	
				if(!collisionParameters.objectB.infiniteInertia)
				{
					collisionParameters.objectB.omega[0] += tempB[0];
					collisionParameters.objectB.omega[1] += tempB[1];
					collisionParameters.objectB.omega[2] += tempB[2];
				}
			}
		}
	}


	double[] torque = new double[3];
	void ApplyContactFriction(double timeStep, TangibleObject object)
	{
		if(timeStep > .001) return; //Without this, for some reason, the simulation explodes/w NaN's...
		
		Math3D.scale(accel, 0);
		
		for(int i = 0; i < collisionParameters.collisionPoints.length; i ++)
		{
			temp[0] = -restingContactTangents[i][0];
			temp[1] = -restingContactTangents[i][1];
			temp[2] = -restingContactTangents[i][2];
	
			Math3D.scale(temp, mewContact*forces[i]/object.mass*phi(i, object));
			Math3D.add3x1plus3x1(accel, temp, accel);
		}

		Math3D.scale(torque, 0);
		for(int i = 0; i < collisionParameters.collisionPoints.length; i ++)
		{
			temp[0] = -restingContactTangents[i][0];
			temp[1] = -restingContactTangents[i][1];
			temp[2] = -restingContactTangents[i][2];
	
			Math3D.scale(temp, mewContact*forces[i]*phi(i, object));
			Math3D.sub3x1minus3x1(collisionParameters.collisionPoints[i], object.displacement, rA);
			Math3D.cross(rA, temp, rB);
			Math3D.add3x1plus3x1(torque, rB, torque);
		}
		Math3D.mult3x3times3x3(object.rotationQuaternion.rotoationMatrix, object.InertiaTensorInverse, rAt);
		Math3D.mult3x3times3x3(rAt, object.rotationMatrixTranspose, rBt);
		Math3D.mult3x3times3x1(rBt, torque, alpha);
	
		Math3D.scale(accel, timeStep);
		Math3D.add3x1plus3x1(object.linearVelocity, accel, object.linearVelocity);
	
		Math3D.scale(alpha, timeStep);
		if(!object.infiniteInertia)
			Math3D.add3x1plus3x1(object.omega, alpha, object.omega);
	}

	double[] tangentialNormal = new double[3];
	void loadTangentialNormals()
	{
		tangentialNormals = new double[0][3];
		 
		for(int impulseIndex = 0; impulseIndex < collisionParameters.collisionPoints.length; impulseIndex++)
		{
			Math3D.sub3x1minus3x1(collisionParameters.collisionPoints[impulseIndex], collisionParameters.objectA.displacement, rA);
			Math3D.sub3x1minus3x1(collisionParameters.collisionPoints[impulseIndex], collisionParameters.objectB.displacement, rB);

			Math3D.cross(collisionParameters.objectA.omega, rA, temp3x1);
			Math3D.add3x1plus3x1(temp3x1, collisionParameters.objectA.linearVelocity, vpa);

			Math3D.cross(collisionParameters.objectB.omega, rB, temp3x1);
			Math3D.add3x1plus3x1(temp3x1, collisionParameters.objectB.linearVelocity, vpb);

			Math3D.sub3x1minus3x1(vpa, vpb, relV);

			if(Math3D.magnitude(relV) < .01)
			{
				tangentialNormals = Math3D.push_back(collisionParameters.collisionNormals[impulseIndex], tangentialNormals);
			}
			else
			{
				Math3D.cross(collisionParameters.collisionNormals[impulseIndex], relV, temp3x1);
				Math3D.cross(collisionParameters.collisionNormals[impulseIndex], temp3x1, tangentialNormal);
				Math3D.scale(tangentialNormal, -1);
				Math3D.normalize(tangentialNormal);
	
				double[] newTangent = new double[3];
				newTangent[0] = tangentialNormal[0];
				newTangent[1] = tangentialNormal[1];
				newTangent[2] = tangentialNormal[2];
	
				tangentialNormals = Math3D.push_back(newTangent, tangentialNormals);
			}
		}
	}
}
