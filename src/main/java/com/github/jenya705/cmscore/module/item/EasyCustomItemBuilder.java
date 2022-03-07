package com.github.jenya705.cmscore.module.item;

import net.kyori.adventure.text.Component;
import net.minestom.server.attribute.Attribute;
import net.minestom.server.attribute.AttributeOperation;
import net.minestom.server.item.Material;
import net.minestom.server.utils.NamespaceID;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Jenya705
 */
public class EasyCustomItemBuilder {

    private final Map<Attribute, CustomItemAttribute> attributes = new HashMap<>();
    private NamespaceID key;
    private Material material;
    private int customModelData = -1;
    private Component displayName;

    public EasyCustomItemBuilder attribute(Attribute attribute, CustomItemAttribute itemAttribute) {
        attributes.put(attribute, itemAttribute);
        return this;
    }

    public EasyCustomItemBuilder attribute(Attribute attribute, float value, AttributeOperation operation) {
        return attribute(attribute, new CustomItemAttribute(value, operation));
    }

    public EasyCustomItemBuilder key(NamespaceID key) {
        this.key = key;
        return this;
    }

    public EasyCustomItemBuilder key(String key) {
        this.key = NamespaceID.from(key);
        return this;
    }

    public EasyCustomItemBuilder material(Material material) {
        this.material = material;
        return this;
    }

    public EasyCustomItemBuilder customModelData(int customModelData) {
        this.customModelData = customModelData;
        return this;
    }

    public EasyCustomItemBuilder displayName(Component displayName) {
        this.displayName = displayName;
        return this;
    }

    public CustomItem build() {
        Objects.requireNonNull(material, "not set material");
        Objects.requireNonNull(key, "not set key");
        return new EasyCustomItem(
                attributes,
                key,
                material,
                customModelData,
                displayName
        );
    }

}
