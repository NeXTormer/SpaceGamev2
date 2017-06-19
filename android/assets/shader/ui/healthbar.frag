//#ifdef GL_ES
precision mediump float;
//#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

uniform float health_t;

void main()
{


    //if(texture2D(u_texture, v_texCoords).r == 166.0)
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

    //xdiff = origin.x - position.x;

    //if(origin.x < health_t)
    {
      //  gl_FragColor = v_color * texture2D(u_texture, v_texCoords) * vec4(v_texCoords.xy, 0.1, 0);

    }
    //else
    {
      //  gl_FragColor = vec4(1, 1, 1, 1);
    }
}
