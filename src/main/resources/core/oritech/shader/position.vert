#version 330 core

layout (location = 0) in vec3 Position;

uniform mat4 Projection, View, Model;

void main() {
    gl_Position = Projection * View * Model * vec4(Position, 1.0);
}
