#version 330 core

layout (location = 0) in vec3 Position;
layout (location = 2) in vec2 UV0;

out vec2 texCoord0;

uniform mat4 Projection, View, Model;

void main() {
    gl_Position = Projection * View * Model * vec4(Position, 1.0);
    texCoord0 = UV0;
}
