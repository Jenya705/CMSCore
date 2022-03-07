package net.hotslicer.core.module.item;

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
public class EasyHotItemBuilder {

    private final Map<Attribute, HotItemAttribute> attributes = new HashMap<>();
    private NamespaceID key;
    private Material material;
    private int customModelData = -1;
    private Component displayName;

    public EasyHotItemBuilder attribute(Attribute attribute, HotItemAttribute itemAttribute) {
        attributes.put(attribute, itemAttribute);
        return this;
    }

    public EasyHotItemBuilder attribute(Attribute attribute, float value, AttributeOperation operation) {
        return attribute(attribute, new HotItemAttribute(value, operation));
    }

    public EasyHotItemBuilder key(NamespaceID key) {
        this.key = key;
        return this;
    }

    public EasyHotItemBuilder key(String key) {
        this.key = NamespaceID.from(key);
        return this;
    }

    public EasyHotItemBuilder material(Material material) {
        this.material = material;
        return this;
    }

    public EasyHotItemBuilder customModelData(int customModelData) {
        this.customModelData = customModelData;
        return this;
    }

    public EasyHotItemBuilder displayName(Component displayName) {
        this.displayName = displayName;
        return this;
    }

    public HotItem build() {
        Objects.requireNonNull(material, "not set material");
        Objects.requireNonNull(key, "not set key");
        return new EasyHotItem(
                attributes,
                key,
                material,
                customModelData,
                displayName
        );
    }

}
