package com.pseudosurface.physics;



public class Math3D 
{
	public static void delete(int surfaceIndex, TangibleObject fromObject)
	{
		ConvexSurface[] newArray = new ConvexSurface[fromObject.surfaces.length-1];
		
		for(int i = 0; i < surfaceIndex; i++)
			newArray[i] = fromObject.surfaces[i];
		
		for(int i = surfaceIndex+1; i < fromObject.surfaces.length; i++)
			newArray[i-1] = fromObject.surfaces[i];
		
		fromObject.surfaces = newArray;
	}
	
	public static void delete(int objectIndex, TangibleObject[] fromList)
	{
		TangibleObject[] newArray = new TangibleObject[fromList.length-1];
		
		for(int i = 0; i < objectIndex; i++)
			newArray[i] = fromList[i];
		
		for(int i = objectIndex+1; i < fromList.length; i++)
			newArray[i-1] = fromList[i];
		
		fromList= newArray;
	}
	
	public static void cross(double[] a3x1, double[] b3x1, double[] store)
	{
		store[0] = a3x1[1] * b3x1[2] - a3x1[2] * b3x1[1];
		store[1] = a3x1[2] * b3x1[0] - a3x1[0] * b3x1[2];
		store[2] = a3x1[0] * b3x1[1] - a3x1[1] * b3x1[0];
	}

	public static double normalize(double[] v)
	{
		double mag = Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);
		v[0] /= mag;
		v[1] /= mag;
		v[2] /= mag;
		return mag;
	}


	public static double normalize(float[] v)
	{
		double mag = Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);
		v[0] /= mag;
		v[1] /= mag;
		v[2] /= mag;
		return mag;
	}
	
	public static double distance(double[] v1, double[] v2)
	{
		return Math.sqrt((v1[0]-v2[0])*(v1[0]-v2[0]) + 
					     (v1[1]-v2[1])*(v1[1]-v2[1]) + 
						 (v1[2]-v2[2])*(v1[2]-v2[2]));
	}

	public static double magnitude(double[] v)
	{
		return Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);
	}

	public static double magnitude(float[] v)
	{
		return Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);
	}
	
	public static void sub3x1minus3x1(double[] va, double[] vb, double[] store)
	{
		store[0] = va[0] - vb[0];
		store[1] = va[1] - vb[1];
		store[2] = va[2] - vb[2];
	}

	public static void sub3x1minus3x1(float[] va, double[] vb, double[] store)
	{
		store[0] = va[0] - vb[0];
		store[1] = va[1] - vb[1];
		store[2] = va[2] - vb[2];
	}
	
	public static void sub3x1minus3x1(float[] va, float[] vb, float[] store)
	{
		store[0] = va[0] - vb[0];
		store[1] = va[1] - vb[1];
		store[2] = va[2] - vb[2];
	}

	public static void add3x1plus3x1(double[] va, double[] vb, double[] store)
	{
		store[0] = va[0] + vb[0];
		store[1] = va[1] + vb[1];
		store[2] = va[2] + vb[2];
	}

	public static void add3x1plus3x1(float[] va, float[] vb, float[] store)
	{
		store[0] = va[0] + vb[0];
		store[1] = va[1] + vb[1];
		store[2] = va[2] + vb[2];
	}
	
	public static void add4x1plus4x1(double[] va, double[] vb, double[] store)
	{
		store[0] = va[0] + vb[0];
		store[1] = va[1] + vb[1];
		store[2] = va[2] + vb[2];
		store[3] = va[3] + vb[3];
	}
	
	public static void add3x1plus3x1(float[] va, double[] vb, double[] store)
	{
		store[0] = va[0] + vb[0];
		store[1] = va[1] + vb[1];
		store[2] = va[2] + vb[2];
	}

	public static void add3x1plus3x1(float[] va, double[] vb, float[] store)
	{
		store[0] = (float) (va[0] + vb[0]);
		store[1] = (float) (va[1] + vb[1]);
		store[2] = (float) (va[2] + vb[2]);
	}
	public static void add3x1plus3x1(double[] va, float[] vb, float[] store)
	{
		store[0] = (float) (va[0] + vb[0]);
		store[1] = (float) (va[1] + vb[1]);
		store[2] = (float) (va[2] + vb[2]);
	}

	public static double det3x3(double[] rowIndexedMatrix)
	{
		return	+rowIndexedMatrix[0]*(rowIndexedMatrix[4]*rowIndexedMatrix[8]-rowIndexedMatrix[5]*rowIndexedMatrix[7])
				-rowIndexedMatrix[3]*(rowIndexedMatrix[1]*rowIndexedMatrix[8]-rowIndexedMatrix[7]*rowIndexedMatrix[2])
				+rowIndexedMatrix[6]*(rowIndexedMatrix[1]*rowIndexedMatrix[5]-rowIndexedMatrix[4]*rowIndexedMatrix[2]);
	}

	public static double det2x2(double[] rowIndexedMatrix)
	{
		return 	rowIndexedMatrix[0]*rowIndexedMatrix[3] - rowIndexedMatrix[1]*rowIndexedMatrix[2];
	}


	public static void cofactor3x3(double[] rowIndexedMatrix, double[] store)
	{
		double[] temp = new double[4];
			
		for(int col = 0; col < 3; col++)
			for(int row = 0; row < 3; row++)
			{
				int i1 = (row+1)%3 + ((col+1)%3)*3,
					i2 = (row+2)%3 + ((col+1)%3)*3,
					i3 = (row+1)%3 + ((col+2)%3)*3,
					i4 = (row+2)%3 + ((col+2)%3)*3;
					
				temp[0] = rowIndexedMatrix[i1];
				temp[1] = rowIndexedMatrix[i2];
				temp[2] = rowIndexedMatrix[i3];
				temp[3] = rowIndexedMatrix[i4];

				store[row + col*3] = det2x2(temp);
			}
	}

	public static void transpose3x3(double[] a, double[] store)
	{
		store[0] = a[0];	store[1] = a[3];	store[2] = a[6];

		store[3] = a[1];	store[4] = a[4];	store[5] = a[7];

		store[6] = a[2];	store[7] = a[5];	store[8] = a[8];
	}

	public static void matrixInverse3x3(double[] rowIndexedMatrix, double[] store)
	{
		double[] temp3x3A = new double[9];
		double[] temp3x3B = new double[9];

		double determinantInverse = 1.0/det3x3(rowIndexedMatrix);

		cofactor3x3(rowIndexedMatrix, temp3x3A);
		transpose3x3(temp3x3A, temp3x3B);
			
		for(int i = 0; i < 9; i++) 
			store[i] = temp3x3B[i]*determinantInverse;
	}

	public static boolean vertexIsCloseEnough(double[] a, double[] b, double maxR)
	{
		if( maxR >   (a[0]-b[0])*(a[0]-b[0])
					+(a[1]-b[1])*(a[1]-b[1])
					+(a[2]-b[2])*(a[2]-b[2]))
			return true;
		else 
			return false;
	}

	public static void scale(double[] x, double[] w)
	{
		for(int i = 0; i < x.length; i++)
			x[i] *= w[i];
	}
	
	public static void scale(double[] v, double factor)
	{
		for(int i = 0; i < v.length; i++)
			v[i] *= factor;
	}

	public static void scale(float[] v, double factor)
	{
		v[0] *= factor;
		v[1] *= factor;
		v[2] *= factor;
	}

	public static void scaleColor(float[] x, float w, float[] store)
	{
		for(int i = 0; i < 3; i++)
			store[i] = w*x[i];
	}
	
	public static double dot(double[] va, double[] vb)
	{
		return va[0]*vb[0] + va[1]*vb[1] + va[2]*vb[2];
	}

	public static void loadTilde(double[] from, double[] into)
	{
		into[0] = 0;		into[1] = -from[2];		into[2] = from[1];

		into[3] = from[2];	into[4] = 0; 			into[5] = -from[0];
			
		into[6] = -from[1];	into[7] = from[0];		into[8] = 0;
	}

	public static void mult3x3times3x1(double[] a3x3, double[] b3x1, double[] store)
	{
		store[0] = a3x3[0]*b3x1[0] + a3x3[1]*b3x1[1] + a3x3[2]*b3x1[2];
		store[1] = a3x3[3]*b3x1[0] + a3x3[4]*b3x1[1] + a3x3[5]*b3x1[2];
		store[2] = a3x3[6]*b3x1[0] + a3x3[7]*b3x1[1] + a3x3[8]*b3x1[2];
	}

	public static double mult1x3times3x1(double[] a1x3, double[] b3x1)
	{
		return Math3D.dot(a1x3, b3x1);
	}
	
	public static void mult1x3times3x3(double[] a1x3, double[] b3x3, double[] store)
	{
		store[0] = b3x3[0]*a1x3[0] + b3x3[3]*a1x3[1] + b3x3[6]*a1x3[2];
		store[1] = b3x3[1]*a1x3[0] + b3x3[4]*a1x3[1] + b3x3[7]*a1x3[2];
		store[2] = b3x3[2]*a1x3[0] + b3x3[5]*a1x3[1] + b3x3[8]*a1x3[2];
	}
	
	
	public static void mult3x3times3x3(double[] a, double[] b, double[] store)
	{
		//For each element, store the rows elements of 'a' times the column elements of 'b'
		store[0] = a[0]*b[0] + a[3]*b[1] + a[6]*b[2];
		store[1] = a[1]*b[0] + a[4]*b[1] + a[7]*b[2];
		store[2] = a[2]*b[0] + a[5]*b[1] + a[8]*b[2];

		store[3] = a[0]*b[3] + a[3]*b[4] + a[6]*b[5];
		store[4] = a[1]*b[3] + a[4]*b[4] + a[7]*b[5];
		store[5] = a[2]*b[3] + a[5]*b[4] + a[8]*b[5];

		store[6] = a[0]*b[6] + a[3]*b[7] + a[6]*b[8];
		store[7] = a[1]*b[6] + a[4]*b[7] + a[7]*b[8];
		store[8] = a[2]*b[6] + a[5]*b[7] + a[8]*b[8];
	}

	public static Face[] push_back(Face newElement, Face[] onto)
	{
		Face[] newOnto = new Face[onto.length+1];
		for(int i = 0; i < onto.length; i++)
			newOnto[i] = onto[i];
		newOnto[onto.length] = newElement;	
		return newOnto;
	}
	
	public static Face[][] push_back(Face[] newElement, Face[][] onto)
	{
		Face[][] newOnto = new Face[onto.length+1][];
		for(int i = 0; i < onto.length; i++)
		{
			newOnto[i] = new Face[onto[i].length];
			for(int j = 0; j < onto[i].length; j++)
				newOnto[i][j] = onto[i][j];
		}
		newOnto[onto.length] = new Face[newElement.length];
		for(int i = 0; i < newElement.length; i++)
			newOnto[onto.length][i] = newElement[i];
		return newOnto;
	}

	public static double[][] push_back(double[][] newElement, double[][] onto)
	{
		for(int i = 0; i < newElement.length; i++)
			onto = push_back(newElement[i], onto);
		return onto;
	}
	
	public static double[][] push_back(double[] newElement, double[][] onto)
	{
		double[][] newOnto = new double[onto.length+1][];
		for(int i = 0; i < onto.length; i++)
		{
			newOnto[i] = new double[onto[i].length];
			for(int j = 0; j < onto[i].length; j++)
				newOnto[i][j] = onto[i][j];
		}
		newOnto[onto.length] = new double[newElement.length];
		for(int i = 0; i < newElement.length; i++)
			newOnto[onto.length][i] = newElement[i];
		return newOnto;
	}

	public static int[][] push_back(int[] newElement, int[][] onto)
	{
		int[][] newOnto = new int[onto.length+1][];
		for(int i = 0; i < onto.length; i++)
		{
			newOnto[i] = new int[onto[i].length];
			for(int j = 0; j < onto[i].length; j++)
				newOnto[i][j] = onto[i][j];
		}
		newOnto[onto.length] = new int[newElement.length];
		for(int i = 0; i < newElement.length; i++)
			newOnto[onto.length][i] = newElement[i];
		return newOnto;
	}

	public static TangibleObject[][] push_back(TangibleObject[] newElement, TangibleObject[][] onto)
	{
		TangibleObject[][] newOnto = new TangibleObject[onto.length+1][];
		for(int i = 0; i < onto.length; i++)
		{
			newOnto[i] = new TangibleObject[onto[i].length];
			for(int j = 0; j < onto[i].length; j++)
				newOnto[i][j] = onto[i][j];
		}
		newOnto[onto.length] = new TangibleObject[newElement.length];
		for(int i = 0; i < newElement.length; i++)
			newOnto[onto.length][i] = newElement[i];
		return newOnto;
	}

	public static ConvexSurface[] push_back(ConvexSurface newElement, ConvexSurface[] onto)
	{
		ConvexSurface[] newOnto = new ConvexSurface[onto.length+1];
		for(int i = 0; i < onto.length; i++)
			newOnto[i] = onto[i];
		newOnto[onto.length] = newElement;	
		return newOnto;
	}

	public static TangibleObject[] push_back(TangibleObject newElement, TangibleObject[] onto)
	{
		TangibleObject[] newOnto = new TangibleObject[onto.length+1];
		for(int i = 0; i < onto.length; i++)
			newOnto[i] = onto[i];
		newOnto[onto.length] = newElement;	
		return newOnto;
	}

}
