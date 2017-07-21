//#ifdef GL_ES
precision mediump float;
//#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

uniform float health_t;
uniform float powerupCooldown;


void main()
{

    //if color is red -> healthbar
    //maybe bad performance?
    if(texture2D(u_texture, v_texCoords).r > 0.5f)
    {
        if(gl_FragCoord.x < health_t)
        {
            gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
        }
        else
        {
            discard;
        }
    }
    //else -> powerupcooldown
    else
    {
        if(gl_FragCoord.y > powerupCooldown)
        {
            gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
        }
        else
        {
            discard;
        }
    }
}
