package com.pseudosurface.levels.template;

import android.opengl.GLES20;

import com.pseudosurface.levels.level1.Renderer1;
import com.pseudosurface.levels.level1.Shader1;
import com.pseudosurface.physics.ConvexSurface;
import com.pseudosurface.physics.Face;
import com.pseudosurface.physics.Math3D;
import com.pseudosurface.physics.Shapes;
import com.pseudosurface.physics.TangibleFace;
import com.pseudosurface.physics.XZEquation;

public class Demon extends TextureQuad
{
	public Renderer renderer;
	
	public Demon(float width, float height, Renderer renderer) 
	{
		super(width, height, renderer);

		ConvexSurface surface = Shapes.loadCube(width/3, height/1.75, 1);
		
		surface.loadMaxRadius();
		
		color_lit_highligt[0] = 0.0f;
		color_lit_highligt[1] = .0f;
		color_lit_highligt[2] = 0.0f;
		color_lit_highligt[3] = 1.0f;

		color_shadow_highligt[0] = 0f;
		color_shadow_highligt[1] = 0f;
		color_shadow_highligt[2] = 0f;
		color_shadow_highligt[3] = 1.0f;

		color_lit_solid[0] = 0.35f;
		color_lit_solid[1] = 0.55f;
		color_lit_solid[2] = 0.35f;
		color_lit_solid[3] = 1.0f;
		
		color_shadow_solid[0] = 0.0f;//15f;
		color_shadow_solid[1] = 0.15f;
		color_shadow_solid[2] = 0.0f;//15f;
		color_shadow_solid[3] = 1.0f;

		surface.relativeRotation.loadAxisAngle(0, 1, 0, 0);
		surface.relativePosition[0] = 0;
		surface.relativePosition[1] = 0;
		surface.relativePosition[2] = 0;

		surfaces = Math3D.push_back(surface, surfaces);

		density = .06;
		
		this.loadPhysicalProperties();
		
		this.isDecoration = false;
		
		transform(0);
	}

	public void draw(float[] modelMatrix, float[] viewMatrix, float[] projectionMatrix)
	{
	    GLES20.glUseProgram(Renderer1.shader.mProgram);
	    GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
	    GLES20.glEnable(GLES20.GL_BLEND);
	    GLES20.glUniform1i(Shader.isDemonHandle, 1);

        GLES20.glDisable(GLES20.GL_CULL_FACE);
	    super.draw(modelMatrix, viewMatrix, projectionMatrix);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
	    
	    GLES20.glUniform1i(Shader.isDemonHandle, 0);
	    GLES20.glDisable(GLES20.GL_BLEND);
	}
}
