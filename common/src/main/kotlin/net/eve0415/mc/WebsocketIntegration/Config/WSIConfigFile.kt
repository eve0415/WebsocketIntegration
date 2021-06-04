package net.eve0415.spigot.WebsocketIntegration.Config

import java.io.File
import java.io.InputStreamReader
import net.eve0415.spigot.WebsocketIntegration.WebsocketManager
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

public class WSIConfigFile {
  companion object {
    fun load(file: File): WSIConfigKey {
      if (!file.exists()) {
        val isReader =
            InputStreamReader(WebsocketManager::class.java.getResourceAsStream("config.yml"))
        // file.writer()
        isReader.copyTo(file.writer())
        // val br = BufferedReader(isReader)
        // val writer =
        // PrintWriter(File(WebsocketManager::class.java.getResource("config.yml").getPath()))
      }
      return Yaml(Constructor(WSIConfigKey::class.java))
          .loadAs(file.reader(), WSIConfigKey::class.java)
    }
  }
}
