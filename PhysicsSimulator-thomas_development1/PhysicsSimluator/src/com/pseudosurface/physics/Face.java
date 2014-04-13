package com.pseudosurface.physics;


//Test comment.

public class Face 
{
	public double[][] faceVertices;
	public double[][] faceVerticesTransformed;
	
	public double[][] edgeNormals;
	public double[][] edgeNormalsTransformed;
	
	public double[][] edgeCenters;
	public double[][] edgeCentersTransformed;
	
	public double[] faceCenter = new double [3];
	public double[] faceCenterTransformed = new double [3];
	
	public double[] normal = new double[3];
	public double[] normalTransformed = new double[3];
	
	private double[] original = new double[3];
	private double[] transformed = new double[3];
	
	private double EPS;
	private double[] tempQ = new double[3];
	private double[] tempPoint = new double[3];
	private double[] rayVector = new double[3];
	private double rayLength;
	
	double[] centroid;
	
	public double radius;

	public Face(double[] verts, int numVerts)
	{	
		faceVertices = new double[numVerts][];
		faceVerticesTransformed = new double[numVerts][];
		
		edgeNormals = new double[numVerts][];
		edgeNormalsTransformed = new double[numVerts][];
		
		edgeCenters = new double[numVerts][];
		edgeCentersTransformed = new double[numVerts][];

		faceCenter[0] = 0;
		faceCenter[1] = 0;
		faceCenter[2] = 0;
		
		double[] va = new double[3];
		double[] vb = new double[3];
		double[] cross = new double[3];

		for(int i = 0; i < numVerts; i++)
		{
			faceVertices[i] = new double[3];
			faceVerticesTransformed[i] = new double[3];
			
			faceVertices[i][0] = verts[i*3+0];
			faceVertices[i][1] = verts[i*3+1];
			faceVertices[i][2] = verts[i*3+2];	

			faceVerticesTransformed[i][0] = verts[i*3+0];
			faceVerticesTransformed[i][1] = verts[i*3+1];
			faceVerticesTransformed[i][2] = verts[i*3+2];	
		}
		
		va[0] = faceVertices[1][0]-faceVertices[0][0];
		va[1] = faceVertices[1][1]-faceVertices[0][1];
		va[2] = faceVertices[1][2]-faceVertices[0][2];
		
		vb[0] = faceVertices[2][0]-faceVertices[1][0];
		vb[1] = faceVertices[2][1]-faceVertices[1][1];
		vb[2] = faceVertices[2][2]-faceVertices[1][2];

		Math3D.cross(va, vb, normal);

		Math3D.normalize(normal);

		for(int i = 0; i < numVerts; i++)
		{
			faceCenter[0] += verts[i*3+0];
			faceCenter[1] += verts[i*3+1];
			faceCenter[2] += verts[i*3+2];

			////////////////////////////////////////////////////////
			edgeNormals[i] = new double[3];
			edgeNormalsTransformed[i] = new double[3];
			
			va[0] = verts[(i+1)%numVerts*3+0]-verts[i*3+0];
			va[1] = verts[(i+1)%numVerts*3+1]-verts[i*3+1];
			va[2] = verts[(i+1)%numVerts*3+2]-verts[i*3+2];

			Math3D.cross(va, normal, cross);
			
			Math3D.normalize(cross);

			edgeNormals[i][0] = cross[0];
			edgeNormals[i][1] = cross[1];
			edgeNormals[i][2] = cross[2];

			////////////////////////////////////////////////////////

			edgeCenters[i] = new double[3];
			edgeCentersTransformed[i] = new double[3];
			
			edgeCenters[i][0] = (verts[i*3+0] + verts[(i+1)%numVerts*3+0])/2.0;
			edgeCenters[i][1] = (verts[i*3+1] + verts[(i+1)%numVerts*3+1])/2.0;
			edgeCenters[i][2] = (verts[i*3+2] + verts[(i+1)%numVerts*3+2])/2.0;
			////////////////////////////////////////////////////////
		}
		
		faceCenter[0] /= (double) numVerts;
		faceCenter[1] /= (double) numVerts;
		faceCenter[2] /= (double) numVerts;
		
		correctNormals();
		EPS = .00001;
		
		centroid = computeCentroid();
		
		radius = -Double.MAX_VALUE;
		for(int i = 0; i < faceVertices.length; i++)
			if(Math3D.distance(faceCenter, faceVertices[i])>radius)
				radius = Math3D.distance(faceCenter, faceVertices[i]);
	}

	void correctNormals()
	{
		double[] R = new double[3];
		double[] N = new double[3];

		for(int i = 0; i < edgeCenters.length; i++)
		{
			R[0] = edgeCenters[i][0] - faceCenter[0];
			R[1] = edgeCenters[i][1] - faceCenter[1];
			R[2] = edgeCenters[i][2] - faceCenter[2];
			
			N[0] = edgeNormals[i][0];
			N[1] = edgeNormals[i][1];
			N[2] = edgeNormals[i][2];

			if(Math3D.dot(R,N) < 0)
			{
				edgeNormals[i][0] *= -1.0;
				edgeNormals[i][1] *= -1.0;
				edgeNormals[i][2] *= -1.0;
			}
		}
		
		if(Math3D.dot(normal, faceCenter) < 0)
		{
			normal[0] *= -1.0;
			normal[1] *= -1.0;
			normal[2] *= -1.0;

			double[][] vertices = new double[faceVertices.length][3];
			double[][] edges = new double[faceVertices.length][3];
			double[][] centers = new double[faceVertices.length][3];
			
			for(int i = 0; i < vertices.length; i++)
			{
				vertices[i][0] = faceVertices[vertices.length-1-i][0];
				vertices[i][1] = faceVertices[vertices.length-1-i][1];
				vertices[i][2] = faceVertices[vertices.length-1-i][2];
				
				edges[i][0] = edgeNormals[vertices.length-1-i][0];
				edges[i][1] = edgeNormals[vertices.length-1-i][1];
				edges[i][2] = edgeNormals[vertices.length-1-i][2];

				centers[i][0] = edgeCenters[vertices.length-1-i][0];
				centers[i][1] = edgeCenters[vertices.length-1-i][1];
				centers[i][2] = edgeCenters[vertices.length-1-i][2];
			}
			
			faceVertices = vertices;
			edgeNormals = edges;
			edgeCenters = centers;
		}

		//////////////////////////////////////////////////
		//double normal[3];
	}

	void rotate(Quaternion rotation, double[] translation)
	{	
		for(int i = 0; i < faceVerticesTransformed.length; i++)
		{
			original[0] = faceVertices[i][0];
			original[1] = faceVertices[i][1];
			original[2] = faceVertices[i][2];

			rotation.transform(original, transformed);

			faceVerticesTransformed[i][0] = transformed[0] + translation[0];
			faceVerticesTransformed[i][1] = transformed[1] + translation[1];
			faceVerticesTransformed[i][2] = transformed[2] + translation[2];

			original[0] = edgeNormals[i][0];
			original[1] = edgeNormals[i][1];
			original[2] = edgeNormals[i][2];

			rotation.transform(original, transformed);

			edgeNormalsTransformed[i][0] = transformed[0];
			edgeNormalsTransformed[i][1] = transformed[1];
			edgeNormalsTransformed[i][2] = transformed[2];

			original[0] = edgeCenters[i][0];
			original[1] = edgeCenters[i][1];
			original[2] = edgeCenters[i][2];

			rotation.transform(original, transformed);

			edgeCentersTransformed[i][0] = transformed[0] + translation[0];
			edgeCentersTransformed[i][1] = transformed[1] + translation[1];
			edgeCentersTransformed[i][2] = transformed[2] + translation[2];
		}
		
		original[0] = faceCenter[0];
		original[1] = faceCenter[1];
		original[2] = faceCenter[2];

		rotation.transform(original, transformed);

		faceCenterTransformed[0] = transformed[0] + translation[0];
		faceCenterTransformed[1] = transformed[1] + translation[1];
		faceCenterTransformed[2] = transformed[2] + translation[2];
		
		original[0] = normal[0];
		original[1] = normal[1];
		original[2] = normal[2];

		rotation.transform(original, transformed);

		normalTransformed[0] = transformed[0];
		normalTransformed[1] = transformed[1];
		normalTransformed[2] = transformed[2];
	}

	public void projectPointOnto(double[] q, double[] store)
	{
		double[] p = this.faceVerticesTransformed[0];
		double[] n = this.normalTransformed.clone();
		
		double[] difference = new double[3];
		Math3D.sub3x1minus3x1(q, p, difference);
		Math3D.scale(n, Math3D.dot(difference, n));
		Math3D.sub3x1minus3x1(q, n, store);
	}
	
	public double getPlaneVertexDistance(double[] point)
	{
		Math3D.sub3x1minus3x1(point, faceCenterTransformed, tempPoint);
		return Math3D.dot(normalTransformed, tempPoint); 
	}

	void loadEdge(int index, double[][] store)
	{
		store[0][0] = faceVerticesTransformed[index%faceVertices.length][0];
		store[0][1] = faceVerticesTransformed[index%faceVertices.length][1];
		store[0][2] = faceVerticesTransformed[index%faceVertices.length][2];

		store[1][0] = faceVerticesTransformed[(index+1)%faceVertices.length][0];
		store[1][1] = faceVerticesTransformed[(index+1)%faceVertices.length][1];
		store[1][2] = faceVerticesTransformed[(index+1)%faceVertices.length][2];
	}


	boolean pointIsInsideTransformed(double[] point, int[] StoreBSAI)
	{
		double maxR = -Double.MAX_VALUE;
		double r;

	    double[] tempPoint = new double[3];

		for(int i = 0; i < faceVerticesTransformed.length; i++)
		{
			Math3D.sub3x1minus3x1(point, edgeCentersTransformed[i], tempPoint);
		    r = Math3D.dot(edgeNormalsTransformed[i], tempPoint);
		    if(r > maxR)
		    {
		    	maxR = r;
		    	StoreBSAI[0] = i;
		    }
		}
		    
	    if(maxR > 0)
	    	return false;
	    else
	    	return true;
	}

	public boolean rayIntersects(double[][] ray, int[] BSAEdgeI)
	{
		Math3D.sub3x1minus3x1(ray[1], ray[0], rayVector);
		rayLength = Math3D.normalize(rayVector);

		Math3D.sub3x1minus3x1(faceCenterTransformed, ray[0], tempQ);
		double num = Math3D.dot(normalTransformed, tempQ);
		double den = Math3D.dot(normalTransformed, rayVector);
			
		if(Math.abs(den) < EPS)
			return false;
				
		double T = num/den;
			
		if(T <= 0  || T >= rayLength)
			return false;
		
		Math3D.scale(rayVector, T);
		Math3D.add3x1plus3x1(rayVector, ray[0], rayVector);
			
		if(pointIsInsideTransformed(rayVector, BSAEdgeI))
			return true;
		else
			return false;
	}

	public double[][] intersectsFace(Face otherFace)
	{
		double[][] points = new double[0][];
		
		double[][] edgeA;
		double[][] edgeB;
		
		for(int i = 0; i < faceVertices.length; i++)
		{
			edgeA = this.getEdge(i);
			for(int j = 0; j < otherFace.faceVerticesTransformed.length; j++)
			{
				edgeB = otherFace.getEdge(j);
				
				double[] returnA = new double[3];
				double[] returnB = new double[3];
				double[] average = new double[3];
				
				if(CollisionDetector.lineLineIntersect(edgeA[0], edgeA[1], edgeB[0], edgeB[1], returnA, returnB))
				{
					Math3D.add3x1plus3x1(returnA, returnB, average);
					Math3D.scale(average, .5);
					points = append_exclusive(average, points);
				}
				else
				{
					if(CollisionDetector.isBetween(edgeA[0], edgeB[0], edgeB[1]))
						points = append_exclusive(edgeA[0], points);
					if(CollisionDetector.isBetween(edgeA[1], edgeB[0], edgeB[1]))
						points = append_exclusive(edgeA[1], points);

					if(CollisionDetector.isBetween(edgeB[0], edgeA[0], edgeA[1]))
						points = append_exclusive(edgeB[0], points);
					if(CollisionDetector.isBetween(edgeB[1], edgeA[0], edgeA[1]))
						points = append_exclusive(edgeB[1], points);
				}
				
				int[] ignore = new int[1];

				if(this.pointIsInsideTransformed(edgeB[0], ignore))
					points = append_exclusive(edgeB[0], points);
				if(otherFace.pointIsInsideTransformed(edgeA[0], ignore))
					points = append_exclusive(edgeA[0], points);
			}
		}	
		
		return points;
	}

	public double[][] append_exclusive(double[]point, double[][] points)
	{
		for(int i = 0; i < points.length; i++)
			if(Math3D.vertexIsCloseEnough(points[i], point, .001))
				return points;
		
		return Math3D.push_back(point, points);
	}
	
	public double[][] getEdge(int i)
	{
		return new double[][]{ faceVerticesTransformed[i%faceVerticesTransformed.length], faceVerticesTransformed[(i+1)%faceVerticesTransformed.length] };
	}
	
	public double getNthTriangleArea(int n)
	{
		double[] tempVertexA = new double[3];
		double[] tempVertexB = new double[3];

		Math3D.sub3x1minus3x1(faceVerticesTransformed[n], faceVerticesTransformed[0], tempVertexA);
		Math3D.sub3x1minus3x1(faceVerticesTransformed[(n+1)%faceVerticesTransformed.length], faceVerticesTransformed[0], tempVertexB);
		
		double[] cross = new double[3];
		
		Math3D.cross(tempVertexA, tempVertexB, cross);
		
		return Math3D.magnitude(cross)/2;
	}

	double[] computeCentroid()
	{
		double[] eA = new double[]
				{
					faceVertices[1][0] - faceVertices[0][0],
					faceVertices[1][1] - faceVertices[0][1],
					faceVertices[1][2] - faceVertices[0][2]
				};
		
		Math3D.normalize(eA);
		double[] eB = new double[3]; 
		Math3D.cross(eA, normal, eB);
		
		double[][] newVertices = new double[faceVertices.length][2];
		
		for(int i = 0; i < faceVertices.length; i++)
		{
			newVertices[i][0] = (faceVertices[i][0] - faceCenter[0])*eA[0] +
								(faceVertices[i][1] - faceCenter[1])*eA[1] +
								(faceVertices[i][2] - faceCenter[2])*eA[2];

			newVertices[i][1] = (faceVertices[i][0] - faceCenter[0])*eB[0] +
								(faceVertices[i][1] - faceCenter[1])*eB[1] +
								(faceVertices[i][2] - faceCenter[2])*eB[2];
		}
		
		double area = 0;
		for(int i = 0; i < faceVertices.length-1; i++)
			area += newVertices[i][0]*newVertices[i+1][1] - newVertices[i+1][0]*newVertices[i][1];
		area *= .5;
		
		double cX = 0;
		double cY = 0;
		for(int i = 0; i < faceVertices.length-1; i++)
		{
			cX += (newVertices[i][0] + newVertices[i+1][0])*(newVertices[i][0]*newVertices[i+1][1] - newVertices[i+1][0]*newVertices[i][1]);
			cY += (newVertices[i][1] + newVertices[i+1][1])*(newVertices[i][0]*newVertices[i+1][1] - newVertices[i+1][0]*newVertices[i][1]);
		}

		cX *= 1.0/(6.0*area);
		cY *= 1.0/(6.0*area);
		
		return new double[] 
				{ 
					faceCenter[0] + eA[0]*cX + eB[0]*cY,
					faceCenter[1] + eA[1]*cX + eB[1]*cY,
					faceCenter[2] + eA[2]*cX + eB[2]*cY,
				};
	}
}
