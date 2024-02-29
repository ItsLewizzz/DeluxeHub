package fun.lewisdev.deluxehub.module.modules.visual.scoreboard;

import fun.lewisdev.deluxehub.utility.PlaceholderUtil;
import fun.lewisdev.deluxehub.utility.TextUtil;
import fun.lewisdev.deluxehub.utility.universal.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;

/**
 * @author crisdev333
 */
public class ScoreHelper {

    private Scoreboard scoreboard;
    private Objective objective;
    private Player player;

    public ScoreHelper(Player player) {
        this.player = player;
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("sidebar", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Create Teams
        for (int i = 1; i <= 15; i++) {
            Team team = scoreboard.registerNewTeam("SLOT_" + i);
            team.addEntry(genEntry(i));
        }
        player.setScoreboard(scoreboard);
    }

    public void setTitle(String title) {
        title = setPlaceholders(title);

        if (XMaterial.supports(13)) {
            objective.setDisplayName(title.length() > 256 ? title.substring(0, 256) : title);
        } else {
            objective.setDisplayName(title.length() > 32 ? title.substring(0, 32) : title);
        }

    }

    public void setSlot(int slot, String text) {
        Team team = scoreboard.getTeam("SLOT_" + slot);
        String entry = genEntry(slot);
        if (!scoreboard.getEntries().contains(entry)) {
            objective.getScore(entry).setScore(slot);
        }

        text = setPlaceholders(text);
        String pre = getFirstSplit(text);
        String suf = getFirstSplit(ChatColor.getLastColors(pre) + getSecondSplit(text));
        team.setPrefix(pre);
        team.setSuffix(suf);
    }

    public void removeSlot(int slot) {
        String entry = genEntry(slot);
        if (scoreboard.getEntries().contains(entry)) {
            scoreboard.resetScores(entry);
        }
    }

    public String setPlaceholders(String text) {
        return TextUtil.color(PlaceholderUtil.setPlaceholders(text, this.player));
    }

    public void setSlotsFromList(List<String> list) {
        while (list.size() > 15) {
            list.remove(list.size() - 1);
        }

        int slot = list.size();

        if (slot < 15) {
            for (int i = (slot + 1); i <= 15; i++) {
                removeSlot(i);
            }
        }

        for (String line : list) {
            setSlot(slot, line);
            slot--;
        }
    }

    private String genEntry(int slot) {
        return ChatColor.values()[slot].toString();
    }

    private String getFirstSplit(String s) {
        if (XMaterial.supports(13)) {
            return s.length() > 128 ? s.substring(0, 128) : s;
        }
        return s.length() > 16 ? s.substring(0, 16) : s;
    }

    private String getSecondSplit(String s) {
        if (XMaterial.supports(13)) {
            if (s.length() > 256) {
                s = s.substring(0, 256);
            }
            return s.length() > 128 ? s.substring(128) : "";
        }

        if (s.length() > 32) {
            s = s.substring(0, 32);
        }

        return s.length() > 16 ? s.substring(16) : "";
    }

}
