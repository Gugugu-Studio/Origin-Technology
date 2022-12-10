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

DirecionalLight SunLight;

float sunAngle = 0;

float calcSunlightStrength() {
    const float kq = 0.00003;
    float d = (degrees(sunAngle) - 90);
    float deno = kq * d * d * d * d + 1;
    return 1 / deno;
}

void fixAngle() {
    int mul = int(sunAngle / radians(180));
    sunAngle -= mul * radians(180);
}

void updateSun(int time) {
    const float raiseSpeed = 0.005;
    sunAngle = time * raiseSpeed;
    fixAngle();
    if (sunAngle < radians(160) && sunAngle > radians(20)) {
        float y = sin(sunAngle);
        float x = -cos(sunAngle);
        float strength = calcSunlightStrength();
        SunLight.color = vec3(strength, strength, strength);
        SunLight.direction = -normalize(vec3(x, y, 0));
    }
    else {
        SunLight.color = vec3(0, 0, 0);
    }
}

void updateTime(int time) {
    updateSun(time);
}

float calcAtt(LightParamter para, float d) {
    float deno = para.kc + para.kl * d + para.kq * d * d;
    return 1 / deno;
}

vec3 calcAmbientLight() {
    float ka = (sin(sunAngle) + 1) / 2;
    return vec3(0.1, 0.1, 0.1);
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

uniform sampler2D Sampler0;
uniform vec4 ColorModulator;

in VS_OUT {
    vec4 fragPos;
    vec4 vertexColor;
    vec3 vertexNormal;
    vec2 texCoord0;
} fr_in;

void main() {
    vec4 color = texture(Sampler0, fr_in.texCoord0) * fr_in.vertexColor;
    if (color.a < 0.1) {
        discard;
    }
    vec3 normal = normalize(fr_in.vertexNormal);
    updateTime(gameTime);
    vec3 light = calcAmbientLight() + calcDirecitonalLight(SunLight, normal);
    FragColor = color * vec4(light, 1.0);
}
