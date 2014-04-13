package com.pseudosurface.levels.template;

import java.util.concurrent.Callable;

import com.pseudosurface.physics.CollisionDetector;


public class MovementDriver implements Callable {
	SimulatorActivity simulation;
	
    public MovementDriver(SimulatorActivity simulation) 
    {
    	this.simulation = simulation;
    }

	@Override
    public Object call() 
    {
		CollisionDetector detector = new CollisionDetector();
    	try{
			double previousTime = System.nanoTime();
			double thisTime, timeStep;
			
			while(!simulation.returnFlag)
			{	
	    		Thread.sleep(0,1);
		    	thisTime = System.nanoTime(); 
		    	timeStep = (thisTime -previousTime)/1E9;
				
		    	simulation.move(timeStep);

		    	previousTime = thisTime;
			}
    	}
    	catch(Exception e)
    	{
    		System.err.print(e.getMessage());
    		e.printStackTrace();
    		System.exit(1);
    	}
		return null;
	}
}