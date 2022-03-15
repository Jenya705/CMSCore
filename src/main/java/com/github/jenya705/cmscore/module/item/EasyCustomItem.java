package com.github.jenya705.cmscore.module.item;

import com.github.jenya705.cmscore.CoreApplication;
import com.github.jenya705.cmscore.module.item.event.CustomItemEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.minestom.server.attribute.Attribute;
import net.minestom.server.event.EventNode;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.ItemStackBuilder;
import net.minestom.server.item.Material;
import net.minestom.server.utils.NamespaceID;

import java.util.List;
import java.util.Map;

/**
 * @author Jenya705
 */
@RequiredArgsConstructor
public class EasyCustomItem implements CustomItem {

    public static EasyCustomItemBuilder builder() {
        return new EasyCustomItemBuilder();
    }

    @Getter
    private final Map<Attribute, CustomItemAttribute> attributes;

    private final NamespaceID key;
    private final Material material;
    private final int customModelData;
    private final Component displayName;
    private final List<CustomItemListener> listeners;

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

    @Override
    public CustomItem fastRegister() {
        CustomItemModule itemModule = CoreApplication.getInstance().getModule("item");
        itemModule.registerItem(this);
        if (!listeners.isEmpty()) {
            EventNode<CustomItemEvent> eventNode = itemModule.getEventHandler(this);
            listeners.forEach(listener -> listener.register(eventNode));
        }
        return this;
    }
}
