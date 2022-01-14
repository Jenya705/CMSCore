package net.hostlicer.core.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class GameModeCommand extends Command {

    private static class ExactGameModeCommand extends Command {

        public ExactGameModeCommand(GameMode gameMode) {
            super(gameMode.name().toLowerCase(Locale.ROOT));

            EntityFinder defaultEntityFinder = new EntityFinder();
            defaultEntityFinder.setTargetSelector(EntityFinder.TargetSelector.SELF);
            var playerArgument = ArgumentType
                    .Entity("player")
                    .onlyPlayers(true)
                    .setDefaultValue(defaultEntityFinder);

            String formattedPermission = "minecraft.command.gamemode.%s".formatted(getName());

            addSyntax((sender, context) -> {
                EntityFinder playerFinder = context.get(playerArgument);
                List<Entity> entities = playerFinder.find(sender);
                if (!sender.hasPermission("minecraft.command.gamemode") &&
                        !sender.hasPermission(formattedPermission)) {
                    sender.sendMessage(Component
                            .text("You don't have permission to do that")
                            .color(NamedTextColor.RED)
                    );
                    return;
                }
                Component gameModeComponent = Component.translatable("gameMode.%s".formatted(getName()));
                Component successSelf = Component.translatable("commands.gamemode.success.self", gameModeComponent);
                entities.forEach(entity -> {
                    if (!(entity instanceof Player player)) return;
                    player.setGameMode(gameMode);
                    if (entity == sender) {
                        sender.sendMessage(successSelf);
                    }
                    else {
                        sender.sendMessage(Component.translatable(
                                "commands.gamemode.success.other",
                                Objects.requireNonNullElse(
                                        player.getDisplayName(),
                                        player.getName()
                                ),
                                gameModeComponent));
                    }
                });
            }, playerArgument);

        }

    }

    public GameModeCommand() {
        super("gamemode");

        for (GameMode gameMode: GameMode.values()) {
            addSubcommand(new ExactGameModeCommand(gameMode));
        }

    }

}
