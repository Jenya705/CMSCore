package com.github.jenya705.cmscore.module.item;

import com.github.jenya705.cmscore.CoreApplication;
import net.minestom.server.attribute.Attribute;
import net.minestom.server.item.ItemStackBuilder;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.NamespaceID;

import java.util.Map;

/**
 * @author Jenya705
 */
public interface CustomItem {

    Tag<String> idTag = Tag.String("cmsID");

    NamespaceID key();

    ItemStackBuilder createBuilder();

    Map<Attribute, CustomItemAttribute> getAttributes();

    default CustomItem fastRegister() {
        CustomItemModule itemModule = CoreApplication.getInstance().getModule(CustomItemModule.class);
        itemModule.registerItem(this);
        return this;
    }

}
