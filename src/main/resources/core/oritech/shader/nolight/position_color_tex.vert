#version 330 core

layout (location = 0) in vec3 Position;
layout (location = 1) in vec4 Color;
layout (location = 2) in vec2 UV0;

out vec4 vertexColor;
out vec2 texCoord0;

layout (std140) uniform MVPMatrix {
    mat4 Projection;
    mat4 View;
    mat4 Model;
};

void main() {
    gl_Position = Projection * View * Model  * vec4(Position, 1.0);
    vertexColor = Color;
    texCoord0 = UV0;
}
