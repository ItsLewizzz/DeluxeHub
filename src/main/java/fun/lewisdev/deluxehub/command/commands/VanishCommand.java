package fun.lewisdev.deluxehub.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.exceptions.CommandException;
import fun.lewisdev.deluxehub.DeluxeHub;
import fun.lewisdev.deluxehub.Permissions;
import fun.lewisdev.deluxehub.config.Messages;
import fun.lewisdev.deluxehub.module.ModuleType;
import fun.lewisdev.deluxehub.module.modules.player.PlayerVanish;

public class VanishCommand {

    private DeluxeHub plugin;

    public VanishCommand(DeluxeHub plugin) {
        this.plugin = plugin;
    }

    @Command(aliases = { "vanish" }, desc = "Disappear into thin air!")
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
