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

public class GamemodeCommand {

    public GamemodeCommand(DeluxeHubPlugin plugin) {
    }

    @Command(
            aliases = {"gamemode"},
            desc = "Allows you to change gamemode",
            usage = "<gamemode> [player]",
            min = 1,
            max = 2
    )
    public void gamemode(final CommandContext args, final CommandSender sender) throws CommandException {

        if (args.argsLength() == 1) {

            if (!(sender instanceof Player)) throw new CommandException("Console cannot change gamemode");

            Player player = (Player) sender;
            if (!player.hasPermission(Permissions.COMMAND_GAMEMODE.getPermission())) {
                Messages.NO_PERMISSION.send(sender);
                return;
            }

            GameMode gamemode = getGamemode(args.getString(0));

            if (gamemode == null) {
                Messages.GAMEMODE_INVALID.send(sender, "%gamemode%", args.getString(0));
                return;
            }

            Messages.GAMEMODE_CHANGE.send(player, "%gamemode%", gamemode.toString().toUpperCase());
            player.setGameMode(gamemode);

        } else if (args.argsLength() == 2) {
            if (!sender.hasPermission(Permissions.COMMAND_GAMEMODE_OTHERS.getPermission())) {
                Messages.NO_PERMISSION.send(sender);
                return;
            }

            Player player = Bukkit.getPlayer(args.getString(1));
            if (player == null) {
                Messages.INVALID_PLAYER.send(sender, "%player%", args.getString(0));
                return;
            }

            GameMode gamemode = getGamemode(args.getString(0));

            if (gamemode == null) {
                Messages.GAMEMODE_INVALID.send(sender, "%gamemode%", args.getString(0));
                return;
            }

            if (sender.getName().equals(player.getName())) {
                Messages.GAMEMODE_CHANGE.send(player, "%gamemode%", gamemode.toString().toUpperCase());
            } else {
                Messages.GAMEMODE_CHANGE.send(player, "%gamemode%", gamemode.toString().toUpperCase());
                Messages.GAMEMODE_CHANGE_OTHER.send(sender, "%player%", player.getName(), "%gamemode%", gamemode.toString().toUpperCase());
            }

            player.setGameMode(gamemode);
        }
    }

    private GameMode getGamemode(String gamemode) {
        if (gamemode.equals("0") || gamemode.equalsIgnoreCase("survival") || gamemode.equalsIgnoreCase("s")) {
            return GameMode.SURVIVAL;
        } else if (gamemode.equals("1") || gamemode.equalsIgnoreCase("creative") || gamemode.equalsIgnoreCase("c")) {
            return GameMode.CREATIVE;
        } else if (gamemode.equals("2") || gamemode.equalsIgnoreCase("adventure") || gamemode.equalsIgnoreCase("a")) {
            return GameMode.ADVENTURE;
        } else if (gamemode.equals("3") || gamemode.equalsIgnoreCase("spectator") || gamemode.equalsIgnoreCase("sp")) {
            return GameMode.SPECTATOR;
        }
        return null;
    }
}
