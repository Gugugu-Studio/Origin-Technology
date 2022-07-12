#include <Window.h>

#include <iostream>

void CommonFramebufferSizeCallback(GLFWwindow* window, int width, int height) {
    glViewport(0, 0, width, height);
}

Window::Window(const char *title, int width, int height, GLRGBA background, void(*GLInitializer)(bool&), bool &success) :
    title(title), width(width), height(height), background(background)
{
    glfwInit();
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

    this->handle = glfwCreateWindow(this->width, this->height, this->title, NULL, NULL);

    if (this->handle == NULL) {
        std::cout << "Failed to create GLFW window" << std::endl;
        success = false;
        return;
    }

    glfwMakeContextCurrent(this->handle);

    GLInitializer(success);
    if (! success) {
        return;
    }

    glViewport(0, 0, 800, 600);

    glfwSetFramebufferSizeCallback(this->handle, CommonFramebufferSizeCallback);

    success = true;
}

Window::~Window() {
    glfwTerminate();
}

GLFWwindow *Window::GetHandle() {
    return this->handle;
}

void Window::DoMainLoop() {
    this->Init();

    while(! glfwWindowShouldClose(this->handle)) {
        glClearColor(background.R, background.G, background.B, background.A);
        glClear(GL_COLOR_BUFFER_BIT);

        this->Update();

        glfwSwapBuffers(this->handle);
        glfwPollEvents();
    }
}