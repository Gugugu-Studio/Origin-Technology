#version 330 core

out vec4 FragColor;

uniform vec4 ColorModulator;

void main() {
    FragColor = ColorModulator;
}
