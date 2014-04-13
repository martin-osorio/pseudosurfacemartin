package com.pseudosurface.levels.level5;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

import com.pseudosurface.levels.template.Renderer;
import com.pseudosurface.levels.template.Shader;

public class Shader5 extends Shader{
	
	public String getFShader() {
		return "FShader5.glsl";
	}
	
	public String getVShader() {
		return "VShader5.glsl";
	}
	
	public static int isDemonHandle;

	
	public static int isStarboxHandle;
    public int isGalaxyHandle;
	public void loadAdditionalUniforms() 
	{	 
		isDemonHandle = GLES20.glGetUniformLocation(mProgram, "isDemon");
		Renderer.checkGlError("glGetUniformLocation");

		isStarboxHandle = GLES20.glGetUniformLocation(mProgram, "isStarbox");
		Renderer.checkGlError("glGetUniformLocation"); 
		isGalaxyHandle = GLES20.glGetUniformLocation(mProgram, "isGalaxy");
	    Renderer.checkGlError("glGetUniformLocation");
	}
}
