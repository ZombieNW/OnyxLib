package com.zombienw.onyxlib.impl.pack;

import com.zombienw.onyxlib.OnyxPlugin;
import com.zombienw.onyxlib.impl.item.OnyxItemImpl;
import com.zombienw.onyxlib.impl.registry.NamespaceRegistry;
import com.zombienw.onyxlib.impl.registry.OnyxNamespaceImpl;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class PackGenerator {
    private static final int PACK_FORMAT = 46;
    private static final String PACK_DESCRIPTION = "Generated automatically by OnyxLib";

    private final OnyxPlugin corePlugin;
    private final File outputFile;

    public PackGenerator(OnyxPlugin corePlugin) {
        this.corePlugin = corePlugin;
        this.outputFile = new File(corePlugin.getDataFolder(), "resourcepack.zip");
    }

    public String generate() throws IOException {
        List<OnyxItemImpl> assetItems = new ArrayList<>();

        for (OnyxNamespaceImpl ns : NamespaceRegistry.getAllNamespaces()) {
            for (OnyxItemImpl item : ns.getItems()) {
                if (item.getTexturePath() != null) {
                    assetItems.add(item);
                }
            }
        }

        if (assetItems.isEmpty()) {
            return "No items with textures registered. Nothing to generate.";
        }

        Path tempDir = Files.createTempDirectory("onyxlib-pack-");

        try {
            writeMcMeta(tempDir);
            writeIconImage(tempDir);
            writeTextures(tempDir, assetItems);
            writeModelFiles(tempDir, assetItems);
            writeItemDefinitions(tempDir, assetItems);
            zipDirectory(tempDir, outputFile.toPath());
        } finally {
            deleteDirectory(tempDir);
        }

        return "Resource pack generated: " + outputFile.getName() + " (" + assetItems.size() + " items)";
    }

    private void writeMcMeta(Path root) throws IOException {
        String mcmeta = """
            {
              "pack": {
                "pack_format": %d,
                "description": "%s"
              }
            }
            """.formatted(PACK_FORMAT, PACK_DESCRIPTION);

        Files.writeString(root.resolve("pack.mcmeta"), mcmeta);
    }

    private void writeIconImage(Path root) throws IOException {
        InputStream stream = corePlugin.getResource("pack_icon.png");
        if (stream == null) {
            corePlugin.getLogger().warning("pack_icon.png missing from OnyxLib resources. Skipping icon.");
            return;
        }

        Path dest = root.resolve("pack.png");
        Files.copy(stream, dest, StandardCopyOption.REPLACE_EXISTING);
    }

    private void writeTextures(Path root, List<OnyxItemImpl> items) throws IOException {
        for (OnyxItemImpl item : items) {
            String namespace = item.getKey().getNamespace();
            Plugin owningPlugin = NamespaceRegistry.getNamespace(namespace).getPlugin();

            String resourcePath = "assets/" + namespace + "/textures/" + item.getTexturePath() + ".png";
            InputStream stream = owningPlugin.getResource(resourcePath);

            if (stream == null) {
                throw new IOException("Missing texture in " + owningPlugin.getName() + " jar: " + resourcePath);
            }

            Path dest = root.resolve(resourcePath);
            Files.createDirectories(dest.getParent());
            try (stream) {
                Files.copy(stream, dest, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    private void writeModelFiles(Path root, List<OnyxItemImpl> items) throws IOException {
        for (OnyxItemImpl item : items) {
            String namespace = item.getKey().getNamespace();
            String itemId = item.getId();

            Path modelDest = root.resolve("assets/" + namespace + "/models/item/" + itemId + ".json");
            Files.createDirectories(modelDest.getParent());

            String textureRef = namespace + ":" + item.getTexturePath();
            String modelJson = """
                {
                  "parent": "minecraft:item/generated",
                  "textures": {
                    "layer0": "%s"
                  }
                }
                """.formatted(textureRef);

            Files.writeString(modelDest, modelJson);
        }
    }

    private void writeItemDefinitions(Path root, List<OnyxItemImpl> items) throws IOException {
        for (OnyxItemImpl item : items) {
            String namespace = item.getKey().getNamespace();
            String itemId = item.getId();

            // Creates: assets/<namespace>/items/<id>.json
            Path defPath = root.resolve("assets/" + namespace + "/items/" + itemId + ".json");
            Files.createDirectories(defPath.getParent());

            // Points to the visual model created in writeModelFiles()
            String modelRef = namespace + ":item/" + itemId;

            String itemJson = """
                {
                  "model": {
                    "type": "minecraft:model",
                    "model": "%s"
                  }
                }
                """.formatted(modelRef);

            Files.writeString(defPath, itemJson);
        }
    }

    // fuck
    private void zipDirectory(Path source, Path destZip) throws IOException {
        Files.deleteIfExists(destZip);
        Files.createDirectories(destZip.getParent());

        try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(Files.newOutputStream(destZip)))) {
            Files.walk(source).filter(p -> !Files.isDirectory(p)).forEach(p -> {
                ZipEntry zipEntry = new ZipEntry(source.relativize(p).toString().replace('\\', '/'));
                try {
                    zos.putNextEntry(zipEntry);
                    Files.copy(p, zos);
                    zos.closeEntry();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        }
    }

    private void deleteDirectory(Path dir) throws IOException {
        try (var walk = Files.walk(dir)) {
            walk.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }
}