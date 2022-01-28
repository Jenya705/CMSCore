package net.hostlicer.core.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class InstanceConfig {

    private final Map<String, ConfigData> instanceConfigs;

    private final String defaultInstance;

    public static InstanceConfig from(ConfigData config) {
        if (!config.has("testInstance")) {
            ConfigData testInstance = config.get("testInstance", ConfigData.empty());
            testInstance.set("generator", "void");
        }
        Map<String, ConfigData> instanceConfigs = new HashMap<>();
        config.values().forEach((s, o) -> {
            if (o instanceof ConfigData) {
                instanceConfigs.put(s.toLowerCase(Locale.ROOT), (ConfigData) o);
            }
        });
        return new InstanceConfig(
                Collections.unmodifiableMap(instanceConfigs),
                config.get("default", "testInstance")
        );
    }

}
