package net.eve0415.spigot.WebsocketIntegration;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
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
            obj.put("platform", type.getValue());
            obj.put("port", WebsocketManager.getInstance().getServerPort());
            obj.put("totalMemory", runtime.totalMemory() / 1048576L + "MB");
            obj.put("usedMemory", (runtime.totalMemory() - runtime.freeMemory()) / 1048576L + "MB");
            obj.put("freeMemory", runtime.freeMemory() / 1048576L + "MB");
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

        public JSONObject toJSON() {
            return this.obj;
        }
    }
}
