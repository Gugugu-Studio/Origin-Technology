#version 330 core

// COMMON LIB PART
const vec3 ambientLight = vec3(0.3);

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

out vec4 FragColor;

uniform vec4 ColorModulator;

in VS_OUT {
    vec4 fragPos;
    vec3 vertexNormal;
} fr_in;

void main() {
    vec3 normal = normalize(fr_in.vertexNormal);
    vec3 light = ambientLight + calcDirecitonalLight(SunLight, normal);
    FragColor = ColorModulator * vec4(light, 1.0);
}
