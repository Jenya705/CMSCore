package net.hostlicer.core.config;

import lombok.Builder;
import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

@Data
@Builder
public class PingConfig {

    private final Component motd;
    private final String brandName;

    public static PingConfig from(ConfigData config) {
        return PingConfig
                .builder()
                .motd(LegacyComponentSerializer.legacyAmpersand().deserialize(config.get("motd", "HotCore server")))
                .brandName(config.get("brandname", "HotCore"))
                .build();
    }

}
