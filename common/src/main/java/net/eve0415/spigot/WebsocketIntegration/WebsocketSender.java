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

    public void send(final WSIEventState event, final WebsocketBuilder content) {
        if (!WebsocketManager.getInstance().isConnected()) return;
        socket.emit(event.getValue(), content);
    }

    public static WebsocketBuilder builder() {
        return new WebsocketBuilder();
    }

    public static class WebsocketBuilder {
        private final JSONObject obj = new JSONObject();

        public WebsocketBuilder platform(final WSIPlatformType type) throws JSONException {
            obj.put("platform", type.getValue());
            return this;
        }

        public WebsocketBuilder name(final String name) throws JSONException {
            obj.put("name", name);
            return this;
        }

        public WebsocketBuilder UUID(final UUID uuid) throws JSONException {
            obj.put("UUID", uuid);
            return this;
        }

        public WebsocketBuilder message(final String message) throws JSONException {
            obj.put("message", message);
            return this;
        }

        public WebsocketBuilder status(final int onlinePlayers, final int maxPlayers, final double tps)
                throws JSONException {
            final Runtime runtime = Runtime.getRuntime();
            obj.put("onlinePlayers", onlinePlayers);
            obj.put("maxPlayers", maxPlayers);
            obj.put("totalMemory", runtime.totalMemory() / 1048576L + "MB");
            obj.put("usedMemory", (runtime.totalMemory() - runtime.freeMemory()) / 1048576L + "MB");
            obj.put("freeMemory", runtime.freeMemory() / 1048576L + "MB");
            obj.put("tps", tps);
            return this;
        }
    }
}
