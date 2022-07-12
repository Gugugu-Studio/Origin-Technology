#pragma once

#include <GLHeader.h>

class Window {
protected:
    int width;
    int height;
    const char *title;
    GLFWwindow *handle;
    GLRGBA background;
public:
    Window(const char *title, int width, int height, GLRGBA background, void(*GLInitializer)(bool&), bool &success);
    virtual ~Window();
    GLFWwindow *GetHandle();
    virtual void Init() = 0;
    virtual void Update() = 0;
    void DoMainLoop();
};