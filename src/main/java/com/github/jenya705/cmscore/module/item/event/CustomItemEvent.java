package com.github.jenya705.cmscore.module.item.event;

import com.github.jenya705.cmscore.module.item.CustomItem;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.event.trait.ItemEvent;

/**
 * @author Jenya705
 */
public interface CustomItemEvent extends ItemEvent {

    CustomItem getCustomItem();

    EquipmentSlot getSlot();

}
