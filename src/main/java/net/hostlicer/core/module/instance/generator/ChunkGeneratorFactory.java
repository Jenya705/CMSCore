package net.hostlicer.core.module.instance.generator;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import net.hostlicer.core.config.ConfigData;
import net.minestom.server.instance.ChunkGenerator;

import java.util.function.Function;
import java.util.function.Supplier;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChunkGeneratorFactory implements Function<ConfigData, ChunkGenerator> {

    public static ChunkGeneratorFactory of(Function<ConfigData, ChunkGenerator> constructor) {
        return new ChunkGeneratorFactory(constructor);
    }

    public static ChunkGeneratorFactory of(Supplier<ChunkGenerator> constructor) {
        return new ChunkGeneratorFactory(it -> constructor.get());
    }

    @Delegate
    private final Function<ConfigData, ChunkGenerator> generatorConstructor;

}
