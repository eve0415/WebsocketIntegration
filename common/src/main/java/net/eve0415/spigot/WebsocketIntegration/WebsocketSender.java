package net.eve0415.spigot.WebsocketIntegration;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIEventState;

public class WebsocketSender {
    private Socket socket;

    public WebsocketSender(Socket socket) {
        this.socket = socket;
    }

    public void send(WSIEventState event, WebsocketBuilder content) {
        if (!WebsocketManager.getInstance().isConnected()) return;
        socket.emit(event.getValue(), content);
    }

    public static WebsocketBuilder builder() {
        return new WebsocketBuilder();
    }

    public static class WebsocketBuilder {
        private final JSONObject obj = new JSONObject();

        public WebsocketBuilder name(String name) throws JSONException {
            obj.put("name", name);
            return this;
        }

        public WebsocketBuilder UUID(UUID uuid) throws JSONException {
            obj.put("UUID", uuid);
            return this;
        }

        public WebsocketBuilder message(String message) throws JSONException {
            obj.put("message", message);
            return this;
        }

        public WebsocketBuilder status(int onlinePlayers, int maxPlayers, double tps) throws JSONException {
            Runtime runtime = Runtime.getRuntime();
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
