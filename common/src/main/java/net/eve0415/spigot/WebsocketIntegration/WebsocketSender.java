package net.eve0415.spigot.WebsocketIntegration;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import net.eve0415.spigot.WebsocketIntegration.Util.LogEventType;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIEventState;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIPlatformType;

public class WebsocketSender {
    private final Socket socket;

    public WebsocketSender(final Socket socket) {
        this.socket = socket;
    }

    public void send(final WSIEventState event, final JSONObject content) {
        if (!WebsocketManager.getInstance().isConnected()) return;
        socket.emit(event.getValue(), content);
    }

    public static WebsocketBuilder builder() {
        return new WebsocketBuilder();
    }

    public static class WebsocketBuilder {
        private final JSONObject obj = new JSONObject();
        private final Runtime runtime = Runtime.getRuntime();

        public WebsocketBuilder basic(final WSIPlatformType type) throws JSONException {
            final long uptime = System.nanoTime() - WebsocketManager.getInstance().getStartTime();
            final long tempSec = uptime / (1000 * 1000 * 1000);
            final long days = (tempSec / (24 * 60 * 60)) % 24;
            final long hours = (tempSec / (60 * 60)) % 24;
            final long minutes = (tempSec / 60) % 60;
            final long seconds = tempSec % 60;
            final String time = days + ":" + hours + ":" + minutes + ":" + seconds;

            obj.put("platform", type.getValue());
            obj.put("port", WebsocketManager.getInstance().getServerPort());
            obj.put("totalMemory", runtime.totalMemory() / 1048576L + "MB");
            obj.put("usedMemory", (runtime.totalMemory() - runtime.freeMemory()) / 1048576L + "MB");
            obj.put("freeMemory", runtime.freeMemory() / 1048576L + "MB");
            obj.put("uptime", time);
            return this;
        }

        public WebsocketBuilder message(final String name, final UUID uuid, final String message) throws JSONException {
            obj.put("port", WebsocketManager.getInstance().getServerPort());
            obj.put("name", name);
            obj.put("UUID", uuid);
            obj.put("message", message.replaceAll("§.", ""));
            return this;
        }

        public WebsocketBuilder status(final WSIPlatformType type, final int onlinePlayers, final int maxPlayers,
                final double tps) throws JSONException {
            obj.put("onlinePlayers", onlinePlayers);
            obj.put("maxPlayers", maxPlayers);
            obj.put("tps", tps);
            return this;
        }

        public WebsocketBuilder serverName(final int port, final String name) throws JSONException {
            obj.put(String.valueOf(port), name);
            return this;
        }

        public WebsocketBuilder log(final LogEventType event, final String name, final UUID uuid, final String ip)
                throws JSONException {
            obj.put("port", WebsocketManager.getInstance().getServerPort());
            obj.put("event", event.getValue());
            obj.put("name", name);
            obj.put("UUID", uuid);
            obj.put("ip", ip);
            return this;
        }

        public WebsocketBuilder setAddress(final String address) throws JSONException {
            obj.put("address", address);
            return this;
        }

        public WebsocketBuilder clientType(final String type) throws JSONException {
            obj.put("type", type);
            return this;
        }

        public WebsocketBuilder clientVersion(final String version) throws JSONException {
            obj.put("version", version);
            return this;
        }

        public WebsocketBuilder mods(final int mods) throws JSONException {
            obj.put("mods", String.valueOf(mods));
            return this;
        }

        public WebsocketBuilder connectingServer(final String server) throws JSONException {
            obj.put("toServer", server);
            return this;
        }

        public WebsocketBuilder previousServer(final String server) throws JSONException {
            obj.put("fromServer", server);
            return this;
        }

        public WebsocketBuilder connectedServer(final String server) throws JSONException {
            obj.put("currentServer", server);
            return this;
        }

        public WebsocketBuilder kick(final String reason) throws JSONException {
            obj.put("reason", reason);
            return this;
        }

        public WebsocketBuilder fulfill(final String fulfill) throws JSONException {
            obj.put("fulfill", fulfill);
            return this;
        }

        public JSONObject toJSON() {
            return this.obj;
        }
    }
}
