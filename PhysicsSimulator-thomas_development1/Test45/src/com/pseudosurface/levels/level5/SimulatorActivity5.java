package com.pseudosurface.levels.level5;

import com.pseudosurface.levels.template.Renderer;
import com.pseudosurface.levels.template.SimulatorActivity;
import com.pseudosurface.levels.template.World;
import com.pseudosurface.physics.TangibleObject;


public class SimulatorActivity5 extends SimulatorActivity{
	
	public SimulatorActivity5()
	{
		super();
		this.zoom = 35.0f; 
	}
	
	public Renderer getNewRenderer() {
		return new Renderer5(this);
	}

	public World getNewWorld() {
		return new World5(renderer);
	}

	public Shader5 getShader() {
		return new Shader5();
	}

	public void move(double timeStep) {

		//For this level, it is important to update the moving platforms 
		//(which have a non-linear motion) APPROPRIATELY:
		//1) Their position must not not escape the bounds of their path,
		//2) Their new velocity must be updated in order for the graphics to render
			//properly, as well as physically interact with the ball properly.
		//3) The TangibleObject indices in the World.objects array are mapped to 
			//the particular implementation of this level/package (class "World5")
		
		int i = world.getPlayerIndex(); 
		//for(int i = 0; i < simulation.world.objects.length; i++)
			if(!this.world.frozen)
			{	
				this.world.objects[i].transform(timeStep);
				this.world.objects[i].loadAuxilaryValues();
				
				this.world.objects[((World5) world).platform1].transform(timeStep);
				this.world.objects[((World5) world).platform1].loadAuxilaryValues();
				
				if(this.world.objects[((World5) world).platform1].displacement[2] < -33)
				{
					this.world.objects[((World5) world).platform1].displacement[2] = -33;
					this.world.objects[((World5) world).platform1].linearVelocity[2] *= -1;
				}
				else if(this.world.objects[((World5) world).platform1].displacement[2] > 11)
				{
					this.world.objects[((World5) world).platform1].displacement[2] = 11;
					this.world.objects[((World5) world).platform1].linearVelocity[2] *= -1;
				}
				
				this.world.objects[((World5) world).platform2].transform(timeStep);
				this.world.objects[((World5) world).platform2].loadAuxilaryValues();
				
				if(this.world.objects[((World5) world).platform2].displacement[2] < -33)
				{
					this.world.objects[((World5) world).platform2].displacement[2] = -33;
					this.world.objects[((World5) world).platform2].linearVelocity[2] *= -1;
				}
				else if(this.world.objects[((World5) world).platform2].displacement[2] > 11)
				{
					this.world.objects[((World5) world).platform2].displacement[2] = 11;
					this.world.objects[((World5) world).platform2].linearVelocity[2] *= -1;
				}

				this.world.objects[((World5) world).platform3].transform(timeStep);
				this.world.objects[((World5) world).platform3].loadAuxilaryValues();
				
				if(this.world.objects[((World5) world).platform3].displacement[2] < -33)
				{
					this.world.objects[((World5) world).platform3].displacement[2] = -33;
					this.world.objects[((World5) world).platform3].linearVelocity[2] *= -1;
				}
				else if(this.world.objects[((World5) world).platform3].displacement[2] > 11)
				{
					this.world.objects[((World5) world).platform3].displacement[2] = 11;
					this.world.objects[((World5) world).platform3].linearVelocity[2] *= -1;
				}

				this.world.objects[((World5) world).platform4].transform(timeStep);
				this.world.objects[((World5) world).platform4].loadAuxilaryValues();
				
				if(this.world.objects[((World5) world).platform4].displacement[2] < -33)
				{
					this.world.objects[((World5) world).platform4].displacement[2] = -33;
					this.world.objects[((World5) world).platform4].linearVelocity[2] *= -1;
				}
				else if(this.world.objects[((World5) world).platform4].displacement[2] > 11)
				{
					this.world.objects[((World5) world).platform4].displacement[2] = 11;
					this.world.objects[((World5) world).platform4].linearVelocity[2] *= -1;
				}

				this.world.objects[((World5) world).platform5].transform(timeStep);
				this.world.objects[((World5) world).platform5].loadAuxilaryValues();
				
				if(this.world.objects[((World5) world).platform5].displacement[2] < -33)
				{
					this.world.objects[((World5) world).platform5].displacement[2] = -33;
					this.world.objects[((World5) world).platform5].linearVelocity[2] *= -1;
				}
				else if(this.world.objects[((World5) world).platform5].displacement[2] > 11)
				{
					this.world.objects[((World5) world).platform5].displacement[2] = 11;
					this.world.objects[((World5) world).platform5].linearVelocity[2] *= -1;
				}

				this.world.objects[((World5) world).platform6].transform(timeStep);
				this.world.objects[((World5) world).platform6].loadAuxilaryValues();
				
				if(this.world.objects[((World5) world).platform6].displacement[2] < -33)
				{
					this.world.objects[((World5) world).platform6].displacement[2] = -33;
					this.world.objects[((World5) world).platform6].linearVelocity[2] *= -1;
				}
				else if(this.world.objects[((World5) world).platform6].displacement[2] > 11)
				{
					this.world.objects[((World5) world).platform6].displacement[2] = 11;
					this.world.objects[((World5) world).platform6].linearVelocity[2] *= -1;
				}

				this.world.objects[((World5) world).platform7].transform(timeStep);
				this.world.objects[((World5) world).platform7].loadAuxilaryValues();
				
				if(this.world.objects[((World5) world).platform7].displacement[2] < -33)
				{
					this.world.objects[((World5) world).platform7].displacement[2] = -33;
					this.world.objects[((World5) world).platform7].linearVelocity[2] *= -1;
				}
				else if(this.world.objects[((World5) world).platform7].displacement[2] > 11)
				{
					this.world.objects[((World5) world).platform7].displacement[2] = 11;
					this.world.objects[((World5) world).platform7].linearVelocity[2] *= -1;
				}

				this.world.objects[((World5) world).platform8].transform(timeStep);
				this.world.objects[((World5) world).platform8].loadAuxilaryValues();

				if(this.world.objects[((World5) world).platform8].displacement[2] < -33)
				{
					this.world.objects[((World5) world).platform8].displacement[2] = -33;
					this.world.objects[((World5) world).platform8].linearVelocity[2] *= -1;
				}
				else if(this.world.objects[((World5) world).platform8].displacement[2] > 11)
				{
					this.world.objects[((World5) world).platform8].displacement[2] = 11;
					this.world.objects[((World5) world).platform8].linearVelocity[2] *= -1;
				}
			}
		
		return;
	}

	public void collision(int i, int j) {
	}

}
