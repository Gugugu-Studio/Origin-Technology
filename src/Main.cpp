#include <GLHeader.h>

#include <iostream>
#include <cmath>

#include <Window.h>

void InitGLAD(bool &success) {
    if (! gladLoadGLLoader((GLADloadproc)glfwGetProcAddress)) {
        std::cout << "Failed to initialize GLAD" << std::endl;
        success = false;
        return;
    }
    success = true;
}

const char *vertexShaderSource ="\
#version 330 core\n\
layout (location = 0) in vec3 aPos;\n\
void main() {\n\
   gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);\n\
}";

const char *fragmentShaderSource ="\
#version 330 core\n\
out vec4 FragColor;\n\
void main() {\n\
   FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);\n\
}";

float vertices[] = {
    -0.5f, -0.5f, 0.0f,
     0.5f, -0.5f, 0.0f,
     0.0f,  0.5f, 0.0f
};

unsigned int shaderProgram;
unsigned int VAO;
unsigned int VBO;

class GameWindow : public Window {
public:
    GameWindow(bool &success) : Window("Unknown Game", 800, 600, GLRGBA{0.2f, 0.5f, 0.3f, 1.0f}, InitGLAD, success) {
    }
    virtual void Init() override final {
        glGenVertexArrays(1, &VAO);
        glGenBuffers(1, &VBO);

        glBindVertexArray(VAO);
        glBindBuffer(GL_ARRAY_BUFFER, VBO);

        glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 3 * sizeof(float), (void*)0);
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        //------------------

        int success;
        char infoLog[512];

        unsigned int vertexShader;
        vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, 1, &vertexShaderSource, NULL);
        glCompileShader(vertexShader);

        glGetShaderiv(vertexShader, GL_COMPILE_STATUS, &success);
        if(success != 0) {
            glGetShaderInfoLog(vertexShader, 512, NULL, infoLog);
            std::cout << "Vertex Shader Compilation failed!\n" << infoLog << std::endl;
        }

        unsigned int fragmentShader;
        fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, 1, &fragmentShaderSource, NULL);
        glCompileShader(fragmentShader);

        glGetShaderiv(fragmentShader, GL_COMPILE_STATUS, &success);
        if(success != 0) {
            glGetShaderInfoLog(fragmentShader, 512, NULL, infoLog);
            std::cout << "Fragment Shader Compilation failed!\n"  << infoLog << std::endl;
        }

        shaderProgram = glCreateProgram();

        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);

        glGetProgramiv(shaderProgram, GL_LINK_STATUS, &success);
        if(success != 0) {
            std::cout << "Shader Program Linking failed!\n" << infoLog << std::endl;
        }
    }
    virtual void Update() override final {
        glUseProgram(shaderProgram);
        glBindVertexArray(VAO);
        glDrawArrays(GL_TRIANGLES, 0, 3);
    }
};

int main() {
    bool success = false;
    Window *window = new GameWindow(success);

    if (! success) {
        delete window;
        return -1;
    }

    window->DoMainLoop();

    delete window;
    return 0;
}

