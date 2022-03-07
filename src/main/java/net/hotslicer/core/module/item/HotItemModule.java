package net.hotslicer.core.module.item;

import net.hotslicer.core.module.AbstractCoreModule;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.attribute.Attribute;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.item.EntityEquipEvent;
import net.minestom.server.event.trait.ItemEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

/**
 * @author Jenya705
 */
public class HotItemModule extends AbstractCoreModule {

    private final Map<String, HotItem> items = new ConcurrentHashMap<>();

    public HotItemModule() {
        super("item");
    }

    @Override
    public void start() {
        MinecraftServer.getGlobalEventHandler().addChild(buildNode());
        MinecraftServer.getCommandManager().register(new GiveCommand());
        registerItem(EasyHotItem
                .builder()
                .material(Material.STICK)
                .attribute(Attribute.ATTACK_DAMAGE, 1, true)
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

    public void registerItem(HotItem item) {
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
        applyForHotAttributes(entity, item, HotItemAttribute::apply);
    }

    private void retAttributes(LivingEntity entity, ItemStack item) {
        applyForHotAttributes(entity, item, HotItemAttribute::ret);
    }

    private void applyForHotAttributes(LivingEntity entity, ItemStack item, BiFunction<HotItemAttribute, Float, Float> consumer) {
        HotItem hotItem = getHotItem(item);
        if (hotItem == null) return; // nothing to deal with
        var attributes = hotItem.getAttributes();
        if (attributes == null) return; // no attributes to be set
        attributes.forEach((attribute, itemAttribute) -> {
            var entityAttribute = entity.getAttribute(attribute);
            entityAttribute.setBaseValue(consumer.apply(itemAttribute, entityAttribute.getValue()));
        });
    }

    public HotItem getHotItem(ItemStack itemStack) {
        String id = itemStack.getTag(HotItem.idTag);
        if (id != null) return getHotItem(id);
        return null;
    }

    public HotItem getHotItem(String id) {
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
