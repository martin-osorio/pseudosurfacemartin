package com.pseudosurface.physics;

public abstract class XZEquation {

	public float du;
	public float dv;
	public float minu;
	public float maxu;
	public float minv;
	public float maxv;
	
	public double[][] faces;
	public ConvexSurface surface;
	
	public XZEquation(float du, float dv, float minu, float maxu, float minv, float maxv) {
		this.du = du;
		this.dv = dv;
		this.minu = minu;
		this.maxu = maxu;
		this.minv = minv;
		this.maxv = maxv;

		int numFaces = 0;
		
//		for(float x = minu; x < maxu; x += du)
//			for(float z = minv; z < maxv; z += dv)
//				numFaces+=2;

		for(float x = minu; x < maxu; x += du)
			for(float z = minv; z < maxv; z += dv)
				numFaces++;
		
		faces = new double[numFaces][];
		
		int index = 0;
		
//		float x = -dx/2.0f;
//		float z = -dz/2.0f;
		for(float u = minu; u < maxu; u += du)
			for(float v = minv; v < maxv; v += dv)
			{	
				
//				faces[index] = new double[] { 
//						xou(u + 0, v + 0), getHeight(xou(u, v + 0),zov(u, v), u, v), zov(u, v + 0),
//						xou(u + du, v + 0), getHeight(xou(u+du, v + 0), zov(u+du, v+0), u+du, v), zov(u+du, v + 0),
//						xou(u + du, v + dv), getHeight(xou(u+du, v + dv), zov(u+du, v+dv), u+du, v+dv), zov(u+du, v + dv)
//				};
//				
//				index++;
//				
//				faces[index] = new double[] {
//						xou(u + 0, v + 0), getHeight(xou(u + 0, v + 0), zov(u + 0, v), u, v), zov(u + 0, v + 0),
//						xou(u + 0, v + dv), getHeight(xou(u + 0, v + dv), zov(u + 0, v+dv), u, v+dv), zov(u + 0, v + dv),
//						xou(u + du, v + dv), getHeight(xou(u+du, v + dv), zov(u + du, v+dv), u+du, v+dv), zov(u + +du, v + dv)
//				};
//				
//				index++;

				faces[index] = new double[] { 
						xou(u + 0, v + 0), getHeight(xou(u, v + 0),zov(u, v), u, v), zov(u, v + 0),
						xou(u + 0, v + dv), getHeight(xou(u + 0, v + dv), zov(u + 0, v+dv), u, v+dv), zov(u + 0, v + dv),
						xou(u + du, v + dv), getHeight(xou(u+du, v + dv), zov(u+du, v+dv), u+du, v+dv), zov(u+du, v + dv),
						xou(u + du, v + 0), getHeight(xou(u+du, v + 0), zov(u+du, v+0), u+du, v), zov(u+du, v + 0)
				};
				
				
				index++;
			}
		surface = loadSurface();
	}

	abstract public double xou(double u, double v);
	abstract public double zov(double u, double v);
	
	abstract public float getHeight(double x, double z, double u, double v);
	abstract public double[] getNormal(double x, double z);
	abstract public ConvexSurface loadSurface();
	abstract public Face[] getFaces(double[] position, float radius);
}
