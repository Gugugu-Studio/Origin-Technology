#pragma once

#include <string>
#include <GLHeader.h>

class Shader {
    GLuint programId;
public:
    Shader(std::string vertexShader, std::string fragmentShader);
    ~Shader();
    void Use();
};