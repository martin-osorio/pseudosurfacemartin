package com.pseudosurface.physics;



public class ConvexSurface 
{
	public XZEquation equation;
	//Describe the surface type so optimizations can be made in the collision detection.
	public int SURFACE_TYPE = ABSTRACT;
	
	public static final int ABSTRACT = 0;
	public static final int SPHERE = 1;
	public static final int CUBE = 2;
	public static final int CYLINDER = 3;
	public static final int CONE = 4;
	public static final int FACE = 5;
	
	public float radius = -1.0f; // If sphere, cylinder or cone.
	public float width = -1.0f; //If cube.
	public double length = -1.0f; //If cube.
	public float height = -1.0f; //If cube, cone, or cylinder.
	
	public Face[] faces;
	
	public double[] relativePosition = new double[3];
	public Quaternion relativeRotation;
	
	public int[][] edgeIndexing;
	public int[][] pointIndexing;

	public double[] maxPositiveAxis = new double[3];
	public double[] maxNegativeAxis = new double[3];
	
	public double maxRadius;
	
	public ConvexSurface()
	{
		relativePosition[0] = 0;
		relativePosition[1] = 0;
		relativePosition[2] = 0;

		relativeRotation = new Quaternion(0, 1, 0, 0);
	}

	public ConvexSurface(XZEquation equation)
	{
		this.equation = equation;
		relativePosition[0] = 0;
		relativePosition[1] = 0;
		relativePosition[2] = 0;

		relativeRotation = new Quaternion(0, 1, 0, 0);
	}

	public void loadTempAABB()
	{
		maxPositiveAxis[0] = -Double.MAX_VALUE;
		maxPositiveAxis[1] = -Double.MAX_VALUE;
		maxPositiveAxis[2] = -Double.MAX_VALUE;

		maxNegativeAxis[0] = Double.MAX_VALUE;
		maxNegativeAxis[1] = Double.MAX_VALUE;
		maxNegativeAxis[2] = Double.MAX_VALUE;

		for(int i = 0; i < faces.length; i++)
			for(int j = 0; j < faces[i].faceVerticesTransformed.length; j++)
			{
				if(faces[i].faceVertices[j][0]-relativePosition[0] > maxPositiveAxis[0])
					maxPositiveAxis[0] = faces[i].faceVertices[j][0]-relativePosition[0];
				if(faces[i].faceVertices[j][1]-relativePosition[1] > maxPositiveAxis[1])
					maxPositiveAxis[1] = faces[i].faceVertices[j][1]-relativePosition[1];
				if(faces[i].faceVertices[j][2]-relativePosition[2] > maxPositiveAxis[2])
					maxPositiveAxis[2] = faces[i].faceVertices[j][2]-relativePosition[2];

				if(faces[i].faceVertices[j][0]-relativePosition[0] < maxNegativeAxis[0])
					maxNegativeAxis[0] = faces[i].faceVertices[j][0]-relativePosition[0];
				if(faces[i].faceVertices[j][1]-relativePosition[1] < maxNegativeAxis[1])
					maxNegativeAxis[1] = faces[i].faceVertices[j][1]-relativePosition[1];
				if(faces[i].faceVertices[j][2]-relativePosition[2] < maxNegativeAxis[2])
					maxNegativeAxis[2] = faces[i].faceVertices[j][2]-relativePosition[2];
			}
	}
	public void loadEdgeIndexing() 
	{
		double[][] edgesArray = new double[0][];

		double[][] edge = new double[2][];
		edge[0] = new double[3];
		edge[1] = new double[3];

		boolean found = false;
		
		edgeIndexing = new int[0][]; // { {face, edge}, {face, edge} + etc }
			
		//Go through each of the faces on this surface and store the edges, but only if the edge is not one already stored.
		for(int i = 0; i < faces.length; i ++)
			for(int j = 0; j < faces[i].faceVertices.length; j++)
			{
				faces[i].loadEdge(j, edge);

				for(int k =0; k < edgesArray.length; k+=2)
					if( Math3D.vertexIsCloseEnough(edgesArray[k], edge[0], .01) && Math3D.vertexIsCloseEnough(edgesArray[k+1], edge[1], .01) ||
						Math3D.vertexIsCloseEnough(edgesArray[k], edge[1], .01) && Math3D.vertexIsCloseEnough(edgesArray[k+1], edge[0], .01))
					{
						found = true;
						//add to the reverseIndexing int[][] array the index of the face connected to the edge
							
					}
							
				if(!found)
				{
					double[] point = new double[3];
					point[0] = edge[0][0];
					point[1] = edge[0][1];
					point[2] = edge[0][2];
					edgesArray = Math3D.push_back(point, edgesArray);
						
					point[0] = edge[1][0];
					point[1] = edge[1][1];
					point[2] = edge[1][2];
					edgesArray = Math3D.push_back(point, edgesArray);

					int[] indices = new int[2];
					indices[0] = i;
					indices[1] = j;

					edgeIndexing = Math3D.push_back(indices, edgeIndexing);
				}
				else
					found = false;
			}
	}

	boolean rayIntersects(double[][] ray)
	{
		double EPS;
		double[] tempQ = new double[3];
		double[] rayVector = new double[3];
		double rayLength;
		int[] BSAEdgeI = new int[1];
		
		for(int i = 0; i < faces.length; i++)
		{
		
			Math3D.sub3x1minus3x1(ray[1], ray[0], rayVector);
			rayLength = Math3D.normalize(rayVector);
	
			Math3D.sub3x1minus3x1(faces[i].faceCenterTransformed, ray[0], tempQ);
			double num = Math3D.dot(faces[i].normalTransformed, tempQ);
			double den = Math3D.dot(faces[i].normalTransformed, rayVector);
				
			if(Math.abs(den) < .01)
				return false;
					
			double T = num/den;
				
			if(T <= 0  || T >= rayLength)
				return false;
			
			Math3D.scale(rayVector, T);
			Math3D.add3x1plus3x1(rayVector, ray[0], rayVector);
				
			if(faces[i].pointIsInsideTransformed(rayVector, BSAEdgeI))
				return true;
		}
		
		return false;
	}
	
	public void loadPointIndexing()
	{
		double[][] pointArray = new double[0][];
		
		double[][] edge = new double[2][];
		edge[0] = new double[3];
		edge[1] = new double[3];

		boolean found = false;

		pointIndexing = new int[0][];
			
		for(int i = 0; i < edgeIndexing.length; i++)
		{
			faces[edgeIndexing[i][0]].loadEdge(edgeIndexing[i][1], edge);
			for(int j =0; j < pointArray.length; j++)
				if( Math3D.vertexIsCloseEnough(pointArray[j], edge[0], .01))
					found = true;
			if(!found)
			{
				double[] newVert = new double[3];
				newVert[0] = edge[0][0];
				newVert[1] = edge[0][1];
				newVert[2] = edge[0][2];
				
				pointArray = Math3D.push_back(newVert, pointArray);

				int[] newIndex = new int[2];
				newIndex[0] = edgeIndexing[i][0];
				newIndex[1] = edgeIndexing[i][1];

				pointIndexing = Math3D.push_back(newIndex, pointIndexing);
			}
			else
				found = false;
				
			for(int j =0; j < pointArray.length; j++)
				if( Math3D.vertexIsCloseEnough(pointArray[j], edge[1], .01))
					found = true;
			if(!found)
			{
				double[] newVert = new double[3];
				newVert[0] = edge[1][0];
				newVert[1] = edge[1][1];
				newVert[2] = edge[1][2];
				
				pointArray = Math3D.push_back(newVert, pointArray);

				int[] newIndex = new int[2];
				newIndex[0] = edgeIndexing[i][0];
				newIndex[1] = (edgeIndexing[i][1]+1)%faces[edgeIndexing[i][0]].faceVertices.length;

				pointIndexing = Math3D.push_back(newIndex, pointIndexing);
			}
			else
				found = false;
		}
	}

	public void loadMaxRadius()
	{
		double tempRadius = 0;

		for(int i = 0; i < faces.length; i++)
			for(int j = 0; j < faces[i].faceVerticesTransformed.length; j++)
			{
				tempRadius = Math.sqrt(
									faces[i].faceVertices[j][0] * faces[i].faceVertices[j][0]+
									faces[i].faceVertices[j][1] * faces[i].faceVertices[j][1]+
									faces[i].faceVertices[j][2] * faces[i].faceVertices[j][2]);

				if(tempRadius > maxRadius)
					maxRadius = tempRadius;
			}

		//Avoid round-off errors
		maxRadius += .000001;
	}

	public boolean pointIsInsideTransformed(double[] point)
	{
		double maxR = -Double.MAX_VALUE;
		double temp;
		for(int i = 0; i < faces.length; i ++)
		{
		    temp = faces[i].getPlaneVertexDistance(point);
		    if(temp > maxR)
		    	maxR = temp;
		}
		    
		if(maxR > 0)
		    return false;
	    return true;
	}
	
	public void breakFaces()
	{
		
	}
}
