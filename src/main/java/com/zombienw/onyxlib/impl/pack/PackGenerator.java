package com.zombienw.onyxlib.impl.pack;

import com.zombienw.onyxlib.OnyxPlugin;
import com.zombienw.onyxlib.impl.block.OnyxBlockImpl;
import com.zombienw.onyxlib.impl.item.OnyxItemImpl;
import com.zombienw.onyxlib.impl.registry.NamespaceRegistry;
import com.zombienw.onyxlib.impl.registry.OnyxNamespaceImpl;
import org.bukkit.plugin.Plugin;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PackGenerator {

    private final OnyxPlugin corePlugin;
    private final File outputFile;

    private final PackMetaGenerator metaGenerator = new PackMetaGenerator();
    private final ModelGenerator modelGenerator = new ModelGenerator();
    private final AssetValidator validator = new AssetValidator();

    public PackGenerator(OnyxPlugin corePlugin) {
        this.corePlugin = corePlugin;
        this.outputFile = new File(corePlugin.getDataFolder(), "resourcepack.zip");
    }

    public String generate() throws IOException {
        Path tempDir = Files.createTempDirectory("onyxlib-pack-temp-");
        int totalAssets = 0;

        try {
            metaGenerator.generate(corePlugin, tempDir);

            for (OnyxNamespaceImpl ns : NamespaceRegistry.getAllNamespaces()) {
                Plugin plugin = ns.getPlugin();

                FileUtils.extractAssetsFromJar(plugin, tempDir);
                validator.validate(tempDir, ns);

                for (OnyxItemImpl item : ns.getItems()) {
                    modelGenerator.processItem(tempDir, item);
                    totalAssets++;
                }

                for (OnyxBlockImpl block : ns.getBlocks()) {
                    modelGenerator.processBlock(tempDir, block);
                    totalAssets++;
                }
            }

            if (totalAssets > 0) {
                FileUtils.zipDirectory(tempDir, outputFile.toPath());
            }

        } finally {
            FileUtils.deleteDirectory(tempDir);
        }

        if (totalAssets == 0) return "No assets registered. Nothing to generate.";
        return "Resource pack generated: " + outputFile.getName() + " (" + totalAssets + " items)";
    }
}