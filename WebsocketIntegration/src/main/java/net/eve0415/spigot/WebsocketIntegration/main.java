package net.eve0415.spigot.WebsocketIntegration;

import org.bukkit.plugin.java.JavaPlugin;

import net.eve0415.spigot.WebsocketIntegration.commands.link;
import net.eve0415.spigot.WebsocketIntegration.websocket.EventState;
import net.eve0415.spigot.WebsocketIntegration.websocket.Manager;

public class main extends JavaPlugin {
    public static main instance;

    public Manager websocketManager;
    public linkManager linkManager;
    public sender sender;
    public commandHandler commandHandler;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        getConfig();

        this.websocketManager = new Manager(this);
        this.linkManager = new linkManager(this);
        this.sender = new sender(this);
        this.commandHandler = new commandHandler(this);

        new EventListner(this);

        getCommand("link").setExecutor(new link(this));

        getLogger().info("WebsocketIntegration enabled");
    }

    @Override
    public void onDisable() {
        this.websocketManager.send(EventState.STOPPING, null, null);
        getLogger().info("WebsocketIntegration disabled");
    }

}
