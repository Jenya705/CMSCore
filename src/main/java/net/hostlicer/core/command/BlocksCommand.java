package net.hostlicer.core.command;

import net.hostlicer.core.CorePlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Jenya705
 */
public class BlocksCommand extends Command {

    public BlocksCommand() {
        this("blocks");
    }

    public BlocksCommand(@NotNull String name, @Nullable String... aliases) {
        super(name, aliases);

        var fillWith = ArgumentType.BlockState("fill");

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) {
                return;
            }
            if (!player.hasPermission("core.blocks")){
                return;
            }
            CorePlayer corePlayer = CorePlayer.of(player);
            Block block = context.get(fillWith);
            Instance instance = player.getInstance();
            if (instance == null || corePlayer.isEmptySelection()) return;
            corePlayer.getSelection().forEach((x, y, z) -> instance.setBlock(x, y, z, block));
        }, fillWith);

    }
}
