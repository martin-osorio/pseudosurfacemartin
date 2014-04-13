package com.pseudosurface.levels.level6;

import com.pseudosurface.levels.template.Renderer;
import com.pseudosurface.levels.template.SimulatorActivity;
import com.pseudosurface.levels.template.World;
import com.pseudosurface.physics.Math3D;
import com.pseudosurface.physics.TangibleObject;


public class SimulatorActivity6 extends SimulatorActivity{
	
	public SimulatorActivity6()
	{
		super();
    	this.zoom = 35;
	}
	
	public Renderer getNewRenderer() {
		return new Renderer6(this);
	}

	public World getNewWorld() {
		return new World6(renderer);
	}

	public Shader6 getShader() {
		return new Shader6();
	}

	public void move(double timeStep) {
		
		int i = world.getPlayerIndex(); 
		//for(int i = 0; i < simulation.world.objects.length; i++)
			if(!this.world.frozen)
			{	
				this.world.objects[i].transform(timeStep);
				this.world.objects[i].loadAuxilaryValues();
				
				double angularVelocity = .25;
				
				////////////////////////////////////////////////////////////////////////////
				////////////////////////////////////////////////////////////////////////////
				////////////////////////////////////////////////////////////////////////////

				double time = System.nanoTime()/1E9;
				
				double[] omega = {-angularVelocity, 0, 0};
				
				double theta = -omega[0]*time;
				double delta = 2*Math.PI/4;
				double r =  22;
				double x, y, z;
				
				double[] p = new double[3];
				
				this.world.objects[((World6) world).platform1].transform(timeStep);
				this.world.objects[((World6) world).platform1].loadAuxilaryValues();
				
				x = 11.05;
				y = r*Math.sin(theta) + r - 8; 
				z = r*Math.cos(theta)-11;
				
				p[0] = 0;
				p[1] = r*Math.sin(theta);
				p[2] = r*Math.cos(theta);
				
				Math3D.cross(omega, p, this.world.objects[((World6) world).platform1].linearVelocity);
				
				this.world.objects[((World6) world).platform1].displacement[0] = x;
				this.world.objects[((World6) world).platform1].displacement[1] = y;
				this.world.objects[((World6) world).platform1].displacement[2] = z;
				
				
				
				this.world.objects[((World6) world).platform2].transform(timeStep);
				this.world.objects[((World6) world).platform2].loadAuxilaryValues();

				theta += delta;
				x = 11.05;
				y = r*Math.sin(theta) + r - 8; 
				z = r*Math.cos(theta)-11;
				
				p[0] = 0;
				p[1] = r*Math.sin(theta);
				p[2] = r*Math.cos(theta);
				
				Math3D.cross(omega, p, this.world.objects[((World6) world).platform2].linearVelocity);

				this.world.objects[((World6) world).platform2].displacement[0] = x;
				this.world.objects[((World6) world).platform2].displacement[1] = y;
				this.world.objects[((World6) world).platform2].displacement[2] = z;
				
				

				this.world.objects[((World6) world).platform3].transform(timeStep);
				this.world.objects[((World6) world).platform3].loadAuxilaryValues();
				
				theta += delta;
				x = 11.05;
				y = r*Math.sin(theta) + r - 8; 
				z = r*Math.cos(theta)-11;
				
				p[0] = 0;
				p[1] = r*Math.sin(theta);
				p[2] = r*Math.cos(theta);

				Math3D.cross(omega, p, this.world.objects[((World6) world).platform3].linearVelocity);
				
				this.world.objects[((World6) world).platform3].displacement[0] = x;
				this.world.objects[((World6) world).platform3].displacement[1] = y;
				this.world.objects[((World6) world).platform3].displacement[2] = z;

				
				
				this.world.objects[((World6) world).platform4].transform(timeStep);
				this.world.objects[((World6) world).platform4].loadAuxilaryValues();

				theta += delta;
				x = 11.05;
				y = r*Math.sin(theta) + r - 8; 
				z = r*Math.cos(theta)-11;
				
				p[0] = 0;
				p[1] = r*Math.sin(theta);
				p[2] = r*Math.cos(theta);

				Math3D.cross(omega, p, this.world.objects[((World6) world).platform4].linearVelocity);
				
				this.world.objects[((World6) world).platform4].displacement[0] = x;
				this.world.objects[((World6) world).platform4].displacement[1] = y;
				this.world.objects[((World6) world).platform4].displacement[2] = z;

/////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				omega = new double[] {0, -angularVelocity, 0};
				
				theta = omega[1]*time;
				delta = 2*Math.PI/4;
				r =  22;
				
				this.world.objects[((World6) world).platform5].transform(timeStep);
				this.world.objects[((World6) world).platform5].loadAuxilaryValues();
				
				x = 55.1+r*Math.sin(theta);
				y = 2*r - 8; 
				z = r*Math.cos(theta)-11;
				
				p[0] = r*Math.sin(theta);
				p[1] = 0;
				p[2] = r*Math.cos(theta);
				
				Math3D.cross(omega, p, this.world.objects[((World6) world).platform5].linearVelocity);
				
				this.world.objects[((World6) world).platform5].displacement[0] = x;
				this.world.objects[((World6) world).platform5].displacement[1] = y;
				this.world.objects[((World6) world).platform5].displacement[2] = z;
				
				
				
				this.world.objects[((World6) world).platform6].transform(timeStep);
				this.world.objects[((World6) world).platform6].loadAuxilaryValues();

				theta += delta;
				x = 55.1+r*Math.sin(theta);
				y = 2*r - 8; 
				z = r*Math.cos(theta)-11;
				
				p[0] = r*Math.sin(theta);
				p[1] = 0;
				p[2] = r*Math.cos(theta);
				
				Math3D.cross(omega, p, this.world.objects[((World6) world).platform6].linearVelocity);

				this.world.objects[((World6) world).platform6].displacement[0] = x;
				this.world.objects[((World6) world).platform6].displacement[1] = y;
				this.world.objects[((World6) world).platform6].displacement[2] = z;
				
				

				this.world.objects[((World6) world).platform7].transform(timeStep);
				this.world.objects[((World6) world).platform7].loadAuxilaryValues();
				
				theta += delta;
				x = 55.1+r*Math.sin(theta);
				y = 2*r - 8; 
				z = r*Math.cos(theta)-11;
				
				p[0] = r*Math.sin(theta);
				p[1] = 0;
				p[2] = r*Math.cos(theta);

				Math3D.cross(omega, p, this.world.objects[((World6) world).platform7].linearVelocity);
				
				this.world.objects[((World6) world).platform7].displacement[0] = x;
				this.world.objects[((World6) world).platform7].displacement[1] = y;
				this.world.objects[((World6) world).platform7].displacement[2] = z;

				
				
				this.world.objects[((World6) world).platform8].transform(timeStep);
				this.world.objects[((World6) world).platform8].loadAuxilaryValues();

				theta += delta;
				x = 55.1+r*Math.sin(theta);
				y = 2*r - 8; 
				z = r*Math.cos(theta)-11;
				
				p[0] = r*Math.sin(theta);
				p[1] = 0;
				p[2] = r*Math.cos(theta);

				Math3D.cross(omega, p, this.world.objects[((World6) world).platform8].linearVelocity);
				
				this.world.objects[((World6) world).platform8].displacement[0] = x;
				this.world.objects[((World6) world).platform8].displacement[1] = y;
				this.world.objects[((World6) world).platform8].displacement[2] = z;


/////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////
				/*
				p[0] = r*Math.sin(theta);
				p[1] = -r*Math.sin(theta);
				p[2] = r*Math.cos(theta);
				
				double[] p1 = {r*Math.sin(theta+Math.PI/2),
							   -r*Math.sin(theta+Math.PI/2),
							   r*Math.cos(theta+Math.PI/2)};
				
				Math3D.cross(p, p1, omega);
				Math3D.normalize(omega);
				Math3D.scale(omega, angularVelocity);
				
				theta = angularVelocity*time;
				delta = 2*Math.PI/4;
				r =  22;
				
				////////
				
				this.world.objects[((World6) world).platform9].transform(timeStep);
				this.world.objects[((World6) world).platform9].loadAuxilaryValues();
				
				x = 99.15+r+r*Math.sin(theta);
				y = r - 8 - r*Math.sin(theta); 
				z = r*Math.cos(theta)-11;
				
				p[0] = r*Math.sin(theta);
				p[1] = -r*Math.sin(theta);
				p[2] = r*Math.cos(theta);
				
				Math3D.cross(omega, p, this.world.objects[((World6) world).platform9].linearVelocity);
				
				this.world.objects[((World6) world).platform9].displacement[0] = x;
				this.world.objects[((World6) world).platform9].displacement[1] = y;
				this.world.objects[((World6) world).platform9].displacement[2] = z;

				////////
				
				this.world.objects[((World6) world).platform10].transform(timeStep);
				this.world.objects[((World6) world).platform10].loadAuxilaryValues();
				
				theta += delta;
				x = 99.15+r+r*Math.sin(theta);
				y = r - 8 - r*Math.sin(theta); 
				z = r*Math.cos(theta)-11;
				
				p[0] = r*Math.sin(theta);
				p[1] = -r*Math.sin(theta);
				p[2] = r*Math.cos(theta);
				
				Math3D.cross(omega, p, this.world.objects[((World6) world).platform10].linearVelocity);
				
				this.world.objects[((World6) world).platform10].displacement[0] = x;
				this.world.objects[((World6) world).platform10].displacement[1] = y;
				this.world.objects[((World6) world).platform10].displacement[2] = z;
				
				////////
				
				this.world.objects[((World6) world).platform11].transform(timeStep);
				this.world.objects[((World6) world).platform11].loadAuxilaryValues();
				
				theta += delta;
				x = 99.15+r+r*Math.sin(theta);
				y = r - 8 - r*Math.sin(theta); 
				z = r*Math.cos(theta)-11;
				
				p[0] = r*Math.sin(theta);
				p[1] = -r*Math.sin(theta);
				p[2] = r*Math.cos(theta);
				
				Math3D.cross(omega, p, this.world.objects[((World6) world).platform11].linearVelocity);
				
				this.world.objects[((World6) world).platform11].displacement[0] = x;
				this.world.objects[((World6) world).platform11].displacement[1] = y;
				this.world.objects[((World6) world).platform11].displacement[2] = z;
				
				////////
				
				this.world.objects[((World6) world).platform12].transform(timeStep);
				this.world.objects[((World6) world).platform12].loadAuxilaryValues();
				
				theta += delta;
				x = 99.15+r+r*Math.sin(theta);
				y = r - 8 - r*Math.sin(theta); 
				z = r*Math.cos(theta)-11;
				
				p[0] = r*Math.sin(theta);
				p[1] = -r*Math.sin(theta);
				p[2] = r*Math.cos(theta);
				
				Math3D.cross(omega, p, this.world.objects[((World6) world).platform12].linearVelocity);
				
				this.world.objects[((World6) world).platform12].displacement[0] = x;
				this.world.objects[((World6) world).platform12].displacement[1] = y;
				this.world.objects[((World6) world).platform12].displacement[2] = z;
				*/
				double[] p1 = new double[3];
				
				Math3D.cross(p, p1, omega);
				Math3D.normalize(omega);
				omega[1] = -Math.abs(omega[1]);
				Math3D.scale(omega, angularVelocity);
				
				theta = angularVelocity*time;
				delta = 2*Math.PI/4;
				r =  22;
				
				////////
				
				x = 99.15+r+r*Math.sin(theta);
				y = r - 8 - r*Math.sin(theta); 
				z = r*Math.cos(theta)-11;
				
				p[0] = x;
				p[1] = y;
				p[2] = z;

				p1[0] = 99.15+r+r*Math.sin(theta+angularVelocity*timeStep);
				p1[1] = r - 8 - r*Math.sin(theta+angularVelocity*timeStep);
				p1[2] = r*Math.cos(theta+angularVelocity*timeStep)-11;
				
				Math3D.sub3x1minus3x1(p1, p, p1);
				Math3D.scale(p1, 1.0/timeStep);
				
				this.world.objects[((World6) world).platform9].linearVelocity = p1.clone();
				
				this.world.objects[((World6) world).platform9].displacement[0] = x;
				this.world.objects[((World6) world).platform9].displacement[1] = y;
				this.world.objects[((World6) world).platform9].displacement[2] = z;
				
				this.world.objects[((World6) world).platform9].transform(timeStep);
				this.world.objects[((World6) world).platform9].loadAuxilaryValues();

				////////
				
				theta += delta;
				x = 99.15+r+r*Math.sin(theta);
				y = r - 8 - r*Math.sin(theta); 
				z = r*Math.cos(theta)-11;
				
				p[0] = x;
				p[1] = y;
				p[2] = z;

				p1[0] = 99.15+r+r*Math.sin(theta+angularVelocity*timeStep);
				p1[1] = r - 8 - r*Math.sin(theta+angularVelocity*timeStep);
				p1[2] = r*Math.cos(theta+angularVelocity*timeStep)-11;
				
				Math3D.sub3x1minus3x1(p1, p, p1);
				Math3D.scale(p1, 1.0/timeStep);
				
				this.world.objects[((World6) world).platform10].linearVelocity = p1.clone();
				
				this.world.objects[((World6) world).platform10].displacement[0] = x;
				this.world.objects[((World6) world).platform10].displacement[1] = y;
				this.world.objects[((World6) world).platform10].displacement[2] = z;
				
				this.world.objects[((World6) world).platform10].transform(timeStep);
				this.world.objects[((World6) world).platform10].loadAuxilaryValues();
				
				////////
				
				theta += delta;
				x = 99.15+r+r*Math.sin(theta);
				y = r - 8 - r*Math.sin(theta); 
				z = r*Math.cos(theta)-11;
				
				p[0] = x;
				p[1] = y;
				p[2] = z;

				p1[0] = 99.15+r+r*Math.sin(theta+angularVelocity*timeStep);
				p1[1] = r - 8 - r*Math.sin(theta+angularVelocity*timeStep);
				p1[2] = r*Math.cos(theta+angularVelocity*timeStep)-11;
				
				Math3D.sub3x1minus3x1(p1, p, p1);
				Math3D.scale(p1, 1.0/timeStep);
				
				this.world.objects[((World6) world).platform11].linearVelocity = p1.clone();
				
				this.world.objects[((World6) world).platform11].displacement[0] = x;
				this.world.objects[((World6) world).platform11].displacement[1] = y;
				this.world.objects[((World6) world).platform11].displacement[2] = z;
				
				this.world.objects[((World6) world).platform11].transform(timeStep);
				this.world.objects[((World6) world).platform11].loadAuxilaryValues();
				
				////////
				
				theta += delta;
				x = 99.15+r+r*Math.sin(theta);
				y = r - 8 - r*Math.sin(theta); 
				z = r*Math.cos(theta)-11;
				
				p[0] = x;
				p[1] = y;
				p[2] = z;

				p1[0] = 99.15+r+r*Math.sin(theta+angularVelocity*timeStep);
				p1[1] = r - 8 - r*Math.sin(theta+angularVelocity*timeStep);
				p1[2] = r*Math.cos(theta+angularVelocity*timeStep)-11;
				
				Math3D.sub3x1minus3x1(p1, p, p1);
				Math3D.scale(p1, 1.0/timeStep);
				
				this.world.objects[((World6) world).platform12].linearVelocity = p1.clone();
				
				this.world.objects[((World6) world).platform12].displacement[0] = x;
				this.world.objects[((World6) world).platform12].displacement[1] = y;
				this.world.objects[((World6) world).platform12].displacement[2] = z;
				
				this.world.objects[((World6) world).platform12].transform(timeStep);
				this.world.objects[((World6) world).platform12].loadAuxilaryValues();
			}
		
		return;
	}

	public void collision(int i, int j) {
	}

}
