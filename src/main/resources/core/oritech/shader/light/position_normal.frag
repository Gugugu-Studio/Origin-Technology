#version 330 core

// COMMON LIB PART
struct LightParamter {
    float kc, kl, kq;
};

struct PointLight {
    vec4 position;
    vec3 color;
    LightParamter para;
};

struct DirecionalLight {
    vec3 direction;
    vec3 color;
};

DirecionalLight SunLight = {
    normalize(vec3(0.3, -1.0, 0.3)),
    vec3(1.0, 1.0, 1.0)
};

float calcAtt(LightParamter para, float d) {
    float deno = para.kc + para.kl * d + para.kq * d * d;
    return 1 / deno;
}

vec3 calcAmbientLight(int time) {
    float ka = (sin(time / 100.0f) + 1) / 2;
    return vec3(ka, ka, ka);
}

vec3 calcPointLight(PointLight light, vec4 fragPos, vec3 normal) {
    vec3 lightVec = vec3(light.position - fragPos);
    vec3 lightDir = normalize(lightVec);
    float diff = max(dot(lightDir, normal), 0);
    float att = calcAtt(light.para, length(lightVec));
    vec3 diffuse = diff * light.color;
    diffuse *= att;
    return diffuse;
}

vec3 calcDirecitonalLight(DirecionalLight light, vec3 normal) {
    float diff = max(dot(light.direction, normal), 0);
    vec3 diffuse = diff * light.color;
    return diffuse;
}
// END COMMON LIB PART

// START UNIFORM LIB PART
layout (std140) uniform InfoUniform {
    int gameTime;
};
// END UNIFORM LIB PART

out vec4 FragColor;

in VS_OUT {
    vec4 fragPos;
    vec3 vertexNormal;
} fr_in;

uniform vec4 ColorModulator;

void main() {
    vec3 normal = normalize(fr_in.vertexNormal);
    vec3 light = calcAmbientLight(gameTime) + calcDirecitonalLight(SunLight, normal);
    FragColor = ColorModulator * vec4(light, 1.0);
}
