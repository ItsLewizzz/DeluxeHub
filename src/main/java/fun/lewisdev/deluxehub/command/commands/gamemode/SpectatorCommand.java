package fun.lewisdev.deluxehub.command.commands.gamemode;

import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.exceptions.CommandException;
import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.Permissions;
import fun.lewisdev.deluxehub.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpectatorCommand {

    public SpectatorCommand(DeluxeHubPlugin plugin) {
    }

    @Command(
            aliases = {"gmsp"},
            desc = "Change to spectator mode",
            usage = "[player]",
            max = 1
    )
    public void spectator(final CommandContext args, final CommandSender sender) throws CommandException {
        if (args.argsLength() == 0) {
            if (!(sender instanceof Player)) throw new CommandException("Console cannot change gamemode");

            Player player = (Player) sender;
            if (!player.hasPermission(Permissions.COMMAND_GAMEMODE.getPermission())) {
                Messages.NO_PERMISSION.send(sender);
                return;
            }

            Messages.GAMEMODE_CHANGE.send(player, "%gamemode%", "SPECTATOR");
            player.setGameMode(GameMode.SPECTATOR);
        } else if (args.argsLength() == 1) {
            if (!sender.hasPermission(Permissions.COMMAND_GAMEMODE_OTHERS.getPermission())) {
                Messages.NO_PERMISSION.send(sender);
                return;
            }

            Player player = Bukkit.getPlayer(args.getString(0));
            if (player == null) {
                Messages.INVALID_PLAYER.send(sender, "%player%", args.getString(0));
                return;
            }

            if (sender.getName().equals(player.getName())) {
                Messages.GAMEMODE_CHANGE.send(player, "%gamemode%", "SPECTATOR");
            } else {
                Messages.GAMEMODE_CHANGE.send(player, "%gamemode%", "SPECTATOR");
                Messages.GAMEMODE_CHANGE_OTHER.send(sender, "%player%", player.getName(), "%gamemode%", "SPECTATOR");
            }
            player.setGameMode(GameMode.SPECTATOR);
        }
    }
}