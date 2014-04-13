package com.pseudosurface.levels.level8;

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

public class World8 extends World
{
	public World8(Renderer renderer) 
	{
		super(renderer);
	}


	long startTime = System.nanoTime();

	public double platformSpeed = 7.5f;
	public int movingPlatformIndex1 = -1;
	
	public void loadSimulation() 
	{
		maxDrawDistance = 120;
		
		loadRoad();
		
		loadFloor();
		
		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		loadTrees();
		
		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		loadMovingPlatforms();

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		loadVictoryPlatform();
		
		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		loadPlayer();
		
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

	private void loadPlayer() 
	{
		TangibleSphere sphere = new TangibleSphere(10, 10, 2.5f);
		
		sphere.density = .01;
		
		sphere.loadPhysicalProperties();
		objects = Math3D.push_back(sphere, objects);
		
		sphere = new TangibleSphere(10, 10, 2.5f);

		sphere.rotationQuaternion.loadAxisAngle(0, 1, 0, 0);
		
		sphere.infiniteMass = false;

		
//		float u = -7.5f;
//		float v = (float) (2*Math.PI*3/function_length);
//		
//		double platformLength = 15;
//		double platformWidth = 15;
//
//		double platformX = u-25*Math.sin(function_length*Math.sqrt((v)*(v))) - platformLength/2-1;
//		double platformZ = (2*Math.PI*3/function_length)+60-.75+60+55+15+75-15;
		
//		sphere.displacement[0] = platformX + platformLength/2;
//		sphere.displacement[1] = 6;
//		sphere.displacement[2] = platformZ + platformWidth/2;

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
	}

	private void loadMovingPlatforms() 
	{
		movingPlatformIndex1 = objects.length;

		float platformLength = 15;
		float platformWidth = 15;

		float u = -7.5f;
		float v = (float) (2*Math.PI*3/function_length);
		
		double platformX = u-25*Math.sin(function_length*Math.sqrt((v)*(v))) - platformLength/2-1;
		double platformZ = v+60-.75+60;
		
		loadPlatform(platformX, -15+.25, platformZ, platformLength, platformWidth, platformLength, platformWidth/1);
		
		defaultData[defaultData.length-1].linearVelocity[2] = platformSpeed;
	}
	
	public int victoryPlatformIndex;
	
	private void loadVictoryPlatform()
	{
		victoryPlatformIndex = objects.length;
		
		float u = -7.5f;
		float v = (float) (2*Math.PI*3/function_length);
		
		double platformLength = 15;
		double platformWidth = 15;

		double platformX = u-25*Math.sin(function_length*Math.sqrt((v)*(v))) - platformLength/2-1;
		double platformZ = (2*Math.PI*3/function_length)+60-.75+60+55+15+75;
		
		loadPlatform(platformX, -15+.25, platformZ, platformLength, platformWidth, platformLength, platformWidth);
		
		new Thread(new Runnable(){

			public void run() 
			{
				try
				{
					while(true)
					{
						double time = System.nanoTime()/1E9*Math.PI;

						float r = (float)(.5+.5*Math.sin(time));
						float g = (float)(.5+.5*Math.sin(time+4.0*Math.PI/3.0));
						float b = (float)(.5+.5*Math.sin(time+2.0*Math.PI/3.0));
						
						objects[victoryPlatformIndex].color_lit_solid[0] = r*.55f;
						objects[victoryPlatformIndex].color_lit_solid[1] = g*.55f;
						objects[victoryPlatformIndex].color_lit_solid[2] = b*.55f;

						objects[victoryPlatformIndex].color_shadow_solid[0] = r*.35f;
						objects[victoryPlatformIndex].color_shadow_solid[1] = g*.35f;
						objects[victoryPlatformIndex].color_shadow_solid[2] = b*.35f;

						r = (float)(.5+.5*Math.sin(-time));
						g = (float)(.5+.5*Math.sin(-time+4.0*Math.PI/3.0));
						b = (float)(.5+.5*Math.sin(-time+2.0*Math.PI/3.0));
						
						objects[victoryPlatformIndex].color_lit_highligt[0] = r;
						objects[victoryPlatformIndex].color_lit_highligt[1] = g;
						objects[victoryPlatformIndex].color_lit_highligt[2] = b;

						objects[victoryPlatformIndex].color_shadow_highligt[0] = r;
						objects[victoryPlatformIndex].color_shadow_highligt[1] = g;
						objects[victoryPlatformIndex].color_shadow_highligt[2] = b;
						
						Thread.sleep(50);
					}
				}
				catch(Exception e)
				{
					
				}
			}
			
		}).start();
	}

	private void loadRoad() {

		double platformLength;
		double platformWidth;
		double platformX;
		double platformZ;
		
		int startIndex = objects.length;
		
		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		platformLength = 15;
		platformWidth = 60;
		
		platformX = -platformLength/2;
		platformZ = -5-.1;
		
		loadPlatform(platformX, -15, platformZ, platformLength, platformWidth, platformLength, platformWidth/6);
		
		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		platformLength = 15;
		platformWidth = 60;

		float u = -7.5f;
		float v = (float) (2*Math.PI*3/function_length);
		
		platformX = u-25*Math.sin(function_length*Math.sqrt((v)*(v))) - platformLength/2-1;
		platformZ = v+60-.75;
		
		loadPlatform(platformX, -15+.25, platformZ, platformLength, platformWidth, platformLength, platformWidth/6);

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		platformLength = 15;
		platformWidth = 60;

		platformX = u-25*Math.sin(function_length*Math.sqrt((v)*(v))) - platformLength/2-1;
		platformZ = v+60-.75;
		
		loadPlatform(platformX, -15+.25, platformZ, platformLength, platformWidth, platformLength, platformWidth/6);

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		platformLength = 15;
		platformWidth = 60;

		platformX = u-25*Math.sin(function_length*Math.sqrt((v)*(v))) - platformLength/2-1;
		platformZ = (2*Math.PI*3/function_length)+60-.75+60+55+15;
		
		loadPlatform(platformX, -15+.25, platformZ, platformLength, platformWidth, platformLength, platformWidth/4);
		
		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
//		platformLength = 20;
//		platformWidth = 10;
//		
//		platformX = -5-20;
//		platformZ = -5+10;
//		
//		loadPlatform(platformX, -15, platformZ, platformLength, platformWidth, platformLength/2, platformWidth);
//
//		//////////////////////////////////
//		//////////////////////////////////
//		//////////////////////////////////
//		
//		platformLength = 10;
//		platformWidth = 30;
//		
//		platformX = -platformLength/2-20;
//		platformZ = -5-.1-20;
//		
//		loadPlatform(platformX, -15, platformZ, platformLength, platformWidth, platformLength, platformWidth/3);
//
//		//////////////////////////////////
//		//////////////////////////////////
//		//////////////////////////////////
//		
//		platformLength = 40;
//		platformWidth = 10;
//		
//		platformX = -5-10;
//		platformZ = -5-.1-20;
//		
//		loadPlatform(platformX, -15, platformZ, platformLength, platformWidth, platformLength/4, platformWidth);
//
//		//////////////////////////////////
//		//////////////////////////////////
//		//////////////////////////////////
//		
//		platformLength = 10;
//		platformWidth = 50;
//		
//		platformX = -platformLength/2+20;
//		platformZ = -5-.1-10;
//		
//		loadPlatform(platformX, -15, platformZ, platformLength, platformWidth, platformLength, platformWidth/5);
//		
//		//////////////////////////////////
//		//////////////////////////////////
//		//////////////////////////////////
//
//		platformLength = 60;
//		platformWidth = 10;
//		
//		platformX = -5-40;
//		platformZ = -5+30;
//		
//		loadPlatform(platformX, -15, platformZ, platformLength, platformWidth, platformLength/6, platformWidth);
//
//		//////////////////////////////////
//		//////////////////////////////////
//		//////////////////////////////////
//		
//		platformLength = 10;
//		platformWidth = 70;
//		
//		platformX = -platformLength/2-40;
//		platformZ = -5-.1-40;
//		
//		loadPlatform(platformX, -15, platformZ, platformLength, platformWidth, platformLength, platformWidth/7);
//
//		//////////////////////////////////
//		//////////////////////////////////
//		//////////////////////////////////
//
//
//		platformLength = 80;
//		platformWidth = 10;
//		
//		platformX = -5-30;
//		platformZ = -5-40;
//		
//		loadPlatform(platformX, -15, platformZ, platformLength, platformWidth, platformLength/8, platformWidth);
//		
//		//////////////////////////////////
//		//////////////////////////////////
//		//////////////////////////////////
//
//		platformLength = 10;
//		platformWidth = 90;
//		
//		platformX = -5+40;
//		platformZ = -5-30;
//		
//		loadPlatform(platformX, -15, platformZ, platformLength, platformWidth, platformLength, platformWidth/9);
//		
//		//////////////////////////////////
//		//////////////////////////////////
//		//////////////////////////////////
//
//		platformLength = 40;
//		platformWidth = 10;
//		
//		platformX = -5;
//		platformZ = 45;
//		
//		loadPlatform(platformX, -15, platformZ, platformLength, platformWidth, platformLength/4, platformWidth);

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		for(int i = startIndex; i < objects.length; i++)
		{
			defaultData[i].color_shadow_highligt[0] = 1;
			defaultData[i].color_shadow_highligt[1] = 1f;
			defaultData[i].color_shadow_highligt[2] = 1;
			
			defaultData[i].color_lit_highligt[0] = 1;
			defaultData[i].color_lit_highligt[1] = 1;
			defaultData[i].color_lit_highligt[2] = 1;


			defaultData[i].color_lit_solid[0] = .35f;
			defaultData[i].color_lit_solid[1] = .35f;
			defaultData[i].color_lit_solid[2] = .35f;
			
			defaultData[i].color_shadow_solid[0] = .15f;
			defaultData[i].color_shadow_solid[1] = .15f;
			defaultData[i].color_shadow_solid[2] = .15f;
			
			Math3D.scale(defaultData[i].color_shadow_solid, 1.5f);
			Math3D.scale(defaultData[i].color_lit_solid, 1.5f);
		}
	}


	private void loadTrees() 
	{
		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		int numTrees = 8;

		for(int i = 0; i < numTrees; i ++)
		{
			double treeX = -50;
			double treeZ = i*75;
			
			double platformLength = 10;
			double platformWidth = 10;

			double randYOffset = -4.9;//(Math.random()-1.5)*5.0f;
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
		}

		for(int i = 0; i < numTrees; i ++)
		{
			double treeX = 50;
			double treeZ = i*75;
			
			double platformLength = 10;
			double platformWidth = 10;

			double randYOffset = -4.9;//(Math.random()-1.5)*5.0f;
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
		}
	}


	double function_height = 4.0;
	double function_length = .08;
	
	class Curve extends XZEquation{
		public Curve(float du, float dv, float minu, float maxu, float minv, float maxv)
		{
			super(du, dv, minu, maxu, minv, maxv);
		}

		public float getHeight(double x, double z, double u, double v)
		{
			double ramp = 1; 
			if(Math.abs(u) > 12.5 )
				ramp *= 25/Math.abs(u);
			if(Math.abs(v) > 12.5 )
				ramp *= 25/Math.abs(v);
			
			return (float) (-function_height*Math.cos(function_length*Math.sqrt((z)*(z))));
		}
		
		public double[] getNormal(double x, double z)
		{
			return null;
		}

		
		public double xou(double u, double v)
		{
			return u-25*Math.sin(function_length*Math.sqrt((v)*(v)));
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
	}
	
	private void loadFloor() 
	{
		float du = 15f;
		float minu = -7.5f;
		float maxu = 7.5f;

		float dv = 5.0f;
		float minv = 0.0f;
		float maxv = (float) (2*Math.PI*3/function_length);
		
		TangibleFace concaveTangible = new TangibleFace(new Curve(du, dv, minu, maxu, minv, maxv));
		
		objects = Math3D.push_back(concaveTangible, objects);
		
		concaveTangible = new TangibleFace(new Curve(du, dv, minu, maxu, minv, maxv));
		
		concaveTangible.rotationQuaternion.loadAxisAngle(0, 1, 0, 0);
		
		concaveTangible.cullFaces = false;
		
		concaveTangible.displacement[0] = 0;
		concaveTangible.displacement[1] = -15+function_height;
		concaveTangible.displacement[2] = 55;
			
		concaveTangible.linearVelocity[0] = 0;
		concaveTangible.linearVelocity[1] = 0;
		concaveTangible.linearVelocity[2] = 0;
		
		concaveTangible.color_shadow_highligt[1] = 1.0f;
		
		defaultData = Math3D.push_back(concaveTangible, defaultData);

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		for(int i = objects.length-1; i < objects.length; i++)
		{
			defaultData[i].color_shadow_highligt[0] = 1;
			defaultData[i].color_shadow_highligt[1] = 1f;
			defaultData[i].color_shadow_highligt[2] = 1;
			
			defaultData[i].color_lit_highligt[0] = 1;
			defaultData[i].color_lit_highligt[1] = 1;
			defaultData[i].color_lit_highligt[2] = 1;


			defaultData[i].color_lit_solid[0] = .35f;
			defaultData[i].color_lit_solid[1] = .35f;
			defaultData[i].color_lit_solid[2] = .35f;
			
			defaultData[i].color_shadow_solid[0] = .15f;
			defaultData[i].color_shadow_solid[1] = .15f;
			defaultData[i].color_shadow_solid[2] = .15f;
			
			Math3D.scale(defaultData[i].color_shadow_solid, 1.5f);
			Math3D.scale(defaultData[i].color_lit_solid, 1.5f);
		}
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
		canopy.displacement[1] = y+2*trunkHeight/3;
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
		
		concaveTangible.color_shadow_highligt[0] = 0;
		concaveTangible.color_shadow_highligt[1] = 1f;
		concaveTangible.color_shadow_highligt[2] = 0;
		
		concaveTangible.color_lit_highligt[0] = 0;
		concaveTangible.color_lit_highligt[1] = 0;
		concaveTangible.color_lit_highligt[2] = 0;

		concaveTangible.infiniteMass = true;
		
		concaveTangible = new TangibleFace(new Curve(du, dv, minu, maxu, minv, maxv));

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
