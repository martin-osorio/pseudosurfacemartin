#version 100

precision highp float;
        		
uniform float FShaderTime; 

uniform vec4 litMaterialAmbient;
uniform vec4 litMaterialSpecular;
uniform vec4 litMaterialDiffuse;
uniform float litMaterialShininess;

uniform vec4 shadedMaterialAmbient;
uniform vec4 shadedMaterialSpecular;
uniform vec4 shadedMaterialDiffuse;
uniform float shadedMaterialShininess;
        
uniform vec4 lightDirection; 
uniform vec4 anotherLightPosition; 

uniform vec4 ambientLight; 
uniform vec4 diffuseLight; 
uniform vec4 specularLight; 
        
uniform vec3 spherePosition; 
uniform float sphereRadius; 

varying vec2 textureCoordinate;
varying vec3 v_Position; 
varying vec4 v_Color; 
varying vec3 v_Normal; 
varying vec3 lpos; 

varying vec4 position;

uniform int lightEnabled; 
uniform int isAnotherLight; 
uniform int isSkybox; 
uniform int isStarbox;
uniform int isDemon; 

uniform int isGalaxy;

varying vec3 star_color;

const float cosConeAngleA = 1.0;
const float cosConeAngleB = .6;

const float distA = 70.0;
const float distB = 180.0;

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
	
	
	vec4 drawGalaxy()
	{
	   vec3 R = position.xyz;
	   vec3 L = normalize(R);
	   
		
		float time = FShaderTime*.75+20.0;
		
	   float y = textureCoordinate.y*2.0-1.0;
	   
	   float x = textureCoordinate.x*2.0-1.0;


		vec4 p =10.0*vec4(x, y, y*1.2, x*.9);

	   if(length(p) > 10.0) discard;
	   
		//p.x += 0.28*cos(1234.0);
		//p.y += 0.28*sin(432.0);	
		
//		float xnoise = rand(vec2(time, 335.0));
//		float ynoise = rand(vec2(time+20.0, 35.0));
		
		x = (1.25+sin(time*1.5)*.2)*cos(time);
		y = (1.25+cos(time*1.25)*.2)*sin(time);

		vec4 c = vec4(x, y, 0.0, 0.0);
		
		vec4 Z = p*vec4(1.0,1.0, 1.0, 1.0);

		int iterations_temp;
		float depth_trap = 150.0;
		float red; 
		float green;
		
		const float max_iterations =4.0;
		
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
		red = clamp(sin(NIC+.45), 0.0, 1.0);//red*3.0/4.0);
		green = clamp(sin(NIC)*sin(NIC), 0.0, red*3.0/5.0);

		vec4 finalColor1 = vec4(0.0, red+green, 0.0, clamp(red+green, 0.0, 1.0));
		
			vec4 finalColor2 = vec4(green, green, red, red+green);
			finalColor2 = vec4(finalColor2.a*(.5+.5*sin(time)),
								finalColor2.a*(.5+.5*sin(time+4.0*PI/3.0)),
								finalColor2.a*(.5+.5*sin(time+2.0*PI/3.0)),
								finalColor2.a);
			return finalColor2;
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
	vec2 face_scalar = vec2(.75, 1.35)*1.5;

	float getEyeColor()
	{
		vec2 p = textureCoordinate.xy*2.0-1.0;

    	p.y += .225;
    	
		p.x *= 1.5;
		p.xy *= face_scalar;

    	vec4 a = vec4(-.3, .45, 0, 0);
   	 		vec4 b = vec4(.3, .45, 0, 0);
	
		p.y += cos(p.x*8.0)*.15;

   		float distA = length(p.xy-a.xy);
	    float distB = length(p.xy-b.xy);
		
		float fade_lengthA = .20;
		float fade_lengthB = .20;
		
		float color = (1.0-distA/fade_lengthA)*2.0;
		color = clamp(max(color, (1.0-distB/fade_lengthB))*2.0, 0.0, 1.0);
		return color;
	}

	vec4 drawFace()
	{
		vec2 p = textureCoordinate.xy*2.0-1.0;
    
    	p.y += .215;
    
		p.xy *= face_scalar;

		p.x *= 5.0;
		p.y *= 4.0*(cos(p.x/PI/3.0));
		p.y += 1.5*cos(p.x)+(1.25);

		float r = p.x*p.x+p.y*p.y;
		
    	float teeth_time = (.15*sin(FShaderTime*2.0)+.5)*2.0;
		float xy = cos(p.x*30.0)+cos(p.y*3.0+PI)*(teeth_time);
		
		float color = clamp((3.0/r/r-p.y*p.y), 0.0, 1.0)*xy;
		
		color = clamp(color, 0.0, 1.0);

		//vec4 fractal = getFractal(static_effect_skew);
		float eyeColor = getEyeColor();
		
		vec2 tempCoord = (textureCoordinate-.5)*2.0;
		tempCoord.x *= 2.0;
		
		float circle = pow(1.0-length(tempCoord), .25); 
		float horns = 1.0-clamp(tempCoord.y+.75*pow(1.0-1.5*tempCoord.x*tempCoord.x, .5), 0.0, 1.0);
		
		horns = pow(horns, .25);
		
		circle *= horns;
		
   		float distance = length(anotherLightPosition.xyz - v_Position);
   		float distShadeB = clamp(1.0-(distance-distA)/(distB-distA), 0.0, 1.0);     
   
		return sqrt(distShadeB)*vec4(color+eyeColor, color, color, clamp(color+eyeColor, 0.0, 1.0)+circle);
	}
      
void main()                
{

if(isDemon == 1)
{
	gl_FragColor = drawFace();
}
else if(isStarbox == 1)
{
	float x = gl_PointCoord.x*2.0-1.0;
	float y = gl_PointCoord.y*2.0-1.0;
	gl_FragColor = vec4(star_color.r, star_color.g, star_color.b, 
			(1.25-length(gl_PointCoord.xy*2.0-1.0))*
			pow(length(vec2(0.75, .75)-abs(gl_PointCoord.xy*2.0-1.0)), 12.0)
			);
}
else if(isAnotherLight == 1)           
{   
	float x = gl_PointCoord.x*2.0-1.0;
	float y = gl_PointCoord.y*2.0-1.0;
	gl_FragColor = vec4(1.0, 1.0, 0.0, 
			(1.0-length(gl_PointCoord.xy*2.0-1.0))
			);
}
else if(isGalaxy == 1)           
{   
   //discard;
   gl_FragColor = drawGalaxy();                
}                                                                     
else if(lightEnabled == 0)           
{   
          							
   float distance = length(anotherLightPosition.xyz - v_Position);
   
   if(distance > distB) discard;
   
   float distShadeA = max(1.0-distance/distA, 0.0); 
   float distShadeB = clamp(1.0-(distance-distA)/(distB-distA), 0.0, 1.0); 
   
   vec3 R = v_Position-anotherLightPosition.xyz;
       vec3 L = -normalize(R);
   vec3 D = normalize(lightDirection.xyz-anotherLightPosition.xyz);
			
   float cos_cur_angle = dot(-L, D);
   float spotLightAngleShade = clamp((cos_cur_angle - cosConeAngleB) / 
          							 (cosConeAngleA - cosConeAngleB), -1.0, 1.0);
          			
    vec4 finalColor = sqrt(distShadeB)*((1.0-(distShadeA)*(spotLightAngleShade))*shadedMaterialAmbient+(distShadeA)*(spotLightAngleShade)*litMaterialAmbient);
    				 
	finalColor.a = shadedMaterialAmbient.a*sqrt(distShadeB)*(1.0-(distShadeA));
	gl_FragColor = finalColor;                
}                                                                     
else                              
{                              

   float distance = length(anotherLightPosition.xyz - v_Position);
   
   if(distance > distB) discard;
   
   float distShadeA = max(1.0-distance/distA, 0.0);
   float distShadeB = clamp(1.0-(distance-distA)/(distB-distA), 0.0, 1.0);     
   
   vec3 R = v_Position-anotherLightPosition.xyz;
   vec3 L = -normalize(R);
   vec3 D = normalize(lightDirection.xyz-anotherLightPosition.xyz);
			
   float cos_cur_angle = dot(-L, D);
	  bool shaded = false; 
   float spotLightAngleShade = clamp((cos_cur_angle - cosConeAngleB) / 
          							 (cosConeAngleA - cosConeAngleB), -1.0, 1.0);
    // Will be used for attenuation.              
    // Get a lighting direction vector from the light to the vertex.
   vec3 lightVector = normalize(anotherLightPosition.xyz - v_Position);                      
    // Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
    // pointing in the same direction then it will get max illumination.
   float diffuse = max(dot(normalize(v_Normal), lightVector), 0.1);                                                                                                                                                     
    // Add attenuation. 
    // Multiply the color by the diffuse illumination level to get final output color.
      shaded = spotLightAngleShade < 0.0 || rayIntersectsSphere(v_Position.xyz, normalize(anotherLightPosition.xyz-v_Position.xyz).xyz, sphereRadius, spherePosition);
if(!shaded)
   gl_FragColor = sqrt(distShadeB)*vec4((spotLightAngleShade)*(litMaterialAmbient+diffuse*litMaterialDiffuse+shadedMaterialAmbient).rgb, (litMaterialAmbient+diffuse*litMaterialDiffuse+shadedMaterialAmbient).a*litMaterialAmbient.a)+
   				  sqrt(distShadeB)*vec4((1.0-spotLightAngleShade)*(shadedMaterialAmbient+diffuse*shadedMaterialDiffuse+shadedMaterialAmbient).rgb, (shadedMaterialAmbient+diffuse*shadedMaterialDiffuse+shadedMaterialAmbient).a*shadedMaterialAmbient.a);
else
   gl_FragColor = sqrt(distShadeB)*vec4((shadedMaterialAmbient+diffuse*shadedMaterialDiffuse+shadedMaterialAmbient).rgb, (shadedMaterialAmbient+diffuse*shadedMaterialDiffuse+shadedMaterialAmbient).a);
}                                                                                     
}