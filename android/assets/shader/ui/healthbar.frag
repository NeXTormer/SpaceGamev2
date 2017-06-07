//#ifdef GL_ES
precision mediump float;
//#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

uniform float health;
uniform vec4 origin;

void main()
{

    //gl_FragColor = vec4(1.0, 0.0, 0.5, 1.0);


    if(origin.x < health)
    {
        gl_FragColor = v_color * texture2D(u_texture, v_texCoords) * vec4(0.5, 0.1, 1, 1.0) + vec4(0.6, 0.1, 0.1, 0);

    }
    else
    {
        gl_FragColor = vec4(1.0, 0, 0, 1);
    }
}
