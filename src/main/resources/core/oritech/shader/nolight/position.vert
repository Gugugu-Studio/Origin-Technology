#version 330 core

layout (location = 0) in vec3 Position;

layout (std140) uniform MVPMatrix {
    mat4 Projection;
    mat4 View;
    mat4 Model;
};

void main() {
    gl_Position = Projection * View * Model * vec4(Position, 1.0);
}
