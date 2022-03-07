package com.github.jenya705.cmscore.module.instance;

import com.github.jenya705.cmscore.config.ConfigData;
import com.github.jenya705.cmscore.factory.Factory;
import com.github.jenya705.cmscore.module.CoreTags;
import com.github.jenya705.cmscore.module.instance.generator.VoidGenerator;
import com.github.jenya705.cmscore.module.instance.loader.AnvilLoaderFactory;
import io.github.bloepiloepi.pvp.PvpExtension;
import lombok.AccessLevel;
import lombok.Getter;
import com.github.jenya705.cmscore.module.AbstractCoreModule;
import com.github.jenya705.cmscore.module.instance.generator.FlatGenerator;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.instance.ChunkGenerator;
import net.minestom.server.instance.IChunkLoader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class InstanceModule extends AbstractCoreModule {

    private final Factory<ConfigData, ChunkGenerator> chunkGeneratorFactories = new Factory<>();
    private final Factory<ConfigData, IChunkLoader> chunkLoaderFactories = new Factory<>();

    @Getter(AccessLevel.PRIVATE)
    private final Map<String, InstanceContainer> instances = new ConcurrentHashMap<>();

    public InstanceModule() {
        super("instance");
        InstanceContainer.addTags(
                CoreTags.instancePvp
        );
        chunkLoaderFactories.addFactory("anvil", AnvilLoaderFactory::create);
        chunkGeneratorFactories.addFactory("void", VoidGenerator::new);
        chunkGeneratorFactories.addFactory("flat", FlatGenerator::new);
    }

    @Override
    public void start() {
        PvpExtension.init();
        config().getInstance().getInstanceConfigs().forEach(
                (s, configData) -> {
                    try {
                        instances.put(s, new InstanceContainer(s, configData));
                    } catch (Throwable e) {
                        logger.warn("Failed to load %s instance".formatted(s), e);
                    }
                }
        );
        eventListener(PlayerLoginEvent.class, this::handlePlayerLogin);
    }

    private void handlePlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        InstanceContainer instanceContainer = instances.get(config().getInstance().getDefaultInstance());
        event.setSpawningInstance(instanceContainer.getInstance());
        player.setRespawnPoint(Pos.fromPoint(
                instanceContainer
                        .getRespawnPoint()
                        .add(0.5, 0, 0.5)
        ));
    }

    @Override
    public void stop() {

    }

}
