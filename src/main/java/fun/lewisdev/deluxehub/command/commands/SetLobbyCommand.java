package fun.lewisdev.deluxehub.command.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sun.javafx.scene.control.skin.Utils;
import fun.lewisdev.deluxehub.DeluxeHub;
import fun.lewisdev.deluxehub.Permissions;
import fun.lewisdev.deluxehub.config.Messages;
import fun.lewisdev.deluxehub.module.ModuleType;
import fun.lewisdev.deluxehub.module.modules.world.LobbySpawn;
import fun.lewisdev.deluxehub.utility.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLobbyCommand {

	private DeluxeHub plugin;

	public SetLobbyCommand(DeluxeHub plugin) {
		this.plugin = plugin;
	}

	@Command(
			aliases = {"setlobby"},
			desc = "Set the lobby location"
	)
	public void setlobby(final CommandContext args, final CommandSender sender) throws CommandException {

		if (!sender.hasPermission(Permissions.COMMAND_SET_LOBBY.getPermission())) {
			sender.sendMessage(Messages.NO_PERMISSION.toString());
			return;
		}

		if (!(sender instanceof Player)) {
			sender.sendMessage("Console cannot set the spawn location.");
			return;
		}

		Player player = (Player) sender;
		if(plugin.getModuleManager().getDisabledWorlds().contains(player.getWorld().getName())) {
			sender.sendMessage(TextUtil.color("&cYou cannot set the lobby location in a disabled world."));
			return;
		}

		LobbySpawn lobbyModule = ((LobbySpawn) plugin.getModuleManager().getModule(ModuleType.LOBBY));
		lobbyModule.setLocation(player.getLocation());
		sender.sendMessage(Messages.SET_LOBBY.toString());

	}

}
