package net.hotslicer.core.module.instance.generator;

import net.hotslicer.core.config.ConfigData;
import net.minestom.server.instance.ChunkGenerator;
import net.minestom.server.instance.ChunkPopulator;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class FlatGenerator implements ChunkGenerator {

    private final Block[] layers;

    public FlatGenerator(ConfigData data) {
        this(
                Arrays.stream(data.get(
                                        "layers",
                                        new String[]{
                                                "minecraft:grass_block",
                                                "minecraft:dirt",
                                                "minecraft:dirt",
                                                "minecraft:bedrock"
                                        }
                                )
                        )
                        .map(Block::fromNamespaceId)
                        .toArray(Block[]::new)
        );
    }

    public FlatGenerator(Block[] layers) {
        this.layers = layers;
    }

    @Override
    public void generateChunkData(@NotNull ChunkBatch batch, int chunkX, int chunkZ) {
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int b = 0; b < layers.length; ++b) {
                    batch.setBlock(x, b, z, layers[layers.length - b - 1]);
                }
            }
        }
    }

    @Override
    public @Nullable List<ChunkPopulator> getPopulators() {
        return null;
    }
}
