package net.hostlicer.core.config;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServerConfig {

    private final int port;

    public static ServerConfig from(ConfigData config) {
        return ServerConfig
                .builder()
                .port(config.get("port", 25565))
                .build();
    }

}
