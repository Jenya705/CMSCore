package com.github.jenya705.cmscore.module.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.minestom.server.attribute.Attribute;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.ItemStackBuilder;
import net.minestom.server.item.Material;
import net.minestom.server.utils.NamespaceID;

import java.util.Map;

/**
 * @author Jenya705
 */
@RequiredArgsConstructor
public class EasyHotItem implements HotItem {

    public static EasyHotItemBuilder builder() {
        return new EasyHotItemBuilder();
    }

    @Getter
    private final Map<Attribute, HotItemAttribute> attributes;

    private final NamespaceID key;
    private final Material material;
    private final int customModelData;
    private final Component displayName;

    @Override
    public NamespaceID key() {
        return key;
    }

    @Override
    public ItemStackBuilder createBuilder() {
        return ItemStack.builder(material).meta(meta -> {
            meta.setTag(idTag, key.toString());
            if (customModelData != -1) {
                meta.customModelData(customModelData);
            }
            if (displayName != null) {
                meta.displayName(displayName);
            }
            return meta;
        });
    }

}
