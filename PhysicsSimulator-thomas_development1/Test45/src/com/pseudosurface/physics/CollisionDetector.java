package com.pseudosurface.physics;


import java.util.ArrayList;




public class CollisionDetector 
{
	TangibleObject objectA;
	TangibleObject objectB;
	
	ConvexSurface surfaceA = new ConvexSurface();
	ConvexSurface surfaceB = new ConvexSurface();
	
	double BSAFaceRThis, BSAFaceRThat;
	int BSAFaceIThis, BSAFaceIThat;

	public double[][] collisionPoints;
	public double[][] collisionNormals;
	public TangibleObject[][] collisionObjects;
	
	static double EPS;
	
	public double[] BSApoint = new double[3];
	public double[] BSAnormal = new double[3];
	public double BSAR;

	Face BSAFaceThis;
	Face BSAFaceThat;

	double faceFaceCollisionNormalThreshold = -.95;
	double faceFaceCollisionDistanceThreshold = .05;
	
	double planarDistanceThreshold = .05;
	double planarOrientationThreshold = .95;

	private ArrayList<double[][]> planarGroups;
	private double[][] planeNormals;
	private TangibleObject[][] planeObjects;
	
	public double[][] centers;
	public double[][] omegas;
	
	public CollisionDetector()
	{
		EPS = .00000;
	}

	public void clear()
	{
		collisionPoints = new double[0][];
		collisionNormals = new double[0][];
		collisionObjects = new TangibleObject[0][];
	}

	public void testCollision(TangibleObject a, TangibleObject b)
	{
		BSAR = -Double.MAX_VALUE;
		
		objectA = a;
		objectB = b;
		
		for(int i = 0; i < objectA.surfaces.length; i++)
			for(int j = 0; j < objectB.surfaces.length; j++)
				if(objectsAreInRange(objectA, objectA.surfaces[i], objectB, objectB.surfaces[j]))
				{
					//Sphere
					if(objectA.surfaces[i].SURFACE_TYPE == ConvexSurface.SPHERE &&
							objectB.surfaces[j].SURFACE_TYPE == ConvexSurface.SPHERE)
						testSphereSphereCollision(objectA.surfaces[i], objectB.surfaces[j]);
					
					else if(objectA.surfaces[i].SURFACE_TYPE == ConvexSurface.SPHERE &&
							objectB.surfaces[j].SURFACE_TYPE == ConvexSurface.CUBE || 
							objectA.surfaces[i].SURFACE_TYPE == ConvexSurface.CUBE &&
							objectB.surfaces[j].SURFACE_TYPE == ConvexSurface.SPHERE)
						testSphereCubeCollision(objectA.surfaces[i], objectB.surfaces[j]);
					
					else if(objectA.surfaces[i].SURFACE_TYPE == ConvexSurface.SPHERE &&
							objectB.surfaces[j].SURFACE_TYPE == ConvexSurface.CYLINDER || 
							objectA.surfaces[i].SURFACE_TYPE == ConvexSurface.CYLINDER &&
							objectB.surfaces[j].SURFACE_TYPE == ConvexSurface.SPHERE)
						testSphereCylinderCollision(objectA.surfaces[i], objectB.surfaces[j]);
					
					else if(objectA.surfaces[i].SURFACE_TYPE == ConvexSurface.SPHERE &&
							objectB.surfaces[j].SURFACE_TYPE == ConvexSurface.CONE || 
							objectA.surfaces[i].SURFACE_TYPE == ConvexSurface.CONE &&
							objectB.surfaces[j].SURFACE_TYPE == ConvexSurface.SPHERE)
						testSphereConeCollision(objectA.surfaces[i], objectB.surfaces[j]);

					else if(objectA.surfaces[i].SURFACE_TYPE == ConvexSurface.SPHERE &&
							objectB.surfaces[j].SURFACE_TYPE == ConvexSurface.FACE || 
							objectA.surfaces[i].SURFACE_TYPE == ConvexSurface.FACE &&
							objectB.surfaces[j].SURFACE_TYPE == ConvexSurface.SPHERE)
						testSphereFaceCollision(objectA.surfaces[i], objectB.surfaces[j]);
					

					//Cube
					else if(objectA.surfaces[i].SURFACE_TYPE == ConvexSurface.CUBE &&
							objectB.surfaces[j].SURFACE_TYPE == ConvexSurface.CUBE)
						testAbstractCollision(objectA.surfaces[i], objectB.surfaces[j]);
					
					else if(objectA.surfaces[i].SURFACE_TYPE == ConvexSurface.CUBE &&
							objectB.surfaces[j].SURFACE_TYPE == ConvexSurface.CYLINDER || 
							objectA.surfaces[i].SURFACE_TYPE == ConvexSurface.CYLINDER &&
							objectB.surfaces[j].SURFACE_TYPE == ConvexSurface.CUBE)
						testCubeCylinderCollision(objectA.surfaces[i], objectB.surfaces[j]);
					
					else if(objectA.surfaces[i].SURFACE_TYPE == ConvexSurface.CUBE &&
							objectB.surfaces[j].SURFACE_TYPE == ConvexSurface.CONE || 
							objectA.surfaces[i].SURFACE_TYPE == ConvexSurface.CONE &&
							objectB.surfaces[j].SURFACE_TYPE == ConvexSurface.CUBE)
						testCubeConeCollision(objectA.surfaces[i], objectB.surfaces[j]);

					else if(objectA.surfaces[i].SURFACE_TYPE == ConvexSurface.CUBE &&
							objectB.surfaces[j].SURFACE_TYPE == ConvexSurface.FACE || 
							objectA.surfaces[i].SURFACE_TYPE == ConvexSurface.FACE &&
							objectB.surfaces[j].SURFACE_TYPE == ConvexSurface.CUBE)
						testCubeFaceCollision(objectA.surfaces[i], objectB.surfaces[j]);
					
					
					//Cylinder
					else if(objectA.surfaces[i].SURFACE_TYPE == ConvexSurface.CYLINDER &&
							objectB.surfaces[j].SURFACE_TYPE == ConvexSurface.CYLINDER)
						testCylinderCylinderCollision(objectA.surfaces[i], objectB.surfaces[j]);
					
					else if(objectA.surfaces[i].SURFACE_TYPE == ConvexSurface.CYLINDER &&
							objectB.surfaces[j].SURFACE_TYPE == ConvexSurface.CONE || 
							objectA.surfaces[i].SURFACE_TYPE == ConvexSurface.CONE &&
							objectB.surfaces[j].SURFACE_TYPE == ConvexSurface.CYLINDER)
						testCylinderConeCollision(objectA.surfaces[i], objectB.surfaces[j]);

					else if(objectA.surfaces[i].SURFACE_TYPE == ConvexSurface.CYLINDER &&
							objectB.surfaces[j].SURFACE_TYPE == ConvexSurface.FACE || 
							objectA.surfaces[i].SURFACE_TYPE == ConvexSurface.FACE &&
							objectB.surfaces[j].SURFACE_TYPE == ConvexSurface.CYLINDER)
						testCylinderFaceCollision(objectA.surfaces[i], objectB.surfaces[j]);
					
					//Cone
					else if(objectA.surfaces[i].SURFACE_TYPE == ConvexSurface.CONE &&
							objectB.surfaces[j].SURFACE_TYPE == ConvexSurface.CONE)
						testConeConeCollision(objectA.surfaces[i], objectB.surfaces[j]);
					
					else if(objectA.surfaces[i].SURFACE_TYPE == ConvexSurface.CONE &&
							objectB.surfaces[j].SURFACE_TYPE == ConvexSurface.FACE)
						testConeFaceCollision(objectA.surfaces[i], objectB.surfaces[j]);
					
					else
						testAbstractCollision(objectA.surfaces[i], objectB.surfaces[j]);
				}
	}





	double[] originalA = new double[3];
	double[] transformedA = new double[3];
	double[] translationA = new double[3];

	double[] originalB = new double[3];
	double[] transformedB = new double[3];
	double[] translationB = new double[3];

	double[] collisionNormal = new double[3];
	double[] collisionPoint = new double[3];
	
	private void testSphereSphereCollision(ConvexSurface surfaceA, ConvexSurface surfaceB) 
	{

			originalA[0] = surfaceA.relativePosition[0];
			originalA[1] = surfaceA.relativePosition[1];
			originalA[2] = surfaceA.relativePosition[2];

			objectA.rotationQuaternion.transform(originalA, transformedA);

			translationA[0] = transformedA[0] + objectA.displacement[0];
			translationA[1] = transformedA[1] + objectA.displacement[1];
			translationA[2] = transformedA[2] + objectA.displacement[2];

			originalB[0] = surfaceB.relativePosition[0];
			originalB[1] = surfaceB.relativePosition[1];
			originalB[2] = surfaceB.relativePosition[2];

			objectB.rotationQuaternion.transform(originalB, transformedB);

			translationB[0] = transformedB[0] + objectB.displacement[0];
			translationB[1] = transformedB[1] + objectB.displacement[1];
			translationB[2] = transformedB[2] + objectB.displacement[2];
			
		double distance = Math3D.distance(translationA, translationB);
		
		if(distance < surfaceA.radius+surfaceB.radius)
		{
			Math3D.sub3x1minus3x1(translationA, translationB, collisionPoint);
			collisionNormal = collisionPoint.clone();
			Math3D.normalize(collisionNormal);
			Math3D.normalize(collisionPoint);
			distance = surfaceB.radius-Math.abs(surfaceA.radius-surfaceB.radius)/2.0;
			Math3D.scale(collisionPoint, distance);
			Math3D.add3x1plus3x1(collisionPoint, translationB, collisionPoint);
			appendCollisionPoint(collisionPoint, collisionNormal, new TangibleObject[] {objectA, objectB});
		}
	}

	double[] originalPosition = new double[3];
	double[] rotatedPosition = new double[3];
	double[] translatedPosition = new double[3];

	double[] min_point = new double[3];
	double[] normal = new double[3];

	double[] vector = new double[3];
	double[] x0 = new double[3];
	double[] point = new double[3];
	double[] proj = new double[3];
	
	private void testSphereCubeCollision(ConvexSurface surfaceA, ConvexSurface surfaceB) 
	{
		//testAbstractCollision(surfaceA, surfaceB);
		ConvexSurface cube, sphere;


		
		if(surfaceA.SURFACE_TYPE == ConvexSurface.SPHERE)
		{
			sphere = surfaceA;
			cube = surfaceB;

			originalPosition[0] = surfaceA.relativePosition[0];
			originalPosition[1] = surfaceA.relativePosition[1];
			originalPosition[2] = surfaceA.relativePosition[2];

			objectA.rotationQuaternion.transform(originalPosition, rotatedPosition);

			translatedPosition[0] = rotatedPosition[0] + objectA.displacement[0];
			translatedPosition[1] = rotatedPosition[1] + objectA.displacement[1];
			translatedPosition[2] = rotatedPosition[2] + objectA.displacement[2];
			
		}
		else
		{
			sphere = surfaceB;
			cube = surfaceA;

			originalPosition[0] = surfaceB.relativePosition[0];
			originalPosition[1] = surfaceB.relativePosition[1];
			originalPosition[2] = surfaceB.relativePosition[2];

			objectB.rotationQuaternion.transform(originalPosition, rotatedPosition);

			translatedPosition[0] = rotatedPosition[0] + objectB.displacement[0];
			translatedPosition[1] = rotatedPosition[1] + objectB.displacement[1];
			translatedPosition[2] = rotatedPosition[2] + objectB.displacement[2];
		}

		double min_dist = Double.MAX_VALUE;
		
		Face[] faces = cube.faces;
		
		int bestFaceIndex = 0;
		double largestBSA = -Double.MAX_VALUE;
		
		for(int i = 0; i < faces.length; i++)
		{
			double dist = faces[i].getPlaneVertexDistance(translatedPosition);
			if(largestBSA < dist)
			{
				largestBSA = dist;
				bestFaceIndex = i;
			}
		}
			
		
		int i = bestFaceIndex;
		{
			Face face = faces[i];
			face.projectPointOnto(translatedPosition, proj);
			int[] bsaEdge = new int[1];
			
			if(face.pointIsInsideTransformed(proj, bsaEdge))
			{
				double distance = Math3D.distance(proj, translatedPosition);
				if(distance < min_dist)
				{
					min_dist = distance;
					min_point = proj.clone();
					normal = face.normalTransformed.clone();
				}
			}
			else
			{
				double[][] edge = face.getEdge(bsaEdge[0]);
				Math3D.sub3x1minus3x1(edge[1], edge[0], edgeVector);
				double edgeLength = Math3D.normalize(edgeVector);
				Math3D.sub3x1minus3x1(translatedPosition, edge[0], edgeToSphereVector);
				double distanceAlongEdge = Math3D.dot(edgeToSphereVector, edgeVector);
				
				if(distanceAlongEdge < 0)
					distanceAlongEdge = 0;
				else if(distanceAlongEdge > edgeLength)
					distanceAlongEdge = edgeLength;

				Math3D.scale(edgeVector, distanceAlongEdge);
				Math3D.add3x1plus3x1(edgeVector, edge[0], collisionPoint);

				Math3D.sub3x1minus3x1(translatedPosition, collisionPoint, collisionNormal);
				double distance = Math3D.normalize(collisionNormal);
				
				if(distance < min_dist)
				{
					min_dist = distance;
					min_point = collisionPoint.clone();
					normal = collisionNormal.clone();
				}
			}
		}
//		System.err.println("Test: "+min_dist);
		if(min_dist < sphere.radius)
		{
//			System.err.println("Collision detected.");

			Math3D.sub3x1minus3x1(min_point, translatedPosition, normalTester);
			
			if(Math3D.dot(normal, normalTester) < 0)
				Math3D.scale(normal, -1);

			if(surfaceA.SURFACE_TYPE == ConvexSurface.SPHERE)
			{
				Math3D.scale(normal, -1.0);
				appendCollisionPoint(min_point, normal, new TangibleObject[]{objectA, objectB});

				double[] positionAdjustment = normal.clone();
				Math3D.scale(positionAdjustment, sphere.radius-min_dist);
				Math3D.add3x1plus3x1(objectA.displacement, positionAdjustment, objectA.displacement);
			}
			else
			{
				appendCollisionPoint(min_point, normal, new TangibleObject[]{objectA, objectB});
				
				double[] positionAdjustment = normal.clone();
				Math3D.scale(positionAdjustment, min_dist-sphere.radius);
				Math3D.add3x1plus3x1(objectB.displacement, positionAdjustment, objectB.displacement);
			}
		}
		if(min_dist < sphere.radius)
		{
			if(surfaceA.SURFACE_TYPE == ConvexSurface.SPHERE)
			{				
				Math3D.scale(normal, -1.0);
				appendCollisionPoint(min_point, normal, new TangibleObject[]{objectA, objectB});
			}
			else
			{
				appendCollisionPoint(min_point, normal, new TangibleObject[]{objectA, objectB});
			}
		}
	}
	private void testSphereCylinderCollision(ConvexSurface surfaceA, ConvexSurface surfaceB) 
	{
		testAbstractCollision(surfaceA, surfaceB);
	}
	private void testSphereConeCollision(ConvexSurface surfaceA, ConvexSurface surfaceB) 
	{
		testAbstractCollision(surfaceA, surfaceB);		
	}

	double[] edgeVector = new double[3];
	double[] edgeToSphereVector = new double[3];
	double[] normalTester = new double[3];
	
	private void testSphereFaceCollision(ConvexSurface surfaceA, ConvexSurface surfaceB)
	{
		//testAbstractCollision(surfaceA, surfaceB);
		ConvexSurface polygon, sphere;
		
		if(surfaceA.SURFACE_TYPE == ConvexSurface.SPHERE)
		{
			sphere = surfaceA;
			polygon = surfaceB;

			originalPosition[0] = surfaceA.relativePosition[0];
			originalPosition[1] = surfaceA.relativePosition[1];
			originalPosition[2] = surfaceA.relativePosition[2];

			objectA.rotationQuaternion.transform(originalPosition, rotatedPosition);

			translatedPosition[0] = rotatedPosition[0] + objectA.displacement[0];
			translatedPosition[1] = rotatedPosition[1] + objectA.displacement[1];
			translatedPosition[2] = rotatedPosition[2] + objectA.displacement[2];
			
		}
		else
		{
			sphere = surfaceB;
			polygon = surfaceA;

			originalPosition[0] = sphere.relativePosition[0];
			originalPosition[1] = sphere.relativePosition[1];
			originalPosition[2] = sphere.relativePosition[2];

			objectB.rotationQuaternion.transform(originalPosition, rotatedPosition);

			translatedPosition[0] = rotatedPosition[0] + objectB.displacement[0];
			translatedPosition[1] = rotatedPosition[1] + objectB.displacement[1];
			translatedPosition[2] = rotatedPosition[2] + objectB.displacement[2];
		}
			
		double min_dist = Double.MAX_VALUE;
		
		Face[] faces = polygon.equation.getFaces(translatedPosition, sphere.radius);
		
		for(int i = 0; i < faces.length; i++)
		{
			Face face = faces[i];
			face.projectPointOnto(translatedPosition, proj);
			int[] bsaEdge = new int[1];
			
			if(face.pointIsInsideTransformed(proj, bsaEdge))
			{
				double distance = Math3D.distance(proj, translatedPosition);
				if(distance < min_dist)
				{
					min_dist = distance;
					min_point = proj.clone();
					normal = face.normalTransformed.clone();
				}
			}
			else
			{
				double[][] edge = face.getEdge(bsaEdge[0]);
				Math3D.sub3x1minus3x1(edge[1], edge[0], edgeVector);
				double edgeLength = Math3D.normalize(edgeVector);
				Math3D.sub3x1minus3x1(translatedPosition, edge[0], edgeToSphereVector);
				double distanceAlongEdge = Math3D.dot(edgeToSphereVector, edgeVector);
				
				if(distanceAlongEdge < 0)
					distanceAlongEdge = 0;
				else if(distanceAlongEdge > edgeLength)
					distanceAlongEdge = edgeLength;

				Math3D.scale(edgeVector, distanceAlongEdge);
				Math3D.add3x1plus3x1(edgeVector, edge[0], collisionPoint);

				Math3D.sub3x1minus3x1(translatedPosition, collisionPoint, collisionNormal);
				double distance = Math3D.normalize(collisionNormal);
				
				if(distance < min_dist)
				{
					min_dist = distance;
					min_point = collisionPoint.clone();
					normal = collisionNormal.clone();
				}
			}
		}
//		System.err.println("Test: "+min_dist);
		if(min_dist < sphere.radius)
		{
//			System.err.println("Collision detected.");

			Math3D.sub3x1minus3x1(min_point, translatedPosition, normalTester);
			
			if(Math3D.dot(normal, normalTester) < 0)
				Math3D.scale(normal, -1);

			if(surfaceA.SURFACE_TYPE == ConvexSurface.SPHERE)
			{
				Math3D.scale(normal, -1.0);
				appendCollisionPoint(min_point, normal, new TangibleObject[]{objectA, objectB});

				double[] positionAdjustment = normal.clone();
				Math3D.scale(positionAdjustment, sphere.radius-min_dist);
				Math3D.add3x1plus3x1(objectA.displacement, positionAdjustment, objectA.displacement);
			}
			else
			{
				appendCollisionPoint(min_point, normal, new TangibleObject[]{objectA, objectB});
				
				double[] positionAdjustment = normal.clone();
				Math3D.scale(positionAdjustment, min_dist-sphere.radius);
				Math3D.add3x1plus3x1(objectB.displacement, positionAdjustment, objectB.displacement);
			}
		}
	}
	private void testCubeCylinderCollision(ConvexSurface surfaceA, ConvexSurface surfaceB) 
	{
		testAbstractCollision(surfaceA, surfaceB);
	}
	private void testCubeConeCollision(ConvexSurface surfaceA, ConvexSurface surfaceB) 
	{
		testAbstractCollision(surfaceA, surfaceB);
	}
	private void testCubeFaceCollision(ConvexSurface surfaceA, ConvexSurface surfaceB) 
	{
		System.err.println("Unsupported Collision");
	}
	private void testCylinderCylinderCollision(ConvexSurface surfaceA, ConvexSurface surfaceB) 
	{
		testAbstractCollision(surfaceA, surfaceB);
	}
	private void testCylinderConeCollision(ConvexSurface surfaceA, ConvexSurface surfaceB) 
	{
		testAbstractCollision(surfaceA, surfaceB);
	}
	private void testCylinderFaceCollision(ConvexSurface surfaceA, ConvexSurface surfaceB) 
	{
		System.err.println("Unsupported Collision");		
	}
	private void testConeConeCollision(ConvexSurface surfaceA, ConvexSurface surfaceB) 
	{
		testAbstractCollision(surfaceA, surfaceB);
	}
	private void testConeFaceCollision(ConvexSurface surfaceA, ConvexSurface surfaceB) 
	{
		System.err.println("Unsupported Collision");		
	}
	void testAbstractCollision(ConvexSurface a, ConvexSurface b)
	{
		surfaceA = a;
		surfaceB = b;

		DuplicitFaceWiseBSALoader(a, b);
//		DuplicitFaceFaceCollisionTest(a, b);
		DuplicitVertexCollisionTest(a, b);
		DuplicitEdgeCollisionTest(a, b);
	}

	void DuplicitFaceFaceCollisionTest(ConvexSurface thisSurface, ConvexSurface thatSurface)
	{
		double[] normalThis = thisSurface.faces[BSAFaceIThis].normalTransformed.clone();
		double[] normalThat = thatSurface.faces[BSAFaceIThat].normalTransformed.clone();

		double maxRThis = -Double.MAX_VALUE;
		double minRThis = Double.MAX_VALUE;
		for ( int i = 0; i < thisSurface.faces[BSAFaceIThis].faceVerticesTransformed.length; i++)
		{
			double[] tempPoint = new double[3];
			Math3D.sub3x1minus3x1(thisSurface.faces[BSAFaceIThis].faceVerticesTransformed[i], thatSurface.faces[BSAFaceIThat].faceCenterTransformed, tempPoint);
			double tempR = Math3D.dot(tempPoint, normalThat);
			if(tempR > maxRThis)
				maxRThis = tempR;
			if(tempR < minRThis)
				minRThis = tempR;
		}
		
		double maxRThat = -Double.MAX_VALUE;
		double minRThat = Double.MAX_VALUE;
		for ( int i = 0; i < thatSurface.faces[BSAFaceIThat].faceVerticesTransformed.length; i++)
		{
			double[] tempPoint = new double[3];
			Math3D.sub3x1minus3x1(thatSurface.faces[BSAFaceIThat].faceVerticesTransformed[i], thisSurface.faces[BSAFaceIThis].faceCenterTransformed, tempPoint);
			double tempR = Math3D.dot(tempPoint, normalThis);
			if(tempR > maxRThat)
				maxRThat = tempR;
			if(tempR < minRThat)
				minRThat = tempR;
		}
			
		
		
		
		if(Math3D.dot(normalThis, normalThat) <= faceFaceCollisionNormalThreshold & 
		  minRThis > -faceFaceCollisionDistanceThreshold & maxRThis < faceFaceCollisionDistanceThreshold & 
		  minRThat > -faceFaceCollisionDistanceThreshold & maxRThat < faceFaceCollisionDistanceThreshold)
		{
			
			TangibleObject[] objects = new TangibleObject[2];
			objects[0] = objectA;
			objects[1] = objectB;
			
			appendCollisionPoint(null, thatSurface.faces[BSAFaceIThat].normalTransformed, objects);
			flipCollisionNormal(collisionPoints.length-1);
			//Game.World.frozen =  true;
		}
	}

	static double[] fromA = new double[3];
	static double[] fromB = new double[3];
	
	public static boolean isBetween(double[] point, double[] pointA, double[] pointB)
	{
		Math3D.sub3x1minus3x1(point, pointA, fromA);
		Math3D.sub3x1minus3x1(point, pointB, fromB);
		
		if( Math3D.dot(fromA, fromB) <= 0 ) return true;
		else return false;
	}

	double[] difference = new double[3];
	boolean objectsAreInRange(TangibleObject objectA, ConvexSurface surfaceA,
											  TangibleObject objectB, ConvexSurface surfaceB)
	{
			originalA[0] = surfaceA.relativePosition[0];
			originalA[1] = surfaceA.relativePosition[1];
			originalA[2] = surfaceA.relativePosition[2];

			objectA.rotationQuaternion.transform(originalA, transformedA);

			translationA[0] = transformedA[0] + objectA.displacement[0];
			translationA[1] = transformedA[1] + objectA.displacement[1];
			translationA[2] = transformedA[2] + objectA.displacement[2];

			originalB[0] = surfaceB.relativePosition[0];
			originalB[1] = surfaceB.relativePosition[1];
			originalB[2] = surfaceB.relativePosition[2];

			objectB.rotationQuaternion.transform(originalB, transformedB);

			translationB[0] = transformedB[0] + objectB.displacement[0];
			translationB[1] = transformedB[1] + objectB.displacement[1];
			translationB[2] = transformedB[2] + objectB.displacement[2];

		difference[0] = translationB[0] - translationA[0];
		difference[1] = translationB[1] - translationA[1];
		difference[2] = translationB[2] - translationA[2];
		
		return (Math.sqrt(
					difference[0]*difference[0]+
					difference[1]*difference[1]+
					difference[2]*difference[2])
				<
				surfaceA.maxRadius+
				surfaceB.maxRadius);
	}

	void DuplicitFaceWiseBSALoader(ConvexSurface thisSurface, ConvexSurface thatSurface) 
	{
		double minR;
		double tempR;

		//Find this surface's BSA Face 
		BSAFaceRThis = -Double.MAX_VALUE;
		for(int i = 0; i < thisSurface.faces.length; i++)
		{
			minR = Double.MAX_VALUE;
			for(int j = 0; j < thatSurface.pointIndexing.length; j ++)
			{
				tempR = thisSurface.faces[i].getPlaneVertexDistance(thatSurface.faces[thatSurface.pointIndexing[j][0]].faceVerticesTransformed[thatSurface.pointIndexing[j][1]]);
				if(tempR < minR)
					minR = tempR;
			}
			if(minR > BSAFaceRThis)
			{
				BSAFaceRThis = minR;
				BSAFaceIThis = i;

				BSAFaceThis = thisSurface.faces[i];
				
				if(BSAFaceRThis > BSAR)
				{
					BSApoint[0] = thisSurface.faces[BSAFaceIThis].faceCenterTransformed[0];
					BSApoint[1] = thisSurface.faces[BSAFaceIThis].faceCenterTransformed[1];
					BSApoint[2] = thisSurface.faces[BSAFaceIThis].faceCenterTransformed[2];

					BSAnormal[0] = thisSurface.faces[BSAFaceIThis].normalTransformed[0];
					BSAnormal[1] = thisSurface.faces[BSAFaceIThis].normalTransformed[1];
					BSAnormal[2] = thisSurface.faces[BSAFaceIThis].normalTransformed[2];

					BSAR = BSAFaceRThis;
				}
			}
		}

		//Find that surface's BSA Face 
		BSAFaceRThat = -Double.MAX_VALUE;
		for(int i = 0; i < thatSurface.faces.length; i++)
		{
			minR = Double.MAX_VALUE;
			for(int j = 0; j < thisSurface.pointIndexing.length; j ++)
			{
				tempR = thatSurface.faces[i].getPlaneVertexDistance(thisSurface.faces[thisSurface.pointIndexing[j][0]].faceVerticesTransformed[thisSurface.pointIndexing[j][1]]);
				if(tempR < minR)
					minR = tempR;
			}
			if(minR > BSAFaceRThat)
			{
				BSAFaceRThat = minR;
				BSAFaceIThat = i;
				
				BSAFaceThat = thatSurface.faces[i];
				
				if(BSAFaceRThat > BSAR)
				{
					BSApoint[0] = thatSurface.faces[BSAFaceIThat].faceCenterTransformed[0];
					BSApoint[1] = thatSurface.faces[BSAFaceIThat].faceCenterTransformed[1];
					BSApoint[2] = thatSurface.faces[BSAFaceIThat].faceCenterTransformed[2];
		
					BSAnormal[0] = thatSurface.faces[BSAFaceIThat].normalTransformed[0];
					BSAnormal[1] = thatSurface.faces[BSAFaceIThat].normalTransformed[1];
					BSAnormal[2] = thatSurface.faces[BSAFaceIThat].normalTransformed[2];
					
					BSAR = BSAFaceRThat;
				}
			}
		}
	}

	void DuplicitVertexCollisionTest(ConvexSurface thisSurface, ConvexSurface thatSurface)
	{
		double[] tempNormal = new double[3];

		if(BSAFaceRThis > BSAFaceRThat)
		{
			tempNormal[0] = -thisSurface.faces[BSAFaceIThis].normalTransformed[0];
			tempNormal[1] = -thisSurface.faces[BSAFaceIThis].normalTransformed[1];
			tempNormal[2] = -thisSurface.faces[BSAFaceIThis].normalTransformed[2];
		}
		else
		{
			tempNormal[0] = thatSurface.faces[BSAFaceIThat].normalTransformed[0];
			tempNormal[1] = thatSurface.faces[BSAFaceIThat].normalTransformed[1];
			tempNormal[2] = thatSurface.faces[BSAFaceIThat].normalTransformed[2];
		}
		
			
		for(int i = 0; i < thisSurface.pointIndexing.length; i++)
			if(thatSurface.pointIsInsideTransformed(thisSurface.faces[thisSurface.pointIndexing[i][0]].faceVerticesTransformed[thisSurface.pointIndexing[i][1]]))
			{
				point[0] = thisSurface.faces[thisSurface.pointIndexing[i][0]].faceVerticesTransformed[thisSurface.pointIndexing[i][1]][0];
				point[1] = thisSurface.faces[thisSurface.pointIndexing[i][0]].faceVerticesTransformed[thisSurface.pointIndexing[i][1]][1];
				point[2] = thisSurface.faces[thisSurface.pointIndexing[i][0]].faceVerticesTransformed[thisSurface.pointIndexing[i][1]][2];

				normal[0] = tempNormal[0];
				normal[1] = tempNormal[1];
				normal[2] = tempNormal[2];

				TangibleObject[] objects = new TangibleObject[2];
				objects[0] = objectA;
				objects[1] = objectB;

				appendCollisionPoint(point, normal, objects);
			}
		
		for(int i = 0; i < thatSurface.pointIndexing.length; i++)
			if(thisSurface.pointIsInsideTransformed(thatSurface.faces[thatSurface.pointIndexing[i][0]].faceVerticesTransformed[thatSurface.pointIndexing[i][1]]))
			{
				point[0] = thatSurface.faces[thatSurface.pointIndexing[i][0]].faceVerticesTransformed[thatSurface.pointIndexing[i][1]][0];
				point[1] = thatSurface.faces[thatSurface.pointIndexing[i][0]].faceVerticesTransformed[thatSurface.pointIndexing[i][1]][1];
				point[2] = thatSurface.faces[thatSurface.pointIndexing[i][0]].faceVerticesTransformed[thatSurface.pointIndexing[i][1]][2];

				normal[0] = tempNormal[0];
				normal[1] = tempNormal[1];
				normal[2] = tempNormal[2];

				TangibleObject[] objects = new TangibleObject[2];
				objects[0] = objectA;
				objects[1] = objectB;
				
				appendCollisionPoint(point, normal, objects);
			}	
	}

	void DuplicitEdgeCollisionTest(ConvexSurface thisSurface, ConvexSurface thatSurface)
	{
		Face BSAFace = null;
		Face BSAFaceTemp = null;
		
		double[][] tempPoints = new double[0][];
		double[][] tempNormals = new double[0][];
		
		if(BSAFaceRThis > BSAFaceRThat)
		{
			BSAFace = thisSurface.faces[BSAFaceIThis];
			BSAFaceTemp = thatSurface.faces[BSAFaceIThat];
		}
		else
		{
			BSAFace = thatSurface.faces[BSAFaceIThat];
			BSAFaceTemp = thisSurface.faces[BSAFaceIThis];
		}

		double[][] edgeA, edgeB;
		double[] solutionA = new double[3], 
				 solutionB = new double[3], 
				 average = new double[3],
				 normal = new double[3];

		TangibleObject[] objects = new TangibleObject[2];
		objects[0] = objectA;
		objects[1] = objectB;
		
		for(int i = 0; i < BSAFace.faceVertices.length; i++)
		{
			edgeA = BSAFace.getEdge(i);
			
			for(int j = 0; j < BSAFaceTemp.faceVertices.length; j++)
			{
				edgeB = BSAFaceTemp.getEdge(j);
				
				if(lineLineIntersect(edgeA[0], edgeA[1], edgeB[0], edgeB[1], solutionA, solutionB))
				{
					Math3D.add3x1plus3x1(solutionA, solutionB, average);
					Math3D.scale(average, .5);
					if(Math3D.vertexIsCloseEnough(solutionA, solutionB, .1))
					{
						Math3D.sub3x1minus3x1(edgeA[1], edgeA[0], solutionA);
						Math3D.sub3x1minus3x1(edgeB[1], edgeB[0], solutionB);
						Math3D.cross(solutionA, solutionB, normal);
						Math3D.normalize(normal);
						
						int[] tempBSAI = new int[1];
						
						if(isBetween(average, edgeA[0], edgeA[1]) && 
						   isBetween(average, edgeB[0], edgeB[1]) &&
						   (thatSurface.pointIsInsideTransformed(average)||thisSurface.pointIsInsideTransformed(average)))
						{
							tempPoints = Math3D.push_back(average, tempPoints);
							tempNormals = Math3D.push_back(normal, tempNormals);
						}
					}
				}
			}
		}

		for(int i = 0; i < tempPoints.length; i++)
		{
			appendCollisionPoint(tempPoints[i].clone(), tempNormals[i].clone(), objects);
			flipCollisionNormal(collisionObjects.length-1);
		}
	}
	
	public void consolodateCollisions()
	{	
		planarGroups = new ArrayList<double[][]>(0);
		planeNormals = new double[0][];
		planeObjects = new TangibleObject[0][];
		
		for(int i = 0; i < collisionPoints.length; i++)
		{
			boolean found = false;
			for(int j = 0; j < planarGroups.size(); j++)
			{
				double[][] currentPlanarGroup = planarGroups.get(j);
				double[] difference = new double[3];
				
				Math3D.sub3x1minus3x1(collisionPoints[i], currentPlanarGroup[0], difference);
				
				if(Math3D.dot(planeNormals[j], difference) <= planarDistanceThreshold &&
				   Math3D.dot(planeNormals[j], collisionNormals[i]) >= planarOrientationThreshold &&
				   ((planeObjects[j][0] == collisionObjects[i][0] && planeObjects[j][1] == collisionObjects[i][1]) ||
				    (planeObjects[j][0] == collisionObjects[i][1] && planeObjects[j][1] == collisionObjects[i][0])))
				{
					found = true;
					
					planarGroups.set(j, Math3D.push_back(collisionPoints[i], currentPlanarGroup));
				}
			}
			
			if(!found)
			{
				double[][] tempPlanarGroup = new double[0][];
				tempPlanarGroup = Math3D.push_back(collisionPoints[i].clone(), tempPlanarGroup);
				planarGroups.add(tempPlanarGroup);
				planeNormals = Math3D.push_back(collisionNormals[i], planeNormals);
				planeObjects = Math3D.push_back(collisionObjects[i], planeObjects);
			}
		}
		
		organizeCollisionsIntoPolygons();
	}
	
	void organizeCollisionsIntoPolygons()
	{	
		omegas = new double[planarGroups.size()][];
		centers = new double[planarGroups.size()][];
		
		for(int i = 0; i < planarGroups.size(); i++)
		{
			if(planarGroups.get(i).length >= 3)
			{
				centers[i] = new double[3];
				for(int j = 0; j < planarGroups.get(i).length; j++)
					Math3D.add3x1plus3x1(centers[i], planarGroups.get(i)[j], centers[i]);
				Math3D.scale(centers[i], 1.0/(double)planarGroups.get(i).length);
	
				double[] ydir = new double[3];
				Math3D.sub3x1minus3x1(planarGroups.get(i)[1], planarGroups.get(i)[0], ydir);
				Math3D.normalize(ydir);
				  
				double[] xdir = new double[3];
				Math3D.cross(ydir, planeNormals[i], xdir);
				
				double[] center2d = new double[] { Math3D.dot(centers[i], xdir), Math3D.dot(centers[i], ydir) };
				double[][] points2d = new double[planarGroups.get(i).length][2];
				for(int j = 0; j < planarGroups.get(i).length; j++)
				{
					points2d[j][0] = Math3D.dot(xdir, planarGroups.get(i)[j]);
					points2d[j][1] = Math3D.dot(ydir, planarGroups.get(i)[j]);
				}
				
				omegas[i] = new double[planarGroups.get(i).length];

				for(int j = 0; j < planarGroups.get(i).length; j++)
				{
					omegas[i][j] = Math.atan2(points2d[j][1] - center2d[1], points2d[j][0] - center2d[0]);
				}

				double[][] currentGroup = planarGroups.get(i);
				boolean found = true;
				while(found)
				{
					found = false;
					for(int j = 0; j < omegas[i].length-1; j++)
					{
						
						if(omegas[i][j] < omegas[i][j+1])
						{
							double swapOmegaA = omegas[i][j];
							double swapOmegaB = omegas[i][j+1];
							omegas[i][j] = swapOmegaB;
							omegas[i][j+1] = swapOmegaA;
							
							double[] swapPointA = currentGroup[j];
							double[] swapPointB = currentGroup[j+1];
							currentGroup[j] = swapPointB;
							currentGroup[j+1] = swapPointA;
							
							found = true;
						}
					}
				}
				planarGroups.set(i, currentGroup);
				loadCentroid(i);
			}
			else
			{
				for(int j = 0; j < planarGroups.get(i).length; j++)
				{
					appendCollisionPoint(planarGroups.get(i)[j], planeNormals[i], planeObjects[i]);
					flipCollisionNormal(collisionPoints.length-1);
				} 
			}
		}
	}

	
	void loadCentroid(int i)
	{
		double[] vertices = new double [planarGroups.get(i).length*3];
		for(int j = 0; j < planarGroups.get(i).length; j++)
		{
			vertices[j*3 + 0] = planarGroups.get(i)[j][0];
			vertices[j*3 + 1] = planarGroups.get(i)[j][1];
			vertices[j*3 + 2] = planarGroups.get(i)[j][2];
		}
		Face tempFace = new Face(vertices, planarGroups.get(i).length);
		appendCollisionPoint(tempFace.centroid, tempFace.normal, planeObjects[i]);
		flipCollisionNormal(collisionPoints.length-1);
	}

	static double[] p13 = new double[3];
	static double[] p43 = new double[3];
	static double[] p21 = new double[3];
	
	public static boolean lineLineIntersect(double[] p1, double[] p2,double[] p3,double[] p4, double[] pointA, double[] pointB)
	{
		double d1343,d4321,d1321,d4343,d2121;
		double numer,denom;

		//Vector of the line segment connecting the first points of each line segment:
		p13[0] = p1[0] - p3[0];
		p13[1] = p1[1] - p3[1];
		p13[2] = p1[2] - p3[2];
			   
		//Vector of the second line segment:
		p43[0] = p4[0] - p3[0];
		p43[1] = p4[1] - p3[1];
		p43[2] = p4[2] - p3[2];
			   
		if (Math.abs(p43[0]) < EPS && Math.abs(p43[1]) < EPS && Math.abs(p43[2]) < EPS)
			return(false);
			   
		//Vector of the first line segment:
		p21[0] = p2[0] - p1[0];
		p21[1] = p2[1] - p1[1];
		p21[2] = p2[2] - p1[2];
			   
		if (Math.abs(p21[0]) < EPS && Math.abs(p21[1]) < EPS && Math.abs(p21[2]) < EPS)
			return(false);
			   
		//Compute the dot products:
		d1343 = p13[0] * p43[0] + p13[1] * p43[1] + p13[2] * p43[2];
		d4321 = p43[0] * p21[0] + p43[1] * p21[1] + p43[2] * p21[2];
		d1321 = p13[0] * p21[0] + p13[1] * p21[1] + p13[2] * p21[2];
		d4343 = p43[0] * p43[0] + p43[1] * p43[1] + p43[2] * p43[2];
		d2121 = p21[0] * p21[0] + p21[1] * p21[1] + p21[2] * p21[2];

		//The denominator of mua
		denom = d2121 * d4343 - d4321 * d4321;
			   
		if (Math.abs(denom) < EPS)
			return(false);
			   
		//The numerator of mua
		numer = d1343 * d4321 - d1321 * d4343;

		double mua = numer / denom;
		double mub = (d1343 + d4321 * (mua)) / d4343;

			   
		pointA[0] = p1[0] + mua * p21[0];
		pointA[1] = p1[1] + mua * p21[1];
		pointA[2] = p1[2] + mua * p21[2];
			   
		pointB[0] = p3[0] + mub * p43[0];
		pointB[1] = p3[1] + mub * p43[1];
		pointB[2] = p3[2] + mub * p43[2];
		
		if(mua > -.5 && mub > -.5 && mua < 1.5 && mub < 1.5)
			return true;
		else
			return false;
	}

	public boolean appendCollisionPoint(double[]point, double[] normal, TangibleObject[] objects)
	{
		for(int i = 0; i < collisionPoints.length; i++)
			if(Math3D.vertexIsCloseEnough(collisionPoints[i], point, .001))
				return false;
		collisionObjects = Math3D.push_back(objects, collisionObjects);
		collisionNormals = Math3D.push_back(normal.clone(), collisionNormals);
		collisionPoints = Math3D.push_back(point.clone(), collisionPoints);
		
		return true;
	}

	//Makes the collision normal faces away from object B.
	private void flipCollisionNormal(int i) 
	{
			originalA[0] = surfaceA.relativePosition[0];
			originalA[1] = surfaceA.relativePosition[1];
			originalA[2] = surfaceA.relativePosition[2];

			objectA.rotationQuaternion.transform(originalA, transformedA);

			translationA[0] = transformedA[0] + objectA.displacement[0];
			translationA[1] = transformedA[1] + objectA.displacement[1];
			translationA[2] = transformedA[2] + objectA.displacement[2];
			
			originalB[0] = surfaceB.relativePosition[0];
			originalB[1] = surfaceB.relativePosition[1];
			originalB[2] = surfaceB.relativePosition[2];

			objectB.rotationQuaternion.transform(originalB, transformedB);

			translationB[0] = transformedB[0] + objectB.displacement[0];
			translationB[1] = transformedB[1] + objectB.displacement[1];
			translationB[2] = transformedB[2] + objectB.displacement[2];
		
		Math3D.sub3x1minus3x1(collisionPoints[i], translationA, translationA);
		Math3D.sub3x1minus3x1(collisionPoints[i], translationB, translationB);
		
		Math3D.normalize(translationA);
		Math3D.normalize(translationB);
		
		if(Math3D.dot(translationA, collisionNormals[i]) > Math3D.dot(translationB, collisionNormals[i]))
			Math3D.scale(collisionNormals[i], -1);
	}
}
