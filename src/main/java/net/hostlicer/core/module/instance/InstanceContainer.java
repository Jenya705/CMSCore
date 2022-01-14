package net.hostlicer.core.module.instance;

import lombok.AccessLevel;
import lombok.Getter;
import net.hostlicer.core.CoreApplication;
import net.hostlicer.core.config.ConfigData;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Instance;
import net.minestom.server.tag.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Getter(AccessLevel.PROTECTED)
public class InstanceContainer {

    private static final Set<String> globalTags = new CopyOnWriteArraySet<>();

    public static void addTags(String... tags) {
        globalTags.addAll(Arrays.asList(tags));
    }

    @Getter(AccessLevel.PROTECTED)
    private final Logger logger;

    private final String name;
    private final Instance instance;
    private final Point respawnPoint;

    private final InstanceModule instanceModule = CoreApplication.getInstance().getModule("instance");

    public InstanceContainer(String instanceName, ConfigData data) {
        logger = LoggerFactory.getLogger("HotCore - %s".formatted(instanceName));
        logger.info("Loading {} instance", instanceName);
        name = instanceName;
        instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        respawnPoint = data.get("spawn", new Vec(0, 64, 0));
        Object generatorObject = data.get("generator");
        if (generatorObject instanceof ConfigData generatorData) {
            instance.setChunkGenerator(instanceModule.createChunkGenerator(
                    generatorData.get("type", "void"), generatorData));
        }
        else {
            instance.setChunkGenerator(instanceModule.createChunkGenerator(
                    Objects.requireNonNullElse(generatorObject, "void").toString(), null));
        }
        data.values().forEach((name, obj) -> {
            if (globalTags.contains(name)) {
                addTag(name, obj);
            }
        });
    }

    private void addTag(String name, Object obj) {
        if (obj instanceof Byte) {
            instance.setTag(Tag.Byte(name), (byte) obj);
        }
        else if (obj instanceof Short) {
            instance.setTag(Tag.Short(name), (short) obj);
        }
        else if (obj instanceof Integer) {
            instance.setTag(Tag.Integer(name), (int) obj);
        }
        else if (obj instanceof Long) {
            instance.setTag(Tag.Long(name), (long) obj);
        }
        else if (obj instanceof Float) {
            instance.setTag(Tag.Float(name), (float) obj);
        }
        else if (obj instanceof Double) {
            instance.setTag(Tag.Double(name), (double) obj);
        }
        else if (obj instanceof String) {
            instance.setTag(Tag.String(name), (String) obj);
        }
        else {
            throw new IllegalArgumentException("Type is not supported");
        }
    }

}
