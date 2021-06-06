package net.eve0415.mc.WebsocketIntegration.Config

import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor
import java.io.File
import java.nio.file.Files

class WIConfigFile {
  companion object {
    fun load(file: File): WIConfigKey {
      if (!file.exists())
        Files.copy(WIConfigFile::class.java.getResourceAsStream("/config.yml"), file.toPath())

      return Yaml(CustomClassLoaderConstructor(WIConfigKey::class.java.classLoader))
        .loadAs(file.reader(), WIConfigKey::class.java)
    }
  }
}
