package fun.lewisdev.deluxehub.command.commands;

import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.exceptions.CommandException;
import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.Permissions;
import fun.lewisdev.deluxehub.config.Messages;
import fun.lewisdev.deluxehub.module.ModuleType;
import fun.lewisdev.deluxehub.module.modules.player.PlayerVanish;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand {

    private DeluxeHubPlugin plugin;

    public VanishCommand(DeluxeHubPlugin plugin) {
        this.plugin = plugin;
    }

    @Command(
            aliases = {"vanish"},
            desc = "Disappear into thin air!"
    )
    public void vanish(final CommandContext args, final CommandSender sender) throws CommandException {

        if (!sender.hasPermission(Permissions.COMMAND_VANISH.getPermission())) {
            Messages.NO_PERMISSION.send(sender);
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
