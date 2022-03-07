package com.github.jenya705.cmscore.module.instance;

import io.github.bloepiloepi.pvp.PvpExtension;
import lombok.AccessLevel;
import lombok.Getter;
import com.github.jenya705.cmscore.CoreApplication;
import com.github.jenya705.cmscore.config.ConfigData;
import com.github.jenya705.cmscore.module.CoreTags;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.trait.EntityEvent;
import net.minestom.server.event.trait.InstanceEvent;
import net.minestom.server.instance.IChunkLoader;
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

    private static final Set<Tag<?>> globalTags = new CopyOnWriteArraySet<>();

    public static void addTags(Tag<?>... tags) {
        globalTags.addAll(Arrays.asList(tags));
    }

    @Getter(AccessLevel.PROTECTED)
    protected final Logger logger;

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
        if (data.has("loader")) {
            loader(data);
        }
        else {
            generator(data);
        }
        data.values().forEach((name, obj) ->
                globalTags
                        .stream()
                        .filter(it -> name.equals(it.getKey()))
                        .findAny()
                        .ifPresent(value -> addTag(value, obj))
        );
        nodes();
    }

    private void nodes() {
        EventNode<EntityEvent> eventNode = EventNode.event(
                "%s-instance".formatted(name),
                EventFilter.ENTITY,
                event -> {
                    if (event instanceof InstanceEvent instanceEvent) {
                        return Objects.equals(instanceEvent.getInstance(), instance);
                    }
                    return Objects.equals(event.getEntity().getInstance(), instance);
                }
        );
        if (Objects.requireNonNullElse(instance.getTag(CoreTags.instancePvp), 0).byteValue() != 0) {
            eventNode.addChild(PvpExtension.attackEvents());
            eventNode.addChild(PvpExtension.damageEvents());
        }
        MinecraftServer.getGlobalEventHandler().addChild(eventNode);
    }

    private void loader(ConfigData data) {
        Object loaderObject = data.get("loader");
        if (loaderObject == null) return;
        IChunkLoader loader;
        if (loaderObject instanceof ConfigData loaderData) {
            if (!loaderData.has("type")) return;
            loader = instanceModule.getChunkLoaderFactories().create(
                    loaderData.get("type", ""), loaderData
            );
        }
        else {
            loader = instanceModule.getChunkLoaderFactories().create(
                    loaderObject.toString()
            );
        }
        if (loader == null) return;
        loader.loadInstance(instance);
    }

    private void generator(ConfigData data) {
        Object generatorObject = data.get("generator");
        if (generatorObject instanceof ConfigData generatorData) {
            instance.setChunkGenerator(instanceModule.getChunkGeneratorFactories().create(
                    generatorData.get("type", "void"), generatorData)
            );
        }
        else {
            instance.setChunkGenerator(instanceModule.getChunkGeneratorFactories().create(
                    Objects.requireNonNullElse(generatorObject, "void").toString(), null)
            );
        }
    }

    @SuppressWarnings("unchecked")
    private void addTag(Tag<?> tag, Object obj) {
        if (obj instanceof Boolean) {
            instance.setTag((Tag<Byte>) tag, (byte) ((boolean) obj ? 1 : 0));
        }
        else if (obj instanceof Byte) {
            instance.setTag((Tag<Byte>) tag, (byte) obj);
        }
        else if (obj instanceof Short) {
            instance.setTag((Tag<Short>) tag, (short) obj);
        }
        else if (obj instanceof Integer) {
            instance.setTag((Tag<Integer>) tag, (int) obj);
        }
        else if (obj instanceof Long) {
            instance.setTag((Tag<Long>) tag, (long) obj);
        }
        else if (obj instanceof Float) {
            instance.setTag((Tag<Float>) tag, (float) obj);
        }
        else if (obj instanceof Double) {
            instance.setTag((Tag<Double>) tag, (double) obj);
        }
        else if (obj instanceof String) {
            instance.setTag((Tag<String>) tag, (String) obj);
        }
        else {
            throw new IllegalArgumentException("Type is not supported");
        }
    }

}
