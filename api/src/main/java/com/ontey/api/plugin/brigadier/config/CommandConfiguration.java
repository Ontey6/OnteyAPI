package com.ontey.api.plugin.brigadier.config;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public record CommandConfiguration(
  List<String> names,
  String description,
  String permission,
  Map<String, ?> options
) {
   
   public @NotNull Map<String, Object> serialize() {
      return Map.of(
        "names", names,
        "permission", permission,
        "description", description,
        "options", options
      );
   }
}