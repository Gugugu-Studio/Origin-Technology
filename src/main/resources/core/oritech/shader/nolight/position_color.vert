#version 330 core

layout (location = 0) in vec3 Position;
layout (location = 1) in vec4 Color;

out vec4 vertexColor;

uniform mat4 Projection, View;

void main() {
    gl_Position = Projection * View * vec4(Position, 1.0);
    vertexColor = Color;
}
