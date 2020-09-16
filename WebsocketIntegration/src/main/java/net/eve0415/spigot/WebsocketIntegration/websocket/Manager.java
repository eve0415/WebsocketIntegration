package net.eve0415.spigot.WebsocketIntegration.websocket;

import java.net.URISyntaxException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import net.eve0415.spigot.WebsocketIntegration.main;

public class Manager {
    private final main instance;
    private Socket socket;

    public boolean isAlive = false;

    public Manager(final main instance) {
        this.instance = instance;

        try {
            this.socket = IO.socket("http://localhost:25500");

            this.socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    instance.getLogger().info("Connected");
                    isAlive = true;
                    send(EventState.STARTING, null, null);
                }
            });

            this.socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    instance.getLogger().warning("Disconnected: " + args[0].toString());
                    isAlive = false;
                }
            });

            this.socket.on(Socket.EVENT_RECONNECT, new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    instance.getLogger().info("Reconnected");
                    isAlive = true;
                    updateStatus();
                }
            });

            this.socket.on("message", new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    try {
                        instance.sender.processer(new JSONObject((args[0].toString())));
                    } catch (final JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            this.socket.on("command", new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    try {
                        instance.commandHandler.processer(new JSONObject((args[0].toString())));
                    } catch (final JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            this.socket.on("link", new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    try {
                        instance.linkManager.processer(new JSONObject((args[0].toString())));
                    } catch (final JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            this.socket.connect();
        } catch (final URISyntaxException e) {
            this.instance.getLogger().warning(e.toString());
            e.printStackTrace();
        }

        whenIsTheServerReady();
    }

    // runTaskLater works when the server is ready for player to join
    // meaning it will send a websocket when the server is ready!
    private void whenIsTheServerReady() {
        Bukkit.getScheduler().runTaskAsynchronously(this.instance, new Runnable() {
            @Override
            public void run() {
                send(EventState.STARTED, null, null);
                updateStatus();
                autoUpdateStatus();
            }
        });
    }

    private void autoUpdateStatus() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateStatus();
            }
        }.runTaskTimer(this.instance, 0, 200);
    }

    private void updateStatus() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(this.instance, new Runnable() {
            public void run() {
                send(EventState.STATUS, null, null);
            }
        }, 20L);
    }

    public void send(final EventState event, final Player player, final String text) {
        if (!isAlive)
            return;
        final JSONObject obj = new JSONObject();

        switch (event) {
            case STARTING:
            case STARTED:
            case STOPPING:
                this.socket.emit(event.getValue(), 0);
                break;

            case JOIN:
            case QUIT:
                updateStatus();

            case MESSAGE:
            case ACHIEVEMENT:
            case DEATH:
                try {
                    obj.put("name", player.getName());
                    obj.put("UUID", player.getUniqueId());
                    obj.put("message", text.replaceAll("§.", ""));

                    this.socket.emit(event.getValue(), obj);
                    updateStatus();
                } catch (final JSONException e) {
                    e.printStackTrace();
                }
                break;

            case COMMAND:
                try {
                    obj.put("name", player.getName());
                    obj.put("UUID", player.getUniqueId());
                    obj.put("result", text);

                    this.socket.emit(event.getValue(), obj);
                } catch (final JSONException e) {
                    e.printStackTrace();
                }
                break;

            case STATUS:
                final Runtime runtime = Runtime.getRuntime();
                try {
                    obj.put("onlineplayer", Bukkit.getOnlinePlayers().size());
                    obj.put("maxPlayer", Bukkit.getMaxPlayers());
                    obj.put("totalMemory", runtime.totalMemory() / 1048576L + "MB");
                    obj.put("usedMemory", (runtime.totalMemory() - runtime.freeMemory()) / 1048576L + "MB");
                    obj.put("freeMemory", runtime.freeMemory() / 1048576L + "MB");
                    obj.put("tps", Math.round(Bukkit.getTPS()[0] * 100.0D) / 100.0D);
                    this.socket.emit(event.getValue(), obj);
                } catch (final JSONException e) {
                    e.printStackTrace();
                }
                break;

            case LINK:
                try {
                    obj.put("UUID", player.getUniqueId());
                    obj.put("code", text);
                    this.socket.emit(event.getValue(), obj);
                } catch (final JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
        return;
    }
}