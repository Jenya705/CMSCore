package com.github.jenya705.cmscore.module.item.event;

import com.github.jenya705.cmscore.module.item.CustomItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.event.entity.EntityDamageEvent;
import net.minestom.server.event.trait.EntityEvent;
import net.minestom.server.item.ItemStack;

/**
 * @author Jenya705
 */
@Getter
@RequiredArgsConstructor
public class CustomItemEntityDamageEvent implements CustomItemEvent, EntityEvent {

    private final CustomItem customItem;
    private final EquipmentSlot slot;
    private final ItemStack itemStack;

    @Delegate
    private final EntityDamageEvent damageEvent;

}
