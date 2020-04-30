package fun.lewisdev.deluxehub.command.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import fun.lewisdev.deluxehub.DeluxeHub;
import fun.lewisdev.deluxehub.Permissions;
import fun.lewisdev.deluxehub.config.Messages;
import fun.lewisdev.deluxehub.module.ModuleType;
import fun.lewisdev.deluxehub.module.modules.player.PlayerVanish;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand {

	private DeluxeHub plugin;

	public VanishCommand(DeluxeHub plugin) {
		this.plugin = plugin;
	}

	@Command(
			aliases = {"vanish"},
			desc = "Disappear into thin air!"
	)
	public void vanish(final CommandContext args, final CommandSender sender) throws CommandException {

		if (!sender.hasPermission(Permissions.COMMAND_VANISH.getPermission())) {
			sender.sendMessage(Messages.NO_PERMISSION.toString());
			return;
		}

		if (!(sender instanceof Player)) {
			sender.sendMessage("Console cannot set the spawn location.");
			return;
		}

		Player player = (Player) sender;
		PlayerVanish vanishModule = ((PlayerVanish) plugin.getModuleManager().getModule(ModuleType.VANISH));
		vanishModule.toggleVanish(player);
	}

}
