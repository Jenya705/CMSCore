package com.github.jenya705.cmscore.module.item;

import com.github.jenya705.cmscore.module.item.event.*;
import lombok.RequiredArgsConstructor;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.entity.EntityDamageEvent;
import net.minestom.server.event.item.EntityEquipEvent;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.trait.EntityEvent;
import net.minestom.server.event.trait.ItemEvent;
import net.minestom.server.item.ItemStack;

import java.util.function.BiConsumer;

/**
 * @author Jenya705
 */
@RequiredArgsConstructor
public class CustomItemNodeManager {

    private final CustomItemModule module;

    public EventNode<Event> buildCustomItemEventNode() {
        EventNode<Event> globalNode = EventNode.all("global-custom-item");
        registerEntityEvent(globalNode, EntityAttackEvent.class, CustomItemEntityAttackEvent::new);
        registerEntityEvent(globalNode, EntityDamageEvent.class, CustomItemEntityDamageEvent::new);
        registerEntityEvent(globalNode, EntityEquipEvent.class, CustomItemEquipEvent::new);
        globalNode.addListener(PlayerBlockInteractEvent.class, event -> {
            ItemStack item = event.getPlayer().getItemInHand(event.getHand());
            CustomItem customItem = module.getCustomItem(item);
            if (customItem == null) return;
            module.getEventHandler(customItem).call(new CustomItemUseEvent(
                    customItem,
                    handToSlot(event.getHand()),
                    item,
                    event.getPlayer(),
                    null,
                    event.getBlock()
            ));
        });
        globalNode.addListener(PlayerEntityInteractEvent.class, event -> {
            ItemStack item = event.getPlayer().getItemInHand(event.getHand());
            CustomItem customItem = module.getCustomItem(item);
            if (customItem == null) return;
            module.getEventHandler(customItem).call(new CustomItemUseEvent(
                    customItem,
                    handToSlot(event.getHand()),
                    item,
                    event.getPlayer(),
                    event.getTarget(),
                    null
            ));
        });
        globalNode.addListener(PlayerUseItemEvent.class, event -> {
            CustomItem customItem = module.getCustomItem(event.getItemStack());
            if (customItem == null) return;
            module.getEventHandler(customItem).call(new CustomItemUseEvent(
                    customItem,
                    handToSlot(event.getHand()),
                    event.getItemStack(),
                    event.getPlayer(),
                    null,
                    null
            ));
        });
        return globalNode;
    }

    private <T extends EntityEvent> void registerEntityEvent(EventNode<Event> eventNode, Class<T> clazz,
                                                             CustomItemEventFactoryMethod<T> factoryMethod) {
        eventNode.addListener(clazz, event -> entityEvent(event, factoryMethod));
    }

    private <T extends ItemEvent> void registerItemEvent(EventNode<Event> eventNode, Class<T> clazz,
                                                         CustomItemEventFactoryMethod<T> factoryMethod) {
        eventNode.addListener(clazz, event -> itemEvent(event, factoryMethod));
    }

    private <T extends EntityEvent> void entityEvent(T event, CustomItemEventFactoryMethod<T> factoryMethod) {
        if (event.getEntity() instanceof LivingEntity livingEntity) {
            forEachEquipment(livingEntity, (item, slot) -> {
                CustomItem customItem = module.getCustomItem(item);
                if (customItem == null) return;
                module.getEventHandler(customItem).call(factoryMethod.createEvent(customItem, slot, item, event));
            });
        }
    }

    private <T extends ItemEvent> void itemEvent(T event, CustomItemEventFactoryMethod<T> factoryMethod) {
        CustomItem customItem = module.getCustomItem(event.getItemStack());
        if (customItem == null) return;
        module.getEventHandler(customItem).call(factoryMethod.createEvent(customItem, null, event.getItemStack(), event));
    }

    private void forEachEquipment(LivingEntity entity, BiConsumer<ItemStack, EquipmentSlot> consumer) {
        consumer.accept(entity.getHelmet(), EquipmentSlot.HELMET);
        consumer.accept(entity.getChestplate(), EquipmentSlot.CHESTPLATE);
        consumer.accept(entity.getLeggings(), EquipmentSlot.LEGGINGS);
        consumer.accept(entity.getBoots(), EquipmentSlot.BOOTS);
        consumer.accept(entity.getItemInOffHand(), EquipmentSlot.OFF_HAND);
        consumer.accept(entity.getItemInMainHand(), EquipmentSlot.MAIN_HAND);
    }

    private EquipmentSlot handToSlot(Player.Hand hand) {
        return hand == Player.Hand.MAIN ? EquipmentSlot.MAIN_HAND : EquipmentSlot.OFF_HAND;
    }

}
