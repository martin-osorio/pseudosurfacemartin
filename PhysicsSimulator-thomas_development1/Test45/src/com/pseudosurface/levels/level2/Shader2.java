package com.pseudosurface.levels.level2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

import com.pseudosurface.levels.template.Renderer;
import com.pseudosurface.levels.template.Shader;

public class Shader2 extends Shader{
	
    public int isGalaxyHandle;
	
	public String getFShader() {
		return "FShader2.glsl";
	}
	
	public String getVShader() {
		return "VShader2.glsl";
	}
	
	public static int isStarboxHandle;
	public void loadAdditionalUniforms() 
	{	
		isStarboxHandle = GLES20.glGetUniformLocation(mProgram, "isStarbox");
		Renderer.checkGlError("glGetUniformLocation"); 
		isGalaxyHandle = GLES20.glGetUniformLocation(mProgram, "isGalaxy");
	    Renderer.checkGlError("glGetUniformLocation");
	}
}
