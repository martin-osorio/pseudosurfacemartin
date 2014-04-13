package com.pseudosurface.levels.level9;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

import com.pseudosurface.levels.template.Renderer;
import com.pseudosurface.levels.template.Shader;

public class Shader9 extends Shader{
	
	
	public String getFShader() {
		return "FShader9.glsl";
	}
	
	public String getVShader() {
		return "VShader9.glsl";
	}
	
	public static int isStarboxHandle;
    public int isGalaxyHandle;
	public void loadAdditionalUniforms() 
	{	
		isStarboxHandle = GLES20.glGetUniformLocation(mProgram, "isStarbox");
		Renderer.checkGlError("glGetUniformLocation"); 
		isGalaxyHandle = GLES20.glGetUniformLocation(mProgram, "isGalaxy");
	    Renderer.checkGlError("glGetUniformLocation");
	}
}
