//#ifdef GL_ES
precision mediump float;
//#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

void main()
{

    //gl_FragColor = vec4(1.0, 0.0, 0.5, 1.0);

    gl_FragColor = v_color * texture2D(u_texture, v_texCoords) * vec4(1.0, .5, 1.0, 1.0);
}
