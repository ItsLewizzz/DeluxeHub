package fun.lewisdev.deluxehub.action.actions;

import org.bukkit.entity.Player;

import fun.lewisdev.deluxehub.DeluxeHub;
import fun.lewisdev.deluxehub.action.Action;
import fun.lewisdev.deluxehub.utility.TextUtil;
import fun.lewisdev.deluxehub.utility.reflection.Titles;

public class TitleAction implements Action {

    @Override
    public String getIdentifier() {
        return "TITLE";
    }

    @Override
    public void execute(DeluxeHub plugin, Player player, String data) {
        String[] args = data.split(";");

        String mainTitle = TextUtil.color(args[0]);
        String subTitle = TextUtil.color(args[1]);

        int fadeIn;
        int stay;
        int fadeOut;
        try {
            fadeIn = Integer.parseInt(args[2]);
            stay = Integer.parseInt(args[3]);
            fadeOut = Integer.parseInt(args[4]);
        } catch (NumberFormatException ex) {
            fadeIn = 1;
            stay = 3;
            fadeOut = 1;
        }

        if (plugin.getServerVersionNumber() > 10) {
            player.sendTitle(mainTitle, subTitle, fadeIn * 20, stay * 20, fadeOut * 20);
        } else {
            Titles.sendTitle(player, fadeIn * 20, stay * 20, fadeOut * 20, mainTitle, subTitle);
        }
    }
}
