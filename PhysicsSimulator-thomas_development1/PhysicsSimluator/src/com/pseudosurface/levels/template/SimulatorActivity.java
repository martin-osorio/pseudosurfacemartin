/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. IAN'S UPDATE
 */

package com.pseudosurface.levels.template;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.doghouse.physicssimluator.model.Score;
import com.doghouse.physicssimluator.views.HighScoreFragment;
import com.doghouse.physicssimluator.views.LevelCompletionFragment;
import com.doghouse.physicssimulator.util.HighScoreManager;
import com.pseudosurface.physics.CollisionMatrix;
import com.pseudosurface.physics.Quaternion;

import android.content.Context;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public abstract class SimulatorActivity extends FragmentActivity implements SensorEventListener{

    public GLSurfaceView mGLView;
    
    public float zoom = 24.5f;
    public float minZoom = 10;
	public float angleUD = (float) (Math.PI/4);
	public static float angleLR = (float) (Math.PI/2);

	public VictoryInterface victoryCallback = null;
	
    public World world;
    
    SensorManager sensorManager;
    Sensor gravitySensor;

	public double startTime = System.nanoTime();

	float[] linear_acceleration = new float[3];
	
    double previousTime;
    int nrOfProcessors = -1;
    
    public SimulatorActivity()
    {
    	world = getNewWorld();
    }
    
    public boolean returnFlag = false;
    
    public void startWord()
    {
    	if(nrOfProcessors != -1) return;
    	
    	CollisionMatrix.setObjects(world.objects);
    	
    	returnFlag = false;
    	
    	nrOfProcessors = Runtime.getRuntime().availableProcessors();
//		ExecutorService es = Executors.newFixedThreadPool(nrOfProcessors);
		ExecutorService es = Executors.newFixedThreadPool(2);
    	es.submit(new MovementDriver(this));
		es.submit(new CollisionDriver(this));
    }
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
//        mGLView.setPreserveEGLContextOnPause(false);


        /////////////////////
        
        mGLView = new MyGLSurfaceView(this, this);
        setContentView(mGLView);
        
        /////////////////////
        
//        RelativeLayout rLayout = new RelativeLayout(this);
//        LayoutParams rlParams = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT); 
//        rLayout.setLayoutParams(rlParams);
//
//        RelativeLayout.LayoutParams tParams = new RelativeLayout.LayoutParams
//                (LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//        tParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
//        tParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//        TextView text=new TextView(this); 
//        text.setText("GOLDEN Gate"); 
//        text.setTextColor(Color.WHITE);                            
//        text.setTypeface(Typeface.DEFAULT_BOLD);
//        text.setLayoutParams(tParams);
//
//        mGLView = new MyGLSurfaceView(this, this);
//        rLayout.addView(mGLView);
//        rLayout.addView(text);
//        setContentView(rLayout);

        /////////////////////
		
		victoryCallback = new VictoryInterface(){

			@Override
			public void victoryCallback(int stars, long time) {
				int level = getIntent().getExtras().getInt("level");
				Score score = new Score(level, stars, time);
				if(HighScoreManager.isPossibleHighScore(score, level)){
					HighScoreFragment.showHighScoreFragment(level, stars, time, SimulatorActivity.this);
				}else{
					LevelCompletionFragment.showLevelCompletionFragment(level, stars, time, SimulatorActivity.this);
				}
		}};
        
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        mGLView.onPause();
        sensorManager.unregisterListener(this);
        
        returnFlag = true;
        ((Renderer) ((MyGLSurfaceView)mGLView).mRenderer).clean();
        
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        mGLView.onResume();
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_GAME);
    }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}
	

	double[] accelleration = new double[3];
	double[] tempAccelleration = new double[3];
	Quaternion ud = new Quaternion();
	Quaternion quat = new Quaternion();
	public void onSensorChanged(SensorEvent event) 
	{
		float mag = (float) Math.sqrt(event.values[0]*event.values[0] + 
								event.values[1]*event.values[1] +
								event.values[2]*event.values[2]);

		linear_acceleration[0] = event.values[0]/mag;
		linear_acceleration[1] = event.values[1]/mag;
		linear_acceleration[2] = event.values[2]/mag;
		
    	for(int i = 0; i < world.objects.length; i++)
    	{
    		if(!world.objects[i].infiniteMass)
    		{
    			float scalar = 5.0f;
    			
    			
    			tempAccelleration[0] = scalar*linear_acceleration[1];
    			tempAccelleration[1] = -scalar*linear_acceleration[2];
    			tempAccelleration[2] = scalar*linear_acceleration[0];
    			
    			
    			ud.loadAxisAngle(Math.cos(angleLR-Math.PI), 0, Math.sin(angleLR-Math.PI), Math.PI/2.0-Math.PI/3.5);
    			
    			quat.loadAxisAngle(0, 1, 0, 0-angleLR-Math.PI);
    			quat = ud.times(quat);
    			
    			quat.loadRotationMatrix();
    			quat.transform(tempAccelleration, accelleration);

//    			world.objects[i].constantAcceleration[1] = -3.0*scalar;
    			
//    			world.objects[i].constantAcceleration[0] = -scalar*accelleration[2];
//    			world.objects[i].constantAcceleration[1] = scalar*accelleration[1];
//    			world.objects[i].constantAcceleration[2] = scalar*accelleration[0];
    		}
    	}
			
	}
	
	public abstract void collision(int i, int j);
	public abstract Renderer getNewRenderer();
	public abstract World getNewWorld();
	public abstract Shader getShader();
	public abstract void move(double timeStep);

	public Renderer renderer;
	
	public Renderer getRenderer() {
		if(renderer == null)
			renderer = getNewRenderer();
		return renderer;
	}
}

class MyGLSurfaceView extends GLSurfaceView {

	public double lastDoubleJump = System.nanoTime();
	public double jumpDelay = 1.25;
	public int numJumps = 0;
	public int maxJumps = 1;
	
	public double jumpSpeed = 15.0;
	
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();
            Log.d("Double Tap", "Tapped at: (" + x + "," + y + ")");

        	if(numJumps < maxJumps)
        	{
        		simulation.world.objects[simulation.world.getPlayerIndex()].linearVelocity[1] +=
        				jumpSpeed;
        		numJumps++;
        	}
            			
            
            return true;
        }
	}

    GestureDetector gestureDetector;
    
    public final Renderer mRenderer;

    SimulatorActivity simulation;
    
    public MyGLSurfaceView(Context context, SimulatorActivity simulation) {
        super(context);
        
        gestureDetector = new GestureDetector(context, new GestureListener());

        this.simulation = simulation;
        
        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = simulation.getRenderer();
        
        setRenderer(mRenderer);

        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

	/** Determine the space between the first two fingers */
   private float spacing(MotionEvent event) {
      float x = event.getX(0) - event.getX(1);
      float y = event.getY(0) - event.getY(1);
      return FloatMath.sqrt(x * x + y * y);
   }

   /** Calculate the mid point of the first two fingers */
   private void midPoint(PointF point, MotionEvent event) {
      float x = event.getX(0) + event.getX(1);
      float y = event.getY(0) + event.getY(1);
      point.set(x / 2, y / 2);
   }
	   
   // We can be in one of these 3 states
   final int NONE = 0;
   final int ZOOM = 2;
   int mode = NONE;
   float oldDist = 1f;
   
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();
        
       // Remember some things for zooming
       PointF start = new PointF();
       PointF mid = new PointF();
       switch (event.getAction() & MotionEvent.ACTION_MASK) {
       case MotionEvent.ACTION_DOWN:
          start.set(event.getX(), event.getY());
//          Log.d(TAG, "mode=DRAG");

          break;
       case MotionEvent.ACTION_POINTER_DOWN:
          oldDist = spacing(event);
          if (oldDist > 10f) {
             midPoint(mid, event);
             mode = ZOOM;
//             Log.d(TAG, "mode=ZOOM");
          }
          break;
       case MotionEvent.ACTION_UP:
       case MotionEvent.ACTION_POINTER_UP:
          mode = NONE;
          oldDist = -1;
//          Log.d(TAG, "mode=NONE");
          break;
       case MotionEvent.ACTION_MOVE:

           if (mode == ZOOM) {
             float newDist = spacing(event);
             if (newDist > simulation.minZoom && oldDist != -1) {
            	 simulation.zoom -= (newDist-oldDist)*.05;
             }
             oldDist = newDist;
          }
           else
           {
                 float dz = x - mPreviousX;
                 float dy = y - mPreviousY;
                 
                 float r = (float) Math.sqrt(dz*dz+dy*dy);

//               double theta = Math.atan2(dy, dz)+Math.PI+SimulatorActivity.angleLR;
               double theta = Math.atan2(dy, dz)+Math.PI+SimulatorActivity.angleLR;
                 
                 try
                 {
                	 simulation.world.objects[simulation.world.getPlayerIndex()].omega[0] += .05f*r*Math.cos(theta);
                	 simulation.world.objects[simulation.world.getPlayerIndex()].omega[2] += .05f*r*Math.sin(theta);
                 }
                 catch(Exception e)
                 {
                 }
                 
            	 //MainActivity.angleLR += dx*.01;
                 //simulation.zoom += dy*.01;
           }
          break;
       }

       mPreviousX = x;
       mPreviousY = y;
       
       gestureDetector.onTouchEvent(event);
       
       return true; // indicate event was handled
    }
}
