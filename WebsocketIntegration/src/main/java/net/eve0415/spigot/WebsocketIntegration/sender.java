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
            if (obj.getString("UUID") != null) {
                name = Bukkit.getPlayer(UUID.fromString(obj.getString("UUID"))) != null
                        ? Bukkit.getPlayer(UUID.fromString(obj.getString("UUID"))).getName()
                        : Bukkit.getOfflinePlayer(UUID.fromString(obj.getString("UUID"))).getName();
            }
            StringHandler(name, obj.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void StringHandler(String name, String message) {
        broadcast("<" + name + "> " + message);
    }

    private void broadcast(String str) {
        Bukkit.broadcastMessage(str);
    }

    public void privateMessage(Player player, String str) {
        player.sendMessage(str);
    }
}