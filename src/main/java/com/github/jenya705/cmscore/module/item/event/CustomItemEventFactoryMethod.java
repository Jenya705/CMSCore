package com.github.jenya705.cmscore.module.item.event;

import com.github.jenya705.cmscore.module.item.CustomItem;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.event.Event;
import net.minestom.server.item.ItemStack;

/**
 * @author Jenya705
 */
@FunctionalInterface
public interface CustomItemEventFactoryMethod<T extends Event> {

    CustomItemEvent createEvent(CustomItem customItem, EquipmentSlot slot, ItemStack itemStack, T event);

}
