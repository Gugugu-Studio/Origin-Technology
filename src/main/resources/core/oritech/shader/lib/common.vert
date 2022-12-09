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
    const float kq = 0.030;
    float d = (degrees(sunAngle) - 90);
    float deno = kq * d * d + 1;
    return 1 / deno;
}

void fixAngle() {
    int mul = int(sunAngle / radians(360));
    sunAngle -= mul * radians(360);
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
    float diff = max(dot(-light.direction, normal), 0);
    vec3 diffuse = diff * light.color;
    return diffuse;
}
// END COMMON LIB PART