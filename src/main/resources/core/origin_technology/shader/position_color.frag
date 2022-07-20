#version 330 core

in vec4 vertexColor;

out vec4 FragColor;

uniform vec4 ColorModulator;

void main() {
    vec4 color = ColorModulator * vertexColor;
    if (color.a == 0.0) {
        discard;
    }
    FragColor = color;
}
