package net.eve0415.spigot.WebsocketIntegration.Util

public interface WSILogger {
  fun error(message: String, error: Throwable)

  fun error(message: String)

  fun warning(message: String)

  fun info(message: String)
}
