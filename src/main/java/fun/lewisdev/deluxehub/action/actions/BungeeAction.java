package fun.lewisdev.deluxehub.action.actions;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.action.Action;
import org.bukkit.entity.Player;
import fun.lewisdev.deluxehub.utility.TextUtil;

public class BungeeAction implements Action {

    @Override
    public String getIdentifier() {
        return "BUNGEE";
    }

    @Override
    public void execute(DeluxeHubPlugin plugin, Player player, String data) {
        if(data.contains(";")) {
            String[] params = data.split(";", 2);
            data = params[0];
            if(params[1].strip().toUpperCase().equals("RESTRICTED")) {
                if(!player.hasPermission("bungeecord.server."+data)) {
                    player.sendMessage(TextUtil.color("&cYou don't have permission to access "+data));
                    return;
                }
            }
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ConnectOther");
        out.writeUTF(player.getName());
        out.writeUTF(data);
        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }
}
