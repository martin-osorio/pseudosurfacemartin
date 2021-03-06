package com.pseudosurface.levels.level5;

import android.opengl.GLES20;

import com.pseudosurface.levels.level4.Shader4;
import com.pseudosurface.levels.template.Renderer;
import com.pseudosurface.levels.template.World;
import com.pseudosurface.physics.ConvexSurface;
import com.pseudosurface.physics.Face;
import com.pseudosurface.physics.Math3D;
import com.pseudosurface.physics.TangibleFace;
import com.pseudosurface.physics.TangibleSphere;
import com.pseudosurface.physics.XZEquation;

public class World5 extends World
{
	public int platform1;
	public int platform2;
	public int platform3;
	public int platform4;
	public int platform5;
	public int platform6;
	public int platform7;
	public int platform8;

	public World5(Renderer renderer) 
	{
		super(renderer);
	}
	
	public void loadSimulation() 
	{
		loadFloor();
		
		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////

		platform1 = objects.length;
		loadPlatform(11+22*0, -33, 22, 22);
		defaultData[objects.length-1].linearVelocity[2] = 5.0;

		platform2 = objects.length;
		loadPlatform(11+22*1, 11, 22, 22);
		defaultData[objects.length-1].linearVelocity[2] = -5.0;

		platform3 = objects.length;
		loadPlatform(11+22*2, -33, 22, 22);
		defaultData[objects.length-1].linearVelocity[2] = 5.0;

		platform4 = objects.length;
		loadPlatform(11+22*3, 11, 22, 22);
		defaultData[objects.length-1].linearVelocity[2] = -5.0;

		platform5 = objects.length;
		loadPlatform(11+22*4, -33, 22, 22);
		defaultData[objects.length-1].linearVelocity[2] = 5.0;

		platform6 = objects.length;
		loadPlatform(11+22*5, 11, 22, 22);
		defaultData[objects.length-1].linearVelocity[2] = -5.0;

		platform7 = objects.length;
		loadPlatform(11+22*6, -33, 22, 22);
		defaultData[objects.length-1].linearVelocity[2] = 5.0;

		platform8 = objects.length;
		loadPlatform(11+22*7, 11, 22, 22);
		defaultData[objects.length-1].linearVelocity[2] = -5.0;

		
		//Destination platform:
		loadPlatform(11+22*8, -11, 22, 22);


		defaultData[objects.length-1].color_shadow_highligt[0] = 0;
		defaultData[objects.length-1].color_shadow_highligt[1] = 0f;
		defaultData[objects.length-1].color_shadow_highligt[2] = 1;
		
		defaultData[objects.length-1].color_lit_highligt[0] = 0;
		defaultData[objects.length-1].color_lit_highligt[1] = 0;
		defaultData[objects.length-1].color_lit_highligt[2] = 1;


		defaultData[objects.length-1].color_lit_solid[0] = .35f;
		defaultData[objects.length-1].color_lit_solid[1] = .35f;
		defaultData[objects.length-1].color_lit_solid[2] = .55f;

		defaultData[objects.length-1].color_shadow_solid[0] = 0f;
		defaultData[objects.length-1].color_shadow_solid[1] = 0f;
		defaultData[objects.length-1].color_shadow_solid[2] = .15f;
		
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

		
		
		
		resetSimulation();
	}


	double function_height = 4.0;
	double function_length = .06;
	
	private void loadFloor() 
	{
		float du = 22.0f/2.0f;
		float minu = -11.0f;
		float maxu = 11.0f;

		float dv = 22.0f/2.0f;
		float minv = -11.0f;
		float maxv = 11.0f;
		
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
		
		concaveTangible.displacement[0] = -.1;
		concaveTangible.displacement[1] = -8;
		concaveTangible.displacement[2] = 0;
		
		defaultData = Math3D.push_back(concaveTangible, defaultData);

		defaultData[objects.length-1].color_shadow_highligt[0] = 0;
		defaultData[objects.length-1].color_shadow_highligt[1] = 0f;
		defaultData[objects.length-1].color_shadow_highligt[2] = 1;
		
		defaultData[objects.length-1].color_lit_highligt[0] = 0;
		defaultData[objects.length-1].color_lit_highligt[1] = 0;
		defaultData[objects.length-1].color_lit_highligt[2] = 1;


		defaultData[objects.length-1].color_lit_solid[0] = .35f;
		defaultData[objects.length-1].color_lit_solid[1] = .35f;
		defaultData[objects.length-1].color_lit_solid[2] = .55f;

		defaultData[objects.length-1].color_shadow_solid[0] = 0f;
		defaultData[objects.length-1].color_shadow_solid[1] = 0f;
		defaultData[objects.length-1].color_shadow_solid[2] = .15f;
	}

	private void loadPlatform(double x, double z, double length, double width)
	{
		float du = (float) (length/2.0f);
		float minu = 0;
		float maxu = (float) length;

		float dv = (float) (width/2.0f);
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
		concaveTangible.displacement[1] = -8;
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


	public int getPlayerIndex() 
	{
		return objects.length-1;
	}
}
