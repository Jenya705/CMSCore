package net.hotslicer.core.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import lombok.Builder;
import lombok.Cleanup;
import lombok.Getter;
import lombok.SneakyThrows;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Getter
@Builder
public class CoreConfig {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static final Logger logger = LoggerFactory.getLogger("HotCore - Config");

    private final ServerConfig server;
    private final PingConfig ping;
    private final InstanceConfig instance;
    private final SettingsConfig settings;

    @SneakyThrows
    public static CoreConfig load(Path path) {

        File configFile = path.resolve("config.json").toFile();
        ConfigData configData;
        if (configFile.exists()) {
            @Cleanup Reader configReader = new FileReader(configFile);
            JSONObject config = (JSONObject) new JSONParser().parse(configReader);
            configData = new ConfigData(config);
        }
        else {
            configFile.createNewFile();
            configData = ConfigData.empty();
        }

        CoreConfig loaded = CoreConfig
                .builder()
                .server(ServerConfig.from(configData.get("server", ConfigData.empty())))
                .ping(PingConfig.from(configData.get("ping", ConfigData.empty())))
                .instance(InstanceConfig.from(configData.get("instance", ConfigData.empty())))
                .settings(SettingsConfig.from(configData.get("settings", ConfigData.empty())))
                .build();

        @Cleanup Writer writer = new FileWriter(configFile);
        String jsonString = configData.getJson().toJSONString();
        Files.writeString(
                configFile.toPath(),
                gson.toJson(gson.fromJson(jsonString, JsonElement.class)),
                StandardOpenOption.WRITE
        );

        return loaded;
    }

}
