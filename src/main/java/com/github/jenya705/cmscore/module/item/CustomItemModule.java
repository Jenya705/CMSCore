package com.github.jenya705.cmscore.module.item;

import com.github.jenya705.cmscore.module.AbstractCoreModule;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.attribute.Attribute;
import net.minestom.server.attribute.AttributeInstance;
import net.minestom.server.attribute.AttributeModifier;
import net.minestom.server.attribute.AttributeOperation;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.item.EntityEquipEvent;
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

    private final Map<String, CustomItem> items = new ConcurrentHashMap<>();

    public CustomItemModule() {
        super("item");
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
                .key("hot:damage_doubler")
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

    public EventNode<ItemEvent> buildNode() {
        EventNode<ItemEvent> node = EventNode.type("item-applier", EventFilter.ITEM);
        node.addListener(EntityEquipEvent.class, e -> {
            if (e.getEntity() instanceof LivingEntity livingEntity) {
                retAttributes(livingEntity, getEquipmentSlot(livingEntity, e.getSlot()));
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
        CustomItem customItem = getHotItem(item);
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

    public CustomItem getHotItem(ItemStack itemStack) {
        String id = itemStack.getTag(CustomItem.idTag);
        if (id != null) return getHotItem(id);
        return null;
    }

    public CustomItem getHotItem(String id) {
        return items.get(id);
    }

    public static ItemStack getEquipmentSlot(LivingEntity entity, EquipmentSlot slot) {
        return switch (slot) {
            case BOOTS -> entity.getBoots();
            case HELMET -> entity.getHelmet();
            case LEGGINGS -> entity.getLeggings();
            case OFF_HAND -> entity.getItemInOffHand();
            case MAIN_HAND -> entity.getItemInMainHand();
            case CHESTPLATE -> entity.getChestplate();
        };
    }

}
