package net.hostlicer.core.module.instance;

import net.hostlicer.core.config.ConfigData;
import net.hostlicer.core.module.AbstractCoreModule;
import net.hostlicer.core.module.instance.generator.ChunkGeneratorFactory;
import net.hostlicer.core.module.instance.generator.VoidGenerator;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.instance.ChunkGenerator;
import net.minestom.server.instance.Instance;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class InstanceModule extends AbstractCoreModule {

    private final Map<String, Function<ConfigData, ChunkGenerator>> chunkGeneratorFactories = new ConcurrentHashMap<>();
    private final Map<String, InstanceContainer> instances = new ConcurrentHashMap<>();

    public InstanceModule() {
        super("instance");
        InstanceContainer.addTags(
                "blockPlace"
        );
        addChunkGeneratorFactory("void", ChunkGeneratorFactory.of(VoidGenerator::new));
    }

    @Override
    public void start() {
        config().getInstance().getInstanceConfigs().forEach(
                (s, configData) -> instances.put(s, new InstanceContainer(s, configData))
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

    public void addChunkGeneratorFactory(String name, Function<ConfigData, ChunkGenerator> factory) {
        chunkGeneratorFactories.put(name.toLowerCase(Locale.ROOT), factory);
    }

    public ChunkGenerator createChunkGenerator(String name, ConfigData configData) {
        var generator = chunkGeneratorFactories.get(name.toLowerCase(Locale.ROOT));
        if (generator == null) return null;
        return generator.apply(configData);
    }

}
