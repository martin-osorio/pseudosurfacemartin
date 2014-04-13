package com.pseudosurface.levels.level2;

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
import com.pseudosurface.physics.TangibleTree;
import com.pseudosurface.physics.XZEquation;

public class World2 extends World
{
	public World2(Renderer renderer) 
	{
		super(renderer);
	}


	public static int fallAwayPlatformsStartIndex;
	public static int numFallawayPlatforms;

	public static int numMovingPlatforms;
	public static int movingPlatformsStartIndex;

	public void loadSimulation() 
	{
//		loadFloor();

//		loadPlatform(-200, -25, -200, 400, 400, 40, 40);
//		defaultData[objects.length-1].isDecoration = true;
		maxDrawDistance = 50.5;
		numFallawayPlatforms = 13;
		fallAwayPlatformsStartIndex = objects.length;
		
		int i = 0;
		loadPlatform(45, -15, 65+i*10, 10, 10, 10, 10);
		i++;
		loadPlatform(45, -15, 65+i*10, 10, 10, 10, 10);
		i++;
		loadPlatform(45, -15, 65+i*10, 10, 10, 10, 10);
		i++;

		i--;
		loadPlatform(45-10, -15, 65+i*10, 10, 10, 10, 10);
		i++;
		loadPlatform(45-10, -15, 65+i*10, 10, 10, 10, 10);
		i++;
		loadPlatform(45-10, -15, 65+i*10, 10, 10, 10, 10);
		i++;

		i--;
		loadPlatform(45-20, -15, 65+i*10, 10, 10, 10, 10);
		loadPlatform(45-30, -15, 65+i*10, 10, 10, 10, 10);
		loadPlatform(45-40, -15, 65+i*10, 10, 10, 10, 10);

		
		i = 0;
		loadPlatform(5+i*10, -15, 155, 10, 10, 10, 10);
		i++;
		loadPlatform(5+i*10, -15, 155, 10, 10, 10, 10);
		i++;
		loadPlatform(5+i*10, -15, 155, 10, 10, 10, 10);
		
		loadPlatform(-5, -15, 215, 10, 10, 10, 10);
		
		numMovingPlatforms = 2;
		movingPlatformsStartIndex = objects.length;
		
		for(i = 0; i < numMovingPlatforms; i++)
		{
			//Moving platform locations/velocities are updated in the SimulatorActivity subclass for this level.
			loadPlatform(5+3*10, -15, 155,//None of the values on this line matter.. See above comment. 
						10, 10, 10, 10);
			
			defaultData[movingPlatformsStartIndex+i].color_lit_solid[0] = .55f;
			defaultData[movingPlatformsStartIndex+i].color_lit_solid[1] = .55f;
			defaultData[movingPlatformsStartIndex+i].color_lit_solid[2] = .35f;
	
			defaultData[movingPlatformsStartIndex+i].color_shadow_solid[0] = .15f;
			defaultData[movingPlatformsStartIndex+i].color_shadow_solid[1] = .15f;
			defaultData[movingPlatformsStartIndex+i].color_shadow_solid[2] = 0;
	
			defaultData[movingPlatformsStartIndex+i].color_lit_highligt[0] = 1;
			defaultData[movingPlatformsStartIndex+i].color_lit_highligt[1] = 1;
			defaultData[movingPlatformsStartIndex+i].color_lit_highligt[2] = 0;
	
			defaultData[movingPlatformsStartIndex+i].color_shadow_highligt[0] = 1;
			defaultData[movingPlatformsStartIndex+i].color_shadow_highligt[1] = 1;
			defaultData[movingPlatformsStartIndex+i].color_shadow_highligt[2] = 0;
		}
		
		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		loadRoad();
		
		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		loadTrees();
		
		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		loadPlayer();
		
		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
//		loadCastle();
		
		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		loadDemon();
		
		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		resetSimulation();
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
		demon.displacement[1] = -15+height/2;
		demon.displacement[2] = 330;
			
		demon.linearVelocity[0] = 0;
		demon.linearVelocity[1] = 0;
		demon.linearVelocity[2] = -10.0;
		
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


	private void loadCastle() 
	{
		double castleWidth = 15;
		double castleHeight = 15/3.0;
		double castleLength = 15;

		double castleX = -7.5+castleWidth/2;
		double castleY = -15+castleHeight/2;
		double castleZ = 325+castleLength/2+20;
		
		TangibleCube cube;


		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////

//		loadPlatform(castleX-castleWidth*2, castleY-castleHeight/2*1.01, castleZ-castleLength*2, castleWidth*4, castleLength*4, castleWidth*4, castleLength*4);
//
//		defaultData[objects.length-1].color_shadow_highligt[0] = 0f;
//		defaultData[objects.length-1].color_shadow_highligt[1] = 0;
//		defaultData[objects.length-1].color_lit_highligt[2] = 1;
//		defaultData[objects.length-1].color_lit_highligt[3] = 1;
//		
//		defaultData[objects.length-1].color_shadow_highligt[0] = 0f;
//		defaultData[objects.length-1].color_shadow_highligt[1] = 0f;
//		defaultData[objects.length-1].color_shadow_highligt[2] = 1;
//		defaultData[objects.length-1].color_shadow_highligt[3] = 1;
//
//		defaultData[objects.length-1].color_lit_solid[0] = .35f;
//		defaultData[objects.length-1].color_lit_solid[1] = .35f;
//		defaultData[objects.length-1].color_lit_solid[2] = .55f;
//		defaultData[objects.length-1].color_lit_solid[3] = 1f;
//
//		defaultData[objects.length-1].color_shadow_solid[0] = 0f;
//		defaultData[objects.length-1].color_shadow_solid[1] = 0f;
//		defaultData[objects.length-1].color_shadow_solid[2] = 0.15f;
//		defaultData[objects.length-1].color_shadow_solid[3] = 1.0f;
//
//		Math3D.scale(defaultData[objects.length-1].color_shadow_solid, 1.85f);
//		Math3D.scale(defaultData[objects.length-1].color_lit_solid, 1.85f);
//
//		defaultData[objects.length-1].isDecoration = true;
		
		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		int startIndex = objects.length;
		
		loadPlatform(castleX-castleWidth/2, castleY-castleHeight/2-.001, castleZ-castleLength/2, castleWidth, castleLength, castleWidth, castleLength);
		
		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		double towerRatio = 1.75;
		double cubeSizeX = castleWidth/3.9;
		double cubeSizeY = castleHeight*towerRatio;
		double cubeSizeZ = castleLength/3.9;
		
		double cubeX = castleX + castleWidth/2;
		double cubeY = castleY-castleHeight/2+cubeSizeY/2.0;
		double cubeZ = castleZ-castleLength/2;
		
		cube = new TangibleCube((int)cubeSizeX,(int)cubeSizeY, (int)cubeSizeZ);

		cube.loadPhysicalProperties();
		objects = Math3D.push_back(cube, objects);

		cube = new TangibleCube((int)castleWidth,(int)castleHeight, (int)castleLength);

		cube.rotationQuaternion.loadAxisAngle(0, 1, 0, 0);

		cube.displacement[0] = cubeX;
		cube.displacement[1] = cubeY;
		cube.displacement[2] = cubeZ;

		cube.infiniteMass = true;
		cube.loadPhysicalProperties();
//		cube.isDecoration = true;
		
		defaultData = Math3D.push_back(cube, defaultData);

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////

		double coneSizeX = castleWidth/3.9/2;
		double coneSizeY = castleHeight/towerRatio*2.0;
		double coneSizeZ = castleLength/3.9/2;
		
		double coneX = cubeX;
		double coneY = Double.NaN; //Cannot be evaluated until the cone is resituated around it's Center of Mass...
		double coneZ = cubeZ;
		
		TangibleCone cone = new TangibleCone(4,(int)Math.sqrt(coneSizeX*coneSizeX+coneSizeZ*coneSizeZ), (int)coneSizeY);

//		cone.loadPhysicalProperties();
		objects = Math3D.push_back(cone, objects);

		cone = new TangibleCone(4,(int)Math.sqrt(coneSizeX*coneSizeX+coneSizeZ*coneSizeZ), (int)coneSizeY);

		cone.loadPhysicalProperties();
		
		cone.rotationQuaternion.loadAxisAngle(0, 1, 0, Math.PI/4);
		
		coneY = castleY-castleHeight/2+castleHeight*towerRatio+coneSizeY/2.0-cone.surfaces[0].relativePosition[1]/2-.01;
		
		cone.displacement[0] = coneX;
		cone.displacement[1] = coneY;
		cone.displacement[2] = coneZ;

		cone.infiniteMass = true;
		cone.loadPhysicalProperties();
		cone.isDecoration = true;
		
		defaultData = Math3D.push_back(cone, defaultData);
		
		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		cubeX = castleX - castleWidth/2;
		cubeY = castleY-castleHeight/2+cubeSizeY/2.0;
		cubeZ = castleZ-castleLength/2;
		
		cube = new TangibleCube((int)cubeSizeX,(int)cubeSizeY, (int)cubeSizeZ);

		cube.loadPhysicalProperties();
		objects = Math3D.push_back(cube, objects);

		cube = new TangibleCube((int)castleWidth,(int)castleHeight, (int)castleLength);

		cube.rotationQuaternion.loadAxisAngle(0, 1, 0, 0);

		cube.displacement[0] = cubeX;
		cube.displacement[1] = cubeY;
		cube.displacement[2] = cubeZ;

		cube.infiniteMass = true;
		cube.loadPhysicalProperties();
//		cube.isDecoration = true;
		
		defaultData = Math3D.push_back(cube, defaultData);


		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////

		coneX = cubeX;
		coneZ = cubeZ;
		
		cone = new TangibleCone(4,(int)Math.sqrt(coneSizeX*coneSizeX+coneSizeZ*coneSizeZ), (int)coneSizeY);

//		cone.loadPhysicalProperties();
		objects = Math3D.push_back(cone, objects);

		cone = new TangibleCone(4,(int)Math.sqrt(coneSizeX*coneSizeX+coneSizeZ*coneSizeZ), (int)coneSizeY);

		cone.loadPhysicalProperties();
		
		cone.rotationQuaternion.loadAxisAngle(0, 1, 0, Math.PI/4);
		
		coneY = castleY-castleHeight/2+castleHeight*towerRatio+coneSizeY/2.0-cone.surfaces[0].relativePosition[1]/2-.01;
		
		cone.displacement[0] = coneX;
		cone.displacement[1] = coneY;
		cone.displacement[2] = coneZ;

		cone.infiniteMass = true;
		cone.loadPhysicalProperties();
		cone.isDecoration = true;
		
		defaultData = Math3D.push_back(cone, defaultData);
		

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		cubeX = castleX - castleWidth/2;
		cubeY = castleY-castleHeight/2+cubeSizeY/2.0;
		cubeZ = castleZ+castleLength/2;
		
		cube = new TangibleCube((int)cubeSizeX,(int)cubeSizeY, (int)cubeSizeZ);

		cube.loadPhysicalProperties();
		objects = Math3D.push_back(cube, objects);

		cube = new TangibleCube((int)castleWidth,(int)castleHeight, (int)castleLength);

		cube.rotationQuaternion.loadAxisAngle(0, 1, 0, 0);

		cube.displacement[0] = cubeX;
		cube.displacement[1] = cubeY;
		cube.displacement[2] = cubeZ;

		cube.infiniteMass = true;
		cube.loadPhysicalProperties();
//		cube.isDecoration = true;
		
		defaultData = Math3D.push_back(cube, defaultData);

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////

		coneX = cubeX;
		coneZ = cubeZ;
		
		cone = new TangibleCone(4,(int)Math.sqrt(coneSizeX*coneSizeX+coneSizeZ*coneSizeZ), (int)coneSizeY);

//		cone.loadPhysicalProperties();
		objects = Math3D.push_back(cone, objects);

		cone = new TangibleCone(4,(int)Math.sqrt(coneSizeX*coneSizeX+coneSizeZ*coneSizeZ), (int)coneSizeY);

		cone.loadPhysicalProperties();
		
		cone.rotationQuaternion.loadAxisAngle(0, 1, 0, Math.PI/4);
		
		coneY = castleY-castleHeight/2+castleHeight*towerRatio+coneSizeY/2.0-cone.surfaces[0].relativePosition[1]/2-.01;
		
		cone.displacement[0] = coneX;
		cone.displacement[1] = coneY;
		cone.displacement[2] = coneZ;

		cone.infiniteMass = true;
		cone.loadPhysicalProperties();
		cone.isDecoration = true;
		
		defaultData = Math3D.push_back(cone, defaultData);

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		cubeX = castleX + castleWidth/2;
		cubeY = castleY-castleHeight/2+cubeSizeY/2.0;
		cubeZ = castleZ+castleLength/2;
		
		cube = new TangibleCube((int)cubeSizeX,(int)cubeSizeY, (int)cubeSizeZ);

		cube.loadPhysicalProperties();
		objects = Math3D.push_back(cube, objects);

		cube = new TangibleCube((int)castleWidth,(int)castleHeight, (int)castleLength);

		cube.rotationQuaternion.loadAxisAngle(0, 1, 0, 0);

		cube.displacement[0] = cubeX;
		cube.displacement[1] = cubeY;
		cube.displacement[2] = cubeZ;

		cube.infiniteMass = true;
		cube.loadPhysicalProperties();
//		cube.isDecoration = true;
		
		defaultData = Math3D.push_back(cube, defaultData);

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////


		coneX = cubeX;
		coneZ = cubeZ;
		
		cone = new TangibleCone(4,(int)Math.sqrt(coneSizeX*coneSizeX+coneSizeZ*coneSizeZ), (int)coneSizeY);

//		cone.loadPhysicalProperties();
		objects = Math3D.push_back(cone, objects);

		cone = new TangibleCone(4,(int)Math.sqrt(coneSizeX*coneSizeX+coneSizeZ*coneSizeZ), (int)coneSizeY);

		cone.loadPhysicalProperties();
		
		cone.rotationQuaternion.loadAxisAngle(0, 1, 0, Math.PI/4);
		
		coneY = castleY-castleHeight/2+castleHeight*towerRatio+coneSizeY/2.0-cone.surfaces[0].relativePosition[1]/2-.01;
		
		cone.displacement[0] = coneX;
		cone.displacement[1] = coneY;
		cone.displacement[2] = coneZ;

		cone.infiniteMass = true;
		cone.loadPhysicalProperties();
		cone.isDecoration = true;
		
		defaultData = Math3D.push_back(cone, defaultData);

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		cubeSizeX = castleWidth/4/2;
		cubeSizeY = castleHeight;
		cubeSizeZ = castleLength-castleLength/4+1;
		
		cubeX = castleX-castleWidth/2;
		cubeY = castleY;
		cubeZ = castleZ;
		
		cube = new TangibleCube((int)cubeSizeX,(int)cubeSizeY, (int)cubeSizeZ);
		
		cube.loadPhysicalProperties();
		objects = Math3D.push_back(cube, objects);

		cube = new TangibleCube((int)castleWidth,(int)castleHeight, (int)castleLength);

		cube.rotationQuaternion.loadAxisAngle(0, 1, 0, 0);

		cube.displacement[0] = cubeX;
		cube.displacement[1] = cubeY;
		cube.displacement[2] = cubeZ;

		cube.infiniteMass = true;
		cube.loadPhysicalProperties();
//		cube.isDecoration = true;
		
		defaultData = Math3D.push_back(cube, defaultData);


		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////

		cubeSizeX = castleWidth/4/2;
		cubeSizeY = castleHeight;
		cubeSizeZ = castleLength-castleLength/4+1;
		
		cubeX = castleX+castleWidth/2;
		cubeY = castleY;
		cubeZ = castleZ;
		
		cube = new TangibleCube((int)cubeSizeX,(int)cubeSizeY, (int)cubeSizeZ);

		cube.loadPhysicalProperties();
		objects = Math3D.push_back(cube, objects);

		cube = new TangibleCube((int)castleWidth,(int)castleHeight, (int)castleLength);

		cube.rotationQuaternion.loadAxisAngle(0, 1, 0, 0);

		cube.displacement[0] = cubeX;
		cube.displacement[1] = cubeY;
		cube.displacement[2] = cubeZ;

		cube.infiniteMass = true;
		cube.loadPhysicalProperties();
//		cube.isDecoration = true;
		
		defaultData = Math3D.push_back(cube, defaultData);


		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////


		cubeSizeX = castleWidth-castleWidth/4+1;
		cubeSizeY = castleHeight;
		cubeSizeZ = castleLength/4/2;
		
		cubeX = castleX;
		cubeY = castleY;
		cubeZ = castleZ+castleLength/2;
		
		cube = new TangibleCube((int)cubeSizeX,(int)cubeSizeY, (int)cubeSizeZ);

		cube.loadPhysicalProperties();
		objects = Math3D.push_back(cube, objects);

		cube = new TangibleCube((int)castleWidth,(int)castleHeight, (int)castleLength);

		cube.rotationQuaternion.loadAxisAngle(0, 1, 0, 0);

		cube.displacement[0] = cubeX;
		cube.displacement[1] = cubeY;
		cube.displacement[2] = cubeZ;

		cube.infiniteMass = true;
		cube.loadPhysicalProperties();
//		cube.isDecoration = true;
		
		defaultData = Math3D.push_back(cube, defaultData);



		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////

		double doorSizeX = 5;

		cubeSizeX = (castleWidth-doorSizeX)/2-castleWidth/8+.75;
		cubeSizeY = castleHeight;
		cubeSizeZ = castleLength/4/2;
		
		cubeX = castleX-doorSizeX/2-cubeSizeX/2;
		cubeY = castleY;
		cubeZ = castleZ-castleLength/2;
		
		cube = new TangibleCube((int)cubeSizeX,(int)cubeSizeY, (int)cubeSizeZ);

		cube.loadPhysicalProperties();
		objects = Math3D.push_back(cube, objects);

		cube = new TangibleCube((int)castleWidth,(int)castleHeight, (int)castleLength);

		cube.rotationQuaternion.loadAxisAngle(0, 1, 0, 0);

		cube.displacement[0] = cubeX;
		cube.displacement[1] = cubeY;
		cube.displacement[2] = cubeZ;

		cube.infiniteMass = true;
		cube.loadPhysicalProperties();
//		cube.isDecoration = true;
		
		defaultData = Math3D.push_back(cube, defaultData);

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////

		cubeX = castleX+doorSizeX/2+cubeSizeX/2;
		cubeY = castleY;
		cubeZ = castleZ-castleLength/2;
		
		cube = new TangibleCube((int)cubeSizeX,(int)cubeSizeY, (int)cubeSizeZ);

		cube.loadPhysicalProperties();
		objects = Math3D.push_back(cube, objects);

		cube = new TangibleCube((int)castleWidth,(int)castleHeight, (int)castleLength);

		cube.rotationQuaternion.loadAxisAngle(0, 1, 0, 0);

		cube.displacement[0] = cubeX;
		cube.displacement[1] = cubeY;
		cube.displacement[2] = cubeZ;

		cube.infiniteMass = true;
		cube.loadPhysicalProperties();
//		cube.isDecoration = true;
		
		defaultData = Math3D.push_back(cube, defaultData);

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		for(int i = startIndex; i < defaultData.length; i++)
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
			
			Math3D.scale(defaultData[i].color_shadow_solid, 1.65f);
			Math3D.scale(defaultData[i].color_lit_solid, 1.65f);
		}

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////

		double platformLength = 5;
		double platformWidth = 20*.98;

		double platformX = castleX-platformLength/2.0-.1;
		double platformZ = 205+120+.05;
		
		loadPlatform(platformX, -15, platformZ, platformLength, platformWidth, platformLength/2, platformWidth);

		defaultData[objects.length-1].color_lit_highligt[0] = .33f*1.3f;
		defaultData[objects.length-1].color_lit_highligt[1] = .16f*1.3f;
		defaultData[objects.length-1].color_lit_highligt[2] = 0;
		defaultData[objects.length-1].color_lit_highligt[3] = 1;
		
		defaultData[objects.length-1].color_shadow_highligt[0] = .66f*1.3f;
		defaultData[objects.length-1].color_shadow_highligt[1] = .32f*1.3f;
		defaultData[objects.length-1].color_shadow_highligt[2] = 0;
		defaultData[objects.length-1].color_shadow_highligt[3] = 1;

		defaultData[objects.length-1].color_lit_solid[0] = .33f;
		defaultData[objects.length-1].color_lit_solid[1] = .16f;
		defaultData[objects.length-1].color_lit_solid[2] = 0f;
		defaultData[objects.length-1].color_lit_solid[3] = 1f;

		defaultData[objects.length-1].color_shadow_solid[0] = .33f/2.0f;
		defaultData[objects.length-1].color_shadow_solid[1] = .16f/2.0f;
		defaultData[objects.length-1].color_shadow_solid[2] = 0.0f;
		defaultData[objects.length-1].color_shadow_solid[3] = 1.0f;

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
	}


	private void loadRoad() {

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		int startIndex = objects.length;
		
		double platformLength = 10;
		double platformWidth = 60;
		
		double platformX = -platformLength/2;
		double platformZ = -5-.1;
		
		loadPlatform(platformX, -15, platformZ, platformLength, platformWidth, platformLength, platformWidth/6);

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		platformLength = 60;
		platformWidth = 10;
		
		platformX = -5;
		platformZ = -5+60;
		
		loadPlatform(platformX, -15, platformZ, platformLength, platformWidth, platformLength/6, platformWidth);

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////

		platformLength = 10;
		platformWidth = 60;
		
		platformX = 45-50;
		platformZ = 65+4*10;
		
		loadPlatform(platformX, -15, platformZ, platformLength, platformWidth, platformLength, platformWidth/6);

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////

		platformLength = 10;
		platformWidth = 10;

		platformX = 5+3*10-2*20;;
		platformZ = 205;
		
		loadPlatform(platformX, -15, platformZ, platformLength, platformWidth, platformLength, platformWidth);

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////

		platformLength = 10;
		platformWidth = 100;

		platformX = 5+3*10-2*20;;
		platformZ = 225;
		
		loadPlatform(platformX, -15, platformZ, platformLength, platformWidth, platformLength, platformWidth/10);
		
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


	private void loadPlayer() 
	{
		TangibleSphere sphere = new TangibleSphere(10, 10, 2.5f);
		
		sphere.density = .01;
		
		sphere.loadPhysicalProperties();
		objects = Math3D.push_back(sphere, objects);
		
		sphere = new TangibleSphere(10, 10, 2.5f);

		sphere.rotationQuaternion.loadAxisAngle(0, 1, 0, 0);
		
		sphere.infiniteMass = false;

		sphere.displacement[0] = 0;
		sphere.displacement[1] = 6;
		sphere.displacement[2] = 0;
		
//		sphere.displacement[0] = 0;
//		sphere.displacement[1] = 6;
//		sphere.displacement[2] = 0;
			
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


	private void loadTrees() 
	{
		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		double treeX = -9+35;
		double treeZ = 35;
		double treeR = 6;
		
		loadTree(4,//Slices
				2, //TrunkRadius
				treeR, //canopyRadius
				10, //trunkHeight
				10, //canopyHeight
				treeX, -9.9, treeZ); //xyz

		defaultData[objects.length-2].rotationQuaternion.loadAxisAngle(0, 1, 0, Math.PI/4);
		defaultData[objects.length-1].rotationQuaternion.loadAxisAngle(0, 1, 0, Math.PI/4);
		defaultData[objects.length-2].isDecoration = true;
		defaultData[objects.length-1].isDecoration = true;

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		double platformLength = 10;
		double platformWidth = 10;
		
		double platformX = -platformLength/2+treeX;
		double platformZ = -platformWidth/2+treeZ;
		
		loadTreePlatform(platformX, -15, platformZ, platformLength, platformWidth, platformLength, platformWidth);	
		defaultData[objects.length-1].isDecoration = true;

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		treeX = -26;
		treeZ = 35;
		
		loadTree(4,//Slices
				2, //TrunkRadius
				treeR, //canopyRadius
				10, //trunkHeight
				10, //canopyHeight
				treeX, -9.9, treeZ); //xyz

		defaultData[objects.length-2].rotationQuaternion.loadAxisAngle(0, 1, 0, Math.PI/4);
		defaultData[objects.length-1].rotationQuaternion.loadAxisAngle(0, 1, 0, Math.PI/4);
		defaultData[objects.length-2].isDecoration = true;
		defaultData[objects.length-1].isDecoration = true;

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		platformX = -platformLength/2+treeX;
		platformZ = -platformWidth/2+treeZ;
		
		loadTreePlatform(platformX, -15, platformZ, platformLength, platformWidth, platformLength, platformWidth);
		defaultData[objects.length-1].isDecoration = true;

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		treeX = 15;
		treeZ = 80;
		
		loadTree(4,//Slices
				2, //TrunkRadius
				treeR, //canopyRadius
				10, //trunkHeight
				10, //canopyHeight
				treeX, -9.9, treeZ); //xyz

		defaultData[objects.length-2].rotationQuaternion.loadAxisAngle(0, 1, 0, Math.PI/4);
		defaultData[objects.length-1].rotationQuaternion.loadAxisAngle(0, 1, 0, Math.PI/4);
		defaultData[objects.length-2].isDecoration = true;
		defaultData[objects.length-1].isDecoration = true;

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		platformX = -platformLength/2+treeX;
		platformZ = -platformWidth/2+treeZ;
		
		loadTreePlatform(platformX, -15, platformZ, platformLength, platformWidth, platformLength, platformWidth);
		defaultData[objects.length-1].isDecoration = true;

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		treeX = 70;
		treeZ = 60;
		
		loadTree(4,//Slices
				2, //TrunkRadius
				treeR, //canopyRadius
				10, //trunkHeight
				10, //canopyHeight
				treeX, -9.9, treeZ); //xyz

		defaultData[objects.length-2].rotationQuaternion.loadAxisAngle(0, 1, 0, Math.PI/4);
		defaultData[objects.length-1].rotationQuaternion.loadAxisAngle(0, 1, 0, Math.PI/4);
		defaultData[objects.length-2].isDecoration = true;
		defaultData[objects.length-1].isDecoration = true;

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		platformX = -platformLength/2+treeX;
		platformZ = -platformWidth/2+treeZ;
		
		loadTreePlatform(platformX, -15, platformZ, platformLength, platformWidth, platformLength, platformWidth);
		defaultData[objects.length-1].isDecoration = true;

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		treeX = 60;
		treeZ = 130;
		
		loadTree(4,//Slices
				2, //TrunkRadius
				treeR, //canopyRadius
				10, //trunkHeight
				10, //canopyHeight
				treeX, -9.9, treeZ); //xyz

		defaultData[objects.length-2].rotationQuaternion.loadAxisAngle(0, 1, 0, Math.PI/4);
		defaultData[objects.length-1].rotationQuaternion.loadAxisAngle(0, 1, 0, Math.PI/4);
		defaultData[objects.length-2].isDecoration = true;
		defaultData[objects.length-1].isDecoration = true;

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		platformX = -platformLength/2+treeX;
		platformZ = -platformWidth/2+treeZ;
		
		loadTreePlatform(platformX, -15, platformZ, platformLength, platformWidth, platformLength, platformWidth);
		defaultData[objects.length-1].isDecoration = true;


		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		treeX = -30;
		treeZ = 140;
		
		loadTree(4,//Slices
				2, //TrunkRadius
				treeR, //canopyRadius
				10, //trunkHeight
				10, //canopyHeight
				treeX, -9.9, treeZ); //xyz

		defaultData[objects.length-2].rotationQuaternion.loadAxisAngle(0, 1, 0, Math.PI/4);
		defaultData[objects.length-1].rotationQuaternion.loadAxisAngle(0, 1, 0, Math.PI/4);
		defaultData[objects.length-2].isDecoration = true;
		defaultData[objects.length-1].isDecoration = true;

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		platformX = -platformLength/2+treeX;
		platformZ = -platformWidth/2+treeZ;
		
		loadTreePlatform(platformX, -15, platformZ, platformLength, platformWidth, platformLength, platformWidth);
		defaultData[objects.length-1].isDecoration = true;



		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		treeX = -30;
		treeZ = 190;
		
		loadTree(4,//Slices
				2, //TrunkRadius
				treeR, //canopyRadius
				10, //trunkHeight
				10, //canopyHeight
				treeX, -9.9, treeZ); //xyz

		defaultData[objects.length-2].rotationQuaternion.loadAxisAngle(0, 1, 0, Math.PI/4);
		defaultData[objects.length-1].rotationQuaternion.loadAxisAngle(0, 1, 0, Math.PI/4);
		defaultData[objects.length-2].isDecoration = true;
		defaultData[objects.length-1].isDecoration = true;

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		platformX = -platformLength/2+treeX;
		platformZ = -platformWidth/2+treeZ;
		
		loadTreePlatform(platformX, -15, platformZ, platformLength, platformWidth, platformLength, platformWidth);
		defaultData[objects.length-1].isDecoration = true;

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		treeX = 70;
		treeZ = 160;
		
		loadTree(4,//Slices
				2, //TrunkRadius
				treeR, //canopyRadius
				10, //trunkHeight
				10, //canopyHeight
				treeX, -9.9, treeZ); //xyz

		defaultData[objects.length-2].rotationQuaternion.loadAxisAngle(0, 1, 0, Math.PI/4);
		defaultData[objects.length-1].rotationQuaternion.loadAxisAngle(0, 1, 0, Math.PI/4);
		defaultData[objects.length-2].isDecoration = true;
		defaultData[objects.length-1].isDecoration = true;

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		platformX = -platformLength/2+treeX;
		platformZ = -platformWidth/2+treeZ;
		
		loadTreePlatform(platformX, -15, platformZ, platformLength, platformWidth, platformLength, platformWidth);
		defaultData[objects.length-1].isDecoration = true;


		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////

		treeX = 5+2*10;
		treeZ = 225;
		
		loadTree(4,//Slices
				2, //TrunkRadius
				treeR, //canopyRadius
				10, //trunkHeight
				10, //canopyHeight
				treeX, -9.9, treeZ); //xyz

		defaultData[objects.length-2].rotationQuaternion.loadAxisAngle(0, 1, 0, Math.PI/4);
		defaultData[objects.length-1].rotationQuaternion.loadAxisAngle(0, 1, 0, Math.PI/4);
		defaultData[objects.length-2].isDecoration = true;
		defaultData[objects.length-1].isDecoration = true;

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		platformX = -platformLength/2+treeX;
		platformZ = -platformWidth/2+treeZ;
		
		loadTreePlatform(platformX, -15, platformZ, platformLength, platformWidth, platformLength, platformWidth);
		defaultData[objects.length-1].isDecoration = true;

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		treeX = 70;
		treeZ = 190;
		
		loadTree(4,//Slices
				2, //TrunkRadius
				treeR, //canopyRadius
				10, //trunkHeight
				10, //canopyHeight
				treeX, -9.9, treeZ); //xyz

		defaultData[objects.length-2].rotationQuaternion.loadAxisAngle(0, 1, 0, Math.PI/4);
		defaultData[objects.length-1].rotationQuaternion.loadAxisAngle(0, 1, 0, Math.PI/4);
		defaultData[objects.length-2].isDecoration = true;
		defaultData[objects.length-1].isDecoration = true;

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		platformX = -platformLength/2+treeX;
		platformZ = -platformWidth/2+treeZ;
		
		loadTreePlatform(platformX, -15, platformZ, platformLength, platformWidth, platformLength, platformWidth);
		defaultData[objects.length-1].isDecoration = true;

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		treeX = 30;
		treeZ = 280;
		
		loadTree(4,//Slices
				2, //TrunkRadius
				treeR, //canopyRadius
				10, //trunkHeight
				10, //canopyHeight
				treeX, -9.9, treeZ); //xyz

		defaultData[objects.length-2].rotationQuaternion.loadAxisAngle(0, 1, 0, Math.PI/4);
		defaultData[objects.length-1].rotationQuaternion.loadAxisAngle(0, 1, 0, Math.PI/4);
		defaultData[objects.length-2].isDecoration = true;
		defaultData[objects.length-1].isDecoration = true;

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		platformX = -platformLength/2+treeX;
		platformZ = -platformWidth/2+treeZ;
		
		loadTreePlatform(platformX, -15, platformZ, platformLength, platformWidth, platformLength, platformWidth);
		defaultData[objects.length-1].isDecoration = true;


		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		platformX = -platformLength/2+treeX;
		platformZ = -platformWidth/2+treeZ;
		
		loadTreePlatform(platformX, -15, platformZ, platformLength, platformWidth, platformLength, platformWidth);
		defaultData[objects.length-1].isDecoration = true;

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		treeX = -30;
		treeZ = 280;
		
		loadTree(4,//Slices
				2, //TrunkRadius
				treeR, //canopyRadius
				10, //trunkHeight
				10, //canopyHeight
				treeX, -9.9, treeZ); //xyz

		defaultData[objects.length-2].rotationQuaternion.loadAxisAngle(0, 1, 0, Math.PI/4);
		defaultData[objects.length-1].rotationQuaternion.loadAxisAngle(0, 1, 0, Math.PI/4);
		defaultData[objects.length-2].isDecoration = true;
		defaultData[objects.length-1].isDecoration = true;

		//////////////////////////////////
		//////////////////////////////////
		//////////////////////////////////
		
		platformX = -platformLength/2+treeX;
		platformZ = -platformWidth/2+treeZ;
		
		loadTreePlatform(platformX, -15, platformZ, platformLength, platformWidth, platformLength, platformWidth);
		defaultData[objects.length-1].isDecoration = true;
	}

	public void loadTreePlatform(double x, double y, double z, double length, double width, double dl, double dw)
	{
		loadPlatform(x, y, z, length, width, dl, dw);

		defaultData[objects.length-1].color_shadow_highligt[0] = 0f;
		defaultData[objects.length-1].color_shadow_highligt[1] = 1f;
		defaultData[objects.length-1].color_lit_highligt[2] = 0;
		defaultData[objects.length-1].color_lit_highligt[3] = 1;
	}
	
	double function_height = 7.0;
	double function_length = .06;
	
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
		concaveTangible.color_shadow_highligt[1] = 0f;
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
		concaveTangible.color_lit_highligt[1] = 1;
		concaveTangible.color_lit_highligt[2] = 0;
		
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
	
	public int getPlayerIndex() 
	{
		return playerIndex;
	}
}
