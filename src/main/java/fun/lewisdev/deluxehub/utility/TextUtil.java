package fun.lewisdev.deluxehub.utility;

import fun.lewisdev.deluxehub.utility.color.IridiumColorAPI;
import org.bukkit.Color;

import java.util.List;

public class TextUtil {

    private static final int CENTER_PX = 154;

    public static String color(final String string) {
        return IridiumColorAPI.process(string);
    }

    public static String getCenteredMessage(String message) {
        if (message == null || message.equals("")) return "";

        message = color(message).replace("<center>", "").replace("</center>", "");

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '�') {
                previousCode = true;

            } else if (previousCode) {
                previousCode = false;
                if (c == 'l' || c == 'L') {
                    isBold = true;
                } else isBold = false;

            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }

        return sb.toString() + message;

    }

    public static String fromList(List<?> list) {
        if (list == null || list.isEmpty()) return null;
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            if(org.bukkit.ChatColor.stripColor(list.get(i).toString()).equals("")) builder.append("\n&r");
            else builder.append(list.get(i).toString()).append(i + 1 != list.size() ? "\n" : "");
        }

        return builder.toString();
    }

    public static String joinString(int index, String[] args) {
        StringBuilder builder = new StringBuilder();
        for (int i = index; i < args.length; i++) {
            builder.append(args[i]).append(" ");
        }
        return builder.toString();
    }

    public static Color getColor(String s) {
        switch (s.toUpperCase()) {
            case "AQUA":
                return Color.AQUA;
            case "BLACK":
                return Color.BLACK;
            case "BLUE":
                return Color.BLUE;
            case "FUCHSIA":
                return Color.FUCHSIA;
            case "GRAY":
                return Color.GRAY;
            case "GREEN":
                return Color.GREEN;
            case "LIME":
                return Color.LIME;
            case "MAROON":
                return Color.MAROON;
            case "NAVY":
                return Color.NAVY;
            case "OLIVE":
                return Color.OLIVE;
            case "ORANGE":
                return Color.ORANGE;
            case "PURPLE":
                return Color.PURPLE;
            case "RED":
                return Color.RED;
            case "SILVER":
                return Color.SILVER;
            case "TEAL":
                return Color.TEAL;
            case "WHITE":
                return Color.WHITE;
            case "YELLOW":
                return Color.YELLOW;
            default:
                return null;
        }
    }

}
