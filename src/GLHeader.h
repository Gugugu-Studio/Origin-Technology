#pragma once

#include <glad/glad.h>
#include <GLFW/glfw3.h>

struct RGBA {
    GLbyte R;
    GLbyte G;
    GLbyte B;
    GLbyte A;
};

struct GLRGBA {
    GLfloat R;
    GLfloat G;
    GLfloat B;
    GLfloat A;
};

inline GLRGBA TurnGLRGBA(RGBA color) {
    GLRGBA glColor;
    glColor.R = color.R / 255.0f;
    glColor.G = color.G / 255.0f;
    glColor.B = color.B / 255.0f;
    glColor.A = color.A / 255.0f;
    return glColor;
}