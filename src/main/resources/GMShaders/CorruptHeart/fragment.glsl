#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform sampler2D u_heart;

uniform float u_timer;
uniform float settings_scale;

varying vec4 v_color;
varying vec2 v_texCoords;

/**
  * @param vec2 origin - 缩放中心点位置
  * @param vec2 currPos - 当前点的位置
  * @param vec2 scale - 缩放比例
  *
  * @returns vec2 - 缩放后的位置
  */
vec2 getScaledPosition(vec2 origin, vec2 currPos, vec2 scale) {
    vec2 ret = vec2(currPos);
    if (currPos.x <= origin.x) {
        ret.x = ret.x + (1.0 - scale.x) * (origin.x - currPos.x);
    } else {
        ret.x = ret.x - (1.0 - scale.x) * (currPos.x - origin.x);
    }
    if (currPos.y <= origin.y) {
        ret.y = ret.y + (1.0 - scale.y) * (origin.y - currPos.y);
    } else {
        ret.y = ret.y - (1.0 - scale.y) * (currPos.y - origin.y);
    }
    if (ret.x < 0.0) {
        ret.x = 0.0;
    }
    if (ret.x > 1.0) {
        ret.x = 1.0;
    }
    if (ret.y < 0.0) {
        ret.y = 0.0;
    }
    if (ret.y > 1.0) {
        ret.y = 1.0;
    }
    return ret;
}

void main() {
    float animationTime = 0.866667;
    float currTime = mod(u_timer, animationTime);
    vec2 pos;
    if (currTime < 0.1) {
        float progress = currTime / 0.1;
        pos = getScaledPosition(vec2(0.5, 1.0), v_texCoords, vec2(1.0 - 0.05 * progress, 1.0 + 0.025 * progress));
        pos.x -= (0.04 * progress) * settings_scale;
        pos.y -= (0.033333 * progress) * settings_scale;
    } else if (currTime < 0.3) {
        float progress = (currTime - 0.1) / 0.2;
        pos = getScaledPosition(vec2(0.5, 1.0), v_texCoords, vec2(0.95 + 0.1 * progress, 1.025 - 0.125 * progress));
        pos.x -= 0.04 * settings_scale;
        pos.y -= 0.033333 * settings_scale;
        pos.x += (0.053333 * progress) * settings_scale;
        pos.y += (0.164444 * progress) * settings_scale;
    } else {
        float progress = (currTime - 0.3) / (animationTime - 0.3);
        pos = getScaledPosition(vec2(0.5, 1.0), v_texCoords, vec2(1.05 - 0.05 * progress, 0.9 + 0.1 * progress));
        pos.x += 0.013333 * settings_scale;
        pos.y += 0.131111 * settings_scale;
        pos.x -= (0.013333 * progress) * settings_scale;
        pos.y -= (0.131111 * progress) * settings_scale;
    }

    v_color.a = 0.5;
    vec4 maskColor = v_color * texture2D(u_texture, v_texCoords);
    vec4 heartColor = texture2D(u_heart, pos);
    heartColor.r = 1.0 - (1.0 - heartColor.r) * heartColor.a;
    heartColor.g = 1.0 - (1.0 - heartColor.g) * heartColor.a;
    heartColor.b = 1.0 - (1.0 - heartColor.b) * heartColor.a;
    vec4 mulColor = maskColor * heartColor;
    mulColor.a = max(maskColor.a, heartColor.a);
    gl_FragColor = mulColor;
}