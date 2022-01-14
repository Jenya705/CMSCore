package net.hostlicer.core.module;

import lombok.AccessLevel;
import lombok.Getter;
import net.hostlicer.core.CoreApplication;
import net.hostlicer.core.config.CoreConfig;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

@Getter(AccessLevel.PROTECTED)
public abstract class AbstractCoreModule {

    protected static CoreApplication core() {
        return CoreApplication.getInstance();
    }

    @Getter
    private final String name;

    private final Logger logger;

    protected AbstractCoreModule(String name) {
        this.name = name;
        logger = LoggerFactory.getLogger("HotCore - %s".formatted(name));
    }

    public abstract void start();

    public abstract void stop();

    protected final <E extends Event> void eventListener(Class<E> eventClass, Consumer<E> listener) {
        MinecraftServer.getGlobalEventHandler().addListener(eventClass, listener);
    }

    protected final CoreConfig config() {
        return core().getConfig();
    }

}
