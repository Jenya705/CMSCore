package net.hostlicer.core.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ConfigData {

    @Getter
    private final JSONObject json;

    public static ConfigData empty() {
        return new ConfigData(new JSONObject());
    }

    @SuppressWarnings("unchecked")
    private <T> T getT(String key, T defaultValue) {
        if (json.containsKey(key)) {
            return (T) json.get(key);
        }
        json.put(key, defaultValue);
        return defaultValue;
    }

    public String[] get(String key, String[] defaultValue) {
        return getT(key, defaultValue);
    }

    public String get(String key, String defaultValue) {
        return getT(key, defaultValue);
    }

    public int get(String key, int defaultValue) {
        return getT(key, (long) defaultValue).intValue();
    }

    public long get(String key, long defaultValue) {
        return getT(key, defaultValue);
    }

    public double get(String key, double defaultValue) {
        return getT(key, defaultValue);
    }

    public boolean get(String key, boolean defaultValue) {
        return getT(key, defaultValue);
    }

    public Point get(String key, Point defaultValue) {
        if (has(key)) {
            ConfigData pointData = get(key, ConfigData.empty());
            return new Vec(
                    pointData.get("x", defaultValue.x()),
                    pointData.get("y", defaultValue.y()),
                    pointData.get("z", defaultValue.z())
            );
        }
        ConfigData pointData = ConfigData.empty();
        pointData.set("x", defaultValue.x());
        pointData.set("y", defaultValue.y());
        pointData.set("z", defaultValue.z());
        set(key, pointData);
        return defaultValue;
    }

    public ConfigData get(String key, ConfigData defaultValue) {
        if (json.containsKey(key)) {
            return new ConfigData(getT(key, null));
        }
        set(key, defaultValue.json);
        return defaultValue;
    }

    public Object get(String key) {
        Object obj = json.get(key);
        if (obj instanceof JSONObject json) {
            return new ConfigData(json);
        }
        return obj;
    }
    
    public boolean has(String key) {
        return json.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    public void set(String key, Object obj) {
        json.put(key, obj);
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, Object> values() {
        Map<String, Object> result = new HashMap<>();
        json.forEach((key, value) -> {
            if (value instanceof JSONObject) {
                result.put(key.toString(), new ConfigData((JSONObject) value));
            }
            else {
                result.put(key.toString(), value);
            }
        });
        return result;
    }
}
