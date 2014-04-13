package com.pseudosurface.levels.level1;

import com.pseudosurface.levels.template.Demon;
import com.pseudosurface.levels.template.Renderer;
import com.pseudosurface.levels.template.World;
import com.pseudosurface.physics.ConvexSurface;
import com.pseudosurface.physics.Face;
import com.pseudosurface.physics.Math3D;
import com.pseudosurface.physics.TangibleCone;
import com.pseudosurface.physics.TangibleCube;
import com.pseudosurface.physics.TangibleCylinder;
import com.pseudosurface.physics.TangibleFace;
import com.pseudosurface.physics.TangibleObject;
import com.pseudosurface.physics.TangibleSphere;
import com.pseudosurface.physics.XZEquation;

public class World1 extends World
{
	public World1(Renderer renderer) 
	{
		super(renderer);
	}


	long startTime = System.nanoTime();

			
	public void loadSimulation() 
	{
		maxDrawDistance = 120;

		loadFloor();
		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		loadTrees();
		
		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		TangibleSphere sphere = new TangibleSphere(10, 10, 2.5f);
		
		sphere.density = .01;
		
		sphere.loadPhysicalProperties();
		objects = Math3D.push_back(sphere, objects);
		
		sphere = new TangibleSphere(10, 10, 2.5f);

		sphere.rotationQuaternion.loadAxisAngle(0, 1, 0, 0);
		
		sphere.infiniteMass = false;
		
		sphere.displacement[0] = 1;
		sphere.displacement[1] = 6;
		sphere.displacement[2] = 0;
			
		sphere.linearVelocity[0] = 0;
		sphere.linearVelocity[1] = 0;
		sphere.linearVelocity[2] = 0;
		
		sphere.omega[0] = .1;//rand()%3-1.5;
		sphere.omega[1] = .12;//rand()%3-1.5;
		sphere.omega[2] = .09;//rand()%3-1.5;

		sphere.constantAcceleration = playerGravity.clone();
		
		defaultData = Math3D.push_back(sphere, defaultData);
		
		playerIndex = objects.length-1;
		
		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
//		sphere = new TangibleSphere(30, 30, 220f);
//		
//		sphere.density = .001;
//		
////		sphere.loadPhysicalProperties();
//		objects = Math3D.push_back(sphere, objects);
//		
//		sphere = new TangibleSphere(30, 30, 220);
//
//		sphere.rotationQuaternion.loadAxisAngle(0, 1, 0, 0);
//
////		sphere.restitution = 1.0;
//
//		sphere.notFalling = true;
//		sphere.isArt = true;
//		
//		sphere.displacement[0] = 6;
//		sphere.displacement[1] = 12;
//		sphere.displacement[2] = 6;
//			
////		sphere.constantAcceleration[1] = -8;
//		
//		sphere.linearVelocity[0] = 0;
//		sphere.linearVelocity[1] = 0;
//		sphere.linearVelocity[2] = 0;
//		
////		sphere.omega[0] = .1;//rand()%3-1.5;
////		sphere.omega[1] = .12;//rand()%3-1.5;
////		sphere.omega[2] = .09;//rand()%3-1.5;
//		
//		defaultData = Math3D.push_back(sphere, defaultData);
		
		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////

		loadDemon();
		
		resetSimulation();
	}


	private void loadTrees() 
	{
		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		int numTrees = 4;
		double dist = 50;
		double rotationOffset = 0;
		
		for(int i = 0; i < numTrees; i ++)
		{
			double theta = 2*Math.PI/numTrees*i+rotationOffset;
			double treeX = dist*Math.cos(theta);
			double treeZ = dist*Math.sin(theta);

			double platformLength = 10;
			double platformWidth = 10;

			double randYOffset = (Math.random()-1.25)*5.0f;
			double platformY = -11.1f+randYOffset;
			
			double platformX = -platformLength/2+treeX;
			double platformZ = -platformWidth/2+treeZ;
			
			loadTree(4,//Slices
					2, //TrunkRadius
					6, //canopyRadius
					10, //trunkHeight
					10, //canopyHeight
					treeX, -6+randYOffset, treeZ); //xyz
	
	
			double rot = Math.random()*Math.PI*2;
			
			defaultData[objects.length-2].rotationQuaternion.loadAxisAngle(0, 1, 0, rot);
			defaultData[objects.length-1].rotationQuaternion.loadAxisAngle(0, 1, 0, rot);
			defaultData[objects.length-2].isDecoration = true;
			defaultData[objects.length-1].isDecoration = true;
			
			platformX = -platformLength/2+treeX;
			platformZ = -platformWidth/2+treeZ;
			
			loadPlatform(platformX, platformY, platformZ, platformLength, platformWidth, platformLength, platformWidth);
			defaultData[objects.length-1].isDecoration = true;
	
			defaultData[objects.length-1].color_lit_solid[0] = 0.35f;
			defaultData[objects.length-1].color_lit_solid[1] = 0.55f;
			defaultData[objects.length-1].color_lit_solid[2] = 0.35f;
			defaultData[objects.length-1].color_lit_solid[3] = 1.0f;
			
			defaultData[objects.length-1].color_shadow_solid[0] = 0.0f;//15f;
			defaultData[objects.length-1].color_shadow_solid[1] = 0.15f;
			defaultData[objects.length-1].color_shadow_solid[2] = 0.0f;//15f;
			defaultData[objects.length-1].color_shadow_solid[3] = 1.0f;
		}

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		dist = 70;
		rotationOffset = 2*Math.PI/numTrees/2.0;
		
		for(int i = 0; i < numTrees; i ++)
		{
			double theta = 2*Math.PI/numTrees*i+rotationOffset;
			double treeX = dist*Math.cos(theta);
			double treeZ = dist*Math.sin(theta);

			double platformLength = 10;
			double platformWidth = 10;

			double randYOffset = (Math.random()-1.5)*5.0f;
			double platformY = -11.1f+randYOffset;
			
			double platformX = -platformLength/2+treeX;
			double platformZ = -platformWidth/2+treeZ;
			
			loadTree(4,//Slices
					2, //TrunkRadius
					6, //canopyRadius
					10, //trunkHeight
					10, //canopyHeight
					treeX, -6+randYOffset, treeZ); //xyz
	
	
			double rot = Math.random()*Math.PI*2;
			
			defaultData[objects.length-2].rotationQuaternion.loadAxisAngle(0, 1, 0, rot);
			defaultData[objects.length-1].rotationQuaternion.loadAxisAngle(0, 1, 0, rot);
			defaultData[objects.length-2].isDecoration = true;
			defaultData[objects.length-1].isDecoration = true;
			
			platformX = -platformLength/2+treeX;
			platformZ = -platformWidth/2+treeZ;
			
			loadPlatform(platformX, platformY, platformZ, platformLength, platformWidth, platformLength, platformWidth);
			defaultData[objects.length-1].isDecoration = true;
	
			defaultData[objects.length-1].color_lit_solid[0] = 0.35f;
			defaultData[objects.length-1].color_lit_solid[1] = 0.55f;
			defaultData[objects.length-1].color_lit_solid[2] = 0.35f;
			defaultData[objects.length-1].color_lit_solid[3] = 1.0f;
			
			defaultData[objects.length-1].color_shadow_solid[0] = 0.0f;//15f;
			defaultData[objects.length-1].color_shadow_solid[1] = 0.15f;
			defaultData[objects.length-1].color_shadow_solid[2] = 0.0f;//15f;
			defaultData[objects.length-1].color_shadow_solid[3] = 1.0f;
		}
	}


	double function_height = 7.0;
	double function_length = .06;
	
	private void loadFloor() 
	{
		float du = 20.0f;
		float minu = -20.0f;
		float maxu = 20.0f;

		float dv = 20.0f;
		float minv = -20.0f;
		float maxv = 20.0f;
		
		XZEquation equation = new XZEquation(du, dv, minu, maxu, minv, maxv){
			public float getHeight(double x, double z, double u, double v)
			{
				double ramp = 1; 
				if(Math.abs(u) > 12.5 )
					ramp *= 25/Math.abs(u);
				if(Math.abs(v) > 12.5 )
					ramp *= 25/Math.abs(v);
				
				return (float) 0;//(ramp*function_height*Math.cos(function_length*Math.sqrt((x)*(x)+(z)*(z))));
			}
			
			public double[] getNormal(double x, double z)
			{
				double[] normal = new double[3];
				
				normal[0] = function_height*function_length*x*Math.sin(function_length*Math.sqrt(x*x+z*z))/Math.sqrt(x*x+z*z);
				normal[1] = 1;
				normal[2] = function_height*function_length*z*Math.sin(function_length*Math.sqrt(x*x+z*z))/Math.sqrt(x*x+z*z);
				
				Math3D.normalize(normal);
				Math3D.scale(normal, 2.5);
				return normal;
			}

			
			public double xou(double u, double v)
			{
				return u;
			}

			public double zov(double u, double v)
			{
				return v;
			}
			
			public ConvexSurface loadSurface()
			{
				ConvexSurface surface = new ConvexSurface(this);

				surface.faces = new Face[faces.length];
				
				for(int i = 0; i < faces.length; i++)
				{
					surface.faces[i] = new Face(faces[i], faces[i].length/3);
					if(surface.faces[i].normal[1] < 0)
						Math3D.scale(surface.faces[i].normal, -1);
				}
				surface.SURFACE_TYPE = ConvexSurface.FACE;
				
				//surface.breakFaces();
				
				//surface.loadEdgeIndexing();
				
				//surface.loadPointIndexing();
				
				return surface;
			}

			public Face[] getFaces(double[] position, float radius) 
			{
				int numFaces = 0;
				for(int i = 0; i < surface.faces.length; i++)
					if(Math3D.distance(position, surface.faces[i].faceCenterTransformed)<radius+surface.faces[i].radius)
						numFaces++;
				
				Face[] faces = new Face[numFaces];

				numFaces = 0;
				for(int i = 0; i < surface.faces.length; i++)
					if(Math3D.distance(position, surface.faces[i].faceCenterTransformed)<radius+surface.faces[i].radius)
					{
						faces[numFaces] = surface.faces[i];
						numFaces++;
					}
				
				return faces; 
			}
		};
		
		TangibleFace concaveTangible = new TangibleFace(equation);
		
		objects = Math3D.push_back(concaveTangible, objects);
		
		equation = new XZEquation(du, dv, minu, maxu, minv, maxv){
			public float getHeight(double x, double z, double u, double v)
			{
				return (float) (function_height*Math.cos(function_length*Math.sqrt((x)*(x)+(z)*(z))));
			}
			
			public double xou(double u, double v)
			{
				return u;
			}

			public double zov(double u, double v)
			{
				return v;
			}
			
			public double[] getNormal(double x, double z)
			{
				double[] normal = new double[3];
				
				normal[0] = function_height*function_length*x*Math.sin(function_length*Math.sqrt(x*x+z*z))/Math.sqrt(x*x+z*z);
				normal[1] = 1;
				normal[2] = function_height*function_length*z*Math.sin(function_length*Math.sqrt(x*x+z*z))/Math.sqrt(x*x+z*z);
				
				Math3D.normalize(normal);
				Math3D.scale(normal, 2.5);
				return normal;
			}

			public ConvexSurface loadSurface()
			{
				ConvexSurface surface = new ConvexSurface(this);

				surface.faces = new Face[faces.length];
				
				for(int i = 0; i < faces.length; i++)
				{
					surface.faces[i] = new Face(faces[i], faces[i].length/3);
					if(surface.faces[i].normal[1] < 0)
						Math3D.scale(surface.faces[i].normal, -1);
				}
				surface.SURFACE_TYPE = ConvexSurface.FACE;
				
				//surface.breakFaces();
				
				//surface.loadEdgeIndexing();
				
				//surface.loadPointIndexing();
				
				return surface;
			}

			public Face[] getFaces(double[] position, float radius) 
			{
				int numFaces = 0;
				for(int i = 0; i < surface.faces.length; i++)
					if(Math3D.distance(position, surface.faces[i].faceCenterTransformed)<radius+surface.faces[i].radius)
						numFaces++;
				
				Face[] faces = new Face[numFaces];

				numFaces = 0;
				for(int i = 0; i < surface.faces.length; i++)
					if(Math3D.distance(position, surface.faces[i].faceCenterTransformed)<radius+surface.faces[i].radius)
					{
						faces[numFaces] = surface.faces[i];
						numFaces++;
					}
				
				return faces; 
			}
		};

		concaveTangible = new TangibleFace(equation);
		
		concaveTangible.rotationQuaternion.loadAxisAngle(0, 1, 0, 0);
		
		concaveTangible.displacement[0] = 0;
		concaveTangible.displacement[1] = -8;
		concaveTangible.displacement[2] = 0;
			
		concaveTangible.linearVelocity[0] = 0;
		concaveTangible.linearVelocity[1] = 0;
		concaveTangible.linearVelocity[2] = 0;
		
		concaveTangible.color_shadow_highligt[1] = 1.0f;
		
		defaultData = Math3D.push_back(concaveTangible, defaultData);
	}
	
	void loadTree(int slices, double trunkRadius, double canopyRadius, double trunkHeight, double canopyHeight, double x, double y, double z)
	{
		TangibleObject trunk;

		trunk = new TangibleCylinder(slices, trunkRadius, trunkHeight);
		
		trunk.loadPhysicalProperties();
		objects = Math3D.push_back(trunk, objects);

		trunk = new TangibleCylinder(slices, trunkRadius, trunkHeight);

		trunk.rotationQuaternion.loadAxisAngle(0, 1, 0, 0);
		
		trunk.displacement[0] = x;
		trunk.displacement[1] = y;
		trunk.displacement[2] = z;
			
		trunk.linearVelocity[0] = 0;
		trunk.linearVelocity[1] = 0;
		trunk.linearVelocity[2] = 0;
		
		trunk.omega[0] = 0;//rand()%3-1.5;
		trunk.omega[1] = 0;//rand()%3-1.5;
		trunk.omega[2] = 0;//rand()%3-1.5;

		trunk.constantAcceleration[1] = 0.0;
		trunk.infiniteMass = true;

		defaultData = Math3D.push_back(trunk, defaultData);
		
		/////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////

		TangibleObject canopy;
		
		canopy = new TangibleCone(slices, canopyRadius, canopyHeight);
		
		canopy.loadPhysicalProperties();
		objects = Math3D.push_back(canopy, objects);

		canopy = new TangibleCone(slices, canopyRadius, canopyHeight);

		canopy.rotationQuaternion.loadAxisAngle(0, 1, 0, 0);
		
		canopy.displacement[0] = x;
		canopy.displacement[1] = y+trunkHeight/2.0;
		canopy.displacement[2] = z;
			
		canopy.linearVelocity[0] = 0;
		canopy.linearVelocity[1] = 0;
		canopy.linearVelocity[2] = 0;
		
		canopy.omega[0] = 0;//rand()%3-1.5;
		canopy.omega[1] = 0;//rand()%3-1.5;
		canopy.omega[2] = 0;//rand()%3-1.5;

		canopy.constantAcceleration[1] = 0.0;
		canopy.infiniteMass = true;

		defaultData = Math3D.push_back(canopy, defaultData);
	}
	private void loadPlatform(double x, double y, double z, double length, double width, double dl, double dw)
	{
		float du = (float) (dl);
		float minu = 0;
		float maxu = (float) length;

		float dv = (float) (dw);
		float minv = 0;
		float maxv = (float) width;
		
		XZEquation equation = new XZEquation(du, dv, minu, maxu, minv, maxv){
			public float getHeight(double x, double z, double u, double v)
			{
				return (float) 0;//(function_height*Math.cos(function_length*Math.sqrt((x)*(x)+(z)*(z))));
			}
			
			public double[] getNormal(double x, double z)
			{
				double[] normal = new double[3];
				
				normal[0] = function_height*function_length*x*Math.sin(function_length*Math.sqrt(x*x+z*z))/Math.sqrt(x*x+z*z);
				normal[1] = 1;
				normal[2] = function_height*function_length*z*Math.sin(function_length*Math.sqrt(x*x+z*z))/Math.sqrt(x*x+z*z);
				
				Math3D.normalize(normal);
				Math3D.scale(normal, 2.5);
				return normal;
			}

			
			public double xou(double u, double v)
			{
				return u;
			}

			public double zov(double u, double v)
			{
				return v;//+12.5*Math.sin(u*.06);
			}
			
			public ConvexSurface loadSurface()
			{
				ConvexSurface surface = new ConvexSurface(this);

				surface.faces = new Face[faces.length];
				
				for(int i = 0; i < faces.length; i++)
				{
					surface.faces[i] = new Face(faces[i], faces[i].length/3);
					if(surface.faces[i].normal[1] < 0)
						Math3D.scale(surface.faces[i].normal, -1);
				}
				surface.SURFACE_TYPE = ConvexSurface.FACE;
				
				//surface.breakFaces();
				
				//surface.loadEdgeIndexing();
				
				//surface.loadPointIndexing();
				
				return surface;
			}

			public Face[] getFaces(double[] position, float radius) 
			{
				int numFaces = 0;
				for(int i = 0; i < surface.faces.length; i++)
					if(Math3D.distance(position, surface.faces[i].faceCenterTransformed)<radius+surface.faces[i].radius)
						numFaces++;
				
				Face[] faces = new Face[numFaces];

				numFaces = 0;
				for(int i = 0; i < surface.faces.length; i++)
					if(Math3D.distance(position, surface.faces[i].faceCenterTransformed)<radius+surface.faces[i].radius)
					{
						faces[numFaces] = surface.faces[i];
						numFaces++;
					}
				
				return faces; 
			}
		};
		
		TangibleFace concaveTangible = new TangibleFace(equation);
		
		objects = Math3D.push_back(concaveTangible, objects);
		
		equation = new XZEquation(du, dv, minu, maxu, minv, maxv){
			public float getHeight(double x, double z, double u, double v)
			{
				return 0;//(float) (function_height*Math.cos(function_length*Math.sqrt((x)*(x)+(z)*(z))));
			}
			
			public double xou(double u, double v)
			{
				return u;
			}

			public double zov(double u, double v)
			{
				return v;
			}
			
			public double[] getNormal(double x, double z)
			{
				double[] normal = new double[3];
				
				normal[0] = function_height*function_length*x*Math.sin(function_length*Math.sqrt(x*x+z*z))/Math.sqrt(x*x+z*z);
				normal[1] = 1;
				normal[2] = function_height*function_length*z*Math.sin(function_length*Math.sqrt(x*x+z*z))/Math.sqrt(x*x+z*z);
				
				Math3D.normalize(normal);
				Math3D.scale(normal, 2.5);
				return normal;
			}

			public ConvexSurface loadSurface()
			{
				ConvexSurface surface = new ConvexSurface(this);

				surface.faces = new Face[faces.length];
				
				for(int i = 0; i < faces.length; i++)
				{
					surface.faces[i] = new Face(faces[i], faces[i].length/3);
					if(surface.faces[i].normal[1] < 0)
						Math3D.scale(surface.faces[i].normal, -1);
				}
				surface.SURFACE_TYPE = ConvexSurface.FACE;
				
				//surface.breakFaces();
				
				//surface.loadEdgeIndexing();
				
				//surface.loadPointIndexing();
				
				return surface;
			}

			public Face[] getFaces(double[] position, float radius) 
			{
				int numFaces = 0;
				for(int i = 0; i < surface.faces.length; i++)
					if(Math3D.distance(position, surface.faces[i].faceCenterTransformed)<radius+surface.faces[i].radius)
						numFaces++;
				
				Face[] faces = new Face[numFaces];

				numFaces = 0;
				for(int i = 0; i < surface.faces.length; i++)
					if(Math3D.distance(position, surface.faces[i].faceCenterTransformed)<radius+surface.faces[i].radius)
					{
						faces[numFaces] = surface.faces[i];
						numFaces++;
					}
				
				return faces; 
			}
		};

		concaveTangible.color_shadow_highligt[0] = 0;
		concaveTangible.color_shadow_highligt[1] = 1f;
		concaveTangible.color_shadow_highligt[2] = 0;
		
		concaveTangible.color_lit_highligt[0] = 0;
		concaveTangible.color_lit_highligt[1] = 0;
		concaveTangible.color_lit_highligt[2] = 0;

		concaveTangible.infiniteMass = true;
		
		concaveTangible = new TangibleFace(equation);

		concaveTangible.infiniteMass = true;
		
		concaveTangible.rotationQuaternion.loadAxisAngle(0, 1, 0, 0);
		
		concaveTangible.displacement[0] = x;
		concaveTangible.displacement[1] = y;
		concaveTangible.displacement[2] = z;
			
		concaveTangible.linearVelocity[0] = 0;
		concaveTangible.linearVelocity[1] = 0;
		concaveTangible.linearVelocity[2] = 0;

		concaveTangible.color_shadow_highligt[0] = 0;
		concaveTangible.color_shadow_highligt[1] = 1;
		concaveTangible.color_shadow_highligt[2] = 0;
		
		concaveTangible.color_lit_highligt[0] = 0;
		concaveTangible.color_lit_highligt[1] = 0;
		concaveTangible.color_lit_highligt[2] = 0;
		
		defaultData = Math3D.push_back(concaveTangible, defaultData);
	}
	
	public void loadDemon()
	{
		float width = 10f;
		float height = 10f;
		
		Demon demon = new Demon(width, height, renderer);
		
		objects = Math3D.push_back(demon, objects);

		demon = new Demon(20, 20, renderer);

//		demon.infiniteMass = true;

		demon.rotationQuaternion.loadAxisAngle(0, 1, 0, 0);
		
		demon.displacement[0] = 0;
		demon.displacement[1] = 0;
		demon.displacement[2] = 5;
			
		demon.linearVelocity[0] = 0;
		demon.linearVelocity[1] = 0;
		demon.linearVelocity[2] = -.1;
		
		demon.constantAcceleration[1] = 0;
		
		defaultData = Math3D.push_back(demon, defaultData);

//		TangibleCube cube = new TangibleCube((int) (width/3), (int)(height/1.75), 1);
//		
//		objects = Math3D.push_back(cube, objects);
//
//		cube = new TangibleCube(10, 10, 10);
//
//		cube.rotationQuaternion.loadAxisAngle(0, 1, 0, 0);
//
//		demon.displacement[0] = 0;
//		demon.displacement[1] = 0;
//		demon.displacement[2] = 5;
//			
//		demon.linearVelocity[0] = 0;
//		demon.linearVelocity[1] = 0;
//		demon.linearVelocity[2] = -.1;
//		
//		cube.constantAcceleration[1] = 0;
//		
//		cube.infiniteMass = true;
//		cube.isDecoration = false;
//		
//		defaultData = Math3D.push_back(cube, defaultData);
	}
	
	public void resetSimulation()
	{
		super.resetSimulation();
		startTime = System.nanoTime();
	}
	
	public int getPlayerIndex() 
	{
		return playerIndex;
	}
}
