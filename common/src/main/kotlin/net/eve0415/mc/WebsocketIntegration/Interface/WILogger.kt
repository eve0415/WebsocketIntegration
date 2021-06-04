package net.eve0415.mc.WebsocketIntegration.Interface

public interface WILogger {
  fun error(message: String, error: Throwable)

  fun error(message: String)

  fun warning(message: String)

  fun info(message: String)
}
