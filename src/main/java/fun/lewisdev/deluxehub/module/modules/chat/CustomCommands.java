package fun.lewisdev.deluxehub.module.modules.chat;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import fun.lewisdev.deluxehub.DeluxeHub;
import fun.lewisdev.deluxehub.command.CustomCommand;
import fun.lewisdev.deluxehub.config.Messages;
import fun.lewisdev.deluxehub.module.Module;
import fun.lewisdev.deluxehub.module.ModuleType;

public class CustomCommands extends Module {

    private List<CustomCommand> commands;

    public CustomCommands(DeluxeHub plugin) {
        super(plugin, ModuleType.CUSTOM_COMMANDS);
    }

    @Override
    public void onEnable() {
        commands = getPlugin().getCommandManager().getCustomCommands();
    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {

        Player player = event.getPlayer();
        if (inDisabledWorld(player.getLocation()))
            return;

        String command = event.getMessage().toLowerCase().replace("/", "");

        for (CustomCommand customCommand : commands) {
            if (customCommand.getAliases().stream().anyMatch(alias -> alias.equals(command))) {
                if (customCommand.getPermission() != null)
                    if (!player.hasPermission(customCommand.getPermission())) {
                        player.sendMessage(Messages.CUSTOM_COMMAND_NO_PERMISSION.toString());
                        event.setCancelled(true);
                        return;
                    }
                event.setCancelled(true);
                getPlugin().getActionManager().executeActions(player, customCommand.getActions());
            }
        }

    }

}
