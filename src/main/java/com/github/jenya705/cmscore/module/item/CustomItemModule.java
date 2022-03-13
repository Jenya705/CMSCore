package com.github.jenya705.cmscore.module.item;

import com.github.jenya705.cmscore.module.AbstractCoreModule;
import com.github.jenya705.cmscore.module.item.event.AllCustomItemEvent;
import com.github.jenya705.cmscore.module.item.event.CustomItemEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.attribute.Attribute;
import net.minestom.server.attribute.AttributeInstance;
import net.minestom.server.attribute.AttributeModifier;
import net.minestom.server.attribute.AttributeOperation;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.item.EntityEquipEvent;
import net.minestom.server.event.trait.EntityEvent;
import net.minestom.server.event.trait.ItemEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

/**
 * @author Jenya705
 */
public class CustomItemModule extends AbstractCoreModule {

    private static final EventFilter<CustomItemEvent, CustomItem> customItemEventFilter =
            EventFilter.from(CustomItemEvent.class, CustomItem.class, CustomItemEvent::getCustomItem);

    private final Map<String, CustomItem> items = new ConcurrentHashMap<>();
    private final Map<String, EventNode<CustomItemEvent>> itemEventHandlers = new ConcurrentHashMap<>();

    public CustomItemModule() {
        super("item");
        MinecraftServer.getGlobalEventHandler().addChild(buildCustomItemEventNode());
    }

    private void listenEvent(Event event) {
        if (event instanceof ItemEvent itemEvent) {
            callEvent(itemEvent.getItemStack(), null, event);
        }
        if (event instanceof EntityEvent entityEvent &&
                entityEvent.getEntity() instanceof LivingEntity livingEntity) {
            callEvent(livingEntity.getItemInMainHand(), EquipmentSlot.MAIN_HAND, event);
            callEvent(livingEntity.getItemInOffHand(), EquipmentSlot.OFF_HAND, event);
            callEvent(livingEntity.getHelmet(), EquipmentSlot.HELMET, event);
            callEvent(livingEntity.getChestplate(), EquipmentSlot.CHESTPLATE, event);
            callEvent(livingEntity.getLeggings(), EquipmentSlot.LEGGINGS, event);
            callEvent(livingEntity.getBoots(), EquipmentSlot.BOOTS, event);
        }
    }

    private void callEvent(ItemStack itemStack, EquipmentSlot slot, Event event) {
        CustomItem customItem = getCustomItem(itemStack);
        if (customItem == null) return;
        EventNode<CustomItemEvent> customItemEventHandler = getEventHandler(customItem);
        if (customItemEventHandler != null) {
            customItemEventHandler.call(new AllCustomItemEvent(
                    customItem, itemStack, slot, event
            ));
        }
    }

    @Override
    public void start() {
        MinecraftServer.getGlobalEventHandler().addChild(buildNode());
        MinecraftServer.getCommandManager().register(new GiveCommand());
        registerItem(EasyCustomItem
                .builder()
                .material(Material.STICK)
                .attribute(Attribute.ATTACK_DAMAGE, 1, AttributeOperation.MULTIPLY_TOTAL)
                .displayName(Component
                        .text("Damage doubler")
                        .color(NamedTextColor.DARK_RED)
                )
                .key("cms:damage_doubler")
                .build()
        );
    }

    @Override
    public void stop() {
    }

    public void registerItem(CustomItem item) {
        getLogger().info("Registering item {}", item.key());
        items.put(item.key().toString(), item);
    }

    public EventNode<Event> buildCustomItemEventNode() {
        EventNode<Event> globalNode = EventNode.all("custom-item-node");
        globalNode.addListener(Event.class, this::listenEvent);
        return globalNode;
    }

    public EventNode<ItemEvent> buildNode() {
        EventNode<ItemEvent> node = EventNode.type("item-applier", EventFilter.ITEM);
        node.addListener(EntityEquipEvent.class, e -> {
            if (e.getEntity() instanceof LivingEntity livingEntity) {
                retAttributes(livingEntity, livingEntity.getEquipment(e.getSlot()));
                applyAttributes(livingEntity, e.getEquippedItem());
            }
        });
        return node;
    }

    private void applyAttributes(LivingEntity entity, ItemStack item) {
        actAttribute(entity, item, AttributeInstance::addModifier);
    }

    private void retAttributes(LivingEntity entity, ItemStack item) {
        actAttribute(entity, item, AttributeInstance::removeModifier);
    }

    private void actAttribute(LivingEntity entity, ItemStack item, BiConsumer<AttributeInstance, AttributeModifier> consumer) {
        CustomItem customItem = getCustomItem(item);
        if (customItem == null) return;
        Map<Attribute, CustomItemAttribute> attributes = customItem.getAttributes();
        if (attributes == null) return;
        attributes.forEach((attribute, customItemAttribute) -> consumer.accept(
                entity.getAttribute(attribute),
                new AttributeModifier(
                        attributeUUID(customItem, attribute),
                        customItem.key() + attribute.key(),
                        customItemAttribute.getValue(),
                        customItemAttribute.getOperation()
                )
        ));
    }

    private UUID attributeUUID(CustomItem item, Attribute attribute) {
        return UUID.nameUUIDFromBytes((item.key() + attribute.key()).getBytes(StandardCharsets.UTF_8));
    }

    public CustomItem getCustomItem(ItemStack itemStack) {
        String id = itemStack.getTag(CustomItem.idTag);
        if (id != null) return getCustomItem(id);
        return null;
    }

    public CustomItem getCustomItem(String id) {
        return items.get(id);
    }

    public EventNode<CustomItemEvent> getEventHandler(String id) {
        itemEventHandlers.computeIfAbsent(id, key -> EventNode.type(
                "custom-item-handler-" + key, customItemEventFilter
        ));
        return itemEventHandlers.get(id);
    }

    public EventNode<CustomItemEvent> getEventHandler(CustomItem item) {
        return getEventHandler(item.key().toString());
    }

}
