package com.github.jenya705.cmscore.module.item.event;

import com.github.jenya705.cmscore.module.item.CustomItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.item.ItemStack;

/**
 * @author Jenya705
 */
@Getter
@RequiredArgsConstructor
public class CustomItemEntityAttackEvent implements CustomItemEvent {

    private final CustomItem customItem;
    private final EquipmentSlot slot;
    private final ItemStack itemStack;

    @Delegate
    private final EntityAttackEvent event;

}
