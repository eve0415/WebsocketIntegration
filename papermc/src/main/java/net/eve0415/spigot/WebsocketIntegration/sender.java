package net.eve0415.spigot.WebsocketIntegration;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONException;
import org.json.JSONObject;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class sender {
    // private main instance;

    public sender(final main instance) {
        // this.instance = instance;
    }

    public void processer(final JSONObject obj) {
        String name;
        try {
            name = obj.getString("name");
            if (!obj.getString("UUID").equals("null")) {
                name = Bukkit.getPlayer(UUID.fromString(obj.getString("UUID"))) == null
                        ? Bukkit.getOfflinePlayer(UUID.fromString(obj.getString("UUID"))).getName()
                        : Bukkit.getPlayer(UUID.fromString(obj.getString("UUID"))).getName();
            }

            final TextComponent message = new TextComponent(stringHandler(name, obj.getString("message")));
            if (!obj.getString("url").equals("null")) {
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("クリックすると添付ファイルが開きます")));
                message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, obj.getString("url")));
            }

            sendMessage(message);
        } catch (final JSONException e) {
            e.printStackTrace();
        }
    }

    private String stringHandler(final String name, final String message) {
        return String.format("<" + name + "> " + message);
    }

    private void sendMessage(final TextComponent message) {
        Bukkit.broadcast(message);
    }

    public void privateMessage(final Player player, final String str) {
        player.sendMessage(str);
    }
}
