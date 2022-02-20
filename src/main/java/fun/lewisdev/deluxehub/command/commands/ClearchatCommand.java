package fun.lewisdev.deluxehub.command.commands;

import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.exceptions.CommandException;
import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.Permissions;
import fun.lewisdev.deluxehub.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearchatCommand {

    public ClearchatCommand(DeluxeHubPlugin plugin) {
    }

    @Command(
            aliases = {"clearchat"},
            desc = "Clear global or a player's chat",
            usage = "[player]",
            max = 1
    )
    public void clearchat(final CommandContext args, final CommandSender sender) throws CommandException {

        if (!(sender.hasPermission(Permissions.COMMAND_CLEARCHAT.getPermission()))) {
            Messages.NO_PERMISSION.send(sender);
            return;
        }

        if (args.argsLength() == 0) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                for (int i = 0; i < 100; i++) {
                    player.sendMessage("");
                }
                Messages.CLEARCHAT.send(player, "%player%", sender.getName());
            }
        } else if (args.argsLength() == 1) {

            Player player = Bukkit.getPlayer(args.getString(0));
            if (player == null) {
                Messages.INVALID_PLAYER.send(sender, "%player%", args.getString(0));
                return;
            }

            for (int i = 0; i < 100; i++) {
                player.sendMessage("");
            }
            Messages.CLEARCHAT_PLAYER.send(sender, "%player%", sender.getName());
        }
    }
}

