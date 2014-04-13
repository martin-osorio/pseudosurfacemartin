package com.pseudosurface.levels.level1;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

import com.pseudosurface.levels.template.Renderer;
import com.pseudosurface.levels.template.Shader;
import com.pseudosurface.levels.template.TextureQuad;

public class Shader1 extends Shader{
	
	public String getFShader() {
		return "FShader1.glsl";
	}
	
	public String getVShader() {
		return "VShader1.glsl";
	}
	
	public static int isStarboxHandle;
	public void loadAdditionalUniforms() 
	{	
		isStarboxHandle = GLES20.glGetUniformLocation(mProgram, "isStarbox");
		Renderer.checkGlError("glGetUniformLocation"); 
	}
}
