package com.pseudosurface.levels.template;

import java.util.concurrent.Callable;

import com.pseudosurface.physics.CollisionDetector;
import com.pseudosurface.physics.CollisionMatrix;
import com.pseudosurface.physics.CollisionResponse;
import com.pseudosurface.physics.Math3D;


public class CollisionDriver implements Callable {
	CollisionDetector detector = new CollisionDetector();
	
	SimulatorActivity simulation;
	
    public CollisionDriver(SimulatorActivity simulation) 
    {
    	this.simulation = simulation;
    }

    public Object call() 
    {
		CollisionDetector detector = new CollisionDetector();
		double previousTime = System.nanoTime();
		double thisTime, timeStep;
		
		while(!simulation.returnFlag)
		{	
	    	try{	    		
				Thread.sleep(0,1);
		    	thisTime = System.nanoTime(); 
		    	timeStep = (thisTime -previousTime)/1E9;
		
//				for(int i = 0; i < ; i++)
		    	int i = simulation.world.getPlayerIndex();
					for(int j = 0; j < simulation.world.objects.length; j++)
					{
						try
						{
//							if(j == 38)
//							{
//								Log.e("cube test", "cube test");
//								Log.e("cube test", ""+
//								(!(
//										CollisionMatrix.matrix[i][j].objectA.infiniteMass 
//										&& CollisionMatrix.matrix[i][j].objectB.infiniteMass
//									))+" && "+ (i != j) + " && " + (!simulation.world.objects[j].isDecoration)
//								);
//							}
//							else
//							{
//								Log.e("other", "other");
//							}
							
							if(!(
									CollisionMatrix.matrix[i][j].objectA.infiniteMass 
									&& CollisionMatrix.matrix[i][j].objectB.infiniteMass
								)
								&& i != j
								&& !simulation.world.objects[j].isDecoration &&
								(Math3D.distance(simulation.world.objects[i].displacement, simulation.world.objects[j].displacement) < simulation.world.maxDrawDistance+simulation.world.objects[j].surfaces[0].maxRadius))
							{
//								Log.e(""+i+" "+j, ""+i+" "+j);
								detector.clear();
									detector.testCollision(CollisionMatrix.matrix[i][j].objectA, CollisionMatrix.matrix[i][j].objectB);
									
									if(detector.collisionPoints.length != 0)
									{
//										simulation.world.objects[i].color_lit_solid[0] += (simulation.world.defaultData[j].color_lit_solid[0]-simulation.world.defaultData[i].color_lit_solid[0])/12.0;
//										simulation.world.objects[i].color_lit_solid[1] += (simulation.world.defaultData[j].color_lit_solid[1]-simulation.world.defaultData[i].color_lit_solid[1])/12.0;
//										simulation.world.objects[i].color_lit_solid[2] += (simulation.world.defaultData[j].color_lit_solid[2]-simulation.world.defaultData[i].color_lit_solid[2])/12.0;
//										simulation.world.objects[i].color_shadow_solid[0] += (simulation.world.defaultData[j].color_shadow_solid[0]-simulation.world.defaultData[i].color_shadow_solid[0])/12.0;
//										simulation.world.objects[i].color_shadow_solid[1] += (simulation.world.defaultData[j].color_shadow_solid[1]-simulation.world.defaultData[i].color_shadow_solid[1])/12.0;
//										simulation.world.objects[i].color_shadow_solid[2] += (simulation.world.defaultData[j].color_shadow_solid[2]-simulation.world.defaultData[i].color_shadow_solid[2])/12.0;

										simulation.collision(i, j);

										if(i == simulation.world.getPlayerIndex()||
											j == simulation.world.getPlayerIndex())
											((MyGLSurfaceView)simulation.mGLView).numJumps = 0; 
									}
									
									CollisionMatrix.matrix[i][j].collisionPoints = detector.collisionPoints.clone();
									CollisionMatrix.matrix[i][j].collisionNormals = detector.collisionNormals.clone();
									CollisionMatrix.matrix[i][j].BSApoint = detector.BSApoint.clone();
									CollisionMatrix.matrix[i][j].BSAnormal = detector.BSAnormal.clone();
									CollisionMatrix.matrix[i][j].BSAR = detector.BSAR;
					
									CollisionResponse responder = new CollisionResponse();
									responder.resolveCollisions(CollisionMatrix.matrix[i][j]);
									responder.LoadContactForces();
									
									if(!CollisionMatrix.matrix[i][j].objectA.infiniteMass)
										responder.ApplyContactForces(timeStep, CollisionMatrix.matrix[i][j].objectA);
					
									if(!CollisionMatrix.matrix[i][j].objectB.infiniteMass)
										responder.ApplyContactForces(timeStep, CollisionMatrix.matrix[i][j].objectB);
							}
						}
						catch(Exception e)
						{
							System.err.println("error on object "+j+" "+e.getMessage());
						}
					}
			
				
				
		    	previousTime = thisTime;
	    	}
	    	catch(Exception e)
	    	{
	    		System.err.print(e.getMessage());
	    		e.printStackTrace();
	    		System.exit(1);
	    	}
		}
    	
    	return null;
    }
}
