package com.github.jenya705.cmscore.module.item.event;

import com.github.jenya705.cmscore.module.item.CustomItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.event.Event;
import net.minestom.server.item.ItemStack;

/**
 * @author Jenya705
 */
@Getter
@RequiredArgsConstructor
public class AllCustomItemEvent implements CustomItemEvent {

    private final CustomItem customItem;
    private final ItemStack itemStack;
    private final EquipmentSlot slot;
    private final Event linked;

}
