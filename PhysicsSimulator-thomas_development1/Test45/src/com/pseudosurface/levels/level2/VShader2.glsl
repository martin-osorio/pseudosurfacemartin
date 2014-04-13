#version 100

        // This matrix member variable provides a hook to manipulate
        // the coordinates of the objects that use this vertex shader
uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
        
uniform float VShaderTime; 

attribute vec4 vNormal;
attribute vec4 vPosition;
        
varying vec4 normal;
varying vec4 position;
         
varying vec3 v_Position;
varying vec4 v_Color;
varying vec3 v_Normal;
varying vec3 lpos;

attribute vec4 vTexCoord;
varying vec2 textureCoordinate;
varying vec3 star_color;
float rand(vec2 co)
{
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

void main()
{
	textureCoordinate = vTexCoord.xy;
    v_Position = (viewMatrix*modelMatrix*vPosition).xyz;
         
    v_Normal = vec3(viewMatrix*modelMatrix * vNormal)-v_Position;
         
   	position = vPosition;
    
	star_color = vec3(rand(vPosition.xy+.1)*.45+.55,
					rand(vPosition.xy+.2)*.45+.55,
					rand(vPosition.xy+.3)*.45+.55);
	gl_PointSize = rand(vPosition.xy)*(35.0+35.0*sin(2.0*VShaderTime+25.0*rand(vPosition.xy)));
    
    gl_Position = projectionMatrix*viewMatrix*modelMatrix * vPosition;
}