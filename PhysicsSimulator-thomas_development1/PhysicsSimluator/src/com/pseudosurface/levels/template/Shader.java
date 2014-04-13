package com.pseudosurface.levels.template;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.opengl.GLES20;
import android.opengl.Matrix;

public abstract class Shader
{
    private String vertexShaderCode;

    private String fragmentShaderCode;   

    public int mProgram;
    public int mTexCoordHandle;
    public int mPositionHandle;
    public int mNormalHandle;
    public int mColorHandle;
    public int lightPerspectiveHandle;
    public int modelMatrixHandle;
    public int viewMatrixHandle;
    public int projectionMatrixHandle;
    public int mFShaderTimeHandle;
    public int mVShaderTimeHandle;


    public int litMaterialAmbientHandle;
    public int litMaterialSpecularHandle;
    public int litMaterialDiffuseHandle;
    public int litMaterialShininessHandle;

    public int shadedMaterialAmbientHandle;
    public int shadedMaterialSpecularHandle;
    public int shadedMaterialDiffuseHandle;
    public int shadedMaterialShininessHandle;

    public int lightDirectionHandle;
    public int lightPositionHandle;

    public int ambientLightHandle;
    public int diffuseLightHandle;
    public int specularLightHandle;

    public int spherePositionHandle;
    public int sphereRadiusHandle;

	public int lightEnabledHandle;
	public int isSkyboxHandle;
	public int isLightHandle;
	public int isStarboxHandle;

    int vertexShader;
    int fragmentShader;

	public static int isDemonHandle;
    public Shader()
    {

    	vertexShaderCode = loadResource(getVShader());
    	fragmentShaderCode = loadResource(getFShader());
    	
	    // prepare shaders and OpenGL program
	    vertexShader = Renderer.loadShader(GLES20.GL_VERTEX_SHADER,
	                                              vertexShaderCode);
	    fragmentShader = Renderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
	                                               fragmentShaderCode);
	
	    mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        Renderer.checkGlError("glCreateProgram");
	    GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        Renderer.checkGlError("glAttachShader");
	    GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        Renderer.checkGlError("glAttachShader");
	    GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
        Renderer.checkGlError("glAttachShader");

        mTexCoordHandle = GLES20.glGetAttribLocation(mProgram, "vTexCoord");
        Renderer.checkGlError("glGetUniformLocation");
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        Renderer.checkGlError("glGetUniformLocation");
        mNormalHandle = GLES20.glGetAttribLocation(mProgram, "vNormal");
        Renderer.checkGlError("glGetUniformLocation");
        //System.err.println("mShaderTimeHandle = " + mShaderTimeHandle);
        mFShaderTimeHandle = GLES20.glGetUniformLocation(mProgram, "FShaderTime");
        Renderer.checkGlError("glGetUniformLocation");

        mVShaderTimeHandle = GLES20.glGetUniformLocation(mProgram, "VShaderTime");
        Renderer.checkGlError("glGetUniformLocation");
        
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        Renderer.checkGlError("glGetUniformLocation");
        
    	isDemonHandle = GLES20.glGetUniformLocation(mProgram, "isDemon");
    	Renderer.checkGlError("glGetUniformLocation");
    	
        lightPerspectiveHandle = GLES20.glGetUniformLocation(mProgram, "lightPerspective");
        Renderer.checkGlError("glGetUniformLocation");
        modelMatrixHandle = GLES20.glGetUniformLocation(mProgram, "modelMatrix");
        Renderer.checkGlError("glGetUniformLocation");
        viewMatrixHandle = GLES20.glGetUniformLocation(mProgram, "viewMatrix");
        Renderer.checkGlError("glGetUniformLocation");
        projectionMatrixHandle = GLES20.glGetUniformLocation(mProgram, "projectionMatrix");
        Renderer.checkGlError("glGetUniformLocation");

        litMaterialAmbientHandle = GLES20.glGetUniformLocation(mProgram, "litMaterialAmbient");
        Renderer.checkGlError("glGetUniformLocation");
        litMaterialSpecularHandle = GLES20.glGetUniformLocation(mProgram, "litMaterialSpecular");
        Renderer.checkGlError("glGetUniformLocation");
        litMaterialDiffuseHandle = GLES20.glGetUniformLocation(mProgram, "litMaterialDiffuse");
        Renderer.checkGlError("glGetUniformLocation");
        litMaterialShininessHandle = GLES20.glGetUniformLocation(mProgram, "litMaterialShininess");
        Renderer.checkGlError("glGetUniformLocation");

        shadedMaterialAmbientHandle = GLES20.glGetUniformLocation(mProgram, "shadedMaterialAmbient");
        Renderer.checkGlError("glGetUniformLocation");
        shadedMaterialSpecularHandle = GLES20.glGetUniformLocation(mProgram, "shadedMaterialSpecular");
        Renderer.checkGlError("glGetUniformLocation");
        shadedMaterialDiffuseHandle = GLES20.glGetUniformLocation(mProgram, "shadedMaterialDiffuse");
        Renderer.checkGlError("glGetUniformLocation");
        shadedMaterialShininessHandle = GLES20.glGetUniformLocation(mProgram, "shadedMaterialShininess");
        Renderer.checkGlError("glGetUniformLocation");

        lightDirectionHandle = GLES20.glGetUniformLocation(mProgram, "lightDirection");
        Renderer.checkGlError("glGetUniformLocation");
        lightPositionHandle = GLES20.glGetUniformLocation(mProgram, "anotherLightPosition");
        Renderer.checkGlError("glGetUniformLocation");

        ambientLightHandle = GLES20.glGetUniformLocation(mProgram, "ambientLight");
        Renderer.checkGlError("glGetUniformLocation");
        diffuseLightHandle = GLES20.glGetUniformLocation(mProgram, "diffuseLight");
        Renderer.checkGlError("glGetUniformLocation");
        specularLightHandle = GLES20.glGetUniformLocation(mProgram, "specularLight");
        Renderer.checkGlError("glGetUniformLocation");

        spherePositionHandle = GLES20.glGetUniformLocation(mProgram, "spherePosition");
        Renderer.checkGlError("glGetUniformLocation");
        sphereRadiusHandle = GLES20.glGetUniformLocation(mProgram, "sphereRadius");
        Renderer.checkGlError("glGetUniformLocation");
        
        lightEnabledHandle = GLES20.glGetUniformLocation(mProgram, "lightEnabled");
        Renderer.checkGlError("glGetUniformLocation");

        isSkyboxHandle = GLES20.glGetUniformLocation(mProgram, "isSkybox");
        Renderer.checkGlError("glGetUniformLocation");
        isLightHandle = GLES20.glGetUniformLocation(mProgram, "isAnotherLight");
        Renderer.checkGlError("glGetUniformLocation");
        isStarboxHandle = GLES20.glGetUniformLocation(mProgram, "isStarbox");
        Renderer.checkGlError("glGetUniformLocation");
        
        loadAdditionalUniforms();
    }

    public abstract String getFShader();
    public abstract String getVShader();
    public abstract void loadAdditionalUniforms();
    
	String loadResource(String name)
	{
		BufferedReader br = new BufferedReader( new InputStreamReader( this.getClass().getResourceAsStream(name) ) );
		String line;
		
		String text = "";
		try{
			while((line = br.readLine()) != null){
				text += line + "\n";
			}
			br.close();
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return text;
	}
	
	public void reloadLights(float[] viewMatrix, float[] lightPos, float[] lightLook) 
	{
        // Calculate position of the light. Rotate and then push into the distance.
		float[] mLightModelMatrix = new float[16];
        Matrix.setIdentityM(mLightModelMatrix, 0);
        Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, 0.0f);      
        Matrix.rotateM(mLightModelMatrix, 0, 0.0f, 0.0f, 1.0f, 0.0f);
        Matrix.translateM(mLightModelMatrix, 0, lightPos[0], lightPos[1], lightPos[2]);
        float[] mLightPosInWorldSpace = new float[16];
        float[] mLightPosInEyeSpace = new float[16];
		float[] position = {0, 0, 0, 1};
        Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, position, 0);
        Matrix.multiplyMV(mLightPosInEyeSpace, 0, viewMatrix, 0, mLightPosInWorldSpace, 0);
        
        mLightModelMatrix = new float[16];
        Matrix.setIdentityM(mLightModelMatrix, 0);
        Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, 0.0f);      
        Matrix.rotateM(mLightModelMatrix, 0, 0.0f, 0.0f, 1.0f, 0.0f);
        Matrix.translateM(mLightModelMatrix, 0, lightLook[0], lightLook[1], lightLook[2]);
        float[] mLightDirInWorldSpace = new float[16];
        float[] mLightDirInEyeSpace = new float[16];
		position = new float[] {0, 0, 0, 1};
        Matrix.multiplyMV(mLightDirInWorldSpace, 0, mLightModelMatrix, 0, position, 0);
        Matrix.multiplyMV(mLightDirInEyeSpace, 0, viewMatrix, 0, mLightDirInWorldSpace, 0);

		float[] ambient = {.75f, .75f, .75f, 0.0f};
		float[] diffuse = {1, 1, 1, 1};//.75f, .75f, .75f, 0.0f};
		float[] specular = {.25f, .25f, .25f, 0.0f};

        GLES20.glUniform4fv(lightPositionHandle, 1, mLightPosInEyeSpace, 0);
        GLES20.glUniform4fv(lightDirectionHandle, 1, mLightDirInEyeSpace, 0);

        GLES20.glUniform4fv(ambientLightHandle, 1, ambient, 0);
        GLES20.glUniform4fv(diffuseLightHandle, 1, diffuse, 0);
        GLES20.glUniform4fv(specularLightHandle, 1, specular, 0);

        GLES20.glUniform1f(mFShaderTimeHandle, (float) ((float) System.nanoTime()/1E9));
	}
	
	public void loadMaterial(float[] lit, float[] shaded)
	{
		float[] litAmbient = {.75f*lit[0], .75f*lit[1], .75f*lit[2], .75f*lit[3]};
		float[] litDiffuse = {.85f*lit[0], .85f*lit[1], .85f*lit[2], .85f*lit[3]};
		float[] litSpecular = {.25f*lit[0], .25f*lit[1], .25f*lit[2], .25f*lit[3]};

		float[] shadedAmbient = {.75f*shaded[0], .75f*shaded[1], .75f*shaded[2], shaded[3]};
		float[] shadedDiffuse = {.5f*shaded[0], .5f*shaded[1], .5f*shaded[2], shaded[3]};
		float[] shadedSpecular = {.25f*shaded[0], .25f*shaded[1], .25f*shaded[2], shaded[3]};

        GLES20.glUniform4fv(litMaterialAmbientHandle, 1, litAmbient, 0);
        GLES20.glUniform4fv(litMaterialSpecularHandle, 1, litSpecular, 0);
        GLES20.glUniform4fv(litMaterialDiffuseHandle, 1, litDiffuse, 0);
        GLES20.glUniform1f(litMaterialShininessHandle, .1f);

        GLES20.glUniform4fv(shadedMaterialAmbientHandle, 1, shadedAmbient, 0);
        GLES20.glUniform4fv(shadedMaterialSpecularHandle, 1, shadedSpecular, 0);
        GLES20.glUniform4fv(shadedMaterialDiffuseHandle, 1, shadedDiffuse, 0);
        GLES20.glUniform1f(litMaterialShininessHandle, .05f);
	}
	
	public void loadSphereShadow(double[] spherePosition, double sphereRadius, float[] viewMatrix)
	{
		float[] mSphereModelMatrix = new float[16];
        Matrix.setIdentityM(mSphereModelMatrix, 0);
        Matrix.translateM(mSphereModelMatrix, 0, 0.0f, 0.0f, 0.0f);      
        Matrix.rotateM(mSphereModelMatrix, 0, 0.0f, 0.0f, 1.0f, 0.0f);
        Matrix.translateM(mSphereModelMatrix, 0, 
        		(float) spherePosition[0],
        		(float) spherePosition[1],
        		(float) spherePosition[2]);
        float[] mSpherePosInWorldSpace = new float[16];
        float[] mSpherePosInEyeSpace = new float[16];
		float[] position = {0, 0, 0, 1};
        Matrix.multiplyMV(mSpherePosInWorldSpace, 0, mSphereModelMatrix, 0, position, 0);
        Matrix.multiplyMV(mSpherePosInEyeSpace, 0, viewMatrix, 0, mSpherePosInWorldSpace, 0);
        
        GLES20.glUniform3fv(spherePositionHandle, 1, mSpherePosInEyeSpace, 0);
        GLES20.glUniform1f(sphereRadiusHandle, (float) sphereRadius);
	}
	
	public void clean()
	{
    	GLES20.glDeleteShader(vertexShader);
    	GLES20.glDeleteShader(fragmentShader); 
    	GLES20.glDeleteProgram(mProgram);
	}
}


