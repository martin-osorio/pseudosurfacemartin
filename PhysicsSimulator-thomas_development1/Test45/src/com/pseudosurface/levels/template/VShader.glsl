#version 100

        // This matrix member variable provides a hook to manipulate
        // the coordinates of the objects that use this vertex shader
uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform float ShaderTime;

uniform vec4 lightDirection;
uniform vec4 lightPosition;
        
attribute vec4 vNormal;
attribute vec4 vPosition;
uniform vec4 vColor;
        
varying vec4 normal;
varying vec4 position;
         
varying vec3 v_Position;
varying vec4 v_Color;
varying vec3 v_Normal;
varying vec3 lpos;

uniform int lightEnabled;
         
void main()
{
    v_Position = (viewMatrix*modelMatrix*vPosition).xyz;
    lpos = (lightPosition).xyz;
         
    v_Color = vColor;
         
    v_Normal = vec3(viewMatrix*modelMatrix * vNormal)-v_Position;
         
   	position = vPosition;
         
    gl_Position = projectionMatrix*viewMatrix*modelMatrix * vPosition;
}