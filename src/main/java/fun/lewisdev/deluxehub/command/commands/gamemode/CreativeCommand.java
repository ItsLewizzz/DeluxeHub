package fun.lewisdev.deluxehub.command.commands.gamemode;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import fun.lewisdev.deluxehub.DeluxeHub;
import fun.lewisdev.deluxehub.config.Messages;
import fun.lewisdev.deluxehub.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreativeCommand {

	public CreativeCommand(DeluxeHub plugin) {}

	@Command(
			aliases = {"gmc"},
			desc = "Change to creative mode",
			usage = "[player]",
			max = 1
	)
	public void creative(final CommandContext args, final CommandSender sender) throws CommandException {
		if(args.argsLength() == 0) {
			if (!(sender instanceof Player)) throw new CommandException("Console cannot change gamemode");

			Player player = (Player)sender;
			if (!player.hasPermission(Permissions.COMMAND_GAMEMODE.getPermission())) {
				player.sendMessage(Messages.NO_PERMISSION.toString());
				return;
			}

			player.sendMessage(Messages.GAMEMODE_CHANGE.toString().replace("%gamemode%", "CREATIVE"));
			player.setGameMode(GameMode.CREATIVE);
		}

		else if(args.argsLength() == 1) {
			if(!sender.hasPermission(Permissions.COMMAND_GAMEMODE_OTHERS.getPermission())) {
				sender.sendMessage(Messages.NO_PERMISSION.toString());
				return;
			}

			Player player = Bukkit.getPlayer(args.getString(0));
			if (player == null) {
				sender.sendMessage(Messages.INVALID_PLAYER.toString().replace("%player%", args.getString(0)));
				return;
			}

			if(sender.getName().equals(player.getName())) {
				player.sendMessage(Messages.GAMEMODE_CHANGE.toString().replace("%gamemode%", "CREATIVE"));
			}else{
				player.sendMessage(Messages.GAMEMODE_CHANGE.toString().replace("%gamemode%", "CREATIVE"));
				sender.sendMessage(Messages.GAMEMODE_CHANGE_OTHER.toString().replace("%player%", player.getName()).replace("%gamemode%", "CREATIVE"));
			}
			player.setGameMode(GameMode.CREATIVE);
		}
	}
}
