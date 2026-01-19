package com.ontey.api.java.filelog;

import com.ontey.api.java.log.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.UUID;

@NullMarked
public class FileLog {
   
   private final File directory;
   
   private final PluginLogger LOGGER;
   
   public FileLog(JavaPlugin plugin) {
      Objects.requireNonNull(plugin, "plugin");
      
      this.LOGGER = new PluginLogger(plugin.getName() + "] [FileLog");
      
      LOGGER.debug("Loading FileLog");
      
      directory = new File(plugin.getDataFolder(), "logs");
      
      if(directory.exists() && !directory.isDirectory())
         throw new IllegalStateException("[OnteyAPI] FileLog directory already exists, but as a file: " + directory.getPath());
      
      if(!directory.exists())
         if(!directory.mkdirs())
            throw new IllegalStateException("[OnteyAPI] Could not generate the logs directory, disabling plugin");
      
      LOGGER.debug("Loaded FileLog");
   }
   
   public void saveStackTrace(Throwable throwable) {
      Objects.requireNonNull(throwable, "throwable cannot be null");
      
      for(int i = 0; i < 5; i++) {
         String name = throwable.getClass().getName() + "-" + UUID.randomUUID() + ".log";
         File file = new File(directory, name);
         
         if(!file.exists()) {
            writeStackTrace(throwable, file, name);
            LOGGER.warn("Saved stack-trace to " + name);
            return;
         }
      }
      
      LOGGER.warn("Could not find a name for the FileLog " + throwable.getClass().getName());
   }
   
   @NullMarked
   private void writeStackTrace(Throwable throwable, File file, String name) {
      try(PrintWriter pw = new PrintWriter(new FileWriter(file, true))) {
         throwable.printStackTrace(pw);
      } catch(IOException ex) {
         LOGGER.error("Could not write to error log '" + name + "'");
      }
   }
}
