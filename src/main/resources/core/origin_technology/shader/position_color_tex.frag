#version 330 core

in vec4 vertexColor;
in vec2 texCoord0;

out vec4 FragColor;

uniform vec4 ColorModulator;
uniform sampler2D Sampler0;

void main() {
    FragColor = ColorModulator * texture(Sampler0, texCoord0) * vertexColor;
}
