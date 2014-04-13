#version 100

precision mediump float;
        		
uniform mat4 modelMatrix; 
uniform mat4 viewMatrix; 
uniform mat4 projectionMatrix; 
uniform float ShaderTime; 

uniform vec4 litMaterialAmbient;
uniform vec4 litMaterialSpecular;
uniform vec4 litMaterialDiffuse;
uniform float litMaterialShininess;

uniform vec4 shadedMaterialAmbient;
uniform vec4 shadedMaterialSpecular;
uniform vec4 shadedMaterialDiffuse;
uniform float shadedMaterialShininess;
        
uniform vec4 lightDirection; 
uniform vec4 lightPosition; 

uniform vec4 ambientLight; 
uniform vec4 diffuseLight; 
uniform vec4 specularLight; 
        
uniform vec3 spherePosition; 
uniform float sphereRadius; 

varying vec3 v_Position; 
varying vec4 v_Color; 
varying vec3 v_Normal; 
varying vec3 lpos; 

varying vec4 position;

uniform int lightEnabled; 
uniform int isLight; 
uniform int isSkybox; 

const float cosConeAngleA = 1.0;
const float cosConeAngleB = .80;

const float distA = 30.0;
const float distB = 50.0;

float PI = 3.14159265359;

vec4 complex(float real, float imaginary)
{
	return vec4(real, imaginary, false, false);
}

vec4 c_multiply(vec4 a, vec4 b)
{
    return complex(a.x * b.x - a.y * b.y,
                         a.x * b.y + a.y * b.x);
}


	float tanh(float val)
	{
		float tmp = exp(val);
		float tanH = (tmp - 1.0 / tmp) / (tmp + 1.0 / tmp);
		return tanH;
	}
	
	vec4 backdrop()
	{
	   vec3 R = position.xyz;
	   vec3 L = normalize(R);
	   
		
		float time = ShaderTime*.75+20.0;
		
	   float y = 14.0*(1.6-acos(L.y));
	   
	   y+= 6.0;
	   
	   bool negative = false;
	   
		vec4 skyBlue = vec4(.45, .45, .75, 0.0);
		vec4 dirtBrown 	= vec4(139.0/255.0, 95.0/255.0, 20.0/255.0, 0.0);
		
	   if(y > 6.0) return skyBlue;
	   else if ( y < -6.0) return dirtBrown;
	   
	   if(y < 0.0) negative = true;
	   
		vec4 earthyNess = skyBlue*clamp(y*4.0, 0.0, 1.0) + dirtBrown*clamp(1.0-y*4.0, 0.0, 1.0);
		float rootyNess = 1.0-abs(clamp(y, -1.0, 0.0));
	   
	   float x = 8.0*abs(1.0*atan(L.z/L.x));

		if(y < 0.0)
		{
			y *= 1.0;
			x += .35*cos(y*2.0);
		}

	   float tempX = y*cos(x+PI/4.0);
	   float tempY = y*sin(x+PI/4.0);;
	   
	   x = tempX;
	   y = tempY;
	   
		vec4 p =vec4(x, y, y*1.2, x*.9);

		//p.x += 0.28*cos(1234.0);
		//p.y += 0.28*sin(432.0);	
		
//		float xnoise = rand(vec2(time, 335.0));
//		float ynoise = rand(vec2(time+20.0, 35.0));
		
		x = (1.25+sin(time*1.5)*.2)*cos(time);
		y = (1.25+cos(time*1.25)*.2)*sin(time);

		vec4 c = vec4(x, y, 0.0, 0.0);
		
		vec4 Z = p*vec4(1.0,1.0, 1.0, 1.0);

		int iterations_temp;
		const float max_iterations =6.0;
		float depth_trap = 150.0;
		for(int iterations=0; iterations < int(max_iterations); iterations++) 
		{
			//Z =Z^3+C
				Z = c + c_multiply(Z, vec4(tanh(Z.x), tanh(Z.y), 1.0, 1.0));
				
				if(dot(Z,Z)>depth_trap) {
						break;
				}
			iterations_temp = iterations;
		}
		
		float NIC = (Z.x * Z.x) + (Z.y * Z.y);
		NIC = float(iterations_temp)/max_iterations-log(sqrt(NIC))/float(iterations_temp);
		float red = clamp(sin(NIC+.45), 0.0, 1.0);//red*3.0/4.0);
		float green = clamp(sin(NIC)*sin(NIC), 0.0, red*3.0/5.0);

		vec4 finalColor1 = .345*vec4(0.0, red+green, 0.0, clamp(red+green, 0.0, 1.0))+earthyNess*(1.0-red-green);
		
		if(length(finalColor1) < .0125)
		{
			if(negative) return dirtBrown;
			else return skyBlue;
		}
		else
			return abs(rootyNess)*.345*vec4(0.0, red+green, 0.0, clamp(red+green, 0.0, 1.0))+earthyNess*(1.0-red-green);
	}
	
	bool rayIntersectsSphere(vec3 rayStart, vec3 rayVector, float sphere_radius, vec3 sphere_position)
	{
		float dist = dot(sphere_position-rayStart, rayVector);
       	if(dist < 0.0 ) return false;
		vec3 pos = dist*rayVector+rayStart;
		dist = length(pos-sphere_position);
		if(dist < sphere_radius-.001) return true;
		return false;
	}
      
void main()                
{
if(isLight == 1)           
{   
	gl_FragColor = vec4(1.0, 1.0, 0.0, 1.0);
}
else if(isSkybox == 1)           
{   
   //discard;
   gl_FragColor = backdrop();                
}                                                                     
else if(lightEnabled == 0)           
{   
          							
   float distance = length(lightPosition.xyz - v_Position);
   
   if(distance > distB) discard;
   
   float distShadeA = max(1.0-distance/distA, 0.0); 
   float distShadeB = clamp(1.0-(distance-distA)/(distB-distA), 0.0, 1.0); 
   
   vec3 R = v_Position-lightPosition.xyz;
       vec3 L = -normalize(R);
   vec3 D = normalize(lightDirection.xyz-lightPosition.xyz);
			
   float cos_cur_angle = dot(-L, D);
   float spotLightAngleShade = clamp((cos_cur_angle - cosConeAngleB) / 
          							 (cosConeAngleA - cosConeAngleB), -1.0, 1.0);
          							 
	gl_FragColor = sqrt(distShadeB)*((1.0-(distShadeA)*(spotLightAngleShade))*shadedMaterialAmbient+(distShadeA)*(spotLightAngleShade)*litMaterialAmbient);                
}                                                                     
else                              
{                              

   float distance = length(lightPosition.xyz - v_Position);
   
   if(distance > distB) discard;
   
   float distShadeA = max(1.0-distance/distA, 0.0);
   float distShadeB = clamp(1.0-(distance-distA)/(distB-distA), 0.0, 1.0);     
   
   vec3 R = v_Position-lightPosition.xyz;
   vec3 L = -normalize(R);
   vec3 D = normalize(lightDirection.xyz-lightPosition.xyz);
			
   float cos_cur_angle = dot(-L, D);
	  bool shaded = false; 
   float spotLightAngleShade = clamp((cos_cur_angle - cosConeAngleB) / 
          							 (cosConeAngleA - cosConeAngleB), -1.0, 1.0);
    // Will be used for attenuation.              
    // Get a lighting direction vector from the light to the vertex.
   vec3 lightVector = normalize(lightPosition.xyz - v_Position);                      
    // Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
    // pointing in the same direction then it will get max illumination.
   float diffuse = max(dot(normalize(v_Normal), lightVector), 0.1);                                                                                                                                                     
    // Add attenuation. 
    // Multiply the color by the diffuse illumination level to get final output color.
      shaded = spotLightAngleShade < 0.0 || rayIntersectsSphere(v_Position.xyz, normalize(lightPosition.xyz-v_Position.xyz).xyz, sphereRadius, spherePosition);
if(!shaded)
   gl_FragColor = sqrt(distShadeB)*vec4((spotLightAngleShade)*(litMaterialAmbient+diffuse*litMaterialDiffuse+shadedMaterialAmbient).rgb, (litMaterialAmbient+diffuse*litMaterialDiffuse+shadedMaterialAmbient).a)+
   				  sqrt(distShadeB)*vec4((1.0-spotLightAngleShade)*(shadedMaterialAmbient+diffuse*shadedMaterialDiffuse+shadedMaterialAmbient).rgb, (shadedMaterialAmbient+diffuse*shadedMaterialDiffuse+shadedMaterialAmbient).a);
else
   gl_FragColor = sqrt(distShadeB)*vec4((shadedMaterialAmbient+diffuse*shadedMaterialDiffuse+shadedMaterialAmbient).rgb, (shadedMaterialAmbient+diffuse*shadedMaterialDiffuse+shadedMaterialAmbient).a);
}                                                                                     
}