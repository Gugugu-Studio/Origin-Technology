#pragma once

#include <glad/glad.h>
#include <GLFW/glfw3.h>

typedef unsigned char byte;

struct RGBA {
    byte R;
    byte G;
    byte B;
    byte A;
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
}