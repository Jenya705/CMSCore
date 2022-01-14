package net.hostlicer.core.loader;

import net.hostlicer.core.CoreApplication;
import net.minestom.server.MinecraftServer;

import java.nio.file.Path;

public final class CoreServerLoader {

    public static void main(String[] args) {
        MinecraftServer server = MinecraftServer.init();
        CoreApplication application = new CoreApplication(Path.of(""));
        server.start("127.0.0.1", application.getConfig().getServer().getPort());
        application.start();
    }

}
