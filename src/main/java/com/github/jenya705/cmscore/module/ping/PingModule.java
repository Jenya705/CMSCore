package com.github.jenya705.cmscore.module.ping;

import com.github.jenya705.cmscore.module.AbstractCoreModule;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.server.ServerListPingEvent;

public class PingModule extends AbstractCoreModule {

    public PingModule() {
        super("Ping");
    }

    @Override
    public void start() {
        MinecraftServer.setBrandName(core().getConfig().getPing().getBrandName());
        MinecraftServer.getGlobalEventHandler().addListener(ServerListPingEvent.class, e ->
                e.getResponseData().setDescription(core().getConfig().getPing().getMotd())
        );
    }

    @Override
    public void stop() {
    }
}
