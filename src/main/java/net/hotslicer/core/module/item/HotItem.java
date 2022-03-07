package net.hotslicer.core.module.item;

import net.hotslicer.core.CoreApplication;
import net.minestom.server.attribute.Attribute;
import net.minestom.server.item.ItemStackBuilder;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.NamespaceID;

import java.util.Map;

/**
 * @author Jenya705
 */
public interface HotItem {

    Tag<String> idTag = Tag.String("hotID");

    NamespaceID key();

    ItemStackBuilder createBuilder();

    Map<Attribute, HotItemAttribute> getAttributes();

    default HotItem fastRegister() {
        HotItemModule itemModule = CoreApplication.getInstance().getModule("item");
        itemModule.registerItem(this);
        return this;
    }

}
