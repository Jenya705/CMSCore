package com.github.jenya705.cmscore.module.item.event;

import com.github.jenya705.cmscore.module.item.CustomItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;

import java.util.Optional;

/**
 * @author Jenya705
 */
@Getter
@RequiredArgsConstructor
public class CustomItemRightClickEvent implements CustomItemEvent {

    private final CustomItem customItem;
    private final EquipmentSlot slot;
    private final ItemStack itemStack;

    @Getter(AccessLevel.PRIVATE)
    private final Entity atEntity;
    @Getter(AccessLevel.PRIVATE)
    private final Block atBlock;

    public Optional<Entity> atEntity() {
        return Optional.ofNullable(atEntity);
    }

    public Optional<Block> atBlock() {
        return Optional.ofNullable(atBlock);
    }

    public boolean atAir() {
        return atEntity == null && atBlock == null;
    }

}
