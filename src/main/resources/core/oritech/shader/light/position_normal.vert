#version 330 core

layout (location = 0) in vec3 Position;
layout (location = 3) in vec3 Normal;

uniform mat4 Projection, View;

out VS_OUT {
    vec4 fragPos;
    vec3 vertexNormal;
} vs_out;

void main() {
    gl_Position = Projection * View * vec4(Position, 1.0);
    // TODO: calc the normal matrix on cpu
    vs_out.fragPos = vec4(Position, 1.0);
    vs_out.vertexNormal = Normal;
}
