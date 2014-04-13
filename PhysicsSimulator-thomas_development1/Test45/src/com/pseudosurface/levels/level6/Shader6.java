package com.pseudosurface.levels.level6;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

import com.pseudosurface.levels.template.Renderer;
import com.pseudosurface.levels.template.Shader;

public class Shader6 extends Shader{
	
	public String getFShader() {
		return "FShader6.glsl";
	}
	
	public String getVShader() {
		return "VShader6.glsl";
	}
	
	public static int isDemonHandle;

    public int isGalaxyHandle;
	public void loadAdditionalUniforms() 
	{	 
		isDemonHandle = GLES20.glGetUniformLocation(mProgram, "isDemon");
		Renderer.checkGlError("glGetUniformLocation");
		isGalaxyHandle = GLES20.glGetUniformLocation(mProgram, "isGalaxy");
	    Renderer.checkGlError("glGetUniformLocation");
	}
}
