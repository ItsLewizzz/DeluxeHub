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

public class FlyCommand {

    public FlyCommand(DeluxeHubPlugin plugin) {
    }

    @Command(
            aliases = {"fly"},
            desc = "Toggle flight mode",
            usage = "[player]",
            max = 1
    )
    public void flight(final CommandContext args, final CommandSender sender) throws CommandException {

        if (args.argsLength() == 0) {
            if (!(sender instanceof Player)) throw new CommandException("Console cannot clear inventory");

            if (!(sender.hasPermission(Permissions.COMMAND_FLIGHT.getPermission()))) {
                sender.sendMessage(Messages.NO_PERMISSION.toString());
                return;
            }

            Player player = (Player) sender;

            if (player.getAllowFlight()) {
                player.sendMessage(Messages.FLIGHT_DISABLE.toString());
                toggleFlight(player, false);
            } else {
                player.sendMessage(Messages.FLIGHT_ENABLE.toString());
                toggleFlight(player, true);
            }
        } else if (args.argsLength() == 1) {
            if (!(sender.hasPermission(Permissions.COMMAND_FLIGHT_OTHERS.getPermission()))) {
                sender.sendMessage(Messages.NO_PERMISSION.toString());
                return;
            }

            Player player = Bukkit.getPlayer(args.getString(0));
            if (player == null) {
                sender.sendMessage(Messages.INVALID_PLAYER.toString().replace("%player%", args.getString(0)));
                return;
            }
            if (player.getAllowFlight()) {
                player.sendMessage(Messages.FLIGHT_DISABLE.toString());
                sender.sendMessage(Messages.FLIGHT_DISABLE_OTHER.toString().replace("%player%", player.getName()));
                toggleFlight(player, false);
            } else {
                player.sendMessage(Messages.FLIGHT_ENABLE.toString());
                sender.sendMessage(Messages.FLIGHT_ENABLE_OTHER.toString().replace("%player%", player.getName()));
                toggleFlight(player, true);
            }
        }
    }

    private void toggleFlight(Player player, boolean value) {
        player.setAllowFlight(value);
        player.setFlying(value);
    }
}
