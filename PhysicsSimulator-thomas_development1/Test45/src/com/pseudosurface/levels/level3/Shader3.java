package com.pseudosurface.levels.level3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

import com.pseudosurface.levels.template.Renderer;
import com.pseudosurface.levels.template.Shader;

public class Shader3 extends Shader{
	
    public int isGalaxyHandle;
	
	public String getFShader() {
		return "FShader3.glsl";
	}
	
	public String getVShader() {
		return "VShader3.glsl";
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
