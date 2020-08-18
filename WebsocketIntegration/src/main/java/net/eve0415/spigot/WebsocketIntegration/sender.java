package net.eve0415.spigot.WebsocketIntegration;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONException;
import org.json.JSONObject;

public class sender {
    // private main instance;

    public sender(main instance) {
        // this.instance = instance;
    }

    public void processer(JSONObject obj) {
        String name;
        try {
            name = obj.getString("name");
            if (obj.getString("UUID").equals(null)) {
                name = Bukkit.getPlayer(UUID.fromString(obj.getString("UUID"))) == null
                        ? Bukkit.getOfflinePlayer(UUID.fromString(obj.getString("UUID"))).getName()
                        : Bukkit.getPlayer(UUID.fromString(obj.getString("UUID"))).getName();
            }
            broadcast(stringHandler(name, obj.getString("message")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String stringHandler(String name, String message) {
        return String.format("<" + name + "> " + message);
    }

    private void broadcast(String str) {
        Bukkit.broadcastMessage(str);
    }

    public void privateMessage(Player player, String str) {
        player.sendMessage(str);
    }
}