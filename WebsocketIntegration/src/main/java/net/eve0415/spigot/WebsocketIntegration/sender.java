package net.eve0415.spigot.WebsocketIntegration;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONException;
import org.json.JSONObject;

public class sender {
    // private main instance;

    public sender(final main instance) {
        // this.instance = instance;
    }

    public void processer(final JSONObject obj) {
        String name;
        try {
            name = obj.getString("name");
            if (obj.getString("UUID").equals(null)) {
                name = Bukkit.getPlayer(UUID.fromString(obj.getString("UUID"))) == null
                        ? Bukkit.getOfflinePlayer(UUID.fromString(obj.getString("UUID"))).getName()
                        : Bukkit.getPlayer(UUID.fromString(obj.getString("UUID"))).getName();
            }
            broadcast(stringHandler(name, obj.getString("message")));
        } catch (final JSONException e) {
            e.printStackTrace();
        }
    }

    private String stringHandler(final String name, final String message) {
        return String.format("<" + name + "> " + message);
    }

    private void broadcast(final String str) {
        Bukkit.broadcastMessage(str);
    }

    public void privateMessage(final Player player, final String str) {
        player.sendMessage(str);
    }
}