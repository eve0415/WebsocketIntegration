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
import net.eve0415.spigot.WebsocketIntegration.Lag;

public class Manager {
    private main instance;
    private Socket socket;

    public boolean isAlive = false;

    public Manager(main instance) {
        this.instance = instance;

        try {
            this.socket = IO.socket("http://localhost:25500");

            this.socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    instance.getLogger().info("Connected");
                    isAlive = true;
                    send(EventState.STARTING, null, null);
                }
            });

            this.socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    instance.getLogger().warning("Disconnected: " + String.valueOf(args[0]));
                    isAlive = false;
                }
            });

            this.socket.on(Socket.EVENT_RECONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    instance.getLogger().info("Reconnected");
                    isAlive = true;
                    updateStatus();
                }
            });

            this.socket.on("message", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    try {
                        instance.sender.processer(new JSONObject(String.valueOf(args[0])));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            this.socket.on("link", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    try {
                        instance.linkManager.processer(new JSONObject(String.valueOf(args[0])));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            this.socket.connect();
        } catch (URISyntaxException e) {
            this.instance.getLogger().warning(String.valueOf(e));
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
                checkTPS();
                updateStatus();
                autoUpdateStatus();
            }
        });
    }

    private void checkTPS() {
        new BukkitRunnable() {
            @Override
            public void run() {
                new Lag();
            }
        }.runTaskTimer(this.instance, 100L, 1L);
    }

    private void autoUpdateStatus() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateStatus();
            }
        }.runTaskTimer(this.instance, 0, 1200);
    }

    private void updateStatus() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(this.instance, new Runnable() {
            public void run() {
                send(EventState.STATUS, null, null);
            }
        }, 20L);
    }

    public void send(EventState event, Player player, String text) {
        if (!isAlive)
            return;
        JSONObject obj = new JSONObject();

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
                    obj.put("name", String.valueOf(player.getName()));
                    obj.put("UUID", String.valueOf(player.getUniqueId()));
                    obj.put("message", text.replaceAll("§.", ""));

                    this.socket.emit(event.getValue(), obj);
                    updateStatus();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case STATUS:
                Runtime runtime = Runtime.getRuntime();
                try {
                    obj.put("onlineplayer", String.valueOf(Bukkit.getOnlinePlayers().size()));
                    obj.put("maxPlayer", String.valueOf(Bukkit.getMaxPlayers()));
                    obj.put("totalMemory", String.valueOf(runtime.totalMemory() / 1048576L + "MB"));
                    obj.put("maxMemory",
                            String.valueOf((runtime.totalMemory() - runtime.freeMemory()) / 1048576L + "MB"));
                    obj.put("freeMemory", String.valueOf(runtime.freeMemory() / 1048576L + "MB"));
                    obj.put("tps", String.valueOf(Lag.getTPS()));
                    this.socket.emit(event.getValue(), obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case LINK:
                try {
                    obj.put("UUID", String.valueOf(player.getUniqueId()));
                    obj.put("code", text);
                    this.socket.emit(event.getValue(), obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
        return;
    }
}