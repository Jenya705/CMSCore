package net.hotslicer.core;

import lombok.Getter;
import net.hotslicer.core.command.BlocksCommand;
import net.hotslicer.core.command.GameModeCommand;
import net.hotslicer.core.command.PosCommand;
import net.hotslicer.core.config.CoreConfig;
import net.hotslicer.core.module.item.HotItemModule;
import net.hotslicer.core.module.AbstractCoreModule;
import net.hotslicer.core.module.instance.InstanceModule;
import net.hotslicer.core.module.ping.PingModule;
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
        addModule(new HotItemModule());
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

}
