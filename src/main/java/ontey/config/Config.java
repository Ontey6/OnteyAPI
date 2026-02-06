package ontey.config;

import lombok.Getter;
import lombok.NonNull;
import ontey.check.Checker;
import ontey.check.TryCatch;
import ontey.plugin.OnteyPlugin;
import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.configuration.file.YamlRepresenter;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.comments.CommentLine;
import org.yaml.snakeyaml.comments.CommentType;
import org.yaml.snakeyaml.nodes.*;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Config extends ConfigSection {
   
   private final OnteyPlugin plugin;
   @Getter
   private final File file;
   private Yaml yaml;
   
   private final DumperOptions dumperOptions;
   private final LoaderOptions loaderOptions;
   private final YamlConstructor constructor;
   private final YamlRepresenter representer;
   
   public Config(@NonNull OnteyPlugin plugin, @NonNull File file) {
      if(!isYamlFile(file.getName()))
         throw new IllegalArgumentException("File needs to be a YAML file");
      
      this.file = file;
      this.plugin = plugin;
      
      createFile();
      
      loaderOptions = new LoaderOptions();
      
      loaderOptions.setProcessComments(true);
      // these default values are acceptable for normal configs
      loaderOptions.setMaxAliasesForCollections(10_000); // To prevent "Billon laughs attacks" (https://en.wikipedia.org/wiki/Billion_laughs_attack)
      loaderOptions.setCodePointLimit(20 * 1024 * 1024); // ~20MB (probably file size)
      
      dumperOptions = new DumperOptions();
      dumperOptions.setProcessComments(true);
      dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
      
      constructor = new YamlConstructor(loaderOptions);
      representer = new YamlRepresenter(dumperOptions);
      representer.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
      
      this.yaml = new Yaml(constructor, representer, dumperOptions, loaderOptions);
      
      load();
   }
   
   public Config(@NonNull OnteyPlugin plugin, @NonNull File file, @NonNull DumperOptions dumperOptions, @NonNull LoaderOptions loaderOptions) {
      this.file = file;
      this.plugin = plugin;
      
      createFile();
      
      this.loaderOptions = loaderOptions;
      this.loaderOptions.setProcessComments(true);
      
      this.dumperOptions = dumperOptions;
      this.dumperOptions.setProcessComments(true);
      
      constructor = new YamlConstructor(loaderOptions);
      representer = new YamlRepresenter(dumperOptions);
   }
   
   public void load() {
      try(Reader reader = new FileReader(file)) {
         Node root = yaml.compose(reader);
         
         map.clear();
         
         if(root instanceof MappingNode mapping)
            fromNodeTree(mapping, this);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }
   
   public void save() {
      try(Writer writer = new FileWriter(file)) {
         yaml.serialize(toNodeTree(this), writer);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }
   
   // Utils
   
   private void createFile() {
      if(file.exists())
         return;
      
      var resource = plugin.getResource(file.getName());
      
      if(resource != null)
         TryCatch.wrapCheckedExceptions(() -> Files.copy(resource, file.toPath()));
      else
         TryCatch.wrapCheckedExceptions(file::createNewFile);
   }
   
   static boolean isYamlFile(String name) {
      return name.endsWith(".yml")
        || name.endsWith(".yaml");
   }
   
   static String cutYamlSuffix(String name) {
      Checker.checkArgument(isYamlFile(name));
      
      int offset = 4;
      if(name.endsWith(".yaml"))
         offset++;
      
      return name.substring(0, name.length() - offset);
   }
   
   private void fromNodeTree(@NotNull MappingNode input, @NotNull ConfigSection section) {
      constructor.flattenMapping(input);
      for (NodeTuple nodeTuple : input.getValue()) {
         Node key = nodeTuple.getKeyNode();
         String keyString = String.valueOf(constructor.construct(key));
         Node value = nodeTuple.getValueNode();
         
         while (value instanceof AnchorNode) {
            value = ((AnchorNode) value).getRealNode();
         }
         
         if (value instanceof MappingNode && !hasSerializedTypeKey((MappingNode) value)) {
            fromNodeTree((MappingNode) value, section.createSection(keyString));
         } else {
            section.set(keyString, constructor.construct(value));
         }
         
         section.setComments(keyString, getCommentLines(key.getBlockComments()));
         if (value instanceof MappingNode || value instanceof SequenceNode) {
            section.setInlineComments(keyString, getCommentLines(key.getInLineComments()));
         } else {
            section.setInlineComments(keyString, getCommentLines(value.getInLineComments()));
         }
      }
   }
   
   private boolean hasSerializedTypeKey(MappingNode node) {
      for(NodeTuple nodeTuple : node.getValue()) {
         Node keyNode = nodeTuple.getKeyNode();
         
         if(keyNode instanceof ScalarNode scalarNode && scalarNode.getValue().equals(ConfigurationSerialization.SERIALIZED_TYPE_KEY))
            return true;
      }
      return false;
   }
   
   private MappingNode toNodeTree(@NotNull ConfigSection section) {
      List<NodeTuple> nodeTuples = new ArrayList<>();
      for (Map.Entry<String, Object> entry : section.getValues(false).entrySet()) {
         Node key = representer.represent(entry.getKey());
         Node value;
         if (entry.getValue() instanceof ConfigSection sec)
            value = toNodeTree(sec);
         else
            value = representer.represent(entry.getValue());
         
         key.setBlockComments(getCommentLines(section.getComments(entry.getKey()), CommentType.BLOCK));
         if (value instanceof MappingNode || value instanceof SequenceNode) {
            key.setInLineComments(getCommentLines(section.getInlineComments(entry.getKey()), CommentType.IN_LINE));
         } else {
            value.setInLineComments(getCommentLines(section.getInlineComments(entry.getKey()), CommentType.IN_LINE));
         }
         
         nodeTuples.add(new NodeTuple(key, value));
      }
      
      return new MappingNode(Tag.MAP, nodeTuples, DumperOptions.FlowStyle.BLOCK);
   }
   
   private @NonNull List<@Nullable String> getCommentLines(@Nullable List<@NonNull CommentLine> comments) {
      List<String> lines = new ArrayList<>();
      if(comments != null) {
         for(CommentLine comment : comments) {
            if(comment.getCommentType() == CommentType.BLANK_LINE) {
               lines.add(null);
            } else {
               String line = comment.getValue();
               line = line.startsWith(" ") ? line.substring(1) : line;
               lines.add(line);
            }
         }
      }
      return lines;
   }
   
   private List<CommentLine> getCommentLines(List<String> comments, CommentType commentType) {
      List<CommentLine> lines = new ArrayList<>();
      
      for(String comment : comments) {
         if(comment == null) {
            lines.add(new CommentLine(null, null, "", CommentType.BLANK_LINE));
         } else {
            String line = comment;
            line = line.isEmpty() ? line : " " + line;
            lines.add(new CommentLine(null, null, line, commentType));
         }
      }
      
      return lines;
   }
}
