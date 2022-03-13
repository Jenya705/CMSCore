package com.github.jenya705.cmscore.module.item;

import com.github.jenya705.cmscore.CoreApplication;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.utils.NamespaceID;

import java.util.Objects;

/**
 * @author Jenya705
 */
public class GiveCommand extends Command {

    public GiveCommand() {
        super("give");

        var itemArgument = ArgumentType.String("item");
        var amountArgument = ArgumentType.Integer("amount").setDefaultValue(1);

        CustomItemModule itemModule = CoreApplication.getInstance().getModule("item");

        addSyntax((sender, context) -> {
            if (!(sender.hasPermission("core.give")) || !(sender instanceof Player player)) {
                return;
            }
            NamespaceID itemID = NamespaceID.from(context.get(itemArgument));
            int amount = context.get(amountArgument);
            ItemStack item;
            if (itemID.domain().equals("minecraft")) {
                item = ItemStack.of(
                        Objects.requireNonNull(Material.fromNamespaceId(itemID)),
                        amount
                );
            }
            else {
                item = itemModule.getCustomItem(itemID.toString())
                        .createBuilder()
                        .amount(amount)
                        .build();
            }
            player.getInventory().addItemStack(item);
        }, itemArgument, amountArgument);

    }

}
