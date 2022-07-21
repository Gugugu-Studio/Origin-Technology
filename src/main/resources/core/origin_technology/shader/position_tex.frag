#version 330 core

in vec2 texCoord0;

out vec4 FragColor;

uniform vec4 ColorModulator;
uniform sampler2D Sampler0;

void main() {
    vec4 color = texture(Sampler0, texCoord0);
    if (color.a == 0.0) {
        discard;
    }
    FragColor = color * ColorModulator;
}
