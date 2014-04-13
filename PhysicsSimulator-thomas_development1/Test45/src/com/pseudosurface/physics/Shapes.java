package com.pseudosurface.physics;


public class Shapes 
{
	public static ConvexSurface loadCube(double scaleX, double scaleY, double scaleZ)
	{
		double r = 0.5;
		double[] face1 = {r*scaleX,r*scaleY,r*scaleZ, -r*scaleX,r*scaleY,r*scaleZ, -r*scaleX,r*scaleY,-r*scaleZ, r*scaleX,r*scaleY,-r*scaleZ}; //Top
		double[] face2 = {r*scaleX,r*scaleY,r*scaleZ, -r*scaleX,r*scaleY,r*scaleZ, -r*scaleX,-r*scaleY,r*scaleZ, r*scaleX,-r*scaleY,r*scaleZ};//Front
		double[] face3 = {r*scaleX,r*scaleY,r*scaleZ, r*scaleX,-r*scaleY,r*scaleZ, r*scaleX,-r*scaleY,-r*scaleZ, r*scaleX,r*scaleY,-r*scaleZ};//Right
		r = -0.5;
		double[] face4 = {r*scaleX,r*scaleY,r*scaleZ, -r*scaleX,r*scaleY,r*scaleZ, -r*scaleX,r*scaleY,-r*scaleZ, r*scaleX,r*scaleY,-r*scaleZ}; //Top
		double[] face5 = {r*scaleX,r*scaleY,r*scaleZ, -r*scaleX,r*scaleY,r*scaleZ, -r*scaleX,-r*scaleY,r*scaleZ, r*scaleX,-r*scaleY,r*scaleZ};//Front
		double[] face6 = {r*scaleX,r*scaleY,r*scaleZ, r*scaleX,-r*scaleY,r*scaleZ, r*scaleX,-r*scaleY,-r*scaleZ, r*scaleX,r*scaleY,-r*scaleZ};//Right

		ConvexSurface surface = new ConvexSurface();

		surface.faces = new Face[6];
		
		surface.faces[0] = new Face(face1, 4);
		surface.faces[1] = new Face(face2, 4);
		surface.faces[2] = new Face(face3, 4);

		surface.faces[3] = new Face(face4, 4);
		surface.faces[4] = new Face(face5, 4);
		surface.faces[5] = new Face(face6, 4);
		
		surface.SURFACE_TYPE = ConvexSurface.CUBE;
		surface.width = (float) scaleX;
		surface.height = (float) scaleX;
		surface.length = (float) scaleZ;
		
		surface.breakFaces();
		surface.loadEdgeIndexing();
		surface.loadPointIndexing();
		return surface;
	}

	public static ConvexSurface loadPrism(double scaleX, double scaleY, double scaleZ,
										 double frontLeft, double frontRight, double backLeft, double backRight)
	{
		double r = 0.5;
		double[] face1 = {-r*scaleX,r*scaleY+frontLeft  ,r*scaleZ, 
						 -r*scaleX,r*scaleY+backLeft,-r*scaleZ, 
						 r*scaleX,r*scaleY+backRight,-r*scaleZ}; //Top1

		double[] face2 = {r*scaleX,r*scaleY+frontRight ,r*scaleZ, 
						 -r*scaleX,r*scaleY+backLeft,-r*scaleZ, 
						 r*scaleX,r*scaleY+backRight,-r*scaleZ}; //Top2

		double[] face3 = {r*scaleX,r*scaleY+frontRight ,r*scaleZ, 
						 -r*scaleX,r*scaleY+frontLeft  ,r*scaleZ,
						 r*scaleX,r*scaleY+backRight,-r*scaleZ}; //Top3

		double[] face4 = {r*scaleX,r*scaleY+frontRight ,r*scaleZ, 
						 -r*scaleX,r*scaleY+frontLeft  ,r*scaleZ, 
						 -r*scaleX,r*scaleY+backLeft,-r*scaleZ}; //Top4
		
		
		
		
		double[] face5 = {r*scaleX,r*scaleY+frontRight,r*scaleZ, 
						 -r*scaleX,r*scaleY+frontLeft,r*scaleZ, 
						 -r*scaleX,-r*scaleY,r*scaleZ,
						 r*scaleX,-r*scaleY,r*scaleZ};//Front
		
		double[] face6 = {r*scaleX,r*scaleY+frontRight,r*scaleZ, 
				  		  r*scaleX,-r*scaleY,r*scaleZ, 
				  		 r*scaleX,-r*scaleY,-r*scaleZ, 
				  		 r*scaleX,r*scaleY+backRight,-r*scaleZ};//Right
		r = -0.5;
		
		double[] face7 = {r*scaleX,r*scaleY,r*scaleZ, 
						  -r*scaleX,r*scaleY,r*scaleZ, 
						  -r*scaleX,r*scaleY,-r*scaleZ, 
						  r*scaleX,r*scaleY,-r*scaleZ}; //bottom
		
		double[] face8 = {r*scaleX,r*scaleY,r*scaleZ, 
						 -r*scaleX,r*scaleY,r*scaleZ, 
						 -r*scaleX,-r*scaleY+backRight,r*scaleZ,
						 r*scaleX,-r*scaleY+backLeft,r*scaleZ};//Back
		
		double[] face9 = {r*scaleX,r*scaleY,r*scaleZ, 
						  r*scaleX,-r*scaleY+backLeft,r*scaleZ, 
						  r*scaleX,-r*scaleY+frontLeft,-r*scaleZ, 
						  r*scaleX,r*scaleY,-r*scaleZ};//Left

		ConvexSurface surface = new ConvexSurface();

		surface.faces = new Face[9];
		
		surface.faces[0] = new Face(face1, 3);
		surface.faces[1] = new Face(face2, 3);
		surface.faces[2] = new Face(face3, 3);
		surface.faces[3] = new Face(face4, 3);
		
		surface.faces[4] = new Face(face5, 4);
		surface.faces[5] = new Face(face6, 4);
		surface.faces[6] = new Face(face7, 4);
		surface.faces[7] = new Face(face8, 4);
		surface.faces[8] = new Face(face9, 4);
		
		surface.SURFACE_TYPE = ConvexSurface.CUBE;
		surface.width = (float) scaleX;
		surface.height = (float) scaleX;
		surface.length = (float) scaleZ;
		
		surface.breakFaces();
		surface.loadEdgeIndexing();
		surface.loadPointIndexing();
		return surface;
	}

	public static ConvexSurface loadOctahedron()
	{
		double[] face1 = {0,1,0,	1,0,0,		0,0,-1};
		double[] face2 = {0,1,0,	0,0,-1,		-1,0,0};
		double[] face3 = {0,1,0,	-1,0,0,		0,0,1};
		double[] face4 = {0,1,0,	0,0,1,		1,0,0};
		double[] face5 = {0,-1,0,	0,0,-1,		1,0,0};
		double[] face6 = {0,-1,0,	-1,0,0,		0,0,-1};
		double[] face7 = {0,-1,0,	0,0,1,		-1,0,0};
		double[] face8 = {0,-1,0,	1,0,0,		0,0,1};

		ConvexSurface surface = new ConvexSurface();
		
		surface.faces = new Face[8];
		
		surface.faces[0] = new Face(face1, 3);
		surface.faces[1] = new Face(face2, 3);
		surface.faces[2] = new Face(face3, 3);
		surface.faces[3] = new Face(face4, 3);
		surface.faces[4] = new Face(face5, 3);
		surface.faces[5] = new Face(face6, 3);
		surface.faces[6] = new Face(face7, 3);
		surface.faces[7] = new Face(face8, 3);

		surface.SURFACE_TYPE = ConvexSurface.ABSTRACT;
		surface.breakFaces();
		surface.loadEdgeIndexing();
		surface.loadPointIndexing();
		return surface;
	}

	public static ConvexSurface loadCylinder(int sides, double height, double radius)
	{
		ConvexSurface surface = new ConvexSurface();

		surface.faces = new Face[sides+2];

		double twoPI = 2*3.1415926535897932384626433832795;

		double alpha, theta;

		for(int i = 0; i < sides; i++)
		{
			alpha = i*(twoPI/sides);
			theta = (i+1)*(twoPI/sides);

			double[] face =   {radius*Math.cos(alpha),		-height/2,		radius*Math.sin(alpha),	
							   radius*Math.cos(alpha),		height/2,		radius*Math.sin(alpha),
							   radius*Math.cos(theta),		height/2,		radius*Math.sin(theta),
							   radius*Math.cos(theta),		-height/2,		radius*Math.sin(theta),};
		
			surface.faces[i] = new Face(face, 4);
		}
		
		double[] top = new double[sides*3];
		double[] bottom = new double[sides*3];

		for(int i = 0; i < sides; i++)
		{
			alpha = i*(twoPI/sides);

			top[i*3+0] = radius*Math.cos(alpha);
			top[i*3+1] = height/2;
			top[i*3+2] = radius*Math.sin(alpha);
			
			bottom[i*3+0] = radius*Math.cos(alpha);
			bottom[i*3+1] = -height/2;
			bottom[i*3+2] = radius*Math.sin(alpha);
		}
		
		surface.faces[sides+0] = new Face(top, sides);
		surface.faces[sides+1] = new Face(bottom, sides);

		surface.SURFACE_TYPE = ConvexSurface.CYLINDER;
		surface.radius = (float) radius;
		surface.height = (float) height;
		
		surface.breakFaces();
		surface.loadEdgeIndexing();
		surface.loadPointIndexing();
		return surface;
	}

	public static ConvexSurface loadSphere(int slices, int stacks, double radius)
	{
		ConvexSurface surface = new ConvexSurface();

		surface.faces = new Face[slices*stacks];

		double twoPI = 2*3.1415926535897932384626433832795;

		double thetaA, phiA, thetaB, phiB;

		/*
		r = [0, infinity)
		theta = [0, 2*pi)
		phi = [0, pi]

		x = r*Math.cos(theta)*Math.sin(phi)
		y = r*Math.cos(phi)
		z = r*Math.sin(theta)*Math.sin(phi)
		*/
		
		for(int i = 0; i < stacks; i++)
		{
			for(int j = 0; j < slices; j++)
			{
				thetaA = j*(twoPI/slices);
				thetaB = (j+1)*(twoPI/slices);
				phiA = i*(twoPI/2/stacks);
				phiB = (i+1)*(twoPI/2/stacks);

				double[] face =   {radius*Math.cos(thetaA)*Math.sin(phiA),	radius*Math.cos(phiA),	radius*Math.sin(thetaA)*Math.sin(phiA),	
								   radius*Math.cos(thetaA)*Math.sin(phiB),	radius*Math.cos(phiB),	radius*Math.sin(thetaA)*Math.sin(phiB),	
								   radius*Math.cos(thetaB)*Math.sin(phiB),	radius*Math.cos(phiB),	radius*Math.sin(thetaB)*Math.sin(phiB),
								   radius*Math.cos(thetaB)*Math.sin(phiA),	radius*Math.cos(phiA),	radius*Math.sin(thetaB)*Math.sin(phiA)};
		
				surface.faces[i*slices+j] = new Face(face, 4);
			}
		}
		

		surface.SURFACE_TYPE = ConvexSurface.SPHERE;
		surface.radius = (float) radius;
		
		surface.breakFaces();
		surface.loadEdgeIndexing();
		surface.loadPointIndexing();
		return surface;
	}



	public static ConvexSurface loadCone(int sides, double height, double radius)
	{
		ConvexSurface surface = new ConvexSurface();

		surface.faces = new Face[sides+1];

		double twoPI = 2*3.1415926535897932384626433832795;

		double alpha, theta;

		for(int i = 0; i < sides; i++)
		{
			alpha = i*(twoPI/sides);
			theta = (i+1)*(twoPI/sides);

			double[] face =   {radius*Math.cos(alpha),	-height/2,		radius*Math.sin(alpha),	
							   0,						height/2,		0,
							   radius*Math.cos(theta),	-height/2,		radius*Math.sin(theta)};
		
			surface.faces[i] = new Face(face, 3);
		}
		
		double[] bottom = new double[sides*3];

		for(int i = 0; i < sides; i++)
		{
			alpha = i*(twoPI/sides);

			bottom[i*3+0] = radius*Math.cos(alpha);
			bottom[i*3+1] = -height/2;
			bottom[i*3+2] = radius*Math.sin(alpha);
		}
		
		surface.faces[sides] = new Face(bottom, sides);

		surface.SURFACE_TYPE = ConvexSurface.CONE;
		surface.radius = (float) radius;
		surface.height = (float) height;
		
		surface.breakFaces();
		surface.loadEdgeIndexing();
		surface.loadPointIndexing();
		return surface;
	}
}
