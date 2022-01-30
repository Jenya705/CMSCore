package net.hostlicer.core.command;

import net.hostlicer.core.CorePlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.location.RelativeVec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Jenya705
 */
public class PosCommand extends Command {

    private static class ExactPosCommand extends Command {

        public ExactPosCommand(int pos) {
            super(Integer.toString(pos + 1));

            var positionArgument = ArgumentType
                    .RelativeBlockPosition("block")
                    .setDefaultValue(new RelativeVec(
                            Vec.ZERO,
                            RelativeVec.CoordinateType.RELATIVE,
                            true, true, true
                    ));

            addSyntax((sender, context) -> {
                if (!(sender instanceof Player player)) {
                    return;
                }
                if (!player.hasPermission("core.pos")) {
                    return;
                }
                RelativeVec relativeVec = context.get(positionArgument);
                Vec position = relativeVec.fromSender(player);
                CorePlayer.of(player).setPos(pos, position);
                sender.sendMessage(Component
                        .text("Success")
                        .color(NamedTextColor.GREEN)
                );
            }, positionArgument);

        }

    }

    public PosCommand() {
        this("pos");
    }

    public PosCommand(@NotNull String name, @Nullable String... aliases) {
        super(name, aliases);

        addSubcommand(new ExactPosCommand(0));
        addSubcommand(new ExactPosCommand(1));

    }
}
