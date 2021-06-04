package net.eve0415.mc.WebsocketIntegration.Interface

public interface WSIChatHandler {
  fun chatHandler(name: String, uuid: String, url: String, message: String)

  fun getName(name: String, uuid: String): String

  fun messageFormatter(name: String, message: String): String {
    return String.format("<" + name + "> " + message)
  }
}
