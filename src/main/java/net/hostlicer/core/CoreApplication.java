package net.hostlicer.core;

import lombok.Getter;
import net.hostlicer.core.command.GameModeCommand;
import net.hostlicer.core.config.CoreConfig;
import net.hostlicer.core.module.AbstractCoreModule;
import net.hostlicer.core.module.instance.InstanceModule;
import net.hostlicer.core.module.ping.PingModule;
import net.minestom.server.MinecraftServer;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
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
    }

    public void start() {
        MinecraftServer.getCommandManager().register(new GameModeCommand());
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
