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
    if(texture2D(u_texture, v_texCoords).r > 0.5)
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



    // -> powerupcooldown
    //else
    //    {
    //        if((1080.0 - gl_FragCoord.y) < 200.0)
    //        {
    //            //gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
    //            gl_FragColor = vec4(1.0, 1.0, 1.0, 0.5);
    //        }
    //        else
    //        {
    //            //discard;
    //            gl_FragColor = vec4(0.0, 0.4, 1.0, 1.0);
    //        }
    //        //gl_FragColor = vec4(0.0, (1080.0 - gl_FragCoord.y)/300.0, 1.0, 1.0);
    //    }
}
