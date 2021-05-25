package fun.lewisdev.deluxehub.module.modules.visual.scoreboard;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import fr.mrmicky.fastboard.FastBoard;
import fun.lewisdev.deluxehub.utility.PlaceholderUtil;
import fun.lewisdev.deluxehub.utility.TextUtil;

public class ScoreHelper {

    private final FastBoard fastBoard;
    private final Player player;

    public ScoreHelper(Player player) {
        this.player = player;
        this.fastBoard = new FastBoard(player);
    }

    public void setTitle(String title) {
        fastBoard.updateTitle(setPlaceholders(title));
    }

    public void removeSlot(int slot) {
        fastBoard.removeLine(slot);
    }

    public void setSlotsFromList(List<String> list) {
        fastBoard.updateLines(setPlaceholders(list));
    }

    public List<String> setPlaceholders(List<String> textList) {
        List<String> parsedList = new ArrayList<>();

        for (String text : textList) {
            parsedList.add(setPlaceholders(text));
        }

        return parsedList;
    }

    public String setPlaceholders(String text) {
        return TextUtil.color(PlaceholderUtil.setPlaceholders(text, player));
    }
}
