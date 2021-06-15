package net.eve0415.mc.websocketintegration.constructor

interface WILogger {
  fun error(message: String, error: Throwable)

  fun error(message: String)

  fun warning(message: String)

  fun info(message: String)
}
