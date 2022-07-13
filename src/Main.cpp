#include <GLHeader.h>

#include <iostream>
#include <cmath>

#include <Window.h>
#include <Shader.h>

void InitGLAD(bool &success) {
    if (! gladLoadGLLoader((GLADloadproc)glfwGetProcAddress)) {
        std::cout << "Failed to initialize GLAD" << std::endl;
        success = false;
        return;
    }
    success = true;
}

float vertices[] = {
    -0.5f, -0.5f, 0.0f,
     0.5f, -0.5f, 0.0f,
     0.0f,  0.5f, 0.0f
};

GLuint VAO;
GLuint VBO;
Shader *shader = NULL;

class GameWindow : public Window {
public:
    GameWindow(bool &success) : Window("Origin Technology", 800, 600, GLRGBA{0.2f, 0.5f, 0.3f, 1.0f}, InitGLAD, success) {
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

        shader = new Shader("./resource/assets/DefaultShader.glvs", "./resource/assets/DefaultShader.glfs");
    }
    virtual void Update() override final {
        shader->Use();
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
    if (shader != NULL) {
        delete shader;
    }
    return 0;
}

