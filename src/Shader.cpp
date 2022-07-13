#include <Shader.h>

#include <fstream>
#include <sstream>
#include <iostream>

#define LOG_SIZE (1024)

Shader::Shader(std::string vertexShader, std::string fragmentShader)
    : programId(glCreateProgram()) {
    std::string vertexShaderCode, fragmentShaderCode;

    std::ifstream vertexShaderFile, fragmentShaderFile;
    std::stringstream vertexShaderStream, fragmentShaderStream;

    vertexShaderFile.exceptions(std::ifstream::failbit | std::ifstream::badbit);
    fragmentShaderFile.exceptions(std::ifstream::failbit | std::ifstream::badbit);

    try {
        vertexShaderFile.open(vertexShader.c_str());
        fragmentShaderFile.open(fragmentShader.c_str());

        vertexShaderStream << vertexShaderFile.rdbuf();
        fragmentShaderStream << fragmentShaderFile.rdbuf();

        vertexShaderFile.close();
        fragmentShaderFile.close();

        vertexShaderCode = vertexShaderStream.str();
        fragmentShaderCode = fragmentShaderStream.str();
    }
    catch (std::ifstream::failure e) {
        std::cout << "Couldn't read shader file!" << std::endl;
        return;
    }
    const char *vertexShaderBuffer = vertexShaderCode.c_str(), *fragmentShaderBuffer = fragmentShaderCode.c_str();

    //------------------------------------------

    int success;
    char log[LOG_SIZE];

    GLuint vertexShaderId = glCreateShader(GL_VERTEX_SHADER), fragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER);
    glShaderSource(vertexShaderId, 1, &vertexShaderBuffer, NULL);
    glCompileShader(vertexShaderId);

    glGetShaderiv(vertexShaderId, GL_COMPILE_STATUS, &success);
    if(success == 0) {
        glGetShaderInfoLog(vertexShaderId, LOG_SIZE, NULL, log);
        std::cout << "Vertex Shader Compilation failed!\n" << log << std::endl;
        return;
    }

    glShaderSource(fragmentShaderId, 1, &fragmentShaderBuffer, NULL);
    glCompileShader(fragmentShaderId);

    glGetShaderiv(fragmentShaderId, GL_COMPILE_STATUS, &success);
    if(success == 0) {
        glGetShaderInfoLog(fragmentShaderId, LOG_SIZE, NULL, log);
        std::cout << "Fragment Shader Compilation failed!\n"  << log << std::endl;
        return;
    }

    //------------------------------------------

    glAttachShader(this->programId, vertexShaderId);
    glAttachShader(this->programId, fragmentShaderId);
    glLinkProgram(this->programId);

    glGetProgramiv(this->programId, GL_LINK_STATUS, &success);
    if (success == 0) {
        glGetProgramInfoLog(this->programId, LOG_SIZE, NULL, log);
        std::cout << "Shader Program Linking failed!\n"  << log << std::endl;
        return;
    }

    glDeleteShader(vertexShaderId);
    glDeleteShader(fragmentShaderId);
}

Shader::~Shader() {
    glDeleteProgram(this->programId);
}

void Shader::Use() {
    glUseProgram(this->programId);
}