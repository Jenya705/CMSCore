package net.hostlicer.core.config;

import lombok.Builder;
import lombok.Data;

/**
 * @author Jenya705
 */
@Data
@Builder
public class SettingsConfig {

    private boolean online;

    public static SettingsConfig from(ConfigData configData) {
        return SettingsConfig
                .builder()
                .online(configData.get("online", false))
                .build();
    }

}
