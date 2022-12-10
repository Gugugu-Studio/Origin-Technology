#version 330 core

layout (location = 0) in vec3 Position;
layout (location = 1) in vec4 Color;
layout (location = 2) in vec2 UV0;
layout (location = 3) in vec3 Normal;

out VS_OUT {
    vec4 fragPos;
    vec4 vertexColor;
    vec3 vertexNormal;
    vec2 texCoord0;
} vs_out;

layout (std140) uniform MVPMatrix {
    mat4 Projection;
    mat4 View;
    mat4 Model;
};

void main() {
    gl_Position = Projection * View * Model * vec4(Position, 1.0);
    vs_out.fragPos = vec4(Position, 1.0);
    vs_out.vertexColor = Color;
    vs_out.texCoord0 = UV0;
    // TODO: calc the normal matrix on cpu
    vs_out.vertexNormal = Normal;
}
