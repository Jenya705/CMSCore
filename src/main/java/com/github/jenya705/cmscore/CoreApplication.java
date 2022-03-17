package com.github.jenya705.cmscore;

import com.github.jenya705.cmscore.command.BlocksCommand;
import com.github.jenya705.cmscore.command.GameModeCommand;
import com.github.jenya705.cmscore.command.PosCommand;
import com.github.jenya705.cmscore.module.AbstractCoreModule;
import com.github.jenya705.cmscore.module.instance.InstanceModule;
import com.github.jenya705.cmscore.module.ping.PingModule;
import lombok.Getter;
import com.github.jenya705.cmscore.config.CoreConfig;
import com.github.jenya705.cmscore.module.item.CustomItemModule;
import net.minestom.server.MinecraftServer;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class CoreApplication {

    @Getter
    private static CoreApplication instance;

    private final Path path;
    private final CoreConfig config;
    private final Map<String, AbstractCoreModule> modules = new ConcurrentHashMap<>();

    public CoreApplication(Path path) {
        if (instance != null) {
            throw new IllegalStateException("CoreApplication is singleton");
        }
        instance = this;
        this.path = path;
        config = CoreConfig.load(path);
        addModule(new PingModule());
        addModule(new InstanceModule());
        addModule(new CustomItemModule());
    }

    public void start() {
        MinecraftServer.getCommandManager().register(new GameModeCommand());
        MinecraftServer.getCommandManager().register(new PosCommand());
        MinecraftServer.getCommandManager().register(new BlocksCommand());
        MinecraftServer.getConnectionManager().setUuidProvider((playerConnection, username) ->
                UUID.nameUUIDFromBytes(("OfflinePlayer:"+username).getBytes(StandardCharsets.UTF_8))
        );
        modules.forEach((s, abstractCoreModule) -> abstractCoreModule.start());
    }

    public void stop() {
        modules.forEach((s, abstractCoreModule) -> abstractCoreModule.stop());
    }

    protected void addModule(AbstractCoreModule module) {
        modules.put(module.getName().toLowerCase(Locale.ROOT), module);
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractCoreModule> T getModule(String name) {
        return (T) modules.get(name.toLowerCase(Locale.ROOT));
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractCoreModule> T getModule(Class<T> clazz) {
        return (T) modules
                .values()
                .stream()
                .filter(it -> it.getClass().isAssignableFrom(clazz))
                .findAny()
                .orElse(null);
    }

}
